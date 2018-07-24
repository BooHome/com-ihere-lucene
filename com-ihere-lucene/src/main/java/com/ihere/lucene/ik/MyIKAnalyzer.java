package com.ihere.lucene.ik;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * IK分词器，Lucene Analyzer接口实现
 * 兼容Lucene 5.4.0版本
 */
public final class MyIKAnalyzer extends Analyzer {

    private boolean useSmart;

    public boolean useSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    /**
     * IK分词器Lucene 5.4.0 Analyzer接口实现类
     *
     * 默认细粒度切分算法
     */
    public MyIKAnalyzer() {
        this(false);
    }

    /**
     * IK分词器Lucene 5.4.0 Analyzer接口实现类
     *
     * @param useSmart
     *            当为true时，分词器进行智能切分
     */
    public MyIKAnalyzer(boolean useSmart) {
        super();
        this.useSmart = useSmart;
    }

    /**
     * 重载Analyzer接口，构造分词组件
     *
     * @param fieldName
     *            the name of the fields content passed to the
     *            TokenStreamComponents sink as a reader
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer _IKTokenizer = new MyIKTokenizer(this.useSmart());
        return new TokenStreamComponents(_IKTokenizer);
    }
}
