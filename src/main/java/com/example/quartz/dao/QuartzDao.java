package com.example.quartz.dao;

import com.example.quartz.entity.Quartz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface QuartzDao extends JpaRepository<Quartz, String>, JpaSpecificationExecutor {
    //查询
    List<Quartz> findAll();
}
