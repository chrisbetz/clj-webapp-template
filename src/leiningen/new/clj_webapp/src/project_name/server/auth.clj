(ns {{sanitized-ns}}.server.auth
     (:require [clojure.tools.logging :as log]
               [cemerick.friend :as friend]
               [friend-oauth2.workflow :as oauth2]
               [cheshire.core :as j]
               [{{sanitized-ns}}.server.config :as config]))

(def config-auth {:roles #{::admin ::user}})
(derive ::admin ::user)

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

(defn workflows []
  [(oauth2/workflow
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
                   :config-auth config-auth})])

