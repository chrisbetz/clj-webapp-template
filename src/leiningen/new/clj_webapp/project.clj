(defproject {{name}}
   "0.1.0-SNAPSHOT"
   :description "FIXME: write this!"
   :url "http://example.com/FIXME"
   :dependencies [[org.clojure/clojure "1.5.0"]
                  [org.clojure/tools.logging "0.2.4"]
                  [ring/ring-core "1.1.8"]
                  [ring/ring-jetty-adapter "1.1.8"]
                  [compojure "1.1.5"]
                  [lib-noir "0.4.2"]
                  [jayq "2.1.0"]
                  [crate "0.2.4"]

                  [com.cemerick/friend "0.1.3"]
                  ;; OAuth2.0 Support
                  [friend-oauth2 "0.0.2"]
                  [clj-http "0.6.4" :exclusions [commons-logging]]
                  [cheshire "5.0.2"]
                  [org.clojure/core.memoize "0.5.2"]
                  ;; /OAuth2.0 Support

                  [ch.qos.logback/logback-classic "1.0.9"]]
   :plugins [[lein-cljsbuild "0.3.0"]
             [lein-ring "0.8.2"]]
   :profiles {:production {:ring {:open-browser? false, :stacktraces? false, :auto-reload? false}}
              :dev {:dependencies [[midje "1.4.0"]
                                   [bultitude "0.2.2"]
                                   [ring-mock "0.1.3"]
                                   [ring/ring-devel "1.1.0"]]
                    :plugins [[lein-midje "2.0.1"]]
                    :cljsbuild {:builds {:main {:compiler {:optimizations :simple
                                                           :pretty-print true}}}}
                    }}
   :ring {:handler {{sanitized-ns}}.server.routes/app
          :init {{sanitized-ns}}.server.server/init
          :destroy {{sanitized-ns}}.server.server/destroy
         }
   :hooks [leiningen.cljsbuild]
   :source-paths ["src"]
   :test-paths ["test"]
   :cljsbuild {:crossovers [{{sanitized-ns}}.crossover]
               :crossover-path "crossover-cljs"
               :crossover-jar true
               :builds {:main {:source-paths ["src/{{fs-path}}/client"]
                               :compiler {:output-to "resources/public/js/app.js"
                                          :externs ["externs/jquery-1.8.js"]
                                          :optimizations :advanced
                                          :pretty-print false}
                               :jar true}}})

