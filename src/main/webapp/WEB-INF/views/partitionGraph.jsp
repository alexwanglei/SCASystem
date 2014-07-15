<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
			<div>
				<table class="easyui-datagrid" title="Task communication in the application" style="wdith:500px" data-options="rownumbers:true">
					<thead>
						<tr>
							<th data-options="field:'appId', width:100">Application Id</th>
							<th data-options="field:'appName',width:100">Application Name</th>
							<th data-options="field:'srcTask',width:80">Src Task</th>
							<th data-options="field:'dstTask',width:80">Dst Task</th>
							<th data-options="field:'variable',width:100">Communication Variable</th>
							<th data-options="field:'type',width:80">Communication Type</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${applications}" var="app">
						<c:forEach items="${app.taskCommunications }" var="tc">
							<tr>
								<td>${app.id }</td>
								<td>${app.name }</td>
								<td>${tc.srcTask.name}</td>
								<td>${tc.dstTask.name}</td>
								<td>${tc.variable.name}</td>
								<td>
									<select id="cc" class="easyui-combobox" name="dd">
										<option>Blackboard</option>
										<option>Buffer</option>
									</select>
								</td>
							</tr>
						</c:forEach>
					</c:forEach>
					</tbody>
				</table>
			</div>
			
			<select id="cc" class="easyui-combobox" name="dd">
										<option>Blackboard</option>
										<option>Buffer</option>
									</select>
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
	editor.setValue(${partitionsXml});
	
	function save(){
		var tab = $('#subtt').tabs('getSelected');
		var index = $('#subtt').tabs('getTabIndex',tab);
		if(index == 0){
			//alert(editor.getValue());
			$.ajax({
				type: "POST",
				url: "savePartitions",
				data:{partitionsXml: editor.getValue(),
					filename:${filename},
				},
				success:function(data){
					alert("save success!");
				}
			});
		}		
	}
</script>
</body>
</html>