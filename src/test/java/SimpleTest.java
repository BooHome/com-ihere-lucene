import com.google.gson.Gson;
import com.ihere.lucene.config.LuceneConfig;
import com.ihere.lucene.entity.TaskEntity;
import com.ihere.lucene.enums.IndexOperationTypeEnum;
import com.ihere.lucene.enums.OperationTypeEnum;
import com.ihere.lucene.help.LuceneHelper;
import com.ihere.lucene.util.IndexWriterUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.lucene.index.IndexWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author fengshibo
 * @create 2018-07-18 9:34
 * @desc ${DESCRIPTION}
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class SimpleTest {
    @Autowired
    private LuceneHelper luceneHelper;


    @Test
    public void testIndex() throws Exception {
        IndexWriter indexWriter1 = IndexWriterUtil.getIndexWriter();
        System.out.println(indexWriter1);
        IndexWriter indexWriter2 = IndexWriterUtil.getIndexWriter();
        System.out.println(indexWriter2);
    }


    @Test
    public void testUpdate() throws Exception {
        Gson gson=new Gson();
        try {
            String line;
            File someFile = new File("C:\\Users\\亲亲小保\\Desktop\\斗罗大陆.txt");
            //输入流
            List<Map<String, String>> maps = new ArrayList<>();
            FileInputStream fis = new FileInputStream(someFile);
            InputStreamReader isr = new InputStreamReader(fis, "GB2312"); //指定以UTF-8编码读入
            BufferedReader br = new BufferedReader(isr);
            Long k = 0L;
            while ((line = br.readLine()) != null) {
                k += 1;
                Map<String, String> map = new HashMap<>();
                map.put("id", k + "");
                map.put("title", "冯世博" + k);
                map.put("content", line + k);
                maps.add(map);
                luceneHelper.updateIndex(gson.toJson(map));
                System.out.println("修改第"+k+"条");
                Thread.sleep(500);
            }
            br.close();
            isr.close();
            String str = gson.toJson(maps);
            long startTime = System.currentTimeMillis();

            long endTime = System.currentTimeMillis();
            float seconds = (endTime - startTime) / 1000F;
            System.out.println("运行时间：" + seconds + " 秒.");
            System.out.println("执行条数：" + maps.size() + "");
            System.out.println("速度：" + maps.size() / seconds + "条/秒.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDel() throws Exception {
        Gson gson=new Gson();
        try {
            String line;
            File someFile = new File("C:\\Users\\亲亲小保\\Desktop\\斗罗大陆.txt");
            //输入流
            List<Map<String, String>> maps = new ArrayList<>();
            FileInputStream fis = new FileInputStream(someFile);
            InputStreamReader isr = new InputStreamReader(fis, "GB2312"); //指定以UTF-8编码读入
            BufferedReader br = new BufferedReader(isr);
            Long k = 0L;
            while ((line = br.readLine()) != null) {
                k += 1;
                Map<String, String> map = new HashMap<>();
                map.put("id", k + "");
                map.put("title", "冯世博" + k);
                map.put("content", line + k);
                maps.add(map);
                luceneHelper.delIndex(2+"");
                System.out.println("删除第"+k+"条");
                Thread.sleep(2500);
            }
            br.close();
            isr.close();
            String str = gson.toJson(maps);
            long startTime = System.currentTimeMillis();

            long endTime = System.currentTimeMillis();
            float seconds = (endTime - startTime) / 1000F;
            System.out.println("运行时间：" + seconds + " 秒.");
            System.out.println("执行条数：" + maps.size() + "");
            System.out.println("速度：" + maps.size() / seconds + "条/秒.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testnum() {
        String json = "{'id':'2','title':'这是我第一次测试的内容我是冯世博','content':'这是我第一次测试的内容'}";
        TaskEntity taskEntity = new TaskEntity(json, IndexOperationTypeEnum.ADD, OperationTypeEnum.ONE);
        String jsons = "[{'id':'1','title':'这是我第一次测试的内容我是冯世博','content':'这是我第一次测试的内容'},{'id':'1','title':'这是我第一次测试的内容我是冯世博','content':'这是我第一次测试的内容'}]";
        TaskEntity taskEntitys = new TaskEntity(jsons, IndexOperationTypeEnum.ADD, OperationTypeEnum.MORE);
        List<TaskEntity> queueEntities = new ArrayList<>();
        queueEntities.add(taskEntity);
        queueEntities.add(taskEntitys);
       Boolean flag=luceneHelper.addTaskLinkList(taskEntitys);
        try {
        //    String s = luceneHelper.indexReader();
          //  System.out.println(s);
            System.out.println(flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLock1() throws InterruptedException {
        Map<String, String> map = new HashMap<>();
        map.put("id", "2");
        map.put("title", "冯世博" );
        map.put("content", "可怕发的修改");
      // luceneHelper.delIndex("2");
     //   luceneHelper.updateIndex(JsonUtil.toJson(map));
       Thread.sleep(100000000);
    }

    /**
     * 测试查询
     */
    @Test
    public void doTestD() {
        try {
            String fileds[] = {"id", "title", "content"};
            String filed1 = "id";
            String filed2 = "title";
            String filed3 = "content";
            long startTimeV = System.currentTimeMillis();
            System.out.println(luceneHelper.findIndexHighLighterResultPage(fileds, "这是我第一次测试的内容", 13, 1, 100));
            long endTimeV = System.currentTimeMillis();
            float secondsV = (endTimeV - startTimeV) / 1000F;
            System.out.println("运行时间：" + secondsV + " 秒.");
            long startTime = System.currentTimeMillis();
          //  System.out.println(JsonUtil.toJson(luceneHelper.findIndexHighLighterResultPage(fileds, "巴蜀，历来有天府之国的美誉，其中，最有名的门派莫过于唐门。", 123, 1, 100)));
            long endTime = System.currentTimeMillis();
            float seconds = (endTime - startTime) / 1000F;
           // System.out.println("运行时间：" + seconds + " 秒.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 测试加入索引
     */
    @Test
    public void addIndexs() {
        Gson gson=new Gson();
        //  articleEntities.add(new ArticleEntity(Long.valueOf(i),"第"+i+"篇文章","第"+i+"个人","第1内容20"+i));
        try {
            String line;
            File someFile = new File("C:\\Users\\亲亲小保\\Desktop\\斗罗大陆.txt");
            //输入流
            List<Map<String, String>> maps = new ArrayList<>();
            FileInputStream fis = new FileInputStream(someFile);
            InputStreamReader isr = new InputStreamReader(fis, "GB2312"); //指定以UTF-8编码读入
            BufferedReader br = new BufferedReader(isr);
            Long k = 0L;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    addIndexs2();
                }
            }).start();
            while ((line = br.readLine()) != null) {
                k += 1;
                Map<String, String> map = new HashMap<>();
                map.put("id", k + "");
                map.put("title", "冯世博" + k);
                map.put("content", line + k);
                maps.add(map);
                luceneHelper.addIndex(gson.toJson(map));
                System.out.println("新增第"+k+"条");
                Thread.sleep(500);
            }

            br.close();
            isr.close();
            String str = gson.toJson(maps);
            long startTime = System.currentTimeMillis();

            long endTime = System.currentTimeMillis();
            float seconds = (endTime - startTime) / 1000F;
            System.out.println("运行时间：" + seconds + " 秒.");
            System.out.println("执行条数：" + maps.size() + "");
            System.out.println("速度：" + maps.size() / seconds + "条/秒.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void addIndexs2() {
        Gson gson=new Gson();
        //  articleEntities.add(new ArticleEntity(Long.valueOf(i),"第"+i+"篇文章","第"+i+"个人","第1内容20"+i));
        try {
            String line;
            File someFile = new File("C:\\Users\\亲亲小保\\Desktop\\斗罗大陆.txt");
            //输入流
            List<Map<String, String>> maps = new ArrayList<>();
            FileInputStream fis = new FileInputStream(someFile);
            InputStreamReader isr = new InputStreamReader(fis, "GB2312"); //指定以UTF-8编码读入
            BufferedReader br = new BufferedReader(isr);
            Long k = 960000L;
            while ((line = br.readLine()) != null) {
                k += 1;
                Map<String, String> map = new HashMap<>();
                map.put("id", k + "");
                map.put("title", "冯世博" + k);
                map.put("content", line + k);
                maps.add(map);

                System.out.println("新增第"+k+"条");
            }
            br.close();
            isr.close();
            String str = gson.toJson(maps);
            long startTime = System.currentTimeMillis();
            luceneHelper.addIndexs(gson.toJson(maps));
            long endTime = System.currentTimeMillis();
            float seconds = (endTime - startTime) / 1000F;
            System.out.println("运行时间：" + seconds + " 秒.");
            System.out.println("执行条数：" + maps.size() + "");
            System.out.println("速度：" + maps.size() / seconds + "条/秒.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testhh() {
        try {
            System.out.println(LuceneConfig.getLuceneIndexPath());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /* @Test
     public void testA() {
         try {
             doTestA();
         } catch (Throwable t) {
             t.printStackTrace();
         }
     }

     @Test
     public void testB() {
         try {
             doTestB();
         } catch (Throwable t) {
             t.printStackTrace();
         }
     }

     @Test
     public void testC() {
         try {
             luceneHelper.indexReader();
         } catch (Throwable t) {
             t.printStackTrace();
         }
     }

     private void doTest() {
         String ikMyDict = luceneConfigHelper.getIKMyDict();
         System.out.println(ikMyDict);
     }


     @Test
     public void doTestA() {
         luceneHelper.addIndex("{'id':'3','title':'这是我第一次测试的内容','content':'这是我第一次测试的内容'}");
     }

     @Test
     public void doTestF() {
         Boolean ikMyDict = luceneHelper.updateIndex("{'id':'1','title':'这是我第一次测试的内容我是冯世博','content':'这是我第一次测试的内容'}");
         System.out.println(ikMyDict);
     }

     @Test
     public void doTestG() {
         Boolean ikMyDict = luceneHelper.delIndex("1");
         System.out.println(ikMyDict);
     }

     @Test
     public void doTestB() {
         try {
             String fileds[] = {"id", "title", "content"};
             String filed1 = "id";
             String filed2 = "title";
             String filed3 = "content";
             TopDocs topDocs = luceneHelper.findIndexTopDocsPage(fileds, "我是冯世博", 1, 11115);
             System.out.println("总数：" + topDocs.totalHits);
             ScoreDoc[] scoreDocs = topDocs.scoreDocs;
             for (ScoreDoc scoreDoc : scoreDocs) {
                 Document doc = luceneHelper.findDoc(scoreDoc);
                 List<IndexableField> fields = doc.getFields();
                 System.out.println("====================================评分：" + scoreDoc.score);
                 for (IndexableField filed :
                         fields) {
                     System.out.println(filed.name() + ":" + doc.get(filed.name()));
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }

     }


     @Test
     public void doTestE() {
         try {
             String json = "[{'A1':'a','B1':'b'},{'A2':'a','B2':'b'},{'A3':'a','B3':'b'}]";
             List<Map<String, String>> list = JsonUtil.fromJson(json, List.class);
             System.out.println(list.get(0).get("A1"));
         } catch (Exception e) {
             e.printStackTrace();
         }

     }
 */
    @Test
    public void doTestH() {
        Gson gson=new Gson();
        int thread_num = 5;
        int client_num = 20;
        ExecutorService exec = Executors.newCachedThreadPool();
        // thread_num个线程可以同时访问
        final Semaphore semp = new Semaphore(thread_num);
        // 模拟client_num个客户端访问
        for (int index = 0; index < client_num; index++) {
            final int NO = index;
            Runnable run = new Runnable() {
                public void run() {
                    try {
                        // 获取许可
                        semp.acquire();
                        System.out.println("开始 Thread并发事情>>>" + NO);
                        System.out.println("可以进行插入>>" + NO);
                        String line;
                        File someFile = new File("C:\\Users\\亲亲小保\\Desktop\\斗罗大陆.txt");
                        //输入流
                        List<Map<String, String>> maps = new ArrayList<>();
                        FileInputStream fis = new FileInputStream(someFile);
                        InputStreamReader isr = new InputStreamReader(fis, "GB2312"); //指定以UTF-8编码读入
                        BufferedReader br = new BufferedReader(isr);
                        Long k = 0L;
                        while ((line = br.readLine()) != null) {
                            System.out.println("第一个while" + line);
                            k += 1;
                            if (k > 13) {
                                break;
                            }
                            Map<String, String> map = new HashMap<>();
                            map.put("id", k + "");
                            map.put("title", "冯世博" + k);
                            map.put("content", line + k);
                            String str = gson.toJson(map);
                            luceneHelper.addIndex(str);
                        }
                        //  }
                        System.out.println("结束 Thread并发事情>>>" + NO);
                        Thread.sleep(1000);
                        semp.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            exec.execute(run);
        }
        // 退出线程池
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        exec.shutdown();

    }


  /*  @Test
    public void doTestHH() {
        int thread_num = 5;
        int client_num = 20;
        ExecutorService exec = Executors.newCachedThreadPool();
        // thread_num个线程可以同时访问
        final Semaphore semp = new Semaphore(thread_num);
        // 模拟client_num个客户端访问
        for (int index = 0; index < client_num; index++) {
            final int NO = index;

            Runnable run = new Runnable() {
                public void run() {
                    try {
                        // 获取许可
                        semp.acquire();
                        System.out.println("开始 Thread并发事情>>>" + NO);
                        System.out.println("可以进行插入>>" + NO);
                        String json = "{a:\"asf\"}";
                        Map map = JsonUtil.fromJson(json, Map.class);
                        //  }
                        System.out.println("结束之前的Map>>>" + map);
                        System.out.println("结束 Thread并发事情>>>" + NO);
                        Thread.sleep(1000);
                        semp.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            exec.execute(run);
        }
        // 退出线程池
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        exec.shutdown();

    }

    @Test
    public void testLock() {
        System.out.println(luceneHelper.isWriteLock());
    }
*/

}
