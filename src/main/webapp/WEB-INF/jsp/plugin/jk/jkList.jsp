<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css"
	href="${path}/apps_res/plugin/jk/css/jk.css${ctp:resSuffix()}">
<script type="text/javascript"
	src="${path}/ajax.do?managerName=jkManager"></script>
<script type="text/javascript" charset="UTF-8"
	src="${path}/apps_res/plugin/jk/js/jkList.js${ctp:resSuffix()}"></script>
<title>quartz任务列表</title>
</head>
<body>
	<div class="toolbar" id="toolbar"></div>
	<div id="table-warp">
		<table id="jkList"></table>
	</div>
</body>
</html>