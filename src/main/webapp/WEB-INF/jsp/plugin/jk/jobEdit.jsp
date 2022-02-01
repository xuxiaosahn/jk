<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="${path}/apps_res/plugin/jk/css/jk.css${ctp:resSuffix()}">
<script type="text/javascript" src="${path}/ajax.do?managerName=jkManager"></script>
<script type="text/javascript" charset="UTF-8" src="${path}/apps_res/plugin/jk/js/jobEdit.js${ctp:resSuffix()}"></script>
<title>任务设置</title>
</head>
<body id="body" class="h100b">
       <div class="form_obj" style="width: 520px;padding-top: 20px;">
		   <%--ID--%>
		   <input type="hidden" id="jobId" name="jobId">
		   <div class="row_div clearfix small_record fixed_label">
			   <div class="label_cell mid_label" title="名称">名称:<%--名称--%></div>
			   <div class="content_cell mid_label">
				   <div class="common_txtbox_wrap">
					   <input type="text" id="jobName" name="jobName" />
				   </div>
			   </div>
		   </div>
		   <div class="row_div clearfix small_record fixed_label">
			   <div class="label_cell mid_label" title="类型">类型:<%--类型--%></div>
			   <div class="content_cell mid_label">
				   <div class="common_txtbox_wrap">
					   <input type="text" id="jobType" name="jobType" />
				   </div>
			   </div>
		   </div>
		<div class="row_div clearfix small_record fixed_label">
			<div class="label_cell mid_label" title="Job名称">Job名称:<%--Job名称--%></div>
			<div class="content_cell mid_label">
				<div class="common_txtbox_wrap">
					<input type="text" id="jobDetailName" name="jobDetailName" />
				</div>
			</div>
		</div>
		<div class="row_div clearfix small_record fixed_label">
			<div class="label_cell mid_label" title="GROUP名称">GROUP名称:<%--GROUP名称--%></div>
			<div class="content_cell mid_label">
				<div class="common_txtbox_wrap">
					<input type="text" id="groupName" name="groupName" />
				</div>
			</div>
		</div>
		<div class="row_div clearfix small_record fixed_label">
			<div class="label_cell mid_label" title="CLASS路径">CLASS路径:<%--CLASS路径--%></div>
			<div class="content_cell mid_label">
				<div class="common_txtbox_wrap">
					<input type="text" id="jobClassName" name="jobClassName" />
				</div>
			</div>
		</div>
		<div class="row_div clearfix small_record fixed_label">
			<div class="label_cell mid_label" title="CRON表达式">CRON表达式:<%--CRON表达式--%></div>
			<div class="content_cell mid_label">
				<div class="common_txtbox_wrap">
					<input type="text" id="jobCronExpression" name="jobCronExpression" />
				</div>
			</div>
		</div>
		<div class="row_div clearfix small_record fixed_label">
			<div class="label_cell mid_label" title="状态">状态:<%--状态--%></div>
			<div class="content_cell mid_label">
				<div class="common_txtbox_wrap">
					<input type="text" id="state" name="state" />
				</div>
			</div>
		</div>		
	</div>
</body>
</html>