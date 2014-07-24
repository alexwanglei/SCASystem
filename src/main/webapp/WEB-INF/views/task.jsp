<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
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
			<form id="task-form" method="post" action="completeTask">
				<table cellpadding="5">
					<tr>
						<td>ID:</td>
						<td><input class="easyui-validatebox" type="text" name="id" data-options="required:true" value=${process.id} disabled="disabled"></input></td>
					</tr>
					<tr>
						<td>Name:</td>
						<td><input class="easyui-validatebox" type="text" name="name" data-options="required:true" value=${process.name}></input></td>
					</tr>
					<tr>
						<td>Stack:</td>
						<td><input class="easyui-validatebox" type="text" name="stack" data-options="required:true" value=${process.stack}></input></td>
					</tr>
					<tr>
						<td>Priorty:</td>
						<td><input class="easyui-validatebox" type="text" name="priorty" data-options="required:true" value=${process.priorty}></input></td>
					</tr>
					<tr>
						<td>Period:</td>
						<td><input class="easyui-validatebox" type="text" name="period" data-options="required:true" value=${process.period}></input></td>
					</tr>
					<tr>
						<td>Capacity:</td>
						<td><input class="easyui-validatebox" type="text" name="capacity" data-options="required:true" value=${process.capacity}></input></td>
					</tr>
					<tr>
						<td>Deadline:</td>
						<td>
							<select class="easyui-combobox" name="deadline">
								<option value="soft" selected="selected">SOFT</option>
								<option value="hard">HARD</option>
							</select>
						</td>
					</tr>
				</table>
				<input type="hidden" name="filename" value=${filename} ></input>		
			</form>
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
	editor.setValue(${process.xmlProcess});
	
/*	$("#task-form").form({
		url:"completeTask",
		onSubmit:function(){
			
		},
		success:function(data){
			alert(data);
		}
	});
*/	
	function save(){
		var tab = $('#subtt').tabs('getSelected');
		var index = $('#subtt').tabs('getTabIndex',tab);
		if(index == 0){
			//alert(editor.getValue());
			$.ajax({
				type: "POST",
				url: "saveTaskByXml",
				data:{taskXml: editor.getValue(),
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
			$("#task-form").submit();
		}
		
	}
</script>
</body>
</html>