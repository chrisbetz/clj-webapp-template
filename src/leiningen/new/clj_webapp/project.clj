(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring/ring-core "1.1.8"]
                 [ring/ring-jetty-adapter "1.1.8"]
                 [compojure "1.1.5"]
                 [lib-noir "0.4.2"]
                 [jayq "2.1.0"]
                 [crate "0.2.4"]
                 ]
  :plugins [[lein-cljsbuild "0.3.0"]
                        [lein-ring "0.8.2"]]
:profiles {:dev {:dependencies [[midje "1.4.0"]]
                 :plugins [[lein-midje "2.0.1"]]}}                        
  :ring {:handler {{sanitized-ns}}.server/app}
  :hooks [leiningen.cljsbuild]
  :source-paths ["src/clj"]
  :cljsbuild { 
    :builds {
      :main {
        :source-paths ["src/cljs"]
        :compiler {:output-to "resources/public/javascripts/{{fs-path}}.js"
                   :externs ["externs/jquery-1.8.js"]
                   :optimizations :simple
                   :pretty-print true}
        :jar true}}}
  :main ^{:skip-aot true} {{sanitized-ns}}.server)

