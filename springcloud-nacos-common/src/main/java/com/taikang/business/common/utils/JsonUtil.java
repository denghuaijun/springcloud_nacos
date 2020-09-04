package com.taikang.business.common.utils;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings({"unchecked","rawtypes"})

public class JsonUtil {
    private static ConcurrentHashMap<String, String> pathCache = new ConcurrentHashMap<String, String>(1024, 0.75f, 1);

    public static JsonUtil instance = new JsonUtil();
    

    static {
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }

    public static String getStringValue(JSONObject jsonData, String path) {
        Object val = getValue(jsonData, path, false);
        return val != null ? val.toString() : null;
    }

    public static Object getValue(JSONObject jsonData, String path, boolean jsonPath_flag) {
        try {
            if (jsonPath_flag) {
                return JSONPath.eval(jsonData, path);
            }
            else {
                String path_new = transferToJsonPath(path);
                return JSONPath.eval(jsonData, path_new);
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    public static List getSubListFromJSON(JSONObject jsonData, String path) {
        List value = (List) getValue(jsonData, path, false);
        return value != null ? value : new ArrayList();
    }

    public static String transferToJsonPath(String path) {
        String path_inner = pathCache.get(path);

        if (path_inner == null) {
            path_inner = path.replaceAll("\\.([0-9]+)([\\.]?)", "[$1\\]$2");
            pathCache.putIfAbsent(path, path_inner);
        }

        return path_inner;
    }


    public static boolean setValueByJsonPath(JSONObject rootObject, String path, Object value) {
        try {
            JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
            JSONPath jsonpath = JSONPath.compile(path);
            return jsonpath.set(rootObject, value);
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean setValue(JSONObject rootObject, String path, Object value) {
        return setValue(rootObject, path, value, true);
    }

    public static boolean setValue(JSONObject rootObject, String path, Object value, boolean jsonPath_format) {
    	  String parentPath = jsonPath_format ? path : transferToJsonPath(path);

          String currentPath = "";
          String key = "";
          Object valueObj = value;

          boolean result = false;
          boolean lastArrayNode = false;

          while (parentPath != null && !(result = setValueByJsonPath(rootObject, parentPath, valueObj))) {

              int indexPos = parentPath.lastIndexOf(".");

              currentPath = parentPath.substring(indexPos + 1);

              if (indexPos >= 0) {
                  parentPath = parentPath.substring(0, indexPos);
              }
              else {
                  int indexParentPathPos2 = parentPath.indexOf('[');
                  if (indexParentPathPos2 > 0) {
                      lastArrayNode = true;
                  }
                  parentPath = null;
              }

              int indexPos2 = currentPath.indexOf('[');
              if (indexPos2 > 0) {
            key = currentPath.substring(0, indexPos2);

				  ArrayList tempObj = null;
                  if (parentPath != null) {

                      Object obj = getValue(rootObject, parentPath + "." + key, true);
                      if (!"".equals(obj)) {
                          tempObj = (ArrayList) obj;
                      }
                  }
                  else {
                      Object obj = getValue(rootObject, key, true);
                      if (!"".equals(obj)) {
                          tempObj = (ArrayList) obj;
                      }
                  }

                  if (tempObj == null) {
                      tempObj = new ArrayList();
                  }

                  tempObj.add(valueObj);
                  valueObj = tempObj;
              }
              else {
                  key = currentPath;
              }

              if (lastArrayNode) {
                  rootObject.put(key, valueObj);
              }
              else {
                  JSONObject tempObj = null;
                  Object obj = getValue(rootObject, parentPath, true);
                  if (!"".equals(obj)) {
                      tempObj = (JSONObject) obj;
                  }

                  if (tempObj == null) {
                      tempObj = new JSONObject();
                  }
                  tempObj.put(key, valueObj);
                  valueObj = tempObj;
              }
          }

          return result;
    }

    private static JSONObject merge(JSONObject target, Object source) {
        if (source == null) return target;
        if (source instanceof JSONObject) return merge(target, (JSONObject) source);
        throw new RuntimeException("JSON megre can not merge JSONObject with " + source.getClass());
    }

    private static JSONArray merge(JSONArray target, Object source) {
        if (source == null) return target;
        if (target instanceof JSONArray) return merge(target, (JSONArray) source);
        target.add(source);
        return target;
    }

    private static JSONArray merge(JSONArray target, JSONArray source) {
        target.addAll(source);
        return target;
    }

    public static JSONObject merge(JSONObject target, JSONObject source) {
        return merge(target, source, true, false);
    }

    public static JSONObject merge(JSONObject target, JSONObject source, boolean overwrite) {
        return merge(target, source, true, true);
    }


    public static JSONObject merge(JSONObject target, JSONObject source, boolean overwrite, boolean appendItemFlag) {
        if (source == null) return target;

        for (String key : target.keySet()) {
            Object value1 = target.get(key);
            Object value2 = source.get(key);
            if (value2 == null) continue;
            if (value1 instanceof JSONArray) {
                if (overwrite)
                    target.put(key, value2);

                else
                    target.put(key, merge((JSONArray) value1, value2));
                continue;
            }

            if (value1 instanceof JSONObject) {
                target.put(key, merge((JSONObject) value1, value2));
                continue;
            }

            if (value1.equals(value2)) {
                continue;
            }
            else if (overwrite) {
                target.put(key, value2);
                continue;
            }

            if (value1.getClass().equals(value2.getClass())) throw new RuntimeException(
                    "JSON merge can not merge two " + value1.getClass().getName() + " Object together");
            throw new RuntimeException(
                    "JSON merge can not merge " + value1.getClass().getName() + " with " + value2.getClass().getName());
        }

        if (appendItemFlag) {
            for (String key : source.keySet()) {
                if (target.containsKey(key)) continue;
                target.put(key, source.get(key));
            }
        }
        return target;
    }

    /**
     * 
     * @param json
     * @param path
     * @return
     */
    public static Object getObjectFromJson(JSONObject json, String path) {
        return JsonUtil.getValue(json, path, false);
    }

    /**
     * ֧�ֶ༶���
     * 
     * @param jsonData
     * @param path
     * @param jsonValue
     * @wangguangyu
     */
    public static void setStringValue(JSONObject jsonData, String path, String jsonValue) {
        setValue(jsonData, path, jsonValue, false);
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * Convert a JSONObject into a well-formed, element-normal XML string.
     * 
     * @param object
     *            A JSONObject.
     * @return A string.
     * @throws JSONException
     */
    public static String toXMLString(Object object) throws JSONException {
        return toXMLString(object, null);
    }

    public static String escape(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        for (int i = 0, length = string.length(); i < length; i++) {
            char c = string.charAt(i);
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Convert a JSONObject into a well-formed, element-normal XML string.
     * 
     * @param object
     *            A JSONObject.
     * @param tagName
     *            The optional name of the enclosing tag.
     * @return A string.
     * @throws JSONException
     */
    public static String toXMLString(Object object, String tagName) throws JSONException {
        StringBuilder sb = new StringBuilder();
        int i;
        JSONArray ja;
        JSONObject jo;
        String key;
        Iterator<String> keys;
        int length;
        String string;
        Object value;
        if (object instanceof JSONObject) {

            // Emit <tagName>

            if (tagName != null) {
                sb.append('<');
                sb.append(tagName);
                sb.append('>');
            }

            // Loop thru the keys.

            jo = (JSONObject) object;
            keys = jo.keySet().iterator();
            while (keys.hasNext()) {
                key = keys.next();
                value = jo.get(key);
                if (value == null) {
                    value = "";
                }
                string = value instanceof String ? (String) value : null;

                // Emit content in body

                if ("content".equals(key)) {
                    if (value instanceof JSONArray) {
                        ja = (JSONArray) value;
                        length = ja.size();
                        for (i = 0; i < length; i += 1) {
                            if (i > 0) {
                                sb.append('\n');
                            }
                            sb.append(escape(ja.get(i).toString()));
                        }
                    }
                    else {
                        sb.append(escape(value.toString()));
                    }

                    // Emit an array of similar keys

                }
                else if (value instanceof JSONArray) {
                    ja = (JSONArray) value;
                    length = ja.size();
                    for (i = 0; i < length; i += 1) {
                        value = ja.get(i);
                        if (value instanceof JSONArray) {
                            sb.append('<');
                            sb.append(key);
                            sb.append('>');
                            sb.append(toXMLString(value));
                            sb.append("</");
                            sb.append(key);
                            sb.append('>');
                        }
                        else {
                            sb.append(toXMLString(value, key));
                        }
                    }
                }
                else if ("".equals(value)) {
                    sb.append('<');
                    sb.append(key);
                    sb.append("/>");

                    // Emit a new tag <k>
                }
                else {
                    sb.append(toXMLString(value, key));
                }
            }
            if (tagName != null) {
                // Emit the </tagname> close tag
                sb.append("</");
                sb.append(tagName);
                sb.append('>');
            }
            return sb.toString();

            // XML does not have good support for arrays. If an array appears in
            // a place
            // where XML is lacking, synthesize an <array> element.

        }
        else {
            if (object instanceof JSONArray) {
                ja = (JSONArray) object;
                length = ja.size();
                for (i = 0; i < length; i += 1) {
                    sb.append(toXMLString(ja.get(i), tagName == null ? "array" : tagName));
                }
                return sb.toString();
            }
            else {
                string = (object == null) ? "null" : escape(object.toString());
                return (tagName == null) ? "\"" + string + "\""
                        : (string.length() == 0) ? "<" + tagName + "/>"
                                : "<" + tagName + ">" + string + "</" + tagName + ">";
            }
        }
    }

    private static JSONObject transferArrayToJSON(JSONArray value_arr, String indexKeyName) {
        JSONObject targetJson = new JSONObject();
        for (int i = 0; i < value_arr.size(); i++) {
            JSONObject json_item = (JSONObject) value_arr.get(i);
            targetJson.put(json_item.getString(indexKeyName), json_item);
        }
        return targetJson;
    }

    public static JSONArray mergerJson(JSONArray target, JSONArray source, String indexKeyName) {
        JSONObject targetJson = transferArrayToJSON(target, indexKeyName);
        JSONArray resultJson = new JSONArray();

        for (int i = 0; i < source.size(); i++) {
            JSONObject source_item = (JSONObject) source.get(i);
            if (targetJson.containsKey(source_item.getString(indexKeyName))) {
                JSONObject target_item = (JSONObject) targetJson.get(source_item.getString(indexKeyName));
                JSONObject merged_Json = merge(target_item, source_item);
                resultJson.add(merged_Json);
            }
        }

        return resultJson;
    }

    public static JSONObject mergerJson(JSONObject targetJsonObj, JSONObject sourceJsonObj, String arrayPath,
            String indexKeyName) {
    	JSONArray target=(JSONArray) JSONPath.eval(targetJsonObj, arrayPath);
    	JSONArray source=(JSONArray) JSONPath.eval(sourceJsonObj, arrayPath);

        setValue(sourceJsonObj, arrayPath, mergerJson(target, source, indexKeyName));
        return sourceJsonObj;
    }

}
