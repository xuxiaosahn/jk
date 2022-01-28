package com.seeyon.apps.jk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.seeyon.ctp.common.controller.BaseController;
import com.seeyon.ctp.common.exceptions.BusinessException;

public class JkController extends BaseController{
	@Override
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		 ModelAndView mav = new ModelAndView("plugin/jk/jkList");
		return mav;
	}
    public ModelAndView	jobEdit(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
    	ModelAndView mav = new ModelAndView("plugin/jk/jobEdit");
		return mav;
    }
}
