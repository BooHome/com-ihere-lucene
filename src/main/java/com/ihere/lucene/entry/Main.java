package com.ihere.lucene.entry;

import com.ihere.lucene.task.TaskRedis;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author fengshibo
 * @create 2018-07-26 9:18
 * @desc ${DESCRIPTION}
 **/
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-search-config.xml");//此文件放在SRC目录下
        TaskRedis taskRedis=(TaskRedis)context.getBean("taskRedis");
        Runnable runnable = new Runnable() {
            public void run() {
                taskRedis.taskDo();
            }
        };
        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                service.shutdown();
            }
        });

        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(runnable, 10, 5, TimeUnit.SECONDS);

    }
}
