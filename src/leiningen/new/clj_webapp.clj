(ns leiningen.new.clj-webapp
  "A Leiningen template for new web application projects"
  (:use [leiningen.new.templates :only [->files name-to-path sanitize-ns renderer year]]))

(def ^{:const true}
  project-version "1.0.0-SNAPSHOT")

(def render (renderer "clj-webapp"))

(defn clj-webapp
  "A Leiningen template for new web application projects"
  [name & features]
  (let [data {:name name
              :fs-path (name-to-path name)
              :sanitized-ns (sanitize-ns name)
              :year (year)
              }]
    (->files data
      [".gitignore" (render ".gitignore" data)]
      ["README.md" (render "README.md" data)]
      ["project.clj" (render "project.clj" data)]
      ["externs/jquery-1.8.js" (render "externs/jquery-1.8.js")]

      ;; logback-config
      ["resources/logback.xml" (render "resources/logback.xml" data)]
      ;; Robots
      ["resources/public/robots.txt" (render "resources/public/robots.txt" data)]
      ;; Foundation: Images
      ["resources/public/images/foundation/orbit/bullets.jpg" (render "resources/public/images/foundation/orbit/bullets.jpg")]
      ["resources/public/images/foundation/orbit/left-arrow-small.png" (render "resources/public/images/foundation/orbit/left-arrow-small.png")]
      ["resources/public/images/foundation/orbit/left-arrow.png" (render "resources/public/images/foundation/orbit/left-arrow.png")]
      ["resources/public/images/foundation/orbit/loading.gif" (render "resources/public/images/foundation/orbit/loading.gif")]
      ["resources/public/images/foundation/orbit/mask-black.png" (render "resources/public/images/foundation/orbit/mask-black.png")]
      ["resources/public/images/foundation/orbit/pause-black.png" (render "resources/public/images/foundation/orbit/pause-black.png")]
      ["resources/public/images/foundation/orbit/right-arrow-small.png" (render "resources/public/images/foundation/orbit/right-arrow-small.png")]
      ["resources/public/images/foundation/orbit/right-arrow.png" (render "resources/public/images/foundation/orbit/right-arrow.png")]
      ["resources/public/images/foundation/orbit/rotator-black.png" (render "resources/public/images/foundation/orbit/rotator-black.png")]
      ["resources/public/images/foundation/orbit/timer-black.png" (render "resources/public/images/foundation/orbit/timer-black.png")]
      ;; Foundation: Javascripts
      ["resources/public/javascripts/app.js" (render "resources/public/javascripts/app.js")]
      ["resources/public/javascripts/foundation.min.js" (render "resources/public/javascripts/foundation.min.js")]
      ;; Foundation: Stylesheets
      ["resources/public/stylesheets/app.css" (render "resources/public/stylesheets/app.css")]
      ["resources/public/stylesheets/foundation.min.css" (render "resources/public/stylesheets/foundation.min.css")]

      ;; Clojure
      ["src/clj/{{fs-path}}/server.clj" (render "src/clj/project_name/server.clj" data)]
      ["src/clj/{{fs-path}}/routes.clj" (render "src/clj/project_name/routes.clj" data)]
      ["src/clj/{{fs-path}}/views/common.clj" (render "src/clj/project_name/views/common.clj" data)]
      ["src/clj/{{fs-path}}/views/index.clj" (render "src/clj/project_name/views/index.clj" data)]
      ["src/clj/{{fs-path}}/config.clj" (render "src/clj/project_name/config.clj" data)]

      ;; Config
      ["resources/config.edn" (render "resources/config.edn" data)]
      ["resources/names.edn" (render "resources/names.edn" data)]


      ;; ClojureScript
      ["src/cljs/{{fs-path}}/client.cljs" (render "src/cljs/project_name/client.cljs" data)]

      ;; Midje
      ["test/clj/{{fs-path}}/test/models/example.clj" (render "test/clj/project_name/test/models/example.clj" data)]
      )))
