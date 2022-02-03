package com.seeyon.apps.jk.manager;

import com.seeyon.apps.jk.dao.JkDao;
import com.seeyon.apps.jk.po.JkJob;
import com.seeyon.apps.jk.vo.QuartzJobsVO;
import com.seeyon.ctp.common.AppContext;
import com.seeyon.ctp.common.quartz.QuartzHolder;
import com.seeyon.ctp.common.quartz.QuartzListener;
import com.seeyon.ctp.util.DBAgent;
import com.seeyon.ctp.util.FlipInfo;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.songjian.utils.StringUtils;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

public class JkManagerImpl implements JkManager{
	private static Log LOG = LogFactory.getLog(JkManagerImpl.class);

	@Inject
	private JkDao jkDao;

	private Scheduler scheduler;
	
	public JkManagerImpl() {
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
	public FlipInfo getJkList(FlipInfo flipInfo, Map<String, Object> query) throws Exception {
		//List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
		StringBuilder hql = new StringBuilder();
		hql.append("FROM JkJob WHERE deleted = 0 ");
		//根据query参数拼接hql语句
		if(query.containsKey("jobName")){
			if(StringUtils.isBlank(MapUtils.getString(query,"jobName"))){
				query.remove("jobName");
			}else {
				hql.append("AND jkJobName LIKE :jobName");
				query.put("jobName", "%" + query.get("jobName") + "%");
			}
		}else if(query.containsKey("jobDetailName")){
			if(StringUtils.isBlank(MapUtils.getString(query,"jobDetailName"))){
				query.remove("jobDetailName");
			}else {
				hql.append("AND jobName LIKE :jobDetailName");
				query.put("jobDetailName", "%" + query.get("jobDetailName") + "%");
			}
		}else if(query.containsKey("jobGroupName")){
			if(StringUtils.isBlank(MapUtils.getString(query,"jobGroupName"))){
				query.remove("jobGroupName");
			}else {
				hql.append("AND jobGroup LIKE :jobGroupName");
				query.put("jobGroupName", "%" + query.get("jobGroupName") + "%");
			}
		}else if(query.containsKey("jobType")){
			hql.append("AND jkJobType = :jobType");
			query.put("jobType",MapUtils.getInteger(query,"jobType"));
		}else if(query.containsKey("createDate")){//TODO 日期处理
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			hql.append("AND createDate BETWEEN :fromDate AND :toDate ");
			String dateArr[] = MapUtils.getString(query,"createDate").split("#");
			String fromDate = dateArr[0];
			String toDate = dateArr[1];
			query.remove("createDate");
			query.put("fromDate",sdf.parse(fromDate));
			query.put("toDate",sdf.parse(toDate));
		}
		hql.append(" ORDER BY createDate DESC");
		List<JkJob> jkJobs = DBAgent.find(hql.toString(), query, flipInfo);
		List<QuartzJobsVO> quartzJobsVOList = new ArrayList<>();
		for(JkJob jkJob : jkJobs){
			TriggerKey triggerKey = new TriggerKey(jkJob.getJobName(),jkJob.getJobGroup());
			Trigger trigger = scheduler.getTrigger(triggerKey);
			if (trigger instanceof CronTrigger) {
				CronTrigger crontrigger = (CronTrigger) trigger;
				// 获取trigger拥有的Job
				JobKey jobKey = trigger.getJobKey();
				JobDetailImpl jobDetail = (JobDetailImpl) scheduler.getJobDetail(jobKey);
				// 组装页面需要显示的数据
				QuartzJobsVO quartzJobsVO = new QuartzJobsVO();
				quartzJobsVO.setJobId(jkJob.getId());
				quartzJobsVO.setJobName(jkJob.getJkJobName());
				quartzJobsVO.setJobType(jkJob.getJkJobType());
				quartzJobsVO.setGroupName(jobKey.getGroup());
				quartzJobsVO.setJobDetailName(jobDetail.getName());
				quartzJobsVO.setJobClassName(jobDetail.getJobClass().getName());
				quartzJobsVO.setJobCronExpression(crontrigger.getCronExpression());
				quartzJobsVO.setTimeZone(crontrigger.getTimeZone().getDisplayName());
				quartzJobsVO.setState(scheduler.getTriggerState(triggerKey));
				quartzJobsVOList.add(quartzJobsVO);
			}
		}
		flipInfo.setData(quartzJobsVOList);
		return flipInfo;
	}
	
	/**
	 * 挂起
	 * @param jobDetailName
	 * @param jobGroupName
	 * @throws Exception
	 * @return
	 */
	public Map<String, Object> jobPause(String jobDetailName, String jobGroupName) throws SchedulerException {
		scheduler.pauseJob(JobKey.jobKey(jobDetailName, jobGroupName));
		return new HashMap<>();
    }
	
	/**
	 * 恢复
	 * @param jobDetailName
	 * @param jobGroupName
	 * @throws Exception
	 */
	public Map<String, Object> jobResume(String jobDetailName, String jobGroupName) throws SchedulerException {
		scheduler.resumeJob(JobKey.jobKey(jobDetailName, jobGroupName));
		return new HashMap<>();
    }

	/**
	 * 新建定时任务
	 * @param params
	 * 		  jobId 任务ID
	 *        jobName 定时任务名称（name）
	 *        jobType 定时任务类型 常规 或者 seeyon
	 *        jobClassName job class全路径
	 *        jobGroupName
	 *        cronExpression cron表达式
	 * @throws Exception
	 */
	public Map<String, Object> jobSave(HashMap<String, Object> params) throws Exception {
		Long jobId = MapUtils.getLong(params,"jobId");
		String jobName = MapUtils.getString(params,"jobName");
		Integer jobType = MapUtils.getInteger(params,"jobType");
		String jobDetailName = MapUtils.getString(params,"jobDetailName");
		String jobClassName = MapUtils.getString(params,"jobClassName");
		String jobGroupName = MapUtils.getString(params,"jobGroupName");
		String cronExpression = MapUtils.getString(params,"cronExpression");
		// 构建job信息
		switch (jobType) {
			case 0:
				if(scheduler.checkExists(new JobKey(jobDetailName, jobGroupName))){//如果存在不创建
					break;
				}
				//TODO 此处应该有bug，当优化
				JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName))
						.withIdentity(jobDetailName, jobGroupName).build();
				// 表达式调度构建器(即任务执行的时间)
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
				// 按新的cronExpression表达式构建一个新的trigger
				CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobDetailName, jobGroupName)
						.withSchedule(scheduleBuilder).build();
				try {
					scheduler.scheduleJob(jobDetail, trigger);
				} catch (SchedulerException e) {
					e.printStackTrace();
					throw new Exception("创建定时任务失败");
				}
				break;
			case 1:
				QuartzHolder.newCronQuartzJob(jobGroupName, jobDetailName, cronExpression,
						new Date(), null, jobDetailName, new HashMap<String, String>());
				break;
		}
		//构建Jk_Job
		JkJob job = buildJkJob(jobId,jobName, jobType, jobDetailName, jobGroupName, cronExpression);
		return new HashMap<>();
	}

	/**
	 * 判断任务是否存在
	 *
	 * @param jobDetailName 定时任务名称
	 * @param jobGroupName  组名称
	 * @throws Exception
	 * @return
	 */
	@Override
	public Map<String, Object> jobExist(String jobDetailName, String jobGroupName) throws Exception {
		if (scheduler.checkExists(new JobKey(jobDetailName,jobGroupName)))
			throw new Exception(String.format("[%s,%s]已存在",jobDetailName,jobGroupName));
		return new HashMap<>();
	}

	/**
	 * 删除任务
	 *
	 * @param jobDetailName 定时任务名称
	 * @param jobGroupName  组名称
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> jobDelete(String jobDetailName, String jobGroupName) throws SchedulerException {
		scheduler.deleteJob(new JobKey(jobDetailName, jobGroupName));
		return new HashMap<>();
	}

	private JkJob buildJkJob(Long jobId,String jobName, Integer jobType,
							 String jobDetailName, String jobGroupName, String cronExpression) throws Exception {
		JkJob job = null;
		if(jobId==null) {
			job = new JkJob();
			job.setNewId();
			job.setJkJobName(jobName);
			job.setJkJobType(jobType);
			job.setJobName(jobDetailName);
			job.setJobGroup(jobGroupName);
			job.setTriggerName(jobDetailName);
			job.setTriggerGroup(jobGroupName);
			job.setMemberId(AppContext.getCurrentUser().getId());
			job.setCreateDate(new Date());
			job.setDeleted(false);
			DBAgent.save(job);
		}else{
			job = jkDao.get(jobId);
			job.setJkJobName(jobName);
			job.setJkJobType(jobType);
			job.setJobName(jobDetailName);
			job.setJobGroup(jobGroupName);
			job.setTriggerName(jobDetailName);
			job.setTriggerGroup(jobGroupName);
			job.setMemberId(AppContext.getCurrentUser().getId());
			job.setCreateDate(new Date());
			job.setDeleted(false);
			jobreschedule(jobDetailName, jobGroupName, cronExpression);
			DBAgent.update(job);
		}
		return job;
	}

	/**
	 * 更新定时任务
	 * @param jobDetailName
	 * @param jobGroupName
	 * @param cronExpression
	 * @throws Exception
	 */
	public boolean jobreschedule(String jobDetailName, String jobGroupName, String cronExpression) throws Exception {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobDetailName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            throw new Exception("更新定时任务失败");
        }
        return true;
    }

	/**
	 * @param id 主键
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> jobById(Long id) throws Exception {
		Map<String, Object> res = new HashMap<>();
		res.put("job",jkDao.get(id));
		return res;
	}

	/**
	 *
	 * @param ids id数组
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> jobBatchDelete(Long[] ids) throws Exception {
		for(Long id : ids){
			JkJob jkJob = jkDao.get(id);
			jobDelete(jkJob.getJobName(),jkJob.getJobGroup());
			jkJob.setDeleted(true);
			DBAgent.update(jkJob);
		}
		return new HashMap<>();
	}

	private static <T extends Job> Class<T> getClass(String classname) throws Exception {
		Class<T> clazz = (Class<T>)Class.forName(classname);
		return clazz;
	}

}
