package com.seeyon.apps.jk.job;

import com.seeyon.ctp.common.quartz.QuartzJob;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class TestQuartz2 implements QuartzJob {
	private static Log LOG = LogFactory.getLog(TestQuartz2.class);
	@Override
	public void execute(Map<String, String> params) {
		// TODO Auto-generated method stub
		System.out.println("do sth.");
		LOG.info("hello "+params.get("name"));
	}
	
}
