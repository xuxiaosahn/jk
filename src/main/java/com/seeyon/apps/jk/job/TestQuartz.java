package com.seeyon.apps.jk.job;

import com.seeyon.ctp.common.quartz.QuartzJob;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class TestQuartz implements QuartzJob {
	private static Log LOG = LogFactory.getLog(TestQuartz.class);
	@Override
	public void execute(Map<String, String> params) {
		// TODO Auto-generated method stub
		System.out.println("do sth.");
		System.out.println("do sth. end");
		LOG.info("hello "+params.get("name"));
	}
	
}
