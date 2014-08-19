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
			<table id="schedule" class="easyui-datagrid" title="Partition Schedule configuration" data-options="rownumbers:true,fitColumns:true,iconCls:'icon-edit',singleSelect:true,toolbar:'#tb',onClickRow:onClickRow">
				<thead>
					<tr>
						<th data-options="<c:out value="${value}"/>">Partition</th>
						<th data-options="field:'duration',width:50,editor:'numberbox'">Duration</th>
						<th data-options="field:'releasePoint',width:50,
								formatter:function(value,row){
									return row.rpname;
								},
								editor:{
									type:'combobox',
									options:{
										valueField:'rpvalue',
										textField:'rpname',
										data:[{rpvalue:'1',rpname:'1'},{rpvalue:'0',rpname:'0'}]
									}
								}
							">Release Point</th>
					</tr>
				</thead>
				
			</table>
			<div id="tb" style="height:auto">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="append()">Append</a>
        		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">Remove</a>
        		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="accept()">Accept</a>
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
	editor.setValue(${module.xmlModule});
	
	var editIndex = undefined;
    function endEditing(){
        if (editIndex == undefined){return true}
        if ($('#schedule').datagrid('validateRow', editIndex)){
            var ed = $('#schedule').datagrid('getEditor', {index:editIndex,field:'partition'});
            var partitionname = $(ed.target).combobox('getText');
            $('#schedule').datagrid('getRows')[editIndex]['partitionName'] = partitionname;
            
            var ed2 = $('#schedule').datagrid('getEditor', {index:editIndex,field:'releasePoint'});
            var releasePoint = $(ed2.target).combobox('getText');
            $('#schedule').datagrid('getRows')[editIndex]['rpname'] = releasePoint;
            
            $('#schedule').datagrid('endEdit', editIndex);
            editIndex = undefined;
            return true;
        } else {
            return false;
        }
    }
    function onClickRow(index){
        if (editIndex != index){
            if (endEditing()){
                $('#schedule').datagrid('selectRow', index)
                        .datagrid('beginEdit', index);
                editIndex = index;
            } else {
                $('#schedule').datagrid('selectRow', editIndex);
            }
        }
    }
    function append(){
        if (endEditing()){
            $('#schedule').datagrid('appendRow',{});
            editIndex = $('#schedule').datagrid('getRows').length-1;
            $('#schedule').datagrid('selectRow', editIndex)
                    .datagrid('beginEdit', editIndex);
        }
    }
    function removeit(){
        if (editIndex == undefined){return}
        $('#schedule').datagrid('cancelEdit', editIndex)
                .datagrid('deleteRow', editIndex);
        editIndex = undefined;
    }
    function accept(){
        if (endEditing()){
            $('#schedule').datagrid('acceptChanges');
        }
    }
	
	
	
	
	function save(){
		var tab = $('#subtt').tabs('getSelected');
		var index = $('#subtt').tabs('getTabIndex',tab);
		if(index == 0){
			//alert(editor.getValue());
			$.ajax({
				type: "POST",
				url: "saveModuleByXml",
				data:{moduleXml: editor.getValue(),
					filename:${filename},
				},
				success:function(data){
					alert("save success!");
				}
			});
		}
		else{
			if(endEditing()){
				var rows = $("#schedule").datagrid('getRows');
				$.ajax({
					type:"POST",
					url:"completeModule",
					data:{schedule:JSON.stringify(rows),
						filename:${filename},						
					},
					success:function(data){
						//alert(data);
						window.location.reload();
					}
				});
			}
		}
		
	}
</script>
</body>
</html>