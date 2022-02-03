<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="${path}/apps_res/plugin/jk/css/jk.css${ctp:resSuffix()}">
<script type="text/javascript" src="${path}/ajax.do?managerName=jkManager"></script>
<script type="text/javascript" charset="UTF-8" src="${path}/apps_res/plugin/jk/js/jobEdit.js${ctp:resSuffix()}"></script>
	<%--引入可编辑列表--%>
<link rel="stylesheet" type="text/css" href="${path}/common/js/ui/seeEditorTable/css/seeEditorTable.css${ctp:resSuffix()}"/>
<script src="${path}/common/js/ui/seeEditorTable/js/seeyon.ui.editorTable.min.js${ctp:resSuffix()}"></script>
<title>任务设置</title>
</head>
<body id="body" class="h100b">
       <div class="form_obj" style="width: 520px;padding-top: 20px;">
		   <%--ID--%>
		   <input type="hidden" id="jobId" name="jobId">
		   <div class="row_div clearfix small_record fixed_label">
			   <div class="label_cell mid_label" title="名称"><i class="required">*</i>名称:<%--名称--%></div>
			   <div class="content_cell mid_label">
				   <div class="common_txtbox_wrap">
					   <input type="text" id="jobName" name="jobName" />
				   </div>
			   </div>
		   </div>
		   <div class="row_div clearfix small_record fixed_label">
			   <div class="label_cell mid_label" title="类型"><i class="required">*</i>类型:<%--类型--%></div>
			   <div class="content_cell mid_label">
<%--				   <div class="common_txtbox_wrap">--%>
<%--					   <input type="text" id="jobType" name="jobType" />--%>
					   <select name="jobType" id="jobType" onchange="changeType(this)" style="width:100%;margin-right: 2px;height:26px;line-height: 26px;border:1px solid #e4e4e4;">
						   <option value="0">原生quartz</option>
						   <option value="1">CTP quartz</option>
					   </select>
<%--				   </div>--%>
			   </div>
		   </div>
		<div class="row_div clearfix small_record fixed_label">
			<div class="label_cell mid_label" title="Job名称"><i class="required">*</i>Job名称:<%--Job名称--%></div>
			<div class="content_cell mid_label">
				<div class="common_txtbox_wrap">
					<input type="text" id="jobDetailName" name="jobDetailName" />
				</div>
			</div>
		</div>
		<div class="row_div clearfix small_record fixed_label">
			<div class="label_cell mid_label" title="GROUP名称"><i class="required">*</i>GROUP名称:<%--GROUP名称--%></div>
			<div class="content_cell mid_label">
				<div class="common_txtbox_wrap">
					<input type="text" id="groupName" name="groupName" />
				</div>
			</div>
		</div>
		<div class="row_div clearfix small_record fixed_label">
			<div class="label_cell mid_label" title="CLASS路径"><i class="required">*</i>CLASS路径:<%--CLASS路径--%></div>
			<div class="content_cell mid_label">
				<div class="common_txtbox_wrap">
					<input type="text" id="jobClassName" name="jobClassName" <c:if test="${job.jobType==1}">readonly="readonly" disabled="disabled"</c:if>/>
				</div>
			</div>
		</div>
		<div class="row_div clearfix small_record fixed_label">
			<div class="label_cell mid_label" title="CRON表达式"><i class="required">*</i>CRON表达式:<%--CRON表达式--%></div>
			<div class="content_cell mid_label">
				<div class="common_txtbox_wrap">
					<input type="text" id="jobCronExpression" name="jobCronExpression" />
				</div>
			</div>
		</div>
	    <div class="row_div clearfix small_record fixed_label">
			<div class="label_cell mid_label">运行参数:</div>
		</div>
		<div id = "paramDiv" style="margin-left: 10px;">
			<div class="toolbar" id="toolbar"></div>
			<table id="formulaTable" class="flexme3" border="0" cellspacing="0"
				   cellpadding="0"> </table>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function () {
		// 此处改为直接从controller里取
		// var transParams = window.parentDialogObj["editJobDialog"].getTransParams();
		var job = '${job}';
		configEditor.init(job?JSON.parse(job):'');
		window.OK = function (params) {
			if(params.cancel){
				return configEditor.cancel(params.callback);
			}else{
				return configEditor.save(params.callback);
			}
		};
	});
</script>
</html>