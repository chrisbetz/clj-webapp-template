(ns {{sanitized-ns}}.server
  (:use compojure.core
        [hiccup core page]
        [hiccup.middleware :only (wrap-base-url)])
  (:require 
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]
            [ring.adapter.jetty :as jetty]
            )
  (:gen-class))

(defn layout [title & content]
  (html {:mode :html}
    (doctype :html5 )
    "<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->"
    "<!--[if IE 8]>    <html class='no-js lt-ie9' lang='en'> <![endif]-->"
    "<!--[if gt IE 8]><!--> <html class='no-js' lang='en'> <!--<![endif]-->"

    [:head [:meta {:charset "utf-8"}]

     "<!-- Set the viewport width to device width for mobile -->"
     [:meta {:name "viewport" :content "width=device-width"}]

     [:title title]
     (include-css "/stylesheets/foundation.min.css")
     (include-css "/stylesheets/app.min.css")]
    [:body content
     (include-js "//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js")
     (include-js "/javascripts/foundation.min.js")
     (include-js "/javascripts/app.js")
     (include-js "/javascripts/{{fs-path}}.js")] "</html>"
    [:script "$(window).load(function(){
      $(\"#featured\").orbit();
    });"
     ]
    ))

(defn index-page []
  (layout "Hello World"
      [:div.row [:div.twelve.columns
      [:h1 "Hello World"]
      [:p#clickable "Click me"]
      ]]
      
      ))

(defroutes main-routes
  (GET "/" [] (index-page))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
      (wrap-base-url)))

(defn -main [& args]
  (jetty/run-jetty app {:port 3000}))

