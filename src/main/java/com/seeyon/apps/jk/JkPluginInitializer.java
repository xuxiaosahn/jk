package com.seeyon.apps.jk;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.seeyon.ctp.common.AbstractSystemInitializer;
import com.seeyon.ctp.common.plugin.PluginDefinition;
import com.seeyon.ctp.common.plugin.PluginInitializer;
import com.seeyon.ctp.common.quartz.QuartzHolder;

public class JkPluginInitializer extends AbstractSystemInitializer implements PluginInitializer{
	private static Log LOG = LogFactory.getLog(JkPluginInitializer.class);
	private static final String GROUP_NAME = "test";
    private static final String JOB_NAME = "testQuartz";
    private static final String CRON_EXPRESSION = "*/10 * * * * ?";// FIXME */1 * * * * ? 每分钟执行一次; 0 0 2 * * ? 每天凌晨2点
    private static final Boolean IS_RE_INIT_JOB = Boolean.FALSE; // xxx 需要重新初始化job
	
	@Override
	public void initialize() {
		LOG.info("初始化quartz管理插件");
		//初始化Job
	      Map<String, String> parameters = new HashMap<String, String>();
	      try {
	          if( !QuartzHolder.hasQuartzJob(GROUP_NAME,JOB_NAME)){
	              QuartzHolder.newCronQuartzJob(GROUP_NAME, JOB_NAME, CRON_EXPRESSION,
	                      new Date(), null, JOB_NAME, parameters);
	          }else if(IS_RE_INIT_JOB){//FIXME 有且要重新生成
	              QuartzHolder.deleteQuartzJobByGroup(GROUP_NAME);
	              QuartzHolder.newCronQuartzJob(GROUP_NAME, JOB_NAME, CRON_EXPRESSION,
	                          new Date(), null, JOB_NAME, parameters);
	          }
	      } catch (Exception e) {
	          e.printStackTrace();
	      }
	}
	@Override
	public void destroy() {
		LOG.info("销毁quartz管理插件");
	}
	@Override
	public boolean isAllowStartup(PluginDefinition definition, Logger logger) {
		return true;
	}
}
