(ns {{sanitized-ns}}.server.routes
     (:use compojure.core
           [hiccup.middleware :only (wrap-base-url)])
     (:require [ring.util.response :as response]
               [compojure.route :as route]
               [compojure.handler :as handler]
               [clojure.tools.logging :as log]
               [cemerick.friend :as friend]
               [friend-oauth2.workflow :as oauth2]
               [cheshire.core :as j]
               [{{sanitized-ns}}.server.config :as config]
               [{{sanitized-ns}}.server.views.index :as index]
               [{{sanitized-ns}}.server.views.welcome :as welcome]))

(def config-auth {:roles #{::user}})

(defn access-token-parsefn-form-decode
 [response]
 (:access_token (clojure.walk/keywordize-keys
    (ring.util.codec/form-decode
      (response :body)))))

(defn access-token-parsefn-json
 [response]
 (:access_token (j/parse-string (:body response) true)))

(defn github-uri-config [client-config]
 {:authentication-uri {:url "https://github.com/login/oauth/authorize"
                       :query {:client_id (:client-id client-config)
                               :response_type "code"
                               :redirect_uri (oauth2/format-config-uri client-config)
                               :scope "user"}}

  :access-token-uri {:url "https://github.com/login/oauth/access_token"
                     :query {:client_id (:client-id client-config)
                             :client_secret (:client-secret client-config)
                             :grant_type "authorization_code"
                             :redirect_uri (oauth2/format-config-uri client-config)
                             :code ""}}})

(defn google-uri-config [client-config]
  {:authentication-uri {:url "https://accounts.google.com/o/oauth2/auth"
                        :query {:response_type "code"
                                :client_id (:client-id client-config)
                                :redirect_uri (oauth2/format-config-uri client-config)
                                :scope "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email"}}

   :access-token-uri {:url "https://accounts.google.com/o/oauth2/token"
                      :query {:client_id (:client-id client-config)
                              :client_secret (:client-secret client-config)
                              :grant_type "authorization_code"
                              :redirect_uri (oauth2/format-config-uri client-config)
                              :code ""}}})

(defroutes main-routes
 ;; remember: /loginWithGoogle, /loginWithGithub are defined in the workflow
 ;; /google-callback and /github-callback are set in the config, as these might change with the settings in the services
 ; (https://code.google.com/apis/console#access and https://github.com/settings/applications).
 (GET "/" [] (index/index-page))
 (GET "/welcome" request (friend/authorize #{::user} (welcome/welcome-page request)))
 (friend/logout (ANY "/logout" request (response/redirect "/")))
 (route/resources "/")
 (route/not-found "Page not found"))

(def app
  (do
    (config/init)
    (-> (handler/site (friend/authenticate main-routes
                       {:allow-anon? true
                        :login-uri "/"
                        :workflows [(oauth2/workflow
                                      {:login-uri "/loginWithGoogle"
                                       :client-config (get-in @config/config [:oauth2 :google])
                                       :uri-config (google-uri-config (get-in @config/config [:oauth2 :google]))
                                       :access-token-parsefn access-token-parsefn-json
                                       :config-auth config-auth})
                                     (oauth2/workflow
                                      {:login-uri "/loginWithGithub"
                                       :client-config (get-in @config/config [:oauth2 :github])
                                       :uri-config (github-uri-config (get-in @config/config [:oauth2 :github]))
                                       :access-token-parsefn access-token-parsefn-form-decode
                                       :config-auth config-auth})]}))
     (wrap-base-url))))
