package com.seeyon.apps.jk.job;

import java.util.Map;

import com.seeyon.ctp.common.quartz.QuartzJob;

public class TestQuartz implements QuartzJob {

	@Override
	public void execute(Map<String, String> params) {
		// TODO Auto-generated method stub
		System.out.println("do sth.");
	}
	
}
