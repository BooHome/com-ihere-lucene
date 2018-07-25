package com.ihere.lucene.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.springframework.util.StringUtils;

/**
 * @author fengshibo
 * @create 2018-07-24 18:37
 * @desc ${DESCRIPTION}
 **/
public class JsonUtil {
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
            gsonBuilder.create().fromJson(jsonInString, Object.class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }
}
