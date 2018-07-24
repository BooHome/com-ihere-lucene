package com.ihere.lucene.help.impl;

import com.google.gson.Gson;
import com.ihere.lucene.config.LuceneConfig;
import com.ihere.lucene.entity.IndexResultEntity;
import com.ihere.lucene.entity.ResultEntity;
import com.ihere.lucene.entity.TaskEntity;
import com.ihere.lucene.enums.IndexOperationTypeEnum;
import com.ihere.lucene.enums.OperationTypeEnum;
import com.ihere.lucene.help.LuceneHelper;
import com.ihere.lucene.ik.IKInitialize;
import com.ihere.lucene.ik.MyIKAnalyzer;
import com.ihere.lucene.task.Task;
import com.ihere.lucene.util.DocumentUtil;
import com.ihere.lucene.util.IndexWriterUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.synonym.SynonymFilterFactory;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.FilesystemResourceLoader;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author fengshibo
 * @create 2018-07-18 11:03
 * @desc ${DESCRIPTION}
 **/
@Component
public class LuceneHelperImpl implements LuceneHelper {


    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    private IndexReader reader = null;

    /**
     * @param field     查询类型
     * @param keywords  关键字
     * @param pageIndex 页码
     * @param pageSize  每页条数
     * @return
     * @throws Exception
     */
    public TopDocs findIndexTopDocsPage(String field, String keywords, int pageIndex, int pageSize) throws Exception {
        Query query = getQuery(field, keywords);
        //不同的规则构造不同的子类...
        TopDocs topDocs = this.searchPageByAfter(query, pageIndex, pageSize);
        return topDocs;
    }

    /**
     * @param field
     * @param keywords
     * @param num
     * @return
     * @throws Exception
     */
    public TopDocs findIndexTopDocsByNum(String field, String keywords, int num) throws Exception {
        Query query = getQuery(field, keywords);
        //不同的规则构造不同的子类...
        TopDocs topDocs = this.searchByNum(query, num);
        return topDocs;
    }

