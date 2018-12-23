package com.example.quartz.controller;

import com.example.quartz.entity.Quartz;
import com.example.quartz.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuartzController {
    @Autowired
    private QuartzService quartzService;
    @GetMapping("list")
    public List<Quartz> list(){
        List<Quartz> quartzList = quartzService.list();
        return quartzList;
    }

    @PostMapping("save")
    public void save(Quartz quartz){
        quartzService.saveQuartz(quartz);
    }

    @PutMapping("update")
    public void update(Quartz quartz){
        quartzService.updateQuartz(quartz);
    }

    @DeleteMapping("delete")
    public void delete(String id){
        quartzService.deleteQuartz(id);
    }
}
