/**
 * jk quartz 定时任务管理
 */
/** 工具栏对象 */
var toolbar;

/** 数列表表对象 */
var grid;

/** 当前选择的实体类型*/
var currentEntityType;

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
            conditionObj.entityType = currentEntityType
            var choose = $('#'+searchobj.p.id).find("option:selected").val();
            //实体名称
            if(choose === 'entityName'){
            	conditionObj.entityName = $('#entityName').val();
            }
            //实体编码
            else if(choose === 'entityCode'){
            	conditionObj.entityCode = $('#entityCode').val();
            }
            //操作类型
            else if(choose === 'synType'){
            	conditionObj.synType = $('#synType').val();
            }
            //同步时间
            else if(choose === 'synDate'){
                var fromDate = $('#from_synDate').val();
                var toDate = $('#to_synDate').val();
                var date = fromDate+'#'+toDate;
                conditionObj.synDate = date;
                if(fromDate != "" && toDate != "" && fromDate > toDate){
                    $.alert("开始时间不能早于结束时间!");
                    return;
                }
            }
            //同步状态
            else if(choose === 'synState'){ //流程状态
            	conditionObj.synState=$('#synState').val();
            }
            
            var val = searchobj.g.getReturnValue();
            if(val !== null){
                $("#jkList").ajaxgridLoad(conditionObj);
            }
        },
        conditions: [{
        	//实体名称
            id: 'entityName',
            name: 'entityName',
            type: 'input',
            text: '名称',
            value: 'entityName',
            maxLength:100
        },{
        	//实体编码
            id: 'entityCode',
            name: 'entityCode',
            type: 'input',
            text: '编码',
            value: 'entityCode',
            maxLength:100
        },{
        	//操作类型
            id: 'synType',
            name: 'synType',
            type: 'select',
            text: '操作类型',
            value: 'synType',
            items: [{
                text:  '新增',
                value: '1'
            }, {
                text:  '更新',
                value: '2'
            }, {
                text:  '删除',
                value: '3'
            }]
        },{
        	//同步时间
            id: 'synDate',
            name: 'synDate',
            type: 'datemulti',
            text: '同步时间',
            value: 'synDate',
            ifFormat:'%Y-%m-%d',
            dateTime: false
        },{
        	//同步状态
        	id:'synState',
        	name:'synState',
        	type:'select',
        	text:'同步状态',
        	value: 'synState',
            items:[{
            	text:'失败',
            	value:'-1'
            },{
            	text:'成功',
            	value:'1'
            }]
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
function rend(txt, data, r, c) {
    if(c === 1){
    	//标题列加深
	    txt="<font class='red-input'>"+txt+"</font>";
        return txt;
    }else if(c===3){
    	return txt;
    }else if(c === 4){
       
        return txt;
    }else if(c === 5){
    	return txt;
    }else if (c === 6){
    	return txt;
    }else{
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
 * 新建/编辑JOB
 */
function _editJob(job){
	var transParams = {};
	if(typeof job !== undefined){
		transParams.job = job;
	}
	var topWin = getCtpTop();
	var editJobDialog = topWin.$.dialog({
		url : _ctxPath + "/jk.do?method=jobEdit",
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
            callBackendMethod("jkManager", "jobPause", rows[0].jobDetailName,rows[0].groupName,{
                success : function(res) {
                    if (res.success){
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
            callBackendMethod("jkManager", "jobResume", rows[0].jobDetailName,rows[0].groupName,{
                success : function(res) {
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