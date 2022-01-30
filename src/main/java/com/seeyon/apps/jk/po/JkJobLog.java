package com.seeyon.apps.jk.po;


import com.seeyon.ctp.common.po.BasePO;

import java.io.Serializable;
import java.util.Date;

public class JkJobLog extends BasePO implements Serializable {
    /**
     * 任务ID
     */
    private Long jobId;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 是否手工执行
     */
    private Boolean isManual;
    /**
     * 任务执行状态
     */
    private ExecuteStatus executeStatus;
    /**
     * 是否删除
     */
    private Boolean isDeleted;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getManual() {
        return isManual;
    }

    public void setManual(Boolean manual) {
        isManual = manual;
    }

    public ExecuteStatus getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(ExecuteStatus executeStatus) {
        this.executeStatus = executeStatus;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}
