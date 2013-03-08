(ns {{sanitized-ns}}.server.views.welcome
     (:use [hiccup core page])
     (:require
       [clj-http.client :as client]
       [cheshire.core :as j]
       [clojure.core.memoize :as memo]
       [{{sanitized-ns}}.server.views.common :as common]))

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



(defn welcome-page [request]
  (let [authentications (get-in request [:session :cemerick.friend/identity :authentications])
        access-token (:access_token (second (first authentications)))
        user (google-infos access-token)]
    (common/layout (str "Hello, " (:name user))
      [:div.row [:div.large-12.columns [:h1 "Hello, "  (:name user) [:img {:src (:avatar_url user)}]]
                 user
                 ]])))


