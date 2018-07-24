package com.ihere.lucene.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengshibo
 * @create 2018-07-18 14:45
 * @desc ${DESCRIPTION}
 **/
public class IndexResultEntity {
    private Integer total;

    private List<ResultEntity> list=new ArrayList<>();

    public IndexResultEntity() {
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<ResultEntity> getList() {
        return list;
    }

    public void setList(List<ResultEntity> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "IndexResultEntity{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
