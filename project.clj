(defproject nutrition-searcher "0.1.0-SNAPSHOT"
  :description "Find foods suitable for a restricted diet"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
  [seesaw "1.4.5"]]
  :resources ["resources"]
  :main ^:skip-aot nutrition-searcher.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
