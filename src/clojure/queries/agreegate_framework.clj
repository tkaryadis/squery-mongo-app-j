(ns queries.agreegate-framework
  (:refer-clojure :only [])
  (:use cmql-core.operators.operators
        cmql-core.operators.stages
        cmql-core.operators.options
        cmql-j.driver.cursor
        cmql-j.driver.document
        cmql-j.driver.settings
        cmql-j.driver.transactions
        cmql-j.driver.utils
        cmql-j.arguments
        cmql-j.commands
        cmql-j.macros
        clojure.pprint)
  (:refer-clojure)
  (:require [clojure.core :as c])
  (:import (com.mongodb.client MongoClients MongoCollection MongoClient)
           (com.mongodb MongoClientSettings)))

(defn connect []
  ;;here randomly we choose to get Documents that are Clojure maps
  (update-defaults :client-settings (-> (MongoClientSettings/builder)
                                        (.codecRegistry clj-registry) ;;Remove this if you want to decode in Java Document
                                        (.build)))

  (update-defaults :client (MongoClients/create ^MongoClientSettings (defaults :client-settings))))

;;q is called like a command,but internally the driver method is used because it returns cursor
(defn aggregate-commands []
  (println "==> 3 most densely populated cities in Texas")
  (c-print-all (q :sample_training.zips
                  (= :state "TX")
                  (group :city
                         {:totalPop (sum :pop)})
                  (sort :!totalPop)
                  (limit 3)))
  (println "==> 3 most popular tags and their posts titles")
  (c-print-all (q :sample_training.posts
                  (unwind :tags)
                  (group :tags
                         {:count (sum 1)
                          :titles (conj-each :title)})
                  (sort :!count)
                  (limit 3)
                  [{:tag :tags} :count :titles])))

(defn aggregate-methods []
  (let [db (.getDatabase (defaults :client) "sample_training")
        zips (.getCollection (.getDatabase (defaults :client) "sample_training") "zips")
        posts (.getCollection (.getDatabase (defaults :client) "sample_training") "posts")]
    (println "==> 3 most densely populated cities in Texas")
    (c-print-all (.aggregate zips
                             (p (= :state "TX")
                                (group :city
                                       {:totalPop (sum :pop)})
                                (sort :!totalPop)
                                (limit 3))))
    (println "==> 3 most popular tags and their posts titles")
    (c-print-all (.aggregate posts
                             (p (unwind :tags)
                                (group :tags
                                       {:count (sum 1)
                                        :titles (conj-each :title)})
                                (sort :!count)
                                (limit 3)
                                [{:tag :tags} :count :titles])))))