<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>SCAS</title>
	<link rel="stylesheet" type="text/css" href="resources/style/main.css" />
	<link rel="stylesheet" type="text/css" href="resources/style/app.css"/>
	<link rel="stylesheet" href="resources/script/jquery-easyui/themes/default/easyui.css" />
	<link rel="stylesheet" href="resources/script/jquery-easyui/themes/icon.css" />
	<script src="resources/script/jquery-easyui/jquery-2.0.0.js"></script>
	<script src="resources/script/jquery-easyui/jquery.easyui.min.js"></script>
	<script src="resources/script/jquery-easyui/datagrid-dnd.js"></script>
</head>
<body>
    <div id="content">
    	<div id="top">
		    <div id="menubar">
			    <a href="#" class="easyui-menubutton" menu="#mm1" >Edit</a>
			    <a href="#" class="easyui-menubutton" menu="#mm2" >Help</a>
			</div>
			<div id="mm1" style="width:150px;">
			    <div iconCls="icon-undo">Undo</div>
			    <div iconCls="icon-redo">Redo</div>
			    <div class="menu-sep"></div>
			    <div>Cut</div>
			    <div>Copy</div>
			    <div>Paste</div>
			    <div class="menu-sep"></div>
			    <div iconCls="icon-remove">Delete</div>
			    <div>Select All</div>
			</div>
			<div id="mm2" style="width:100px;">
			    <div>Help</div>
			    <div>Update</div>
			    <div>About</div>
			</div>
			
			<div id="toolbar">
				<a id="genTask" href="#" class="easyui-linkbutton" plain="true" >CreatTask</a>
				<a id="genPart" href="#" class="easyui-linkbutton" plain="true" >CreatPartition</a>
				<a id="save" href="#" class="easyui-linkbutton" plain="true" >Save</a>
				<a id="refresh" href="#" class="easyui-linkbutton" plain="true" >Refresh</a>
				<a id="genCCode" href="#" class="easyui-linkbutton" plain="true" >GenCCode</a>
				<a id="genConf" href="#" class="easyui-linkbutton" plain="true" >GenConf</a>
			</div>
		</div>
		
		<div id="container" class="easyui-layout">
			<div region="west" title="Navigator" split="true" style="width:200px">
				<ul id="directory" class="easyui-tree" data-options="animate:true"></ul>
			</div>
			<div region="center" >
				<div class="easyui-layout" fit="true">
					<div region="center">
						<div id="tt" class="easyui-tabs" data-options="fit:true,border:false">
							
							<!--  	<div id="grid-container" class="easyui-layout" data-options="fit:true"><div id="left1" region="west" split="true" style="width:500px"></div><div id="right1" region="center"></div></div>-->
							
						</div>
					</div>
					<div id="bottom" region="south" split="true" style="height:150px;">
						<table id='bottomdg' data-options='fit:true,fitColumns:true'></table>
					</div>
				</div>
			</div>
			<div id="rightPanel" title="right panle" data-options="region:'east',split:true" style="width:250px">
				<table id='rightdg' data-options='fit:true,fitColumns:true'></table>
			</div>
		</div>
		
	<!--	<div id="footer">
			<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add"></a>
		</div>  -->
		<div id="ctdlg" class="easyui-dialog" title="Creat a Task" style="width:400px;height:200px" 
				data-options="cache:false,modal:true,closed:true,
						buttons:[{
							text:'Save',
	  						handler:function(){
	  							var tab = $('#tt').tabs('getSelected');
  								var id = tab.attr('id');
	  							var selectedData = $('#leftdg-'+id).datagrid('getSelections');
	  							var formulas = new Array();
	  							for(var i=0; i< selectedData.length; i++){
	  								formulas[i] = selectedData[i]['formula'];
	  							}
								$('#middg-'+id).datagrid('appendRow',{
	  									task:$('#ctname').val(),
	  									formula:formulas,
	  							});
	  							$('#ctdlg').dialog('close');			
	  						}
	  					},{
	  						text:'Close',
	  						handler:function(){
	  							$('#ctdlg').dialog('close');
	  						}
						}]">
			任务名字:<input id="ctname" class="easyui-validatebox" type="text" name="name" data-options="required:true"></input>
		</div>
		<div id="cpdlg" class="easyui-dialog" title="Creat a Partition" style="width:400px;height:200px" 
				data-options="cache:false,modal:true,closed:true,
						buttons:[{
							text:'Save',
	  						handler:function(){
	  							var tab = $('#tt').tabs('getSelected');
  								var id = tab.attr('id');
	  							var selectedData = $('#middg-'+id).datagrid('getSelections');
	  							var tasks = new Array();
	  							for(var i=0; i< selectedData.length; i++){
	  								tasks[i] = selectedData[i]['task'];
	  							}
								$('#rightdg-'+id).datagrid('appendRow',{
	  									partition:$('#cpname').val(),
	  									task:tasks,
	  							});
	  							$('#cpdlg').dialog('close');			
	  						}
	  					},{
	  						text:'Close',
	  						handler:function(){
	  							$('#cpdlg').dialog('close');
	  						}
						}]">
			分区名字:<input id="cpname" class="easyui-validatebox" type="text" name="name" data-options="required:true"></input>
		</div>
		
		<div id="sdlg" class="easyui-dialog" title="Save the Task" style="width:400px;height:200px" 
				data-options="cache:false,modal:true,closed:true,
						buttons:[{
							text:'Save',
	  						handler:function(){
	  								var tab = $('#tt').tabs('getSelected');
  									var id = tab.attr('id');
  									var formulaData = $('#leftdg-'+id).datagrid('getRows');
	  								var taskData = $('#middg-'+id).datagrid('getRows');
	  								var partData = $('#rightdg-'+id).datagrid('getRows');
	  								$.ajax({
					  	  				type:'POST',
					  	  				url:'createTask.do',
					  	  				dataType:'json',
					  	  				data:{filename:$('#sname').val(),formulaList:JSON.stringify(formulaData),taskList:JSON.stringify(taskData),partList:JSON.stringify(partData)},
					  	  				success:function(msg){
					  	  					
					  	  				}
					  	  			});
	  	  							$('#sdlg').dialog('close');
	  	  										
	  						}
	  					},{
	  						text:'Close',
	  						handler:function(){
	  							$('#sdlg').dialog('close');
	  						}
						}]">
			文件保存为:<input id="sname" class="easyui-validatebox" type="text" name="name" data-options="required:true"></input>
		</div>
		
    </div>