    public IndexResultEntity findIndexResultPage(String field, String keywords, int pageIndex, int pageSize) {
        IndexResultEntity indexResultEntity = new IndexResultEntity();
        try {
            TopDocs topDocs = findIndexTopDocsPage(field, keywords, pageIndex, pageSize);
            indexResultEntity = topDocsToEntity(topDocs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return indexResultEntity;
    }

    @Override
    public IndexResultEntity findIndexResultByNum(String field, String keywords, int num) {
        IndexResultEntity indexResultEntity = new IndexResultEntity();
        try {
            TopDocs topDocs = findIndexTopDocsByNum(field, keywords, num);
            indexResultEntity = topDocsToEntity(topDocs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return indexResultEntity;
    }

    @Override
    public IndexResultEntity findIndexResultByNum(String[] fields, String keywords, int num) {
        IndexResultEntity indexResultEntity = new IndexResultEntity();
        try {
            TopDocs topDocs = findIndexTopDocsByNum(fields, keywords, num);
            indexResultEntity = topDocsToEntity(topDocs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return indexResultEntity;
    }

    public IndexResultEntity findIndexResultPage(String fields[], String keywords, int pageIndex, int pageSize) {
        IndexResultEntity indexResultEntity = new IndexResultEntity();
        try {
            TopDocs topDocs = findIndexTopDocsPage(fields, keywords, pageIndex, pageSize);
            indexResultEntity = topDocsToEntity(topDocs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return indexResultEntity;
    }

    @Override
    public IndexResultEntity findIndexHighLighterResultPage(String field, String keywords, int fragmentSize, int pageIndex, int pageSize) {
        IndexResultEntity indexResultEntity = new IndexResultEntity();
        try {
            TopDocs topDocs = findIndexTopDocsPage(field, keywords, pageIndex, pageSize);
            Query query = getQuery(field, keywords);
            indexResultEntity = docsToHighlighter(query, topDocs, fragmentSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return indexResultEntity;
    }

    @Override
    public IndexResultEntity findIndexHighLighterResultByNum(String field, String keywords, int fragmentSize, int num) {
        IndexResultEntity indexResultEntity = new IndexResultEntity();
        try {
            TopDocs topDocs = findIndexTopDocsByNum(field, keywords, num);
            Query query = getQuery(field, keywords);
            indexResultEntity = docsToHighlighter(query, topDocs, fragmentSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return indexResultEntity;
    }

    @Override
    public IndexResultEntity findIndexHighLighterResultByNum(String[] fields, String keywords, int fragmentSize, int num) {
        IndexResultEntity indexResultEntity = new IndexResultEntity();
        try {
            TopDocs topDocs = findIndexTopDocsByNum(fields, keywords, num);
            Query query = getQuery(fields, keywords);
            indexResultEntity = docsToHighlighter(query, topDocs, fragmentSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return indexResultEntity;
    }

    @Override
    public IndexResultEntity findIndexHighLighterResultPage(String[] fields, String keywords, int fragmentSize, int pageIndex, int pageSize) {
        IndexResultEntity indexResultEntity = new IndexResultEntity();
        try {
            TopDocs topDocs = findIndexTopDocsPage(fields, keywords, pageIndex, pageSize);
            Query query = getQuery(fields, keywords);
            indexResultEntity = docsToHighlighter(query, topDocs, fragmentSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return indexResultEntity;
    }


    public TopDocs findIndexTopDocsPage(String fields[], String keywords, int pageIndex, int pageSize) throws Exception {
        Query query = getQuery(fields, keywords);
        TopDocs topDocs = this.searchPageByAfter(query, pageIndex, pageSize);
        return topDocs;
    }

    public TopDocs findIndexTopDocsByNum(String[] fields, String keywords, int num) throws Exception {
        Query query = getQuery(fields, keywords);
        TopDocs topDocs = this.searchByNum(query, num);
        return topDocs;
    }

    /**
     * 新增一条索引
     *
     * @param json
     */
    public Integer addIndex(String json) {
        try {
            this.setUpContentNoHave(DocumentUtil.jsonToDoc(json));
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 批量新增索引
     *
     * @param json
     */
    public Integer addIndexs(String json) {
        try {
            List<Document> documents = DocumentUtil.jsonToDocs(json);
            this.setUpContentNoHaves(documents);
            return documents.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 更新一条索引
     *
     * @param json
     * @return
     */
    public boolean updateIndex(String json) {
        try {
            this.updateContentNoHave(DocumentUtil.jsonToDoc(json));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateIndexs(String json) {
        try {
            this.updateContentNoHave(DocumentUtil.jsonToDocs(json));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除一条索引
     *
     * @param id
     * @return
     */
    public boolean delIndex(String id) {
        try {
            this.deleteBeforeMerge(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delIndexs(List<String> ids) {
        try {
            this.deleteBeforeMerge(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 文档转实体类
     *
     * @param topDocs
     * @return
     */
    public IndexResultEntity topDocsToEntity(TopDocs topDocs) {
        IndexResultEntity indexResultEntity = new IndexResultEntity();
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List<ResultEntity> list = new ArrayList<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            ResultEntity resultEntity = new ResultEntity();
            Map<String, String> map = docToMap(scoreDoc);
            resultEntity.setMap(map);
            resultEntity.setScore(scoreDoc.score);
            list.add(resultEntity);
        }
        indexResultEntity.setList(list);
        indexResultEntity.setTotal(topDocs.totalHits);
        return indexResultEntity;
    }

    /**
     * 文档转map
     *
     * @param scoreDoc
     * @return
     */
    public Map<String, String> docToMap(ScoreDoc scoreDoc) {
        Map<String, String> map = new HashMap<>();
        Document doc = this.findDoc(scoreDoc);
        List<IndexableField> fields = doc.getFields();
        for (IndexableField filed :
                fields) {
            map.put(filed.name(), doc.get(filed.name()));
        }
        return map;
    }

    /**
     * 文章转list<map>
     *
     * @param scoreDocs
     * @return
     */
    public List<Map<String, String>> docToListMap(ScoreDoc[] scoreDocs) {
        List<Map<String, String>> list = new ArrayList<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            list.add(docToMap(scoreDoc));
        }
        return list;
    }

    /**
     * 根据query  文档结果列表  摘要长度     查询高亮结果集  以实体类返回
     *
     * @param query
     * @param topDocs
     * @param fragmentSize
     * @return
     */
    public IndexResultEntity docsToHighlighter(Query query, TopDocs topDocs, int fragmentSize) {
        IndexResultEntity indexResultEntity = new IndexResultEntity();
        List<ResultEntity> resultEntities = new ArrayList<>();
        ScoreDoc scoreDoc[] = topDocs.scoreDocs;
        /**添加设置文字高亮begin*/
        //htmly页面高亮显示的格式化，默认是<b></b>即加粗
        Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
        Scorer scorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(formatter, scorer);
        //设置文字摘要（高亮的部分），此时摘要大小为         int fragmentSize
        Fragmenter fragmenter = new SimpleFragmenter();
        ((SimpleFragmenter) fragmenter).setFragmentSize(fragmentSize);
        highlighter.setTextFragmenter(fragmenter);
        for (int i = 0; i < scoreDoc.length; i++) {
            ResultEntity resultEntity = new ResultEntity();
            Map<String, String> map = new HashMap<>();
            Document doc = this.findDoc(scoreDoc[i]);
            List<IndexableField> fields = doc.getFields();
            for (IndexableField filed :
                    fields) {
                TokenStream tokenStream = null;
                try {
                    if (filed.name().equals(LuceneConfig.getIDName())) {
                        map.put(filed.name(), doc.get(filed.name()));
                    } else {
                        tokenStream = IKInitialize.getAnalyzer().tokenStream(filed.name(), new StringReader(doc.get(filed.name())));
                        String str = highlighter.getBestFragment(tokenStream, doc.get(filed.name()));
                        if (str == null) {
                            map.put(filed.name(), doc.get(filed.name()));
                        } else {
                            map.put(filed.name(), str);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (InvalidTokenOffsetsException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            resultEntity.setMap(map);
            resultEntity.setScore(scoreDoc[i].score);
            resultEntities.add(resultEntity);
        }
        indexResultEntity.setTotal(topDocs.totalHits);
        indexResultEntity.setList(resultEntities);
        return indexResultEntity;
    }

    /**
     * 根据   查询列和关键字  生成query条件
     *
     * @param field
     * @param keywords
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public Query getQuery(String field, String keywords) throws ParseException, IOException {
        /**同义词处理*/
        String result = this.displayTokens(this.convertSynonym(this.analyzeChinese(keywords, LuceneConfig.getIKUserSmart())));
        Analyzer analyzer = new MyIKAnalyzer(); // IK分词器
        QueryParser parser = new QueryParser(field, analyzer);// 在哪查询，第一个参数为查询的Document，在Indexer中创建了
        Query query = parser.parse(result);// 对字段进行解析后返回给查询
        return query;
    }

    /**
     * 根据   多个查询列和关键字  生成query条件
     *
     * @param fields
     * @param keywords
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public Query getQuery(String fields[], String keywords) throws ParseException, IOException {
        /**同义词处理*/
        String result = this.displayTokens(this.convertSynonym(this.analyzeChinese(keywords, true)));
        //需要根据哪几个字段进行检索...
        //查询分析程序（查询解析）
        QueryParser queryParser = new MultiFieldQueryParser(fields, IKInitialize.getAnalyzer());
        //不同的规则构造不同的子类...
        //title:keywords content:keywords
        Query query = queryParser.parse(result);
        return query;
    }


    /**
     * 获取IndexWriter实例
     *
     * @return
     * @throws Exception
     */
    private IndexWriter getWriter() throws Exception {
       /* Directory dir = FSDirectory.open(Paths.get(LuceneConfig.getLuceneIndexPath()));
        Analyzer analyzer = new MyIKAnalyzer(); // IK分词器
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, iwc);*/
        return IndexWriterUtil.getIndexWriter();
    }

    /**
     * 添加文档
     *
     * @param doc
     * @throws Exception
     */
    public void setUpContentNoHave(Document doc) throws Exception {
        IndexWriter writer = this.getWriter();// 得到索引
        writer.addDocument(doc); // 添加文档
        if (writer != null) {

            writer.commit();
            //writer.close();
        }
    }

    /**
     * 添加文档
     *
     * @param docs
     * @throws Exception
     */
    public void setUpContentNoHaves(List<Document> docs) throws Exception {
        IndexWriter writer = this.getWriter();// 得到索引
        for (Document doc : docs) {
            writer.addDocument(doc); // 添加文档
        }
        if (writer != null) {

            writer.commit();
            //writer.close();
        }
    }

    /**
     * 删除 在合并前
     *
     * @param id
     * @throws Exception
     */
    public void deleteBeforeMerge(String id) throws Exception {
        IndexWriter writer = this.getWriter();
        writer.deleteDocuments(new Term(LuceneConfig.getIDName(), id));// term：根据id
        writer.commit();
        //writer.close();
    }

    /**
     * 删除 在合并前
     *
     * @param ids
     * @throws Exception
     */
    public void deleteBeforeMerge(List<String> ids) throws Exception {
        IndexWriter writer = this.getWriter();
        Term[] terms = new Term[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            terms[i] = new Term(LuceneConfig.getIDName(), ids.get(i));
        }
        writer.deleteDocuments(terms);// term：根据id
        writer.commit();
        //writer.close();
    }

    /**
     * 删除礼批量 在合并前
     *
     * @param ids
     * @throws Exception
     */
    public void deleteBeforeMergeBatch(List<String> ids) throws Exception {
        IndexWriter writer = this.getWriter();
        for (String string :
                ids) {
            writer.deleteDocuments(new Term(LuceneConfig.getIDName(), string));// term：根据id
        }
        writer.commit();
        //writer.close();
    }

    /**
     * 删除 在合并后
     *
     * @param id
     * @throws Exception
     */
    public void deleteAfterMerge(Long id) throws Exception {
        IndexWriter writer = this.getWriter();
        writer.deleteDocuments(new Term(LuceneConfig.getIDName(), String.valueOf(id)));
        writer.forceMergeDeletes(); // 强制删除
        writer.commit();
        //writer.close();
    }

    /**
     * 删除批量 在合并后
     *
     * @param ids
     * @throws Exception
     */
    public void deleteAfterMergeBatch(List<String> ids) throws Exception {
        IndexWriter writer = this.getWriter();
        for (String string :
                ids) {
            writer.deleteDocuments(new Term(LuceneConfig.getIDName(), string));// term：根据id
        }
        writer.forceMergeDeletes(); // 强制删除
        writer.commit();
        //writer.close();
    }


    /**
     * 单条记录更新
     *
     * @param document
     * @throws Exception
     */
    public void updateContentNoHave(Document document) throws Exception {
        IndexWriter writer = this.getWriter();
        writer.updateDocument(new Term(LuceneConfig.getIDName(), document.get(LuceneConfig.getIDName())), document);
        writer.commit();
        //writer.close();
    }


    /**
     * 批量记录更新
     *
     * @param documents
     * @throws Exception
     */
    public void updateContentNoHave(List<Document> documents) throws Exception {
        IndexWriter writer = this.getWriter();
        for (int i = 0; i < documents.size(); i++) {
            writer.updateDocument(new Term(LuceneConfig.getIDName(), documents.get(i).get(LuceneConfig.getIDName())), documents.get(i));
        }
        writer.commit();
        //writer.close();
    }

    /**
     * 测试读取文档
     *
     * @throws Exception
     */
    public String indexReader() {
        Directory dir = null;// 得到luceneIndex目录
        try {
            dir = FSDirectory.open(Paths.get(LuceneConfig.getLuceneIndexPath()));
            IndexReader reader = DirectoryReader.open(dir);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[最大文档数：" + reader.maxDoc());
            stringBuffer.append("]  [ ");
            stringBuffer.append("实际文档数：" + reader.numDocs());
            stringBuffer.append("]");
            reader.close();
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * true 不能被调用
     *
     * @return
     */
    public Boolean isWriteLock() {
        try {
            Directory dir = FSDirectory.open(Paths.get(LuceneConfig.getLuceneIndexPath()));// 得到luceneIndex目录
            dir.obtainLock(IndexWriter.WRITE_LOCK_NAME).close();
            return false;
        } catch (Exception failed) {
            return true;
        }
    }


    /**
     * 获取IndexReader实例
     *
     * @return
     * @throws Exception
     */
    public IndexSearcher getIndexSearcher() throws Exception {
        Directory dir = FSDirectory.open(Paths.get(LuceneConfig.getLuceneIndexPath()));// 打开目录
        reader = DirectoryReader.open(dir);// 进行读取
        IndexSearcher is = new IndexSearcher(reader);// 索引查询器
        return is;
    }

    /***
     * 在使用时，searchAfter查询的是指定页数后面的数据，效率更高，推荐使用
     * @param q
     * @param pageIndex
     * @param pageSize
     */
    public TopDocs searchPageByAfter(Query q, int pageIndex, int pageSize) {
        TopDocs tds = null;
        try {
            IndexSearcher searcher = getIndexSearcher();
            //先获取上一页的最后一个元素
            ScoreDoc lastSd = getLastScoreDoc(pageIndex, pageSize, q, searcher);
            //通过最后一个元素搜索下页的pageSize个元素
            tds = searcher.searchAfter(lastSd, q, pageSize);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tds;
    }

    /***
     * 在使用时，searchAfter查询的是指定页数后面的数据，效率更高，推荐使用
     * @param q
     */
    public TopDocs searchByNum(Query q, Integer num) {
        TopDocs tds = null;
        try {
            IndexSearcher searcher = getIndexSearcher();
            //通过最后一个元素搜索下页的pageSize个元素
            tds = searcher.search(q, num);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tds;
    }

    /**
     * 根据页码和分页大小获取上一次的最后一个ScoreDoc
     *
     * @param pageIndex
     * @param pageSize
     * @param query
     * @param searcher
     * @return
     * @throws IOException
     */
    private ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher searcher) throws IOException {
        if (pageIndex <= 1) return null;//如果是第一页就返回空
        int num = pageSize * (pageIndex - 1);//获取上一页的数量
        TopDocs tds = searcher.search(query, num);
        if (tds.totalHits == 0) {
            return null;
        }
        return tds.scoreDocs[num - 1];
    }

    /**
     * 根据   文档得分明细  查询出文档明细
     *
     * @param scoreDoc
     * @return
     */
    public Document findDoc(ScoreDoc scoreDoc) {
        Document document = null;
        IndexSearcher searcher = null;
        try {
            searcher = getIndexSearcher();
            document = searcher.doc(scoreDoc.doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * 此方法描述的是：进行中文拆分
     */
    public String analyzeChinese(String input, boolean userSmart) throws IOException {
        StringBuffer sb = new StringBuffer();
        StringReader reader = new StringReader(input.trim());
        // true　用智能分词　，false细粒度
        IKSegmenter ikSeg = new IKSegmenter(reader, userSmart);
        for (Lexeme lexeme = ikSeg.next(); lexeme != null; lexeme = ikSeg.next()) {
            sb.append(lexeme.getLexemeText()).append(" ");
        }
        return sb.toString();
    }

    /**
     * 此方法描述的是：针对上面方法拆分后的词组进行同义词匹配，返回TokenStream
     * synonyms.txt：同义词表，在resources目录下
     */
    public TokenStream convertSynonym(String input) throws IOException {
        Version ver = Version.LUCENE_5_3_1;
        Map<String, String> filterArgs = new HashMap<String, String>();
        filterArgs.put("luceneMatchVersion", ver.toString());
        filterArgs.put("synonyms", "synonyms.txt");
        filterArgs.put("expand", "true");
        SynonymFilterFactory factory = new SynonymFilterFactory(filterArgs);
        factory.inform(new FilesystemResourceLoader(Paths.get(LuceneConfig.getLuceneIndexPath())));
        Analyzer IKAnalyzer = new MyIKAnalyzer();
        TokenStream ts = factory.create(IKAnalyzer.tokenStream("someField", input));
        return ts;
    }

    /**
     * 分词
     * 此方法描述的是：将tokenstream拼成一个特地格式的字符串，交给IndexSearcher来处理
     */
    public String displayTokens(TokenStream ts) throws IOException {
        StringBuffer sb = new StringBuffer();
        CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        while (ts.incrementToken()) {
            String token = termAttr.toString();
            sb.append(token).append(" ");
            //System.out.print(token + "|");
        }
        // System.out.println();
        ts.end();
        ts.close();
        return sb.toString();
    }

    @Override
    public Boolean addTaskLinkList(TaskEntity taskEntity) {
        return Task.taskQueue.add(taskEntity);
    }

    /**
     * 数据
     * 当新增时用 IndexOperationTypeEnum.ADD
     * 新增一条为  AddIndexOperationTypeEnum.ONE
     * 数据格式： {'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}
     * 新增多条为  AddIndexOperationTypeEnum.MORE
     * 数据格式为： [{'id':'1','title':'这是我第一次测试的内容我是','content':'这是我第一次测试的内容'},{'id':'3','title':'这是我第一次测试的内容','content':'这是我第一次测试的内容'},{'id':'1','title':'这是我第一次','content':'这是我第一次测试的内容'}}]
     * <p>
     * 当修改时用 IndexOperationTypeEnum.EDIT
     * 数据格式： {'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}
     * <p>
     * 当删除时用 IndexOperationTypeEnum.DEL
     * 数据格式（因为使用ID，只传单个id即可）： 1
     * <p>
     * 例：
     * 新增为   new TaskEntity("{'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}"，IndexOperationTypeEnum.ADD, AddIndexOperationTypeEnum.ONE)
     * 修改为   new TaskEntity("{'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}"，IndexOperationTypeEnum.EDIT)
     * 删除为   new TaskEntity("1"，IndexOperationTypeEnum.DEL)
     *
     * @param taskEntity
     * @return
     */
    public Boolean addIndexOperationTask(TaskEntity taskEntity) {
        try {
            if (taskEntity != null) {
                if (taskEntity.getIndexOperationTypeEnum().equals(IndexOperationTypeEnum.ADD)) {
                    if (taskEntity.getOperationTypeEnum().equals(OperationTypeEnum.ONE)) {
                        this.addIndex(taskEntity.getJson());
                        return true;
                    } else if (taskEntity.getOperationTypeEnum().equals(OperationTypeEnum.MORE)) {
                        this.addIndexs(taskEntity.getJson());
                        return true;
                    } else {
                        return false;
                    }
                } else if (taskEntity.getIndexOperationTypeEnum().equals(IndexOperationTypeEnum.EDIT)) {
                    this.updateIndex(taskEntity.getJson());
                    return true;
                } else if (taskEntity.getIndexOperationTypeEnum().equals(IndexOperationTypeEnum.DEL)) {
                    this.delIndex(taskEntity.getJson());
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量新增
     *
     * @param taskEntities
     * @return
     */
    public Boolean addIndexOperationTask(List<TaskEntity> taskEntities) {
        Gson gson=new Gson();
        try {
            List<Map<String, String>> mapsAdd = new ArrayList<>();
            for (TaskEntity taskEntity : taskEntities) {
                    if (taskEntity.getOperationTypeEnum().equals(OperationTypeEnum.ONE)) {
                        Map<String, String> map = gson.fromJson(taskEntity.getJson(), Map.class);
                        mapsAdd.add(map);
                    } else if (taskEntity.getOperationTypeEnum().equals(OperationTypeEnum.MORE)) {
                        List<Map<String, String>> maps = gson.fromJson(taskEntity.getJson(), List.class);
                        mapsAdd.addAll(maps);
                    }
            }
            if (mapsAdd.size() > 0) {
                this.addIndexs(gson.toJson(mapsAdd));
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}

