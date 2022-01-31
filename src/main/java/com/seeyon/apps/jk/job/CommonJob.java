package com.seeyon.apps.jk.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <p>TODO</p>
 *
 * @author DELk
 * @@version 1.0.0
 * @since 2022/1/31
 */
public class CommonJob implements Job {
    private static Log LOG = LogFactory.getLog(CommonJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("执行普通的JOB");
    }
}