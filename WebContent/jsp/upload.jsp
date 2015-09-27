<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.*,java.lang.*"%>
<%@page import="com.song.common.util.HttpPathUtil" language="java"%>
<%
	String path = request.getContextPath();
	String basePath = HttpPathUtil.getBasePath(request);
%>
<!doctype html>
<html lang="zh-cn">
<base href="<%=basePath%>">
<head>
    <title>上传数据</title>
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link href="css/baseCss.css" rel="stylesheet">
    <link href="css/orderDetail.css" rel="stylesheet">
   
</head>
<body >
<script type="text/javascript" src="js/jquery.js"></script>
<div class="tabbable"> 
	<ul class="nav nav-tabs">
		<li class="active">	<a href="#tab1" data-toggle="tab">数据导入</a></li>
	</ul> 
	<div class="tab-content">
		<div class="tab-pane active" id="tab1">
		<form id="form1" class="form-horizontal" name="form1" method="post" enctype="multipart/form-data" action="<%=path %>/uploadFile">
			<span  id="mainform">
		<div class="form-group">
			<label class="col-sm-2 control-label" style='width:200px;text-align: left'>上传文件者姓名：</label>
			<div class="pull-left">
				<input type="text" name="owner" style="width:300px">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" style='width:200px;text-align: left'>请选择导入文件:</label>
			<div class="pull-left">
				<input type="file" name="inputFile" style="width:300px">
			</div>
		</div>
		<button type="button" style="width:120px" class="btn btn-primary">上传数据</button>
		
		</span>
		</form>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
	$(function(){
		$('.btn').on('click',function(){
			var owner=$('input[name=owner]').val();
			var file=$('input[type=file]').val();
			
			if(owner==null||owner==""){
				alert("请输入上传人姓名");
			}else if(file==null||file==""){
				
				alert("请选择上传的文件");
			}else {
				var suffix=file.substring(file.lastIndexOf('.')+1);
				if(suffix=='csv'){
					$('#form1').submit();
				}else{
					alert('请选择csv文件');
				}
			}
		});
	});
	
</script>
</html>