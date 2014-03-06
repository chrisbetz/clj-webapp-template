(ns {{sanitized-ns}}.server.auth
  (:use [clojure.tools.logging])
  (:require [ring.util.response :as response]
            [cemerick.friend :as friend]
            [friend-oauth2.workflow :as oauth2]
            [friend-oauth2.util :as oauth2util]
            [cheshire.core :as j]
            [clj-http.client :as client]
            [{{sanitized-ns}}.server.config :as config]
            [{{sanitized-ns}}.server.users.users :as user]))

(def config-auth {:roles #{::admin ::user}})
(derive ::admin ::user)


;; github:
#_ {
    :login "octocat",
    :id 1,
    :avatar_url "https://github.com/images/error/octocat_happy.gif",
    :gravatar_id "somehexcode",
    :url "https://api.github.com/users/octocat",
    :name "monalisa octocat",
    :company "GitHub",
    :blog "https://github.com/blog",
    :location "San Francisco",
    :email "octocat@github.com",
    :hireable false,
    :bio "There once was... ",
    :public_repos 2,
    :public_gists 1,
    :followers 20,
    :following 0,
    :html_url "https://github.com/octocat",
    :created_at "2008 -01-14T04:33:35Z",
    :type "User",
    :total_private_repos 100,
    :owned_private_repos 100,
    :private_gists 81,
    :disk_usage 10000,
    :collaborators 8,
    :plan {
            :name "Medium",
            :space 400,
            :collaborators 10,
            :private_repos 20
            }
    }
(defn github-infos
  "Github API call for the current authenticated users profile information."
  [access-token]
  (let [url (str "https://api.github.com/user?access_token=" access-token)
        response (client/get url {:accept :json})
        user (j/parse-string (:body response) true)]
    user))




;; google:
#_ {:id "--string--", :email "chris@***", :verified_email true, :name "Christian Betz", :given_name "Christian", :family_name "Betz", :locale "de", :hd "herolabs.com"}
(defn google-infos
  "Google API call for the current authenticated users profile information."
  [access-token]
  (let [url (str "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" access-token)
        response (client/get url {:accept :json})
        user (j/parse-string (:body response) true)]
    user))





(defn access-token-parsefn-form-decode
  [response]
  (:access_token (clojure.walk/keywordize-keys
                   (ring.util.codec/form-decode
                     (response :body)))))

(defn access-token-parsefn-json
  [response]
  (:access_token (j/parse-string (:body response) true)))




(defn github-uri-config [client-config]
  {:authentication-uri {:url   "https://github.com/login/oauth/authorize"
                        :query {:client_id     (:client-id client-config)
                                :response_type "code"
                                :redirect_uri  (oauth2util/format-config-uri client-config)
                                :scope         "user"}}

   :access-token-uri   {:url   "https://github.com/login/oauth/access_token"
                        :query {:client_id     (:client-id client-config)
                                :client_secret (:client-secret client-config)
                                :grant_type    "authorization_code"
                                :redirect_uri  (oauth2util/format-config-uri client-config)
                                :code          ""}}})

(defn google-uri-config [client-config]
  {:authentication-uri {:url   "https://accounts.google.com/o/oauth2/auth"
                        :query {:response_type "code"
                                :client_id     (:client-id client-config)
                                :redirect_uri  (oauth2util/format-config-uri client-config)
                                :scope         "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email"}}

   :access-token-uri   {:url   "https://accounts.google.com/o/oauth2/token"
                        :query {:client_id     (:client-id client-config)
                                :client_secret (:client-secret client-config)
                                :grant_type    "authorization_code"
                                :redirect_uri  (oauth2util/format-config-uri client-config)
                                :code          ""}}})






(defn authenticated-workflow [authentication-map host]
  (let [identity (:identity authentication-map)
        google-user-info (google-infos identity)
        my-user-info (user/get-user-by-id (:id google-user-info))
        new-my-user-info (merge my-user-info google-user-info)
        ]

    (when my-user-info
      (info "A known user signed up/logged in: Getting potentially updated information from OAuth provider for " (:email my-user-info))
      (user/update-user new-my-user-info))

    (cond

      (not my-user-info) ;; did not exist before -> sign up
      (do
        (info "New user signup:" (:email new-my-user-info))
        (user/register-for-signup (assoc new-my-user-info :host host))
        (response/redirect "/thankYouForSignup.html"))

      (user/valid? my-user-info) ;; is valid -> return him
      (do
        (info "Logging in valid user: " (:email my-user-info))
        (merge authentication-map new-my-user-info))

      :default (do
                 (info "A known user, who is not valid" (:email my-user-info))
                 (response/redirect "/sorryUnauthorized.html")))))


(defn workflow [the-workflow]
  "Wraps a given workflow, so that a user needs to be activated first. Get's his/her roles from the database."
  (fn [request]
    (let [optional-authentication-map (the-workflow request)]
      (if (:identity optional-authentication-map)
        (authenticated-workflow optional-authentication-map ((:headers request) "host"))
        optional-authentication-map))))



(defn workflows []
  [(workflow (oauth2/workflow
              {:login-uri            "/loginWithGoogle"
               :client-config        (get-in @config/config [:oauth2 :google])
               :uri-config           (google-uri-config (get-in @config/config [:oauth2 :google]))
               :access-token-parsefn access-token-parsefn-json
               :config-auth          config-auth}))
   (workflow (oauth2/workflow
               {:login-uri            "/loginWithGithub"
                :client-config        (get-in @config/config [:oauth2 :github])
                :uri-config           (github-uri-config (get-in @config/config [:oauth2 :github]))
                :access-token-parsefn access-token-parsefn-form-decode
                :config-auth          config-auth}))])

