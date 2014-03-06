(ns {{sanitized-ns}}.server.routes
   (:use
     compojure.core
     [hiccup.middleware :only (wrap-base-url)])
   (:require
     [ring.util.response :as response]
     [compojure.route :as route]
     [compojure.handler :as handler]
     [clojure.tools.logging :as log]
     [cemerick.friend :as friend]
     [friend-oauth2.workflow :as oauth2]
     [cheshire.core :as j]
     [noir.util.middleware]
     [{{sanitized-ns}}.server.config :as config]
     [{{sanitized-ns}}.server.auth :as auth]
     [{{sanitized-ns}}.server.views.index :as index]
     [{{sanitized-ns}}.server.views.welcome :as welcome]))

(defroutes main-routes
   ;; remember: /loginWithGoogle, /loginWithGithub are defined in the workflow
   ;; /google-callback and /github-callback are set in the config, as these might change with the settings in the services
   ; (https://code.google.com/apis/console#access and https://github.com/settings/applications).
   (GET "/" [] (index/index-page))
   (GET "/welcome" request (friend/authorize #{::auth/user } (welcome/welcome-page request)))
   (friend/logout (ANY "/logout" request (response/redirect "/")))
   (route/resources "/")
   (route/not-found "Page not found"))

 (def app
   (do
     (config/init)
     (-> (handler/site (friend/authenticate main-routes
                         {:allow-anon? true
                          :login-uri "/"
                          :workflows (auth/workflows)}))
       (wrap-base-url))))

