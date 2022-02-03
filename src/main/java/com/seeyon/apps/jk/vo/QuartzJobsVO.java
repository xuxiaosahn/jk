package com.seeyon.apps.jk.vo;

import com.seeyon.apps.jk.po.JkJob;
import org.quartz.Trigger.TriggerState;
import java.util.Map;

public class QuartzJobsVO {
	
	private Long jobId;
	private String jobName;
	private Integer jobType;
	private String jobDetailName;
    private String jobCronExpression;
    private String timeZone;
    private String groupName;
    private String jobClassName;
    private String triggerName;
    private String triggerGroup;
    private TriggerState state;
    private Map<String, String> jobParams;

    public QuartzJobsVO(){

	}

	public QuartzJobsVO(JkJob jkJob){
		this.jobId = jkJob.getId();
		this.jobName = jkJob.getJkJobName();
		this.jobType = jkJob.getJkJobType();
		this.jobDetailName = jkJob.getJobName();
		this.groupName = jkJob.getJobGroup();
		this.triggerName = jkJob.getTriggerName();
		this.triggerGroup = jkJob.getTriggerGroup();
	}
    
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public Integer getJobType() {
		return jobType;
	}
	public void setJobType(Integer jobType) {
		this.jobType = jobType;
	}
	public String getJobDetailName() {
		return jobDetailName;
	}
	public void setJobDetailName(String jobDetailName) {
		this.jobDetailName = jobDetailName;
	}
	public String getJobCronExpression() {
		return jobCronExpression;
	}
	public void setJobCronExpression(String jobCronExpression) {
		this.jobCronExpression = jobCronExpression;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getJobClassName() {
		return jobClassName;
	}
	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public String getTriggerGroup() {
		return triggerGroup;
	}
	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}
	public TriggerState getState() {
		return state;
	}
	public void setState(TriggerState state) {
		this.state = state;
	}
	public Map<String, String> getJobParams() {
		return jobParams;
	}
	public void setJobParams(Map<String, String> jobParams) {
		this.jobParams = jobParams;
	}
}
