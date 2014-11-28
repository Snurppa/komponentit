(ns lomakkeet.datepicker
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [cljs.core.async :refer [put!]]
            [schema.core :as s]
            [sablono.core :refer-macros [html]]
            [cljs-time.core :as t]
            [lomakkeet.fields :as f]))

(def LocalDate (s/pred t/date?))

(def ^:private pikaday-i18n
  #js {:previousMonth "Edellinen kuukakausi",
       :nextMonth     "Seuraava kuukausi",
       :months        #js ["Tammikuu","Helmikuu","Maaliskuu","Huhtikuu","Toukokuu","Kesäkuu","Heinäkuu","Elokuu","Elokuu","Lokakuu","Marraskuu","Joulukuu"],
       :weekdays      #js ["Sunnuntai","Maanantai","Tiistai","Keskiviikko","Torstai","Perjantai","Lauantai"],
       :weekdaysShort #js ["Su","Ma","Ti","Ke","To","Pe","La"]})

(defn jsdate->local-date [v]
  (doto (goog.date.Date.)
    (.setYear (.getFullYear v))
    (.setMonth (.getMonth v))
    (.setDate (.getDate v))))

(defn date->str [v]
  (str (.getDate v) "." (.getMonth v) "." (.getFullYear v)))

(defcomponent date*
  [data :- LocalDate
   owner
   {:keys [ch form-group korks]
    :as opts}]
  (init-state [_]
    {:error? false})
  (did-mount [_]
    (let [input (om/get-node owner "input")]
      (js/Pikaday. #js {:field input
                        ; NOTE: This requires MomentJS
                        :format "DD.MM.YYYY"
                        :firstDay 1
                        :onSelect (fn [v]
                                    (put! ch {:type :change
                                              :korks korks
                                              :value (jsdate->local-date v)}))
                        :i18n pikaday-i18n})))
  (render-state [_ {:keys [error?]}]
    (html
      (form-group
        opts
        [:input.form-control
         {:ref "input"
          :type "text"
          :value (or (date->str data) "")}]))))

(defn date [form label korks & [opts]]
  (f/build date* (merge form opts {:label label :korks korks})))
