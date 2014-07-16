<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="resources/style/main.css" />
	<link rel="stylesheet" href="resources/script/jquery-easyui/themes/default/easyui.css" />
	<link rel="stylesheet" href="resources/script/jquery-easyui/themes/icon.css" />
	<script src="resources/script/jquery-easyui/jquery-2.0.0.js"></script>
	<script src="resources/script/jquery-easyui/jquery.easyui.min.js"></script>
</head>
<style type="text/css" media="screen">
    #editor { 
    	margin-top:0px;
    	margin-bottom:0px;
        width:100%;
        height:100%;
    }
</style>
<body>

	<div id="subtt" class="easyui-tabs"
		data-options="fit:true,border:true,tabPosition:'bottom',tools:'#tab-tools'">
		<div title="source">
			<pre id="editor"></pre>
		</div>
		
		<div title="design">
			<div style="float:left">
				<table id="task" class="easyui-datagrid" title="Tasks" data-options="rownumbers:true,fitColumns:true" style="width:200px">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
							<th data-options="field:'name',width:100">Name</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${tasks}" var="task">
						<tr>
							<td>${task.id}</td>
							<td>${task.name}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			
			<input id="creatApplication" type="button" value="Creat a Application" style="float:left;margin:30px 10px"/>
			
			<div style="float:left">
				<table id="application" class="easyui-datagrid" title="Applications" data-options="rownumbers:true,fitColumns:true,nowrap:false,onClickCell: onClickCell,singleSelect: true" style="width:400px">
					<thead>
						<tr>
							<th data-options="field:'name',width:100,editor:'text'" >Name</th>
							<th data-options="field:'ids',width:100">Tasks ids</th> 
							<th data-options="field:'tasks',width:200">Tasks in the application</th>
						</tr>
					</thead>
				</table>
			</div>	
		</div>
	</div>
	<div id="tab-tools">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'" onclick="save()"></a>
    </div>
<script src="resources/script/ace-builds/src-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>

<script>

	var editor = ace.edit("editor");
	editor.setTheme("ace/theme/eclipse");
	editor.getSession().setMode("ace/mode/xml");
	editor.setValue(${tasksXml});
	
	//创建任务按钮点击处理函数
	$("#creatApplication").click(function(){
		var checkrows = $("#task").datagrid("getChecked");

		var names="";
		var ids="";
		for(var i=0; i<checkrows.length;i++){
			ids=ids+checkrows[i].ck+";";
			names=names+checkrows[i].name+";";
		}
		
		$("#application").datagrid('appendRow',{
			name:"new application",
			ids:ids,
			tasks:names,
		});
		
		$("#task").datagrid('clearChecked');
		
	}); 
	
	//单击编辑单元格
	 $.extend($.fn.datagrid.methods, {
         editCell: function(jq,param){
             return jq.each(function(){
                 var opts = $(this).datagrid('options');
                 var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
                 for(var i=0; i<fields.length; i++){
                     var col = $(this).datagrid('getColumnOption', fields[i]);
                     col.editor1 = col.editor;
                     if (fields[i] != param.field){
                         col.editor = null;
                     }
                 }
                 $(this).datagrid('beginEdit', param.index);
                 for(var i=0; i<fields.length; i++){
                     var col = $(this).datagrid('getColumnOption', fields[i]);
                     col.editor = col.editor1;
                 }
             });
         }
     });
     
     var editIndex = undefined;
     function endEditing(){
         if (editIndex == undefined){return true}
         if ($('#application').datagrid('validateRow', editIndex)){
             $('#application').datagrid('endEdit', editIndex);
             editIndex = undefined;
             return true;
         } else {
             return false;
         }
     }
     function onClickCell(index, field){
         if (endEditing()){
             $('#application').datagrid('selectRow', index)
                     .datagrid('editCell', {index:index,field:field});
             editIndex = index;
         }
     }
	
	
	function save(){
		var tab = $('#subtt').tabs('getSelected');
		var index = $('#subtt').tabs('getTabIndex',tab);
		if(index == 0){
			//alert(editor.getValue());
			$.ajax({
				type: "POST",
				url: "saveTasks",
				data:{tasksXml: editor.getValue(),
					filename:${filename},
				},
				success:function(data){
					if(data.equals("success"))
						alert("save success!");
					else
						alert("save fail!");
				}
			});
		}
		//生成任务
		else{
			var rows = $('#application').datagrid('getRows');
			var applicationNames = [];
			var taskIds = [];
			for(var i=0; i<rows.length;i++){
				applicationNames[i]=rows[i].name;
				taskIds[i] = rows[i].ids;
			}
			$.ajax({
				type:"POST",
				url:"generateApplication",
				traditional: true,
				data:{applicationNames:applicationNames,
					taskIds:taskIds,
					filename:${filename},
				},
				success:function(data){
					if(data=="success"){
						alert("Generate application file success!");
					}
					else{
						alert("Generate application file fail!");
					}
					
				}
			});
		}
	}
</script>
</body>
</html>