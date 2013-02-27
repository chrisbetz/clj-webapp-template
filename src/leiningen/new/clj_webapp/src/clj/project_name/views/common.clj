(ns {{sanitized-ns}}.views.common
  (:use  [hiccup core page]))

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