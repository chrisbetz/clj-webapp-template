(ns {{sanitized-ns}}.server.routes
     (:use compojure.core
           [hiccup.middleware :only (wrap-base-url)])
     (:require [ring.util.response :as response]
               [compojure.route :as route]
               [compojure.handler :as handler]
               [clojure.tools.logging :as log]
               [cemerick.friend :as friend]
               [friend-oauth2.workflow :as oauth2]
               [{{sanitized-ns}}.server.config :as config]
               [{{sanitized-ns}}.server.views.index :as index]
               [{{sanitized-ns}}.server.views.welcome :as welcome]))

(def config-auth {:roles #{::user}})

(defn access-token-parsefn
 [response]
 ((clojure.walk/keywordize-keys
    (ring.util.codec/form-decode
      (response :body))) :access_token))

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

(defroutes main-routes
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
                        :workflows [(oauth2/workflow
                                      {:client-config (get-in @config/config [:oauth2 :github] (log/warn "No github OAuth2 config found."))
                                       :uri-config (github-uri-config (get-in @config/config [:oauth2 :github]))
                                       :access-token-parsefn access-token-parsefn
                                       :config-auth config-auth})]}))
     (wrap-base-url))))
