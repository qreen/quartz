package com.example.quartz.scheduled;

import com.example.quartz.entity.Quartz;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@DisallowConcurrentExecution
public class QuartzJobFactory implements Job {
    private static Map<String, Class<?>> maps = new HashMap<String, Class<?>>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Quartz scheduledJob = (Quartz) context.getMergedJobDataMap().get("scheduleJob");
        try {
            String className = scheduledJob.getClazz();

            Class<?> exec = null;
            if (maps.containsKey(className)) {
                exec = maps.get(className);
                maps.put(className, exec);
            } else {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                if (null == classLoader) {
                    classLoader = QuartzJobFactory.class.getClassLoader();
                }
                exec = classLoader.loadClass(className);
                maps.put(className, exec);
            }
            try {
                invokeMethod(scheduledJob);

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void invokeMethod(Quartz quartz){

        try {
            Class clazz = Class.forName(quartz.getClazz());
            Object o = clazz.newInstance();
            Method md =clazz.getMethod(quartz.getMethod());
            md.invoke(o);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
