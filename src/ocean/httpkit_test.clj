(ns
  ^{:author gavin}
  ocean.httpkit-test
  (:require [org.httpkit.client :as http])
  (:require [clojure.string :as str])
  (:gen-class)
  )

(defn generate_promise [ips]
  (loop [x ips rs []]
    (if (nil? (first x)) rs
      (recur (rest x) (do (conj rs {:p (http/get (str "http://" (first x)) options),:ip (first x)}))))))

(defn get_result [pms] (map (fn [x] (let [{:keys [opts error]} @(:p x)] (if error "-" (:ip x)))) pms))

(defn test_main3 [ips] (map (fn [x] (get_result (generate_promise x))) ips))

(def result (loop [x (test_main3 p_ips) b []]
              (if (nil? (first x)) b (recur (rest x) (do (Thread/sleep 20) (conj b (first x)))))))
(loop [x result] (if (nil? (first x)) nil (recur (do (prn (first x)) (rest x)))))
