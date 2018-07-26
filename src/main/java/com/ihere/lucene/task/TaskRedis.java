package com.ihere.lucene.task;

import com.ihere.lucene.config.LuceneConfig;
import com.ihere.lucene.entity.TaskEntity;
import com.ihere.lucene.enums.IndexOperationTypeEnum;
import com.ihere.lucene.help.LuceneHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fengshibo
 * @create 2018-07-23 16:43
 * @desc ${DESCRIPTION}
 **/
public class TaskRedis {
    private static final Logger logger = LoggerFactory.getLogger(TaskRedis.class);
    /**
     * 任务队列
     */
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;
    @Autowired
    private LuceneHelper luceneHelper;
    public void taskDo1() {
        TaskEntity task=(TaskEntity)redisTemplate.opsForList().leftPop(LuceneConfig.getQueueName());
        System.out.println(task);
    }

    public void taskDo() {
        TaskEntity task = null;
        List<TaskEntity> taskEntityList = new ArrayList<>();
        if (redisTemplate.opsForList().size(LuceneConfig.getQueueName())>0) {
            logger.debug("任务开始");
            // 如果任务队列不为空，则优先从队列中取任务
            task=(TaskEntity)redisTemplate.opsForList().leftPop(LuceneConfig.getQueueName());
            System.out.print(task);
            //执行任务
            Boolean flag = false;
            if (task.getIndexOperationTypeEnum().equals(IndexOperationTypeEnum.ADD)) {
                taskEntityList.add(task);
                taskEntityList = getAddList(taskEntityList);
                try {
                    System.out.print("最后的长度："+taskEntityList.size());
                    flag = luceneHelper.addIndexOperationTask(taskEntityList);
                    if (!flag) {
                        //任务失败
                        redisTemplate.opsForList().leftPushAll(LuceneConfig.getQueueName(),taskEntityList);
                        logger.error("任务失败，" + task + "重新加入队列");
                    }
                } catch (Exception e) {
                    //任务失败
                    redisTemplate.opsForList().leftPushAll(LuceneConfig.getQueueName(),taskEntityList);

                    logger.error("任务失败，" + task + "重新加入队列");
                }
                logger.debug("任务结束");
            } else {
                try {
                    flag = luceneHelper.addIndexOperationTask(task);
                    if (!flag) {
                        //任务失败
                        redisTemplate.opsForList().leftPush(LuceneConfig.getQueueName(),task);
                        logger.error("任务失败，" + task + "重新加入队列");
                    }
                } catch (Exception e) {
                    //任务失败
                    redisTemplate.opsForList().leftPush(LuceneConfig.getQueueName(),task);
                    logger.error("任务失败，" + task + "重新加入队列");
                }
            }
        }
    }

    public List<TaskEntity> getAddList(List<TaskEntity> taskEntityList) {
        if (redisTemplate.opsForList().size(LuceneConfig.getQueueName())>0) {
            TaskEntity task = null;
            task=(TaskEntity)redisTemplate.opsForList().leftPop(LuceneConfig.getQueueName());
            if (task.getIndexOperationTypeEnum().equals(IndexOperationTypeEnum.ADD)) {
                taskEntityList.add(task);
                if (taskEntityList.size() < Integer.parseInt(LuceneConfig.getTaskListSize())) {
                    return getAddList(taskEntityList);
                } else {
                    return taskEntityList;
                }

            } else {
                redisTemplate.opsForList().leftPush(LuceneConfig.getQueueName(),task);
                return taskEntityList;
            }
        }else{
            return taskEntityList;
        }
    }
}
