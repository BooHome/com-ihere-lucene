package com.ihere.lucene.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author fengshibo
 * @create 2018-07-24 18:37
 * @desc ${DESCRIPTION}
 **/
public class JsonValidate {
    /**
     * Google Gson
     *
     * @param jsonInString
     * @return
     */
    public final static boolean isJSONValid(String jsonInString) {
        if (StringUtils.isEmpty(jsonInString) || jsonInString.trim().equals("")) {
            return false;
        }
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.create().fromJson(jsonInString, Map.class);
            return true;
        } catch (Exception ex) {
            try {
                GsonBuilder gsonBuilder = new GsonBuilder();
                List<Map<String,String>> list=gsonBuilder.create().fromJson(jsonInString, List.class);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
