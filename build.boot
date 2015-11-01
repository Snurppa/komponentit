(set-env!
  :source-paths #{"example-src/cljs" "example-src/less" "example-src/html"}
  :resource-paths #{"src/cljs" "src/less"}
  :dependencies '[[org.clojure/clojure    "1.7.0"      :scope "provided"]
                  [org.clojure/clojurescript "1.7.145" :scope "provided"]
                  [boot/core              "2.4.2"      :scope "test"]
                  [adzerk/boot-cljs       "1.7.48-6"   :scope "test"]
                  [adzerk/boot-cljs-repl  "0.2.0"      :scope "test"]
                  [adzerk/boot-reload     "0.4.1"      :scope "test"]
                  [deraen/boot-less       "0.4.2"      :scope "test"]
                  [pandeiro/boot-http     "0.7.0"      :scope "test"]

                  [org.clojure/core.async "0.2.371"]
                  [prismatic/schema "1.0.3"]
                  [metosin/schema-tools "0.6.2"]
                  [com.andrewmcveigh/cljs-time "0.3.14"]
                  [cljsjs/pikaday "1.3.2-0"]
                  [reagent "0.5.1"]

                  ; LESS
                  [org.webjars/bootstrap "3.3.4"]])

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl repl-env]]
  '[adzerk.boot-reload    :refer [reload]]
  '[deraen.boot-less      :refer [less]]
  '[pandeiro.boot-http    :refer [serve]])

(task-options!
  pom {:project 'metosin/lomakkeet
       :version "0.2.1-SNAPSHOT"
       :description "Proof of concept: Form library for Reagent"
       :license {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}}
  cljs {:source-map true}
  less {:source-map true})

(deftask dev
  []
  (comp
    (watch)
    (less)
    (reload :on-jsload 'example.reagent/restart!)
    (cljs-repl)
    (cljs)
    (serve :resource-root "")))