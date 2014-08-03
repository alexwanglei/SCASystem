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
    

</style>
<body>

	<div id="subtt" class="easyui-tabs"
		data-options="fit:true,border:true,tabPosition:'bottom',tools:'#tab-tools'">
		<div title="source">
			<pre id="editor"></pre>
		</div>
		<div title="design">
			<form id="partition-form" method="post" action="completePartition">
			<input type="hidden" name="filename" value=${filename} ></input>
			<div>
				<table id="blackboards" class="easyui-datagrid" title="The Blackboard used in partition" data-options="rownumbers:true,fitColumns:true,singleSelect:true">
					<thead>
						<tr>
							<th data-options="field:'bbId', width:100">Blackboard Id</th>
							<th data-options="field:'bbName',width:100">Blackboard Name</th>
							<th data-options="field:'bbMessageSize',width:100">Message Size</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${blackboards}" var="blackboard">
						<tr>
							<td>${blackboard.id}</td>
							<td>${blackboard.name}</td>
							<td>
								<input class="easyui-validatebox" type="text" name="bbMessageSize" data-options="required:true" value=${blackboard.messageSize}></input>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<div>
				<table id="buffers" class="easyui-datagrid" title="The Buffer used in partition"  data-options="rownumbers:true,fitColumns:true,singleSelect:true">
					<thead>
						<tr>
							<th data-options="field:'bfId', width:50">Buffer Id</th>
							<th data-options="field:'bfName',width:80">Buffer Name</th>
							<th data-options="field:'bfMessageSize',width:80">Message Size</th>
							<th data-options="field:'bfBufferLength',width:80">Buffer Length</th>
							<th data-options="field:'bfBufferDiscipline',width:80">Discipline</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${buffers}" var="buffer">
						<tr>
							<td>${buffer.id}</td>
							<td>${buffer.name}</td>
							<td>
								<input class="easyui-validatebox" type="text" name="bfMessageSize" data-options="required:true" value=${buffer.messageSize}></input>
							</td>
							<td>
								<input class="easyui-validatebox" type="text" name="bfLength" data-options="required:true" value=${buffer.bufferLength}></input>
							</td>
							<td>
								<c:set var="bfDiscipline" value="${buffer.bufferLength}"/>
								<c:choose>
									<c:when test="${bfDiscipline.equals('PRIORITY')}">
										<select name="bfDiscipline">
											<option value="FIFO" >FIFO</option>
											<option value="PRIORITY" selected="selected">PRIORITY</option>
										</select>
									</c:when>
									<c:otherwise>
										<select name="bfDiscipline">
											<option value="FIFO" selected="selected">FIFO</option>
											<option value="PRIORITY" >PRIORITY</option>
										</select>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			
			  
			<div>
				<table id="appSamplePorts" class="easyui-datagrid" title="The Sample Port used by application"  data-options="rownumbers:true,fitColumns:true,singleSelect:true">
					<thead>
						<tr>
							<th data-options="field:'appSpId',width:50">Port Id</th>
							<th data-options="field:'appSpName',width:80">Port Name</th>
							<th data-options="field:'appSpDirection',width:80">Direction</th>
							<th data-options="field:'appSpMessageSize',width:80">Message Size</th>
							<th data-options="field:'appSpRefreshPeriod',width:80">Refresh Period</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${appSamplePorts}" var="appSamplePort">
						<tr>
							<td>${appSamplePort.id}</td>
							<td>${appSamplePort.name}</td>
							<td>${appSamplePort.direction}</td>
							<td>
								<input class="easyui-validatebox" type="text" name="appSpMessageSize" data-options="required:true" value=${appSamplePort.messageSize}></input>
							</td>
							<td>
								<input class="easyui-validatebox" type="text" name="appSpRefreshPeriod" data-options="required:true" value=${appSamplePort.refreshPeriod}></input>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			
		  	<div>
				<table id="appQueuePorts" class="easyui-datagrid" title="The Queue Port used by application"  data-options="rownumbers:true,fitColumns:true,singleSelect:true">
					<thead>
						<tr>
							<th data-options="field:'appQpId',width:50">Port Id</th>
							<th data-options="field:'appQpName',width:80">Port Name</th>
							<th data-options="field:'appQpDirection',width:80">Direction</th>
							<th data-options="field:'appQpMessageSize',width:80">Message Size</th>
							<th data-options="field:'appQpQueueLength',width:80">Queue Length</th>
							<th data-options="field:'appQpProtocol',width:120">Protocol</th>
							<th data-options="field:'appQpDiscipline',width:80">Discipline</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${appQueuePorts}" var="appQueuePort">
						<tr>
							<td>${appQueuePort.id}</td>
							<td>${appQueuePort.name}</td>
							<td>${appQueuePort.direction}</td>
							<td>
								<input style="width:100px" class="easyui-validatebox" type="text" name="appQpMessageSize" data-options="required:true" value=${appQueuePort.messageSize}></input>
							</td>
							<td>
								<input style="width:100px" class="easyui-validatebox" type="text" name="appQpQueueLength" data-options="required:true" value=${appQueuePort.queueLength}></input>
							</td>
							<td>
							  	<c:set var="direction" value="${appQueuePort.direction}"/>
								<c:choose>
									<c:when test="${direction.equals('DESTINATION')}">
										<input  style="width:150px" class="easyui-validatebox" type="text" name="appQpProtocol" data-options="required:true" value="NOT_APPLICABLE" readonly="readonly"></input>
									</c:when>
									<c:otherwise>
										<select style="width:155px" name="appQpProtocol">
											<option value="SENDER_BLOCK"  selected="selected">SENDER_BLOCK</option>
											<option value="RECEIVER_DISCARD">RECEIVER_DISCARD</option>
										</select>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:set var="appQpDiscipline" value="${appQueuePort.discipline}"/>
								<c:choose>
									<c:when test="${appQpDiscipline.equals('PRIORITY')}">
										<select name="appQpDiscipline">
											<option value="FIFO" >FIFO</option>
											<option value="PRIORITY" selected="selected">PRIORITY</option>
										</select>
									</c:when>
									<c:otherwise>
										<select name="appQpDiscipline">
											<option value="FIFO" selected="selected">FIFO</option>
											<option value="PRIORITY" >PRIORITY</option>
										</select>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			
			<div>
				<table id="daSamplePorts" class="easyui-datagrid" title="The Direct-Access Sample Port used by Partition"  data-options="rownumbers:true,fitColumns:true,singleSelect:true">
					<thead>
						<tr>
							<th data-options="field:'daSpId',width:50">Port Id</th>
							<th data-options="field:'daSpName',width:80">Port Name</th>
							<th data-options="field:'daSpDirection',width:80">Direction</th>
							<th data-options="field:'daSpMessageSize',width:80">Message Size</th>
							<th data-options="field:'daSpRefreshPeriod',width:80">Refresh Period</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${daSamplePorts}" var="daSamplePort">
						<tr>
							<td>${daSamplePort.id}</td>
							<td>${daSamplePort.name}</td>
							<td>${daSamplePort.direction}</td>
							<td>
								<input class="easyui-validatebox" type="text" name="daSpMessageSize" data-options="required:true" value=${daSamplePort.messageSize}></input>
							</td>
							<td>
								<input class="easyui-validatebox" type="text" name="daSpRefreshPeriod" data-options="required:true" value=${daSamplePort.refreshPeriod}></input>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		
		
			<div>
				<table id="daQueuePorts" class="easyui-datagrid" title="The Direct-Access Queue Port used by Partition"  data-options="rownumbers:true,fitColumns:true,singleSelect:true">
					<thead>
						<tr>
							<th data-options="field:'daQpId',width:50">Port Id</th>
							<th data-options="field:'daQpName',width:80">Port Name</th>
							<th data-options="field:'daQpDirection',width:80">Direction</th>
							<th data-options="field:'daQpMessageSize',width:80">Message Size</th>
							<th data-options="field:'daQpQueueLength',width:80">Queue Length</th>
							<th data-options="field:'daQpProtocol',width:120">Protocol</th>
							<th data-options="field:'daQpDiscipline',width:80">Discipline</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${daQueuePorts}" var="daQueuePort">
						<tr>
							<td>${daQueuePort.id}</td>
							<td>${daQueuePort.name}</td>
							<td>${daQueuePort.direction}</td>
							<td>
								<input style="width:100px" class="easyui-validatebox" type="text" name="daQpMessageSize" data-options="required:true" value=${daQueuePort.messageSize}></input>
							</td>
							<td>
								<input style="width:100px" class="easyui-validatebox" type="text" name="daQpQueueLength" data-options="required:true" value=${daQueuePort.queueLength}></input>
							</td>
							<td>
							  	<c:set var="direction" value="${daQueuePort.direction}"/>
								<c:choose>
									<c:when test="${direction.equals('DESTINATION')}">
										<input  style="width:150px" class="easyui-validatebox" type="text" name="daQpProtocol" data-options="required:true" value="NOT_APPLICABLE" readonly="readonly"></input>
									</c:when>
									<c:otherwise>
										<select style="width:155px" name="daQpProtocol">
											<option value="SENDER_BLOCK"  selected="selected">SENDER_BLOCK</option>
											<option value="RECEIVER_DISCARD">RECEIVER_DISCARD</option>
										</select>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:set var="daQpDiscipline" value="${daQueuePort.discipline}"/>
								<c:choose>
									<c:when test="${daQpDiscipline.equals('PRIORITY')}">
										<select name="daQpDiscipline">
											<option value="FIFO" >FIFO</option>
											<option value="PRIORITY" selected="selected">PRIORITY</option>
										</select>
									</c:when>
									<c:otherwise>
										<select name="daQpDiscipline">
											<option value="FIFO" selected="selected">FIFO</option>
											<option value="PRIORITY" >PRIORITY</option>
										</select>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
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
			$("#partition-form").submit();
		}
		
	}
</script>
</body>
</html>