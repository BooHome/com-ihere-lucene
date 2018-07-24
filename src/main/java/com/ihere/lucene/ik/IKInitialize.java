package com.ihere.lucene.ik;

import org.apache.lucene.analysis.Analyzer;

/**
 * @author fengshibo
 * @create 2018-07-18 10:48
 * @desc ${DESCRIPTION}
 **/
public class IKInitialize {

    private static Analyzer analyzer = null;

    static {
        analyzer = new MyIKAnalyzer();
    }


    /*
     * 获取分词器
     * */
    public static Analyzer getAnalyzer() {
        return analyzer;
    }
}
