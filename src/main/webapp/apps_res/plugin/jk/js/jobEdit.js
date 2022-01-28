var configEditor = (function () {
	var ajax = new jkManager();
	var jobMap = {};
	var isNew = true;//是否新建
	var jobSelected;
	function _initPage(params){
		
	}
	function _init(selecteds){
		var options = [];
		for (var index = 0; index < selecteds.length; index++) {
			var jobDetailName = selecteds[index];
			jobMap[job.jobDetailName] = jobDetailName;
		}
		jobSelected = new CtpUiComSelect($("#jobDetailName").get(0), { options: options });
	}
	/**
	 * 校验页面参数，校验成功返回 数据对象，校验失败返回false
	 */
	function _validateAndGet(onlyGet){
		//var selectedJob = jobMap[jobSelected.getSelValue()[0]];
		var job = {};
		job.jobDetailName = $("#jobDetailName").val();
		job.groupName = $("#groupName").val();
		job.jobCronExpression = $("#jobCronExpression").val();
		return job;
	}
	var saving = false;
	return {
		init: _initPage,
		save:function (callback){
			var data = _validateAndGet(false);
			if(!data || saving){
				return;
			}
			saving = true;
			ajax.addJob(data.jobDetailName,data.groupName,data.jobCronExpression,{
				success:function (rs) {
					saving = false;
					if(!rs.success){
						$.error(rs.msg);
						return;
					}
					callback();
				}
			});
		},
		cancel:function (callback){
			
		}
	}
})();
$(function () {
	var transParams = window.parentDialogObj["editJobDialog"].getTransParams();
	configEditor.init(transParams);
	window.OK = function (params) {
		if(params.cancel){
			return configEditor.cancel(params.callback);
		}else{
			return configEditor.save(params.callback);
		}
	};
});