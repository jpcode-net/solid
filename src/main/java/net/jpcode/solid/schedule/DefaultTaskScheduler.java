/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.schedule;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.jpcode.solid.model.TaskInfo;
import net.jpcode.solid.schedule.job.GatheJob;
import net.jpcode.solid.service.TaskService;

/**
 * @author: billy zhang
 */
@Slf4j
public class DefaultTaskScheduler implements TaskScheduler {

    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    private static final String JOB_DISPATCH_GROUP_NAME = "JOB_DISPATCH_GROUP_NAME";
    private static final String TRIGGER_DISPATCH_GROUP_NAME = "TRIGGER_DISPATCH_GROUP_NAME";

    private static final String JOB_TASK_GROUP_NAME = "JOB_TASK_GROUP_NAME";
    private static final String TRIGGER_TASK_GROUP_NAME = "TRIGGER_TASK_GROUP_NAME";

    private static final String DISPATCH_NAME = "DISPATCH";
    
    private Map<String, TaskInfo> taskMap = new HashMap<>();
    
    private Scheduler scheduler;

	@Autowired
	private ApplicationContext context;
	
    @Value("${app.schedule.dispatch-cron}")
    private String dispatchCron;
	
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private ObjectMapper jsonMapper;
    
    private Scheduler getScheduler() throws SchedulerException {
    	if (scheduler == null) {
    		scheduler = schedulerFactory.getScheduler();
    	}
    	
    	return scheduler;
    }
    
    @Override
    public void start() throws Exception {
    	
        this.scheduler = getScheduler();
        
        JobDetail jobDetail = getDispatchJobDetail();
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withSchedule(CronScheduleBuilder.cronSchedule(dispatchCron))
                .withIdentity(new TriggerKey(DISPATCH_NAME, TRIGGER_DISPATCH_GROUP_NAME))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
       
        log.info("start dispatch finished.");
        
    }

    private JobDetail getDispatchJobDetail() throws ClassNotFoundException, NoSuchMethodException {
    	Map<String, Object> jobArgs = new HashMap<>();
        jobArgs.put("scheduler", this);
        
        DispatchJob dispJob = new DispatchJob();
        context.getAutowireCapableBeanFactory().autowireBean(dispJob);
        
        MethodInvokingJobDetailFactoryBean jobDetailFactory = new MethodInvokingJobDetailFactoryBean();
        jobDetailFactory.setName(DISPATCH_NAME);
        jobDetailFactory.setGroup(JOB_DISPATCH_GROUP_NAME);
        jobDetailFactory.setTargetObject(dispJob);
        jobDetailFactory.setTargetMethod("execute");
        jobDetailFactory.setArguments(jobArgs);
        jobDetailFactory.afterPropertiesSet();
        
        return jobDetailFactory.getObject();
    }
    
    @Override
    public void stop() {
    	taskMap.clear();
    	
		try {
			scheduler.shutdown();
			scheduler = null;
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			log.error(ex.getMessage(), ex);
		}
    }
    
    @Override
    public void dispatch() {
    	
    	try {
			this.scheduler = getScheduler();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			log.error(ex.getMessage(), ex);
			return;
		}
    	
    	
    	List<TaskInfo> tasklist = taskService.selectTaskList();
    	
    	scheduleTasks(tasklist);
    }
    
