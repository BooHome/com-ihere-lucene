package com.ihere.lucene.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author fengshibo
 * @create 2018-07-17 16:24
 * @desc ${DESCRIPTION}
 **/
public class LuceneConfig  {
    private static String indexPath;
    private static String mydictPath;
    private static String stopdictPath;
    private static String synonymsPath;
    private static String idName;
    private static String ikUserSmart;
    private static String taskSmart;
    static
    {
        try {
            //获取当前类加载器
            ClassLoader classLoader=LuceneConfig.class.getClassLoader();
            //通过当前累加载器方法获得 文件db.properties的一个输入流
            InputStream is=classLoader.getResourceAsStream("search.properties");
            //创建一个Properties 对象
            Properties properties=new Properties();
            //加载输入流
            properties.load(is);
            Boolean isWindowsFlag=System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
            if(isWindowsFlag){
                indexPath=properties.getProperty("indexes.windows.index.path");
            }else{
                indexPath=properties.getProperty("indexes.linux.index.path");
            }
            mydictPath=properties.getProperty("indexes.mydict.path");
            stopdictPath=properties.getProperty("indexes.stopdict.path");
            synonymsPath=properties.getProperty("indexes.synonyms.path");
            idName=properties.getProperty("indexes.id.name");
            ikUserSmart=properties.getProperty("indexes.ik.usersmart");
            taskSmart=properties.getProperty("indexes.task.size");

        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static String getLuceneIndexPath(){
        return  indexPath;
    }
    public static String getIKMyDict(){
        return  mydictPath;
    }
    public static String getIKStopDict(){
        return  stopdictPath;
    }
    public static String getIKSysDict(){
        return  synonymsPath;
    }
    public static String getIDName(){
        return  idName;
    }
    public static Boolean getIKUserSmart(){
        if(ikUserSmart.equals("true")){
            return true;
        }
        return  false;
    }

    public static String getTaskListSize(){
        return  taskSmart;
    }

}
