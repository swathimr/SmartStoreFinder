package com.sjsu.nimbleshop.Vo;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Futurewei-1 on 5/7/2016.
 */
public class DataWrapper {

    public List<TemplateVo> templateList;

    public static DataWrapper fromJson(String s) {
        return new Gson().fromJson(s, DataWrapper.class);
    }
    public String toString() {
        return new Gson().toJson(this);
    }

    public List<TemplateVo> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<TemplateVo> templateList) {
        this.templateList = templateList;
    }
}

