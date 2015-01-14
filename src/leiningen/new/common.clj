(ns leiningen.new.common
  (:require
    [leiningen.new.templates :refer [renderer ->files]]
    [clojure.pprint :refer [code-dispatch pprint with-pprint-dispatch]]))

(def dependency-indent 17)
(def plugin-indent 12)
(def root-indent 2)

(def render (renderer "luminus"))

(defn render-asset [options asset]
  (if (string? asset)
    asset
    (let [[target source] asset]
      [target (render source options)])))

(defn render-assets [assets options]
  (apply ->files options (map (partial render-asset options) assets)))

(defn pprint-code [code]
  (-> (pprint code)
      with-out-str
      (.replaceAll "," "")
      ;(.replaceAll "\\\\n" "\n")
      ))

(defn form->str [form]
  (let [text (pprint-code form)]
    (subs text 0 (count text))))

(defn indented-code [n form]
  (let [text (form->str form)
        indents (apply str (repeat n " "))]
    (.replaceAll (str text) "\n" (str "\n" indents))))

(defn indent [n form]
  (if (map? form)
    (indented-code n form)
    (->> form
         (map str)
         (clojure.string/join "\n"))))