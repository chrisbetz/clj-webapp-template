(ns {{sanitized-ns}}.server.views.welcome
     (:use [hiccup core page])
     (:require
       [clj-http.client :as client]
       [cheshire.core :as j]
       [clojure.core.memoize :as memo]
       [{{sanitized-ns}}.server.views.common :as common]))

(defn- github-infos*
 "Github API call for the current authenticated users profile information."
 [access-token]
 (let [url (str "https://api.github.com/user?access_token=" access-token)
       response (client/get url {:accept :json})
       user (j/parse-string (:body response) true)]
   user))

(def github-infos (memo/memo-ttl github-infos* (* 10 60 1000)))

(defn welcome-page [request]
  (let [authentications (get-in request [:session :cemerick.friend/identity :authentications])
        access-token (:access_token (second (first authentications)))
        user (github-infos access-token)]
    (common/layout (str "Hello, " (:name user))
      [:div.row [:div.large-12.columns [:h1 "Hello, "  (:name user) [:img {:src (:avatar_url user)}]]]])))

