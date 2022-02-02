package com.seeyon.apps.jk.manager;

import com.seeyon.ctp.util.FlipInfo;
import org.quartz.SchedulerException;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>TODO</p>
 *
 * @author DELk
 * @@version 1.0.0
 * @since 2022/2/1
 * @return ajax 返回Map key:success,msg
 */
public interface JkManager {
    /**
     * 获取定时任务列表
     * @param flipInfo
     * @param query
     * @return
     * @throws SchedulerException
     */
    FlipInfo getJkList(FlipInfo flipInfo, Map<String, Object> query) throws Exception;
    /**
     * 挂起
     * @param jobDetailName
     * @param jobGroupName
     * @throws Exception
     * @return
     */
    Map<String, Object> jobPause(String jobDetailName, String jobGroupName) throws SchedulerException;
    /**
     * 恢复
     * @param jobDetailName
     * @param jobGroupName
     * @throws Exception
     */
    Map<String, Object> jobResume(String jobDetailName, String jobGroupName) throws SchedulerException;
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
    Map<String, Object> jobSave(HashMap<String, Object> params) throws Exception;

    /**
     * 判断任务是否存在
     * @param jobDetailName 定时任务名称
     * @param jobGroupName  组名称
     * @throws Exception
     * @return
     */
    Map<String, Object> jobExist(String jobDetailName, String jobGroupName) throws Exception;

    /**
     * 删除任务
     * @param jobDetailName 定时任务名称
     * @param jobGroupName 组名称
     * @return
     * @throws Exception
     */
    Map<String, Object> jobDelete(String jobDetailName, String jobGroupName) throws SchedulerException;
    /**
     * 删除任务(批量)
     * @param ids id数组
     * @return
     * @throws Exception
     */
    Map<String, Object> jobBatchDelete(Long[] ids) throws Exception;
}
