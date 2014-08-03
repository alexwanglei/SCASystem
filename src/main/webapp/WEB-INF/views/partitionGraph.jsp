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
			<div>
				<table id="tc" class="easyui-datagrid" title="Task communication in the application" style="wdith:500px" data-options="rownumbers:true,fitColumns:true,singleSelect:true,onClickRow:onClickRow_tc">
					<thead>
						<tr>
							<th data-options="field:'appId', width:100">Application Id</th>
							<th data-options="field:'appName',width:100">Application Name</th>
							<th data-options="field:'srcTask',width:80">Src Task</th>
							<th data-options="field:'dstTask',width:80">Dst Task</th>
							<th data-options="field:'variable',width:100">Communication Variable</th>
							<th data-options="field:'type',width:80,
								formatter:function(value,row){
									return row.typename;
								},
								editor:{
									type:'combobox',
									options:{
										valueField:'typevalue',
										textField:'typename',
										data:[{typevalue:'blackboard',typename:'Blackboard'},{typevalue:'buffer',typename:'Buffer'}]
									}
								}
							">Communication Type</th>
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
								<td></td>
							</tr>
						</c:forEach>
					</c:forEach>
					</tbody>
				</table>
			</div>
			
			<div>
				<table id="ac" class="easyui-datagrid" title="Communications between applications "  data-options="rownumbers:true,fitColumns:true,singleSelect:true,onClickRow:onClickRow_ac">
					<thead>
						<tr>
							<th data-options="field:'srcApp',width:80">Src Application</th>
							<th data-options="field:'dstApp',width:80">Dst Application</th>
							<th data-options="field:'variable',width:100">Communication Variable</th>
							<th data-options="field:'type',width:80,
								formatter:function(value,row){
									return row.typename;
								},
								editor:{
									type:'combobox',
									options:{
										valueField:'typevalue',
										textField:'typename',
										data:[{typevalue:'sample',typename:'Sample'},{typevalue:'queue',typename:'Queue'}]
									}
								}
							">Communication Type</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${appCommList}" var="appCom">
						<tr>
							<td>${appCom.srcApp.name}</td>
							<td>${appCom.dstApp.name}</td>
							<td>${appCom.variable.name}</td>
							<td></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			
			<div>
				<table id="ec" class="easyui-datagrid" title="Communications between applications and external "  data-options="rownumbers:true,fitColumns:true,singleSelect:true,onClickRow:onClickRow_ec">
					<thead>
						<tr>
							<th data-options="field:'srcApp',width:80">Src Application</th>
							<th data-options="field:'dstApp',width:80">Dst Application</th>
							<th data-options="field:'variable',width:100">Communication Variable</th>
							<th data-options="field:'type',width:80,
								formatter:function(value,row){
									return row.typename;
								},
								editor:{
									type:'combobox',
									options:{
										valueField:'typevalue',
										textField:'typename',
										data:[{typevalue:'sample',typename:'Sample'},{typevalue:'queue',typename:'Queue'}]
									}
								}
							">Communication Type</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${exCommList}" var="exCom">
						<tr>
							<td>${exCom.srcApp.name}</td>
							<td>${exCom.dstApp.name}</td>
							<td>${exCom.variable.name}</td>
							<td></td>
						</tr>
					</c:forEach>
					</tbody>
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
	editor.setValue(${partitionsXml});
	
	var editIndex = undefined;
    function endEditing_tc(){
        if (editIndex == undefined){return true}
        if ($('#tc').datagrid('validateRow', editIndex)){
            var ed = $('#tc').datagrid('getEditor', {index:editIndex,field:'type'});
            var typename = $(ed.target).combobox('getText');
            $('#tc').datagrid('getRows')[editIndex]['typename'] = typename;
            $('#tc').datagrid('endEdit', editIndex);
            editIndex = undefined;
            return true;
        } else {
            return false;
        }
    }
	function onClickRow_tc(index){
        if (editIndex != index){
            if (endEditing_tc()){
                $('#tc').datagrid('selectRow', index)
                        .datagrid('beginEdit', index);
                editIndex = index;
            } else {
                $('#tc').datagrid('selectRow', editIndex);
            }
        }
    }
	
	var editIndex2 = undefined;
    function endEditing_ac(){
        if (editIndex2 == undefined){return true}
        if ($('#ac').datagrid('validateRow', editIndex2)){
            var ed = $('#ac').datagrid('getEditor', {index:editIndex2,field:'type'});
            var typename = $(ed.target).combobox('getText');
            $('#ac').datagrid('getRows')[editIndex2]['typename'] = typename;
            $('#ac').datagrid('endEdit', editIndex2);
            editIndex2 = undefined;
            return true;
        } else {
            return false;
        }
    }
	function onClickRow_ac(index){
        if (editIndex2 != index){
            if (endEditing_ac()){
                $('#ac').datagrid('selectRow', index)
                        .datagrid('beginEdit', index);
                editIndex2 = index;
            } else {
                $('#ac').datagrid('selectRow', editIndex2);
            }
        }
    }
	
	
	var editIndex3 = undefined;
    function endEditing_ec(){
        if (editIndex3 == undefined){return true}
        if ($('#ec').datagrid('validateRow', editIndex3)){
            var ed = $('#ec').datagrid('getEditor', {index:editIndex3,field:'type'});
            var typename = $(ed.target).combobox('getText');
            $('#ec').datagrid('getRows')[editIndex3]['typename'] = typename;
            $('#ec').datagrid('endEdit', editIndex3);
            editIndex3 = undefined;
            return true;
        } else {
            return false;
        }
    }
	function onClickRow_ec(index){
        if (editIndex3 != index){
            if (endEditing_ec()){
                $('#ec').datagrid('selectRow', index)
                        .datagrid('beginEdit', index);
                editIndex3 = index;
            } else {
                $('#ec').datagrid('selectRow', editIndex3);
            }
        }
    }
	
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
					if(data.equals("success"))
						alert("save success!");
					else
						alert("save fail!");
				}
			});
		}
		else{
			if(endEditing_tc()&&endEditing_ac()&&endEditing_ec()){
				var tcRows = $('#tc').datagrid('getRows');
				var acRows = $('#ac').datagrid('getRows');
				var ecRows = $('#ec').datagrid('getRows');
				var tcData = JSON.stringify(tcRows);
				var acData = JSON.stringify(acRows);
				var ecData = JSON.stringify(ecRows);
				
				$.ajax({
					type:"POST",
					url:"generateSAModel",
					data:{tc:tcData,
						ac:acData,
						ec:ecData,
						filename:${filename},
					},
					success:function(data){
						alert(data);
						if(data=="success")
							alert("save success!");
						else
							alert("save fail!");
					}
				});
			}
			
		}
		
	}
</script>
</body>
</html>