/**
 * jk quartz 定时任务管理
 */
/** 工具栏对象 */
var toolbar;

/** 数列表表对象 */
var grid;

var proce; //进度条
function closeProce(){
    if(proce){
        proce.close();
    }
}

/**
 * 页面初始化
 */
$().ready(function () {

    var toolbarArray = new Array();
    //全部日志
	toolbarArray.push({id: "add",name: '新建',className: "ico16",click:addJob});
	toolbarArray.push({id: "edit",name: '编辑',className: "ico16 editor_16",click:editJob});
    toolbarArray.push({id: "pause",name: '暂停',className: "ico16 toback_16",click:pauseJob});
    toolbarArray.push({id: "resume",name: '恢复',className: "ico16 refresh_16",click:resumeJob});
    toolbarArray.push({id: "delete",name: '删除',className: "ico16 del_16",click:deleteJob});

    //设置工具栏
    toolbar = $("#toolbar").toolbar({
        toolbar: toolbarArray
    });
    
    //搜索框
    var topSearchSize = 5;
    if($.browser.msie && $.browser.version=='6.0'){
        topSearchSize = 5;
    }
    var searchobj = $.searchCondition({
        top:topSearchSize,
        right:10,
        searchHandler: function(){
        	//查询条件对象
            var conditionObj = new Object();
            var choose = $('#'+searchobj.p.id).find("option:selected").val();
            if(choose === 'jobName'){
            	conditionObj.jobName = $('#jobName').val();
            }
            else if(choose === 'jobDetailName'){
            	conditionObj.jobDetailName = $('#jobDetailName').val();
            }
            else if(choose === 'jobGroupName'){
                conditionObj.jobGroupName = $('#jobGroupName').val();
            }
            else if(choose === 'jobType'){
            	conditionObj.jobType = $('#jobType').val();
            }
            else if(choose === 'createDate'){
                var fromDate = $('#from_createDate').val();
                var toDate = $('#to_createDate').val();
                if (fromDate == ""){
                    $.alert("开始时间不能为空");
                    return;
                }
                if (toDate == ""){
                    $.alert("结束时间不能为空");
                    return;
                }
                var date = fromDate+'#'+toDate;
                conditionObj.createDate = date;
                if(fromDate != "" && toDate != "" && fromDate > toDate){
                    $.alert("开始时间不能晚于结束时间!");
                    return;
                }
            }
            //同步状态
            else if(choose === 'triggerState'){ //流程状态
            	conditionObj.triggerState=$('#triggerState').val();
            }
            var val = searchobj.g.getReturnValue();
            if(val !== null){
                $("#jkList").ajaxgridLoad(conditionObj);
            }
        },
        conditions: [{
        	//任务名称
            id: 'jobName',
            name: 'jobName',
            type: 'input',
            text: '名称',
            value: 'jobName',
            maxLength:100
        },{
        	//jobDetailName
            id: 'jobDetailName',
            name: 'jobDetailName',
            type: 'input',
            text: 'jobDetailName',
            value: 'jobDetailName',
            maxLength:100
        },{
            //jobGroupName
            id: 'jobGroupName',
            name: 'jobGroupName',
            type: 'input',
            text: '组名称',
            value: 'jobGroupName',
            maxLength:100
        },{
        	//jobType
            id: 'jobType',
            name: 'jobType',
            type: 'select',
            text: '类型',
            value: 'jobType',
            items: [{
                text:  '原生quartz',
                value: 0
            }, {
                text:  'CTP quartz',
                value: 1
            }]
        },{
        	//创建时间
            id: 'createDate',
            name: 'createDate',
            type: 'datemulti',
            text: '创建时间',
            value: 'createDate',
            ifFormat:'%Y-%m-%d %H:%M',
            dateTime: true
        }]
    });
    
    //表格加载
    grid = $('#jkList').ajaxgrid({
		gridType : 'autoGrid', // 宽度自适应
        colModel: [{
        	//选择框
            display: 'jobId',
            name: 'jobId',
            width: 'smallest',
			align : 'center',
            type: 'checkbox'
        }, {
            display: '名称',
            name: 'jobName',
            sortable : true,
            width: 'medium',
            align : 'center'
        }, {
            display: '类型',
            name: 'jobType',
            sortable : true,
            width: 'medium',
            align : 'center'
        }, {
            display: 'JobDetail名称',
            name: 'jobDetailName',
            sortable : true,
            width: 'medium',
            align : 'center'
        }, {
            display: 'GROUP名称',
            name: 'groupName',
            sortable : true,
            width: 'medium',
			align : 'center'
        }, {
            display: 'CLASS路径',
            name: 'jobClassName',
            sortable : true,
            width: 'medium',
			align : 'center'
        }, {
            display: 'CRON表达式',
            name: 'jobCronExpression',
            sortable : true,
            width: 'medium',
			align : 'center'
        }, {
            display: '状态',
            name: 'state',
            sortable : true,
            width: 'small',
			align : 'center'
        }],
        dblclick: function(data, r, cIndex){
                _editJob(data);
        },
        render : rend,
        showTableToggleBtn: true,
        parentId: $('.layout_center').eq(0).attr('id'),
        vChange: true,
        vChangeParam: {
            overflow: "hidden",
            autoResize:true
        },
        // isHaveIframe:true,
        slideToggleBtn:false,
		resizable : false,
		parentId: "table-warp",
        managerName : "jkManager",
        managerMethod : "getJkList"
    });
    $('#jkList').ajaxgridLoad();
    //默认显示全部日志
    $("#all_a").css('backgroundColor','white'); 
});

/**
 * 列表加载 回调函数
 */
function rend(txt,rowData, rowIndex, colIndex,colObj) {
    if(colIndex === 1){
    	//标题列加深
	    txt="<font class='red-input'>"+txt+"</font>";
        return txt;
    }else {
        if (colObj.name === "jobType") {
            if (txt == 0) {
                txt = "原生quartz"
            } else if (txt == 1) {
                txt = "CTP quartz"
            }
        }else if (colObj.name === "state") {
            if (txt === "NORMAL") {
                txt = "<span class=\"ico16 online_news_16\"></span>"+"运行中";
            } else if (txt === "PAUSED") {
                txt = "<span class=\"ico16 flow_pend_16\"></span>"+"暂停";
            } else if (txt === "COMPLETE") {
                txt = "<span class=\"ico16 flow_end_16\"></span>" + "完成";
            } else if (txt === "ERROR") {
                txt = "<span class=\"ico16 h_risk_16\"></span>"+"错误";
            } else if (txt === "BLOCKED") {
                txt = "<span class=\"ico16 unstart_16\"></span>"+"阻塞";
            }
        }
        return txt;
    }
} 

/**
 * 新建Job
 */
function addJob(){
	_editJob();
}
/**
 * 编辑Job
 */
function editJob(job){
    var rows = grid.grid.getSelectRows();
    if(rows.length == 0){
        $.alert("请选择要编辑的数据");//请选择要编辑的数据
        return;
    }
    _editJob(rows[0]);
}
/**
 * 编辑Job
 */
function deleteJob(){
    var rows = grid.grid.getSelectRows();
    if(rows.length == 0){
        $.alert("请选择要删除的数据");//请选择要编辑的数据
        return;
    }
    var ids = new Array();
    rows.forEach(function (e){
        ids.push(e.jobId);
    })
    $.confirm({
        'msg': "确定要删除吗？删除后不可恢复！",
        ok_fn: function () {
            proce = $.progressBar({text:"正在删除任务"});
            callBackendMethod("jkManager","jobBatchDelete",ids,{
                success:function (rs) {
                    if(rs.success){
                        $.infor("删除数据成功");//删除数据成功
                    }else{
                        $.error(rs.msg);
                    }
                    closeProce();
                    _doRelaod();
                }
            });
        },
        cancel_fn:function(){

        }
    });
}

/**
 * 新建/编辑JOB
 */
function _editJob(job){
	var transParams = {};
	var idStr;
	if(typeof job !== undefined){
		transParams.id = job.jobId;//这个不用了，留着备忘
        idStr = "&id="+job.jobId;
	}
	var topWin = getCtpTop();
	var editJobDialog = topWin.$.dialog({
		url : _ctxPath + "/jk.do?method=jobEdit"+idStr,
		id:"editJobDialog",
		title : "任务设置",
		targetWindow : topWin,
		transParams : transParams,
		width : 540,
		height : 400,
	    closeParam:{
	        'show':true,
	        autoClose:false,
	        handler:function(){
	        	editJobDialog.getReturnValue({
					callback:function () {// 关闭dialog
						editJobDialog.close();
					},
					cancel:true
				});
	        }
	    },
		buttons: [{
			id: "sure", 
			isEmphasize: true,
			text: $.i18n('common.button.ok.label'),
			handler: function () {
				editJobDialog.getReturnValue({
					callback:function () {// 保存完成 关闭dialog,并重新加载页面
						_doRelaod();
						editJobDialog.close();
					}
				});
			}
		}, {
			id: "cancel",
			text: $.i18n('common.button.cancel.label'),
			handler: function () {
				editJobDialog.getReturnValue({
					callback:function () {// 关闭dialog
						editJobDialog.close();
					},
					cancel:true
				});
			}
		}]
	});
}
/**
 * 暂停Job
 */
function pauseJob(){
	var rows = grid.grid.getSelectRows();
	var ids = "";
    if(rows.length <= 0) {
        $.alert("请选择要重新执行的记录");
        return true;
    }
    if(rows.length > 1){
		$.alert("只能选择一条记录");
        return true;
	}
    var confirm = $.confirm({
        'msg': "是否进行暂停操作?",
        ok_fn: function () {
            proce = $.progressBar({text:"正在暂停任务"});
            callBackendMethod("jkManager", "jobPause", rows[0].jobDetailName,rows[0].groupName,{
                success : function(res) {
                    if (res.success){
                        closeProce();
                        $.messageBox({
                            'title':'提示框',
                            'type': 0,
                            'msg': '暂停成功',
                            'imgType':0,
                            ok_fn:function(){
                                _doRelaod();
                            }
                        });
                    }else{
                        $.error(res.msg);
                    }
                }
            });
        }
    });
}
/**
 * 恢复Job
 */
function resumeJob(){
	var rows = grid.grid.getSelectRows();
	var ids = "";
    if(rows.length <= 0) {
        $.alert("请选择要重新执行的记录");
        return true;
    }
    if(rows.length > 1){
		$.alert("只能选择一条记录");
        return true;
	}
    var confirm = $.confirm({
        'msg': "是否进行恢复操作?",
        ok_fn: function () {
            proce = $.progressBar({text:"正在恢复任务"});
            callBackendMethod("jkManager", "jobResume", rows[0].jobDetailName,rows[0].groupName,{
                success : function(res) {
                    closeProce();
                    if (res.success){
                        $.messageBox({
                            'title':'提示框',
                            'type': 0,
                            'msg': '恢复成功',
                            'imgType':0,
                            ok_fn:function(){
                                _doRelaod();
                            }
                        });
                    }else{
                        $.error(res.msg);
                    }
                }
            });
        }
    });
}

function _doRelaod(){
	$("#jkList").ajaxgridLoad();
}