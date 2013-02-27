(ns {{sanitized-ns}}.routes
     (:use compojure.core
           [hiccup.middleware :only (wrap-base-url)])
     (:require [compojure.route :as route]
               [compojure.handler :as handler]
               [{{sanitized-ns}}.views.index :as index]))

(defroutes main-routes
 (GET "/" [] (index/index-page))
 (route/resources "/")
 (route/not-found "Page not found"))

(def app
 (-> (handler/site main-routes)
   (wrap-base-url)))
