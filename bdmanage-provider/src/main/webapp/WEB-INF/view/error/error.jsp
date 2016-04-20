<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	response.setHeader("Content-Type", "application/json; charset=UTF-8");
%>
{"has_error":true, "message":"${exception.getMessage()}"}