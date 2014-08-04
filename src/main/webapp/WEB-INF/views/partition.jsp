<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
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
    
    table{
		border-collapse:collapse;
		border:none;
		align:center;	
		font-size:12px;
		margin:10px auto;
	}
	th,td{
		border:1px solid #ccc;
	}
	div table th{
		background-color:#E0ECFF;	
	}
</style>
<body>

	<div id="subtt" class="easyui-tabs"
		data-options="fit:true,border:true,tabPosition:'bottom',tools:'#tab-tools'">
		<div title="source">
			<pre id="editor"></pre>
		</div>
		<div title="design">
			<form id="partition-form" method="post">
					<p>Intra-partition communication</p>
					<table id="buffer-table" title="Buffer">
						<tr>
							<th>Id</th>
							<th>Name</th>
							<th>MessageSize</th>
							<th>BufferLength</th>
							<th>Discipline</th>
						</tr>
						<c:forEach items="${buffers}" var="buffer">
							<tr>
								<td>${buffer.id}</td>
								<td>${buffer.name}</td>
								<td>
									<input type="text" value=${buffer.messageSize} name="bufferMessageSize"></input>
								</td>
								<td>
									<input type="text" value=${buffer.bufferLength} name="bufferLength"></input>
								</td>
								<td>
									<select name="bufferDiscipline">
										<option value="fifo">FIFO</option>
										<option value="priority">PRIORITY</option>
									</select>
								</td>
							</tr>
						</c:forEach>
					</table>
				
					<table id="blackboard-table" title="Blackboard">
						<tr>
							<th>Id</td>
							<th>Name</td>
							<th>MessageSize</td>
						</tr>
						<c:forEach items="${blackboards}" var="blackboard">
							<tr>
								<td>${blackboard.id}</td>
								<td>${blackboard.name}</td>
								<td>
									<input type="text" value=${blackboard.messageSize} name="blackboardMessageSize"></input>
								</td>
							</tr>
						</c:forEach>
					</table>
			
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
	editor.setValue(${partitionXml});
	
	function save(){
		var tab = $('#subtt').tabs('getSelected');
		var index = $('#subtt').tabs('getTabIndex',tab);
		if(index == 0){
			//alert(editor.getValue());
			$.ajax({
				type: "POST",
				url: "savePartitionByXml",
				data:{partitionXml: editor.getValue(),
					filename:${filename},
				},
				success:function(data){
					alert("save success!");
				}
			});
		}
		else{
			
		}
	}
</script>
</body>
</html>