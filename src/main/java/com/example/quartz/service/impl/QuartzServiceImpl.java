package com.example.quartz.service.impl;

import com.example.quartz.dao.QuartzDao;
import com.example.quartz.entity.Quartz;
import com.example.quartz.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuartzServiceImpl implements QuartzService {
    @Autowired
    QuartzDao quartzDao;

    @Override
    public void saveQuartz(Quartz quartz) {
        quartzDao.save(quartz);
    }

    @Override
    public void updateQuartz(Quartz quartz) {
        quartzDao.save(quartz);
    }

    @Override
    public void deleteQuartz(String id) {
        quartzDao.deleteById(id);
    }

    //查询
    public List<Quartz> list(){
        return quartzDao.findAll();
    }
}
