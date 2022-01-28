package com.seeyon.apps.jk.vo;

import org.quartz.Trigger.TriggerState;

public class QuartzJobsVO {
	
	private String jobKey;
	private String jobDetailName;
    private String jobCronExpression;
    private String timeZone;
    private String groupName;
    private String jobClassName;
    private String triggerName;
    private String triggerGroup;
    private TriggerState state;
    
	public String getJobKey() {
		return jobKey;
	}
	public void setJobKey(String jobKey) {
		this.jobKey = jobKey;
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
    
}