</body>
<script type="text/javascript">
	$("#refresh").click(function(){
		$.ajax({
			type:"GET",
			url:"refreshDirectory",
			success:function(data){
				$("#directory").tree('reload');
				$("#directory").tree({
					data:eval("("+data.directory+")")
				})
			}
		});
	});
	
	//生成C代码
	$("#genCCode").click(function(){
		var node = $("#directory").tree('getSelected');
		var filename = node.text;
		//获取文件扩展名
		var pastfix = filename.substr(filename.indexOf(".")+1, filename.length);
		if(node == null ||pastfix != "amm" ){
			alert("don't select the module file!");
		}
		else {
			//获取文件在File文件的完整路径
			var filename =node.text;
			var parentnode;
			var curnode = node;
			while(parentnode=$("#directory").tree('getParent',curnode.target))
			{
				filename = parentnode.text+"/"+ filename;
				curnode = parentnode;
			}
			$.ajax({
				type:"POST",
				url:"generateCCode",
				data:{filename:filename},
				success:function(data){
					alert("success");
				}
			});
		}
	});
	
	//生成配置文件
	$("#genConf").click(function(){
		var node = $("#directory").tree('getSelected');
		var filename = node.text;
		//获取文件扩展名
		var pastfix = filename.substr(filename.indexOf(".")+1, filename.length);
		if(node == null ||pastfix != "amm" ){
			alert("don't select the module file!");
		}
		else {
			//获取文件在File文件的完整路径
			var filename =node.text;
			var parentnode;
			var curnode = node;
			while(parentnode=$("#directory").tree('getParent',curnode.target))
			{
				filename = parentnode.text+"/"+ filename;
				curnode = parentnode;
			}
			$.ajax({
				type:"POST",
				url:"generateConf",
				data:{filename:filename},
				success:function(data){
					alert("success");
				}
			});
		}
	});
	
	//初始化目录树
	$("#directory").tree({
		data: ${directory}
	});
	
	//双击打开目录中的文件
	$("#directory").tree({
		onDblClick:function(node){
			//判断双击的是文件还是文件夹
			if(node.text.indexOf(".")!=-1){
				//如果双击的文件已经打开
				if($("#tt").tabs('getTab',node.text)!=null){
					$("#tt").tabs('select',node.text);
					return;
				}
				
				//获取文件在File文件的完整路径
				var filename =node.text;
				var parentnode;
				var curnode = node;
				while(parentnode=$("#directory").tree('getParent',curnode.target))
				{
					filename = parentnode.text+"/"+ filename;
					curnode = parentnode;
				}
				
				//获取文件扩展名
				var pastfix = filename.substr(filename.indexOf(".")+1, filename.length);

				//打开一个tab
				var title = node.text;
				
				alert(pastfix);
				switch(pastfix)
				{
				case "formular":
					parseFormula(filename, title);
					break;
				case "t":
					alert("任务图文件");
					break;
				case "p":
					alert("分区表文件");
					break;
				case "amt":
					parseAmt(filename, title);
					break;
				case "amp":
					parseAmp(filename, title);
					break;
				case "amm":
					parseAmm(filename, title);
					break;
				case "c":
					parseC(filename, title);
					break;
				case "xml":
					parseConf(filename, title);
					break;
				}
			}
		}
	});
	
	//解析公式文件
	function parseFormula(filename, title){
		alert(filename);
		var href = "showFormula?filename="+filename;
		var content = '<iframe  frameborder="0" src="'+href+'" style="width:99%;height:99%;"></iframe>';
		var tabId = title.replace(".","");
		$("#tt").tabs('add',{
			id:tabId,
			title:title,
			closable:true,
			content:content,
		});
	}
	
	
	//解析任务模型的文件
	function parseAmt(filename, title){
		var href = "showTaskModel?filename="+filename;
		var content = '<iframe  frameborder="0" src="'+href+'" style="width:99%;height:99%;"></iframe>';
		var tabId = title.replace(".","");
		$("#tt").tabs('add',{
			id:tabId,
			title:title,
			closable:true,
			content:content,
		});
	}
	
	//解析模块模型的文件
	function parseAmm(filename, title){
		var href = "showModuleModel?filename="+filename;
		var content = '<iframe  frameborder="0" src="'+href+'" style="width:99%;height:99%;"></iframe>';
		var tabId = title.replace(".","");
		$("#tt").tabs('add',{
			id:tabId,
			title:title,
			closable:true,
			content:content,
		});
	}
	
	//解析模块模型的文件
	function parseAmp(filename, title){
		var href = "showPartitionModel?filename="+filename;
		var content = '<iframe  frameborder="0" src="'+href+'" style="width:99%;height:99%;"></iframe>';
		var tabId = title.replace(".","");
		$("#tt").tabs('add',{
			id:tabId,
			title:title,
			closable:true,
			content:content,
		});
	}
	
	//解析C文件
	function parseC(filename, title){
		var href = "showCCode?filename="+filename;
		var content = '<iframe  frameborder="0" src="'+href+'" style="width:99%;height:99%;"></iframe>';
		var tabId = title.replace(".","");
		$("#tt").tabs('add',{
			id:tabId,
			title:title,
			closable:true,
			content:content,
		});
	}
	
	//解析conf文件
	function parseConf(filename, title){
		var href = "showConf?filename="+filename;
		var content = '<iframe  frameborder="0" src="'+href+'" style="width:99%;height:99%;"></iframe>';
		var tabId = title.replace(".","");
		$("#tt").tabs('add',{
			id:tabId,
			title:title,
			closable:true,
			content:content,
		});
	}
	
</script>
</html>
