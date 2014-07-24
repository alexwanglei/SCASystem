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
			<form id="partition-form" method="post">
				<table id="msContainer" title="Intra-partition communication" data-options="rownumbers:true,fitColumns:true,singleSelect: true" >
					<thead>
						<tr>
							<th data-options="field:id">Id</th>
							<th data-options="field:type">Type</th>
							<th data-options="field:name">Name</th>
							<th data-options="field:messageSize">MessageSize</th>
						<!-- 	<th data-options="field:bufferLength">BufferLength</th>
							<th data-options="field:discipline">Discipline</th> -->
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${partitions.intraComs.msgContainer}" var="mc">
							<tr>
								<td>${mc.id}</td>
								<td>
									<c:if test="${fn:contains(${mc.name},'buffer')}">
										<c:out value="${'Buffer'}"/>
									</c:if>
									<c:if test="${fn:contains(${mc.name},'blackboard')}">
										<c:out value="${'Blackboard'}"/>
									</c:if>
								</td>
								<td>${mc.name}</td>
								<td>
									<input type="text" value=${mc.messageSize}></input>
								</td>
								
							</tr>
						</c:forEach>
					</tbody>
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
	editor.setValue(${partition.xmlPartition});
	
	function save(){
		var tab = $('#subtt').tabs('getSelected');
		var index = $('#subtt').tabs('getTabIndex',tab);
		if(index == 0){
			//alert(editor.getValue());
			$.ajax({
				type: "POST",
				url: "savePartitionByXml",
				data:{taskXml: editor.getValue(),
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