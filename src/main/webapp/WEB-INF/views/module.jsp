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
			<table id="schedule" class="easyui-datagrid" title="Partition Schedule configuration" data-options="rownumbers:true,fitColumns:true,singleSelect:true,toolbar:toolbar">
				<thead>
					<tr>
						<th data-options="field:'partition',width:100">Partition</th>
						<th data-options="field:'duration',width:50">Duration</th>
						<th data-options="field:'releasePoint',width:50">Release Point</th>
					</tr>
				</thead>
			</table>
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
	
	var toolbar=[{
		text:'Add',
		iconCls:'icon-add',
		handler:function(){
			alert("add");
			$('#schedule').datagrid('appendRow',{
				partition:'',
				duration: '',
				releasePoint:''
			});
		}
	},{
		text:'Delete',
		iconCls:'icon-remove',
		handler:function(){
			alert("del");
		}
	}]
	
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
			
		}
		
	}
</script>
</body>
</html>