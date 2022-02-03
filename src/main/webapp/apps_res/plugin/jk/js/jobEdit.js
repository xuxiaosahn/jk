var configEditor = (function () {
	var ajax = new jkManager();
	var gridObj;
	function _initPage(job){
		var transParams = window.parentDialogObj["editJobDialog"].getTransParams()
		//job = transParams.job;
		if(job) {
			$("#jobId").val(job.jobId);
			$("#jobName").val(job.jobName);
			$("#jobType").val(job.jobType);
			$("#jobDetailName").val(job.jobDetailName);
			$("#jobClassName").val(job.jobClassName);
			$("#groupName").val(job.groupName);
			$("#jobCronExpression").val(job.jobCronExpression);
		}
		$().ready(function () {
			//初始化toolbar
			$("#toolbar").toolbar({
				toolbar: [
					{
						name: "新增参数",
						className: "ico16 add",
						click: addConstant
					}
				]
			});
			//初始化表格
			gridObj = $("#formulaTable").ajaxEditorGrid({
				searchHTML: 'condition_box',
				colModel: [{
					display: 'id',
					name: 'id',
					width: '35',
					sortable: false,
					type: 'checkbox',
					isToggleHideShow: false
				},
					{
						display: "参数名称",
						name: 'paramName',
						width: 'medium',
						sortable: true,
						validator: function (a) {
							return a.length > 0 ;
						},
						validatorMsg: "参数名称不能为空"
					},
					{
						display: "参数值",
						name: 'paramValue',
						width: 'medium',
						sortable: true,
						validator: function (a) {
							return a.length > 0;
						},
						validatorMsg: "参数值不能为空"
					}],
				gridType: "autoGrid",
				showTableToggleBtn: true,
				managerName: "jkManager",
				managerMethod: "getParams",
				height:280,
				idField: 'id',
				parentId: "paramDiv",
				vChange: true,
				vChangeParam: {
					overflow: "hidden",
					autoResize: true
				},
				slideToggleBtn: true,
				usepager:false,
				onSuccess: initData
			});
			//加载表格
			var o = new Object();
			gridObj.ajaxgridLoad(o);
		});

		// 处理列表数据，不可修改的数据置灰
		function initData(listData) {
			var i = 0;
			if(job.jobParams){
				for(var key in job.jobParams){
					gridObj.addNewRow(seeyonCom.uid(), [i,key,job.jobParams[key]]);
					i++;
				}
			}
		}
		function addConstant() {
			gridObj.addNewRow(seeyonCom.uid(), gridObj.getDefCellValues());
		}
	}
	function _init(){

	}
	/**
	 * 校验页面参数，校验成功返回 数据对象，校验失败返回false
	 */
	function _validateAndGet(func,callback){
		//var selectedJob = jobMap[jobSelected.getSelValue()[0]];
		var job = {};
		job.jobId = $("#jobId").val();
		job.jobName = $("#jobName").val();
		job.jobType = $("#jobType").val();
		job.jobDetailName = $("#jobDetailName").val();
		job.jobClassName = $("#jobClassName").val();
		job.jobGroupName = $("#groupName").val();
		job.cronExpression = $("#jobCronExpression").val();
		if(gridObj.getChangeSubmitData()){
			// var paramMap = new Map();
			// var list = new Array();
			// list = JSON.parse(gridObj.getChangeSubmitData());
			// list.forEach(function (param) {
			// 	paramMap.set(param["paramName"],param["paramValue"]);
			// });
			job.jobParams = gridObj.getChangeSubmitData()
		}
		ajax.jobExist(job.jobDetailName,job.groupName,{
			success:function (rs) {
				if(!rs.success){
					$.error(rs.msg);
				}else{
					func(job,callback);
				}
			}
		});
	}
	var saving = false;
	function saveData(data,callback){
		if(!data || saving){
			return;
		}
		saving = true;
		ajax.jobSave(JSON.stringify(data),{
			success:function (rs) {
				saving = false;
				if(!rs.success){
					$.error(rs.msg);
					return;
				}
				callback();
			}
		});
	}
	return {
		init: _initPage,
		save:function (callback){
			_validateAndGet(saveData,callback);
		},
		cancel:function (callback){
			callback();
		}
	}
})();
function changeType(obj){//类型选择
	if(obj.value == "1"){
		$("#jobClassName").val("com.seeyon.ctp.common.quartz.QuartzJobProxy");
		$("#jobClassName").attr("readonly","readonly").attr("disabled","disabled");
	}else{
		$("#jobClassName").val("");
		$("#jobClassName").removeAttr("disabled").removeAttr("readonly");
	}
}
