<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
    <title>后台管理  登陆</title>
    <link rel="stylesheet" href="<%=basePath%>/public/ui/css/pintuer.css">
    <link rel="stylesheet" href="<%=basePath%>/public/ui/css/admin.css">
    <script src="<%=basePath%>/public/ui/js/jquery.js"></script>
    <script src="<%=basePath%>/public/ui/js/pintuer.js"></script>
    <script src="<%=basePath%>/public/ui/js/respond.js"></script>
    <script src="<%=basePath%>/public/ui/js/admin.js"></script>
    
    <script src="<%=basePath%>/public/common/util.js"></script>
</head>

<body>
<div class="container">
    <div class="line">
        <div class="xs6 xm4 xs3-move xm4-move">
            <br /><br />
            <div class="media media-y">
                <img src="<%=basePath%>/public/ui/images/logo.png" class="radius" alt="后台管理系统" />
            </div>
            <br /><br />
            <c:if test="${ message != null}">
        	<div class="alert alert-yellow"><span class="close"></span>${ message }</div>
        </c:if>
            <form action="<%=basePath%>/account/dologin" method="post">
            <input type="hidden" name="action" value="post" />
            <div class="panel">
                <div class="panel-head"><strong>后台管理 登陆</strong></div>
                <div class="panel-body" style="padding:30px;">
                    <div class="form-group">
                        <div class="field field-icon-right">
                            <input type="text" class="input" name="username" placeholder="登录账号" data-validate="required:请填写账号,length#>=1:账号长度不能短于1个字符,length#<20:账号长度不能超过20字符" />
                            <span class="icon icon-user"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="field field-icon-right">
                            <input type="password" class="input" name="password" placeholder="登录密码" data-validate="required:请填写密码,length#>=6:密码长度不能短于6个字符,length#<=12:密码长度不能超过20字符" />
                            <span class="icon icon-key"></span>
                        </div>
                    </div>
                    <!-- 
                    <div class="form-group">
                        <div class="field">
                            <input type="text" class="input" name="passcode" placeholder="填写右侧的验证码" data-validate="required:请填写右侧的验证码" />
                            <img src="images/passcode.jpg" width="80" height="32" class="passcode" />
                        </div>
                    </div>
                     -->
                </div>
                <div class="panel-foot text-center"><button class="button button-block bg-main text-big">立即登录后台</button></div>
            </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>