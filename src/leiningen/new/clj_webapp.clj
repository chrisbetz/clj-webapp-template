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
      ["Procfile" (render "Procfile" data)] ;; this is for heroku support, make it optional?
      ["externs/jquery-1.8.js" (render "externs/jquery-1.8.js")]

      ;; logback-config
      ["resources/logback.xml" (render "resources/logback.xml" data)]
      ;; Robots
      ["resources/public/robots.txt" (render "resources/public/robots.txt" data)]
      ;; Foundation: Javascripts
      ["resources/public/js/vendor/custom.modernizr.js" (render "resources/public/js/vendor/custom.modernizr.js")]
      ["resources/public/js/vendor/zepto.js" (render "resources/public/js/vendor/zepto.js")]
      ["resources/public/js/foundation.min.js" (render "resources/public/js/foundation.min.js")]
      ;; Foundation: Stylesheets
      ["resources/public/css/app.css" (render "resources/public/css/app.css")]
      ["resources/public/css/normalize.css" (render "resources/public/css/normalize.css")]
      ["resources/public/css/foundation.min.css" (render "resources/public/css/foundation.min.css")]

      ;; Server
      ["src/{{fs-path}}/server/server.clj" (render "src/project_name/server/server.clj" data)]
      ["src/{{fs-path}}/server/routes.clj" (render "src/project_name/server/routes.clj" data)]
      ["src/{{fs-path}}/server/auth.clj" (render "src/project_name/server/auth.clj" data)]
      ["src/{{fs-path}}/server/users/users.clj" (render "src/project_name/server/users/users.clj" data)]
      ["src/{{fs-path}}/server/views/common.clj" (render "src/project_name/server/views/common.clj" data)]
      ["src/{{fs-path}}/server/views/index.clj" (render "src/project_name/server/views/index.clj" data)]
      ["src/{{fs-path}}/server/views/welcome.clj" (render "src/project_name/server/views/welcome.clj" data)]
      ["src/{{fs-path}}/server/config.clj" (render "src/project_name/server/config.clj" data)]

      ;; Client
      ["src/{{fs-path}}/client/main.cljs" (render "src/project_name/client/main.cljs" data)]

      ;; Crossover (both Client and Server)
      ["src/{{fs-path}}/crossover/common.clj" (render "src/project_name/crossover/common.clj" data)]

      ;; Config
      ["resources/config.edn" (render "resources/config.edn" data)]
      ["resources/oauth2.edn" (render "resources/oauth2.edn" data)] ;; this is for OAuth-Support


      ;; Midje
      ["test/{{fs-path}}/server/test/views/example.clj" (render "test/project_name/server/test/views/example.clj" data)]
      ["test/{{fs-path}}/crossover/test/models/example.clj" (render "test/project_name/crossover/test/models/example.clj" data)]
      )
    (println "Congratulations, you created your clj-webapp-project " (:name data) ".\nDon't forget to update resouces/oauth2.edn with your client-id, client-secret, and your callback-url.\n")
    ))
