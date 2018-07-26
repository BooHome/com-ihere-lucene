package com.ihere.lucene.util;

import com.ihere.lucene.config.LuceneConfig;
import com.ihere.lucene.ik.MyIKAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * @author fengshibo
 * @create 2018-07-20 14:38
 * @desc ${DESCRIPTION}
 **/
public class IndexWriterUtil {
    private static final Logger logger = LoggerFactory.getLogger(IndexWriterUtil.class);
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
                    logger.info("IndexWrite已正常close:{}",LocalDateTime.now().toString());
                } catch (Exception e) {
                    logger.error("IndexWrite在close时发生异常:{}",LocalDateTime.now().toString());
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
