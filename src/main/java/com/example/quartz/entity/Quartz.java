package com.example.quartz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "tb_quartz")
public class Quartz {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    //执行类
    private String clazz;
    //执行方法
    private String method;
    //执行表达式
    private String expression;
    //是否启用
    private Boolean enable;
    //创建时间
    private String createDate;

    public String getClazz() {
        return clazz;
    }

    public String getMethod() {
        return method;
    }

    public String getExpression() {
        return expression;
    }

    public Boolean getEnable() {
        return enable;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Quartz() {
    }

    public Quartz( String id,String clazz, String method, String expression, Boolean enable, String createDate) {
        this.id = id;
        this.clazz = clazz;
        this.method = method;
        this.expression = expression;
        this.enable = enable;
        this.createDate = createDate;
    }
}
