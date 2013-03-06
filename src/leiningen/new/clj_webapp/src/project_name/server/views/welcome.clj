(ns {{sanitized-ns}}.server.views.welcome
     (:use [hiccup core page])
     (:require [{{sanitized-ns}}.server.views.common :as common]))

(defn welcome-page [request]
  (let [authentications (get-in request [:session :cemerick.friend/identity :authentications])
        access-token (:access_token (second (first authentications)))
        first-name access-token
        user (get-in request [:session :cemerick.friend/identity])]
    (common/layout (str "Hello, " first-name)
      [:div.row [:div.large-12.columns [:h1 "Hello, "  user]
                 ]])))