package com.example.quartz.scheduled;

import com.example.quartz.entity.Quartz;
import com.example.quartz.service.QuartzService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleSerImpl {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private QuartzService quartzService;

    /**
     * 启动所用定时器
     */
    @PostConstruct
    private void init() {
        List<Quartz> scheduleJobs = quartzService.list();
        for (Quartz scheduleJob : scheduleJobs) {
            this.start(scheduleJob);
        }
    }

    public CronScheduleBuilder verifyTrigger(Quartz quartz) {
        CronScheduleBuilder scheduleBuilder = null;
        try {
            scheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getExpression());
        } catch (Exception e) {
            throw new RuntimeException("表达式不正确,请重新输入.");
        }
        return scheduleBuilder;
    }

    public List<Quartz> jobs() {
        List<Quartz> jobList = new ArrayList<Quartz>();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Quartz scheduleJob = new Quartz();
                    scheduleJob.setId(jobKey.getName());
                    //				scheduleJob.setScheduleJobGroup(new ScheduleJobGroup(jobKey.getGroup()));
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    scheduleJob.setEnable(triggerState.name().equals("NORMAL"));
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        scheduleJob.setExpression(cronExpression);
                    }
                    jobList.add(scheduleJob);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("获取失败");
        }
        return jobList;
    }

    public void start(Quartz scheduleJob) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(scheduleJob.getId()), "group");//组合名称（定时器名称+分组名称）
        // 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
        CronTrigger trigger = null;
        try {
            trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            throw new RuntimeException("表达式不正确,请重新输入.");
        }

        // 不存在，创建一个
        if (null == trigger) {
            JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
                    .withIdentity(String.valueOf(scheduleJob.getId()), "group").build();
            jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);

            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = verifyTrigger(scheduleJob);

            // 按新的cronExpression表达式构建一个新的trigger
            trigger = TriggerBuilder.newTrigger().withIdentity(String.valueOf(scheduleJob.getId()), "group")
                    .withSchedule(scheduleBuilder).build();

            try {
                scheduler.scheduleJob(jobDetail, trigger);
                if (!scheduleJob.getEnable()) {
                    stop(scheduleJob);
                }
            } catch (SchedulerException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            // Trigger已存在，那么更新相应的定时设置
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = verifyTrigger(scheduleJob);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            try {
                scheduler.rescheduleJob(triggerKey, trigger);
            } catch (SchedulerException e) {
                throw new RuntimeException("trigger执行失败.");
            }
        }

    }

    public void stop(Quartz scheduleJob) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(String.valueOf(scheduleJob.getId()), String.valueOf("group"));
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException("定时器停止失败.");
        }
    }

    public void restart(Quartz scheduledJob) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(String.valueOf(scheduledJob.getId()), "group");
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException("定时器重启失败.");
        }
    }

    public void del(Quartz scheduledJob) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(String.valueOf(scheduledJob.getId()), "group");
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException("定时器删除失败.");
        }

    }
}
