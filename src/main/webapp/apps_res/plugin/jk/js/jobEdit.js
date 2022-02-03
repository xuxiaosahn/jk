var configEditor = (function () {
	var ajax = new jkManager();
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