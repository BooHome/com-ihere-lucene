package com.ihere.lucene.util;

import com.ihere.lucene.config.LuceneConfig;
import com.ihere.lucene.ik.MyIKAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

/**
 * @author fengshibo
 * @create 2018-07-20 14:38
 * @desc ${DESCRIPTION}
 **/
public class IndexWriterUtil {

    private static IndexWriter indexWriter;

    static {
        try {
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new MyIKAnalyzer());
            indexWriter = new IndexWriter( FSDirectory.open(Paths.get(LuceneConfig.getLuceneIndexPath())), indexWriterConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 当前线程结束时，自动关闭IndexWriter，使用Runtime对象
         */
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try {
                    closeIndexWriter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取indexWriter
     * @return
     */
    public static IndexWriter getIndexWriter() {
        return indexWriter;
    }

    /**
     * 关闭IndexWriter
     * @throws Exception
     */
    public static void closeIndexWriter() throws Exception {
        if(indexWriter != null) {
            indexWriter.close();
        }
    }

}
