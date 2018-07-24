package com.ihere.lucene.util;

import com.google.gson.Gson;
import com.ihere.lucene.config.LuceneConfig;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fengshibo
 * @create 2018-07-18 14:41
 * @desc ${DESCRIPTION}
 **/
public class DocumentUtil {

    public static Document jsonToDoc( String json){
        Gson gson=new Gson();
        Map<String, String> map = gson.fromJson(json, Map.class);
        Document doc =DocumentUtil.mapToDoc(map);
        return doc;
    }

    public static Document mapToDoc( Map<String, String> map){
        Document doc = new Document();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if(entry.getKey().equals(LuceneConfig.getIDName())){
                StringField stringField = new StringField(entry.getKey(),entry.getValue() , Field.Store.YES);
                doc.add(stringField);// 将属性以及值存储
            }else {
                TextField textField = new TextField(entry.getKey(), entry.getValue(), Field.Store.YES);
                doc.add(textField);// 将属性以及值存储
            }
        }
        return doc;
    }

    public static List<Document> jsonToDocs(String json){
        Gson gson=new Gson();
        List<Map<String, String>> list = gson.fromJson(json, List.class);
        List<Document> documents=new ArrayList<>();
        for (Map<String, String> map:
             list) {
            Document doc = DocumentUtil.mapToDoc(map);
            documents.add(doc);
        }
        return documents;
    }


}
