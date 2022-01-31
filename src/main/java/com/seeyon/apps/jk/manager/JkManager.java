package com.seeyon.apps.jk.manager;

import com.seeyon.apps.jk.po.JkJob;
import com.seeyon.apps.jk.po.JkJobType;
import com.seeyon.apps.jk.vo.QuartzJobsVO;
import com.seeyon.ctp.common.AppContext;
import com.seeyon.ctp.common.quartz.QuartzListener;
import com.seeyon.ctp.util.DBAgent;
import com.seeyon.ctp.util.FlipInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.*;

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
	public Map<String,Object> addJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
		Map<String,Object> res = new HashMap<>();
		// 构建job信息
		try {
			JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName))
					.withIdentity(jobClassName, jobGroupName).build();
			// 表达式调度构建器(即任务执行的时间)
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
					.withSchedule(scheduleBuilder).build();
			//构建Jk_Job
			JkJob job = new JkJob();
			job.setNewId();
			job.setJkJobName("测试");
			job.setJkJobType(JkJobType.common.ordinal());
			job.setJobName("测试");
			job.setJobGroup("test");
			job.setTriggerName("测试");
			job.setTriggerGroup("test");
			job.setMemberId(AppContext.getCurrentUser().getId());
			job.setCreateDate(new Date());
			job.setJobName("测试");
			job.setDeleted(false);
			DBAgent.save(job);
			try {
				scheduler.scheduleJob(jobDetail, trigger);
			} catch (SchedulerException e) {
				throw new Exception("创建定时任务失败");
			}
		}catch(Exception e){
			res.put("success",false);
			res.put("success",e.getMessage());
			e.printStackTrace();
		}
		/*QuartzHolder.newCronQuartzJob(jobGroupName, jobClassName, cronExpression,
                new Date(), null, jobClassName, new HashMap<String, String>());*/
		res.put("success",true);
		return res;
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

	public static <T extends Job> Class<T> getClass(String classname) throws Exception {
		Class<T> clazz = (Class<T>)Class.forName(classname);
		return clazz;
	}

}
