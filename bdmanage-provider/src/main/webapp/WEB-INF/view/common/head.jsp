
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;


%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="renderer" content="webkit">
    <title>后台管理  ${ pageTitle }</title>
    <link rel="stylesheet" href="<%=basePath%>/public/ui/css/pintuer.css">
    <link rel="stylesheet" href="<%=basePath%>/public/ui/css/admin.css">
    <script src="<%=basePath%>/public/ui/js/jquery.js"></script>
    <script src="<%=basePath%>/public/ui/js/pintuer.js"></script>
    <script src="<%=basePath%>/public/ui/js/respond.js"></script>
    <script src="<%=basePath%>/public/ui/js/admin.js"></script>
    
    <script src="<%=basePath%>/public/common/util.js"></script>
    
<script type="text/javascript">

</script>
</head>