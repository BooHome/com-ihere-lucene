package com.ihere.lucene.task;

import com.ihere.lucene.config.LuceneConfig;
import com.ihere.lucene.entity.TaskEntity;
import com.ihere.lucene.enums.IndexOperationTypeEnum;
import com.ihere.lucene.help.LuceneHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fengshibo
 * @create 2018-07-23 16:43
 * @desc ${DESCRIPTION}
 **/
public class Task {
    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    /**
     * 任务队列
     */
    public static LinkedList<TaskEntity> taskQueue = new LinkedList<TaskEntity>();

    @Autowired
    private LuceneHelper luceneHelper;

    public void taskDo() {
        TaskEntity task = null;
        List<TaskEntity> taskEntityList = new ArrayList<>();
        if (!taskQueue.isEmpty()) {
            logger.debug("任务开始");
            // 如果任务队列不为空，则优先从队列中取任务
            task = taskQueue.removeFirst();
            //执行任务
            Boolean flag = false;
            if (task.getIndexOperationTypeEnum().equals(IndexOperationTypeEnum.ADD)) {
                taskEntityList.add(task);
                taskEntityList = getAddList(taskEntityList);
                try {
                    flag = luceneHelper.addIndexOperationTask(taskEntityList);
                    if (!flag) {
                        //任务失败
                        taskQueue.addAll(taskEntityList);
                        logger.error("任务失败，" + task + "重新加入队列");
                    }
                } catch (Exception e) {
                    //任务失败
                    taskQueue.addAll(taskEntityList);
                    logger.error("任务失败，" + task + "重新加入队列");
                }
                logger.debug("任务结束");
            } else {
                try {
                    flag = luceneHelper.addIndexOperationTask(task);
                    if (!flag) {
                        //任务失败
                        taskQueue.addLast(task);
                        logger.error("任务失败，" + task + "重新加入队列");
                    }
                } catch (Exception e) {
                    //任务失败
                    taskQueue.addLast(task);
                    logger.error("任务失败，" + task + "重新加入队列");
                }
            }
        }
    }

    public List<TaskEntity> getAddList(List<TaskEntity> taskEntityList) {
        if (!taskQueue.isEmpty()) {
            TaskEntity task = null;
            task = taskQueue.removeFirst();
            if (task.getIndexOperationTypeEnum().equals(IndexOperationTypeEnum.ADD)) {
                taskEntityList.add(task);
                if (taskEntityList.size() < Integer.parseInt(LuceneConfig.getTaskListSize())) {
                    return getAddList(taskEntityList);
                } else {
                    return taskEntityList;
                }

            } else {
                taskQueue.add(task);
                return taskEntityList;
            }
        }else{
            return taskEntityList;
        }
    }
}
