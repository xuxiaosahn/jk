package com.seeyon.apps.jk.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;

import com.seeyon.apps.jk.vo.QuartzJobsVO;
import com.seeyon.ctp.common.quartz.QuartzHolder;
import com.seeyon.ctp.common.quartz.QuartzJobProxy;
import com.seeyon.ctp.common.quartz.QuartzListener;
import com.seeyon.ctp.util.FlipInfo;

public class JkManager {
	private static Log LOG = LogFactory.getLog(JkManager.class);

	private Scheduler scheduler;
	
	public JkManager() {
		if(scheduler == null) {
			try {
				scheduler = QuartzListener.getScheduler();
			} catch (SchedulerException e) {
				LOG.error(e.getMessage());
			}
		}
	}

	/**
	 * 获取定时任务列表
	 * @param flipInfo
	 * @param query
	 * @return
	 * @throws SchedulerException
	 */
	public FlipInfo getJkList(FlipInfo flipInfo, Map<String, String> query) throws SchedulerException {
		//List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
		List<String> triggerGroupNames = new ArrayList<String>(1);
		triggerGroupNames.add("test");
		List<QuartzJobsVO> quartzJobsVOList = new ArrayList<>();
		for (String groupName : triggerGroupNames) {
			// 组装group的匹配，为了模糊获取所有的triggerKey或者jobKey
			GroupMatcher groupMatcher = GroupMatcher.groupEquals(groupName);
			// 获取所有的triggerKey
			Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
			for (TriggerKey triggerKey : triggerKeySet) {
				// 通过triggerKey在scheduler中获取trigger对象
				Trigger trigger = scheduler.getTrigger(triggerKey);
				if(trigger instanceof CronTrigger) {
					CronTrigger crontrigger = (CronTrigger)trigger;
					// 获取trigger拥有的Job
					JobKey jobKey = trigger.getJobKey();
					JobDetailImpl jobDetail = (JobDetailImpl)scheduler.getJobDetail(jobKey);
					// 组装页面需要显示的数据
					QuartzJobsVO quartzJobsVO = new QuartzJobsVO();
					quartzJobsVO.setJobKey(jobKey.getName());
					quartzJobsVO.setGroupName(groupName);
					quartzJobsVO.setJobDetailName(jobDetail.getName());
					quartzJobsVO.setJobClassName(jobDetail.getJobClass().getName());
					quartzJobsVO.setJobCronExpression(crontrigger.getCronExpression());
					quartzJobsVO.setTimeZone(crontrigger.getTimeZone().getDisplayName());
					quartzJobsVO.setState(scheduler.getTriggerState(triggerKey));
					quartzJobsVOList.add(quartzJobsVO);
				}
			}
		}
		flipInfo.setData(quartzJobsVOList);
		return flipInfo;
	}
	
	/**
	 * 挂起
	 * @param jobClassName
	 * @param jobGroupName
	 * @throws Exception
	 */
	public boolean jobPause(String jobClassName, String jobGroupName) throws Exception{   
        scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
        return true;
    }
	
	/**
	 * 恢复
	 * @param jobClassName
	 * @param jobGroupName
	 * @throws Exception
	 */
	public boolean jobresume(String jobClassName, String jobGroupName) throws Exception{
        scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
        return true;
    }

	/**
	 * 新建定时任务
	 * @param jobClassName
	 * @param jobGroupName
	 * @param cronExpression
	 * @throws Exception
	 */
	public boolean addJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
		/**
		// 构建job信息
		JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass())
				.withIdentity(jobClassName, jobGroupName).build();
		// 表达式调度构建器(即任务执行的时间)
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
				.withSchedule(scheduleBuilder).build();
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new Exception("创建定时任务失败");
		}
		*/
		QuartzHolder.newCronQuartzJob(jobGroupName, jobClassName, cronExpression,
                new Date(), null, jobClassName, new HashMap<String, String>());
		return true;
	}
	
	/**
	 * 更新定时任务
	 * @param jobClassName
	 * @param jobGroupName
	 * @param cronExpression
	 * @throws Exception
	 */
	public boolean jobreschedule(String jobClassName, String jobGroupName, String cronExpression) throws Exception
    {               
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            System.out.println("更新定时任务失败"+e);
            throw new Exception("更新定时任务失败");
        }
        return true;
    }

	public static QuartzJobProxy getClass(String classname) throws Exception {
		Class<?> class1 = Class.forName(classname);
		return (QuartzJobProxy) class1.newInstance();
	}

}
