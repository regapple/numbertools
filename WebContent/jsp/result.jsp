<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.*,java.lang.*"%>
<%@page import="com.song.common.util.HttpPathUtil" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String result=(String)request.getAttribute("result");

	String path = request.getContextPath();
	String basePath = HttpPathUtil.getBasePath(request);
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>上传数据结果</title>
</head>
<body>
	<h2><%=result %><a href="<%=path%>/list">返回查询列表</a></h2>
</body>
</html>