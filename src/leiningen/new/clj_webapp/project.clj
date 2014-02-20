(defproject {{name}}
   "0.1.0-SNAPSHOT"
   :description "FIXME: write this!"
   :url "http://example.com/FIXME"
   :dependencies [org.clojure/clojure "1.5.0"]
                  [org.clojure/tools.logging "0.2.6"]
                  [ring/ring-core "1.2.1"]
                  [ring/ring-jetty-adapter "1.2.1"]
                  [compojure "1.1.6"]
                  [lib-noir "0.8.1"]
                  [jayq "2.5.0"]
                  [crate "0.2.5"]
	
                  [com.cemerick/friend "0.2.0"]
                  ;; OAuth2.0 Support
                  [friend-oauth2 "0.1.1"]
                  [clj-http "0.7.9" :exclusions [commons-logging]]
                  [cheshire "5.3.1"]
                  [org.clojure/core.memoize "0.5.6"]
                  ;; /OAuth2.0 Support

                  [ch.qos.logback/logback-classic "1.1.1"]
           		]
               :plugins [[lein-ancient "0.5.4"]
               [lein-cljsbuild "1.0.2"]
             [lein-ring "0.8.10"]]
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
   :hooks [leiningen.cljsbuild;; Will cljsbuild upon startup
          leiningen.package.hooks.deploy ;; Will deploy configured packages to remote repos when lein deploy is issued
          leiningen.package.hooks.install] ;; Will install configured packages to local repo when lein install is issued.
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
                               :jar true}}}
:package {:skipjar true ;; If true, will not make a jar.  Defaults to false.
            :autobuild true;; If true, will automatically build the artifact if it
            ;; does not exist.  Defaults to false.
            :reuse false ;; If true, will use an existing artifact if available.
            ;; Defaults to false.
            :artifacts [ ;; Collection of artifacts to build/package/install/deploy
                         {:build "ring uberjar" ;; Lein command used to build the artifact. e.g ring war, ring uberwar, war, assemble
                          :classifier "standalone"
                          :extension "jar"
                          }]})