    private boolean taskIsScheduled(TaskInfo taskInfo) throws SchedulerException {
		JobKey jobKey = getTaskJobKey(taskInfo.getName());
        TriggerKey triggerKey = getTaskTriggerKey(taskInfo.getName());
        
    	return scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey);
    }

    private boolean taskIsModified(TaskInfo ti) {
    
    	if (!taskMap.containsKey(ti.getName()))
    		return true;
    	
    	TaskInfo old = taskMap.get(ti.getName());
    	
    	return (old.getModified().compareTo(ti.getModified())) != 0;
    }
    
    public void scheduleTasks(List<TaskInfo> tasklist) {

    	int addCount = 0;
    	int updateCount = 0;
    	int deleteCount = 0;
    	
    	for (TaskInfo ti : tasklist) {

	        try {
		        
		        if (ti.getDel() == 1) {
		        	if (taskIsScheduled(ti)) {  // 删除任务
		        		deleteJob(ti.getName());
		        		deleteCount++;
		        	}
		        	continue;
		        }
	        
	            if (taskIsScheduled(ti)) {
	            	if (taskIsModified(ti)) {  // 更新任务
	            		updateJob(ti);
	            		updateCount++;
	            	}
	            } else {
	            	addJob(ti);  // 增加任务
	            	addCount++;
	            }
	            
            	taskMap.put(ti.getName(), ti);
	        } catch (Exception ex) {
	        	ex.printStackTrace();
				log.error(ex.getMessage(), ex);
	        }
    	}
        
        log.info(String.format("dispatch finished. add:%d, update:%d, delete:%d", addCount, updateCount, deleteCount));
    }
    
    public Collection<TaskInfo> getTaskList() {
    	return taskMap.values();
    }
    
    public int getTaskCount() {
    	
		try {
			return getScheduler().getJobKeys(GroupMatcher.anyJobGroup()).size();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			log.error(ex.getMessage(), ex);
		}
    	
    	return 0;
    }
    
    private JobKey getTaskJobKey(String name) {
    	return new JobKey(name, JOB_TASK_GROUP_NAME);
    }
    
    private TriggerKey getTaskTriggerKey(String name) {
    	return new TriggerKey(name, TRIGGER_TASK_GROUP_NAME);
    }
    
    private boolean addJob(TaskInfo task) throws Exception {
       
        String cronExp = getTaskCronExpression(task);

        JobDetail jobDetail = getTaskJobDetail(task);
        
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .withIdentity(getTaskTriggerKey(task.getName()))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
       
        return true;
    }

    private JobDetail getTaskJobDetail(TaskInfo task) throws Exception {
    	Map<String, Object> jobData = getTaskJobData(task);
    	
    	Class<? extends GatheJob> jobClass = (Class<? extends GatheJob>)Class.forName(task.getJobClazz());
    	GatheJob targetJob = jobClass.newInstance();
    	context.getAutowireCapableBeanFactory().autowireBean(targetJob);
        
    	MethodInvokingJobDetailFactoryBean jobDetailFactory = new MethodInvokingJobDetailFactoryBean();
        jobDetailFactory.setName(task.getName());
        jobDetailFactory.setGroup(JOB_TASK_GROUP_NAME);
        jobDetailFactory.setTargetObject(targetJob);
        jobDetailFactory.setTargetMethod("execute");
        jobDetailFactory.setArguments(jobData);
        jobDetailFactory.afterPropertiesSet();
        
        return jobDetailFactory.getObject();
    }
    
    private Map<String, Object> getTaskJobData(TaskInfo task) throws IOException {
    	Map<String, Object> map = (Map<String, Object>)jsonMapper.readValue(task.getJobData(), Map.class);
    	map.put("taskId", task.getId());
    	return map;
    }
    
    private String getTaskCronExpression(TaskInfo task) throws SchedulerException {
    	if (!CronExpression.isValidExpression(task.getCronExp())) {
            throw new SchedulerException(String.format("Illegal cron expression format %s", task.getCronExp()) );
            
        }
    	
    	return task.getCronExp();
    }
    
    private boolean updateJob(TaskInfo task) throws Exception {
        
        String cronExp = getTaskCronExpression(task);
        
        JobKey jobKey = getTaskJobKey(task.getName());
        TriggerKey triggerKey = getTaskTriggerKey(task.getName());
        
    	Map<String, Object> jobData = getTaskJobData(task);

        if (taskIsScheduled(task)) {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            jobDetail.getJobDataMap().clear();
            jobDetail.getJobDataMap().putAll(jobData);
            
            Trigger newTrigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                    .withIdentity(triggerKey)
                    .build();
            scheduler.rescheduleJob(triggerKey, newTrigger);
            return true;
        }
      
        return false;
    }

    private boolean deleteJob(String jobName) throws Exception {
        boolean result = false;
        JobKey jobKey = getTaskJobKey(jobName);
        
        if (scheduler.checkExists(jobKey)) {
            result = scheduler.deleteJob(jobKey);
        }

        return result;
    }

}
