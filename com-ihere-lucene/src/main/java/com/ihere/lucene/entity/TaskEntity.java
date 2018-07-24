package com.ihere.lucene.entity;

import com.ihere.lucene.enums.IndexOperationTypeEnum;
import com.ihere.lucene.enums.OperationTypeEnum;

/**
 * @author fengshibo
 * @create 2018-07-19 14:11
 * @desc ${DESCRIPTION}
 **/
public class TaskEntity {
    /**
     * 数据
     * 当新增时用 IndexOperationTypeEnum.ADD
     *      新增一条为  OperationTypeEnum.ONE
     *      数据格式： {'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}
     *      新增多条为  OperationTypeEnum.MORE
     *      数据格式为： [{'id':'1','title':'这是我第一次测试的内容我是','content':'这是我第一次测试的内容'},{'id':'3','title':'这是我第一次测试的内容','content':'这是我第一次测试的内容'},{'id':'1','title':'这是我第一次','content':'这是我第一次测试的内容'}}]
     * 当修改时用 IndexOperationTypeEnum.EDIT
     * 数据格式： {'id':'1','title':'这是我第一次测试','content':'这是我第一次测试的内容'}
     * 当删除时用 IndexOperationTypeEnum.DEL
     * 数据格式（因为使用ID，只传单个id即可）： 1
     */
    private String json;
    /**
     * 针对索引操作类型
     * 当新增时用 IndexOperationTypeEnum.ADD
     * 当修改时用 IndexOperationTypeEnum.EDIT
     * 当删除时用 IndexOperationTypeEnum.DEL
     */
    private IndexOperationTypeEnum indexOperationTypeEnum;
    /**
     * 新增方式
     *   新增一条为  OperationTypeEnum.ONE
     *   新增多条为  OperationTypeEnum.MORE
     */
    private OperationTypeEnum OperationTypeEnum;

    /**
     *
     * @param json
     * @param indexOperationTypeEnum
     * @param OperationTypeEnum
     */
    public TaskEntity(String json, IndexOperationTypeEnum indexOperationTypeEnum, OperationTypeEnum OperationTypeEnum) {
        this.json = json;
        this.indexOperationTypeEnum = indexOperationTypeEnum;
        this.OperationTypeEnum = OperationTypeEnum;
    }

    public TaskEntity(String json, IndexOperationTypeEnum indexOperationTypeEnum) {
        this.json = json;
        this.indexOperationTypeEnum = indexOperationTypeEnum;
    }

    public TaskEntity() {
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "json='" + json + '\'' +
                ", indexOperationTypeEnum=" + indexOperationTypeEnum +
                ", OperationTypeEnum=" + OperationTypeEnum +
                '}';
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public IndexOperationTypeEnum getIndexOperationTypeEnum() {
        return indexOperationTypeEnum;
    }

    public void setIndexOperationTypeEnum(IndexOperationTypeEnum indexOperationTypeEnum) {
        this.indexOperationTypeEnum = indexOperationTypeEnum;
    }

    public OperationTypeEnum getOperationTypeEnum() {
        return OperationTypeEnum;
    }

    public void setOperationTypeEnum(OperationTypeEnum OperationTypeEnum) {
        this.OperationTypeEnum = OperationTypeEnum;
    }
}
