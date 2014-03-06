(ns {{sanitized-ns}}.server.views.welcome
     (:use [hiccup core page])
     (:require
       [clj-http.client :as client]
       [cheshire.core :as j]
       [clojure.core.memoize :as memo]
       [{{sanitized-ns}}.server.views.common :as common]
       [{{sanitized-ns}}.server.auth :as auth]))




(defn welcome-page [request]
  (let [authentications (get-in request [:session :cemerick.friend/identity :authentications])
        access-token (:access_token (second (first authentications)))
        user (auth/google-infos access-token)]
    (common/layout (str "Hello, " (:name user))
      [:div.row [:div.large-12.columns [:h1 "Hello, "  (:name user) [:img {:src (:avatar_url user)}]]
                 user
                 ]])))


