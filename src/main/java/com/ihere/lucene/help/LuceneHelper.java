package com.ihere.lucene.help;

import com.ihere.lucene.entity.IndexResultEntity;
import com.ihere.lucene.entity.TaskEntity;

import java.util.List;
import java.util.Map;


/**
 * 用于索引操作
 * @author fengshibo
 * @create 2018-07-18 11:02
 * @desc ${DESCRIPTION}
 **/

public interface LuceneHelper {
    /**
     * 任务队列新加任务
     * 任务格式
     * 当新增时用 IndexOperationTypeEnum.ADD
     *      新增一条为  AddIndexOperationTypeEnum.ONE
     *      数据格式： {'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}
     *      新增多条为  AddIndexOperationTypeEnum.MORE
     *      数据格式为： [{'id':'1','title':'这是我第一次测试的内容我是','content':'这是我第一次测试的内容'},{'id':'3','title':'这是我第一次测试的内容','content':'这是我第一次测试的内容'},{'id':'1','title':'这是我第一次','content':'这是我第一次测试的内容'}}]
     *
     * 当修改时用 IndexOperationTypeEnum.EDIT
     * 数据格式： {'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}
     *
     * 当删除时用 IndexOperationTypeEnum.DEL
     * 数据格式（因为使用ID，只传单个id即可）： 1
     *
     * 例：
     *     新增为   new TaskEntity("{'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}"，IndexOperationTypeEnum.ADD, AddIndexOperationTypeEnum.ONE)
     *     修改为   new TaskEntity("{'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}"，IndexOperationTypeEnum.EDIT)
     *     删除为   new TaskEntity("1"，IndexOperationTypeEnum.DEL)
     * @param taskEntity
     * @return
     */
    Boolean addTaskLinkList(TaskEntity taskEntity);
    /**
     * 数据
     * 当新增时用 IndexOperationTypeEnum.ADD
     *      新增一条为  AddIndexOperationTypeEnum.ONE
     *      数据格式： {'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}
     *      新增多条为  AddIndexOperationTypeEnum.MORE
     *      数据格式为： [{'id':'1','title':'这是我第一次测试的内容我是','content':'这是我第一次测试的内容'},{'id':'3','title':'这是我第一次测试的内容','content':'这是我第一次测试的内容'},{'id':'1','title':'这是我第一次','content':'这是我第一次测试的内容'}}]
     *
     * 当修改时用 IndexOperationTypeEnum.EDIT
     * 数据格式： {'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}
     *
     * 当删除时用 IndexOperationTypeEnum.DEL
     * 数据格式（因为使用ID，只传单个id即可）： 1
     *
     * 例：
     *     新增为   new TaskEntity("{'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}"，IndexOperationTypeEnum.ADD, AddIndexOperationTypeEnum.ONE)
     *     修改为   new TaskEntity("{'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}"，IndexOperationTypeEnum.EDIT)
     *     删除为   new TaskEntity("1"，IndexOperationTypeEnum.DEL)
     *
     * 队列用
     * @param taskEntity
     * @return
     */
    Boolean addIndexOperationTask(TaskEntity taskEntity);

    /**
     * 批量新增
     * 队列用
     * @param taskEntities
     */
    Boolean addIndexOperationTask(List<TaskEntity> taskEntities);


    /**
     * 新增一条索引
     * @param json
     * @return
     */
    //Integer addIndex(String json);

    /**
     * 新增一组索引
     * @param json
     * @return
     */
    //Integer addIndexs(String json);


    /**
     * 根据单个关键字查询 返回分页实体类
     * @param field  查询类型
     * @param keywords  关键字
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    IndexResultEntity findIndexResultPage(String field, String keywords, int pageIndex, int pageSize);
    /**
     * 根据单个关键字查询 返回分页实体类
     * @param field  查询类型
     * @param keywords  关键字
     * @param num
     * @return
     * @throws Exception
     */
    //IndexResultEntity findIndexResultByNum(String field, String keywords, int num);
    /**
     * 根据单个关键字查询 返回分页实体类
     * @param fields  查询类型
     * @param keywords  关键字
     * @param num
     * @return
     * @throws Exception
     */
    //IndexResultEntity findIndexResultByNum(String fields[], String keywords, int num);
    /**
     * 根据多个关键字查询 返回分页实体类
     * @param fields  查询类型
     * @param keywords  关键字
     *
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    IndexResultEntity findIndexResultPage(String fields[], String keywords, int pageIndex, int pageSize);


    /**
     * 根据多个关键字查询 返回分页实体类
     * @param map  查询列名 ： 关键字
     *
     * @param isMust  查询条件是否必须
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    IndexResultEntity findIndexResultPage(Map<String,String> map,Boolean isMust, int pageIndex, int pageSize);

    /**
     * 高亮 根据单个关键字查询 返回分页实体类
     * @param field  查询类型
     * @param keywords  关键字
     * @param fragmentSize 高亮摘要长度
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    IndexResultEntity findIndexHighLighterResultPage(String field, String keywords, int fragmentSize, int pageIndex, int pageSize);
    /**
     *高亮  根据单个关键字查询 返回分页实体类
     * @param field  查询类型
     * @param keywords  关键字
     * @param fragmentSize 高亮摘要长度
     * @param num
     * @return
     * @throws Exception
     */
    //IndexResultEntity findIndexHighLighterResultByNum(String field, String keywords, int fragmentSize, int num);
    /**
     * 高亮 根据单个关键字查询 返回分页实体类
     * @param fields  查询类型
     * @param keywords  关键字
     * @param fragmentSize 高亮摘要长度
     * @param num
     * @return
     * @throws Exception
     */
    //IndexResultEntity findIndexHighLighterResultByNum(String fields[], String keywords, int fragmentSize, int num);
    /**
     *高亮  根据多个关键字查询 返回分页实体类
     * @param fields  查询类型
     * @param keywords  关键字
     * @param fragmentSize 高亮摘要长度
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    IndexResultEntity findIndexHighLighterResultPage(String fields[], String keywords, int fragmentSize, int pageIndex, int pageSize);


    /**
     * 根据多个关键字查询 返回分页实体类  高亮
     * @param map  查询列名 ： 关键字
     *
     * @param isMust  查询条件是否必须
     * @param fragmentSize  高亮摘要长度
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    IndexResultEntity findIndexHighLighterResultPage(Map<String,String> map,Boolean isMust,int fragmentSize, int pageIndex, int pageSize);
    /**
     * 修改一条记录
     * @param json
     * @return
     */
    //boolean updateIndex(String json);
    /**
     * 批量修改记录
     * @param json
     * @return
     */
    //boolean updateIndexs(String json);

    /**
     * 删除一条记录
     * @param id
     * @return
     */
   // boolean delIndex(String id);


    /**
     * 批量删除记录
     * @param ids
     * @return
     */
   // boolean delIndexs(List<String> ids);

    /**
     * true 表示不可用   false 表示可用
     * @return
     */
    //Boolean isWriteLock();

    /**
     * 测试读取   文档数量
     */
    String indexReader() throws Exception;


}
