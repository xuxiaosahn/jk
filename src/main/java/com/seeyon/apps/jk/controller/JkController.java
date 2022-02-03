package com.seeyon.apps.jk.controller;

import com.alibaba.fastjson.JSON;
import com.seeyon.apps.jk.manager.JkManager;
import com.seeyon.apps.jk.po.JkJob;
import com.seeyon.apps.jk.vo.QuartzJobsVO;
import com.seeyon.ctp.common.AppContext;
import com.seeyon.ctp.common.controller.BaseController;
import com.seeyon.ctp.common.exceptions.BusinessException;
import com.seeyon.ctp.common.quartz.QuartzListener;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class JkController extends BaseController{

	@Inject
	private JkManager jkManager;

	public JkManager getJkManager() {
		return jkManager;
	}

	public void setJkManager(JkManager jkManager) {
		this.jkManager = jkManager;
	}

	@Override
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		 ModelAndView mav = new ModelAndView("plugin/jk/jkList");
		return mav;
	}

	/**
	 * 新建/编辑
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ModelAndView	jobEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		ModelAndView mav = new ModelAndView("plugin/jk/jobEdit");
		if(StringUtils.isNotBlank(id)){
			Map<String, Object> res = jkManager.jobById(Long.parseLong(id));
			if(MapUtils.getBoolean(res,"success")){
				JkJob job = (JkJob)MapUtils.getObject(res,"job");
				QuartzJobsVO jobsVO = new QuartzJobsVO(job);
				//取job和trigger的值
				JobKey jobKey = new JobKey(job.getJobName(),job.getJobGroup());
				TriggerKey triggerKey = new TriggerKey(job.getTriggerName(),job.getTriggerGroup());
				CronTrigger trigger = (CronTrigger) QuartzListener.getScheduler().getTrigger(triggerKey);
				JobDetail jobDetail = QuartzListener.getScheduler().getJobDetail(jobKey);
				jobsVO.setJobClassName(jobDetail.getJobClass().getName());
				jobsVO.setJobCronExpression(trigger.getCronExpression());
				//取运行参数的值，懒得做类型判断，暂且全用String
				JobDataMap jobDataMap = jobDetail.getJobDataMap();
				if(jobDataMap.getKeys().length>0) {
					Map<String,String> tempMap = new HashMap<>();
					for (String key : jobDataMap.getKeys()) {
						tempMap.put(key, jobDataMap.getString(key));
					}
					jobsVO.setJobParams(tempMap);
				}
				Map<String, Job> jobNames = AppContext.getBeansOfType(Job.class);
				mav.addObject("job",JSON.toJSON(jobsVO));
			}
		}
		return mav;
    }
}
