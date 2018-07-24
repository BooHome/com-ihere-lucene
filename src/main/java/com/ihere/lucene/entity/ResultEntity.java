package com.ihere.lucene.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengshibo
 * @create 2018-07-18 14:46
 * @desc ${DESCRIPTION}
 **/
public class ResultEntity {

    private Map<String,String> map=new HashMap<>();

    private float score;

    public ResultEntity(Map<String, String> map, float score) {
        this.map = map;
        this.score = score;
    }

    public ResultEntity() {
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "map=" + map +
                ", score=" + score +
                '}';
    }
}
