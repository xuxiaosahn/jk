package com.seeyon.apps.jk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.seeyon.ctp.common.AbstractSystemInitializer;
import com.seeyon.ctp.common.plugin.PluginDefinition;
import com.seeyon.ctp.common.plugin.PluginInitializer;

public class JkPluginInitializer extends AbstractSystemInitializer implements PluginInitializer{
	private static Log LOG = LogFactory.getLog(JkPluginInitializer.class);
	@Override
	public void initialize() {
		LOG.info("初始化quartz管理插件");
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
