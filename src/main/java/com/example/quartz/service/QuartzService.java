package com.example.quartz.service;

import com.example.quartz.entity.Quartz;

import java.util.List;

public interface QuartzService {
    //查询
    List<Quartz> list();
    //添加
    void saveQuartz(Quartz quartz);
    //编辑
    void updateQuartz(Quartz quartz);
    //删除
    void deleteQuartz(String id);
}
