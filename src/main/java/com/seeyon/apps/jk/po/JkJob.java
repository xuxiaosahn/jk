package com.seeyon.apps.jk.po;

import com.seeyon.ctp.common.po.BasePO;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 版权 @Copyright: 2022 Delk Inc. All rights reserved. 	
 * 项目名称： jk<br/>
 * 包名称：   com.seeyon.apps.jk.po
 * 文件名称： JkJob.java<br/>
 * 类名称：   JkJob<br/>  
 * 创建人：  @author 2543607510@qq.com<br/>   
 * 创建时间： 2022年1月29日/上午10:43:47<br/>   
 * 类描述：<br/> 
 * @version <br/>  
 * TODO
 */
public class JkJob extends BasePO implements Serializable{

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = -4585442694432583010L;
	/**
	 * 任务名称（描述）
	 */
	private String jkJobName;
	/**
	 * 任务类型
	 */
	private JkJobType jkJobType;
	/**
	 * 调度器名称
	 */
	private String schedName;
	/**
	 * 任务名称
	 */
	private String jobName;
	/**
	 * 任务组名称
	 */
	private String jobGroup;
	/**
	 * 触发器名称
	 */
	private String triggerName;
	/**
	 * 触发器组名称
	 */
	private String triggerGroup;
	/**
	 * 创建人ID
	 */
	private Long memberId;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新人ID
	 */
	private Long modifyId;
	/**
	 * 更新时间
	 */
	private Date modifyDate;
	/**
	 * 是否删除
	 */
	private Boolean isDeleted;
	
	public String getJkJobName() {
		return jkJobName;
	}
	public void setJkJobName(String jkJobName) {
		this.jkJobName = jkJobName;
	}
	public JkJobType getJkJobType() {
		return jkJobType;
	}
	public void setJkJobType(JkJobType jkJobType) {
		this.jkJobType = jkJobType;
	}
	public String getSchedName() {
		return schedName;
	}
	public void setSchedName(String schedName) {
		this.schedName = schedName;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
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
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getModifyId() {
		return modifyId;
	}
	public void setModifyId(Long modifyId) {
		this.modifyId = modifyId;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Boolean getDeleted() {
		return isDeleted;
	}
	public void setDeleted(Boolean deleted) {
		isDeleted = deleted;
	}
}
