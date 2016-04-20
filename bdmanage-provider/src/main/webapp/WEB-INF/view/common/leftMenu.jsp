<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="admin-navbar">
      <span class="float-right">
      	<a class="button button-little bg-main" href="#">前台首页</a>
          <a class="button button-little bg-yellow" href="<%=basePath%>/account/dologout">注销登录</a>
      </span>
    <ul class="nav nav-inline admin-nav" id="navliuiids">

        <c:forEach items="${menulist}" var="menuparent"  varStatus="status">

            <li class="${nvset.mainMenu[status.index]}">
                <c:if test="${!empty  menuparent.mendetailist[0].page_url}">
                    <a href="<%=basePath%>/${menuparent.mendetailist[0].page_url}?pid=${status.index}&cid=0" class="icon-cog">${menuparent.menu_name_cn}</a>
                </c:if>
                <ul>
                    <c:choose>
                        <c:when test="${empty  menuparent.mendetailist[0].page_url}">
                            <li class="${nvset.subMenu[0] }"><a href="<%=basePath %>/${menuparent.mendetailist[0].page_url}?pid=${status.index}&cid=0">${menuparent.mendetailist[0].menu_name_cn}</a></li>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${menuparent.mendetailist}" var="menuchildren"  varStatus="s">
                                <li class="${nvset.subMenu[s.index] }"><a href="<%=basePath %>/${menuchildren.page_url}?pid=${status.index}&cid=${s.index}">${menuchildren.menu_name_cn}</a></li>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </ul>

            </li>

        </c:forEach>

        <script>
            $(function(){

                $("input[name='parentid'][value='0']").next().html("全选：");

                $("input[name='parentid'][value='0']").siblings('input').each(function(i,n){
                    $(this).on('click',{'pid':$(this).val()},function(event){
                        if(this.checked){
                            $("input[name='parentid'][value="+event.data.pid+"]").siblings('input').each(function(i,n){
                                $(this).prop("checked",true);
                            });
                        }else{
                            $("input[name='parentid'][value="+event.data.pid+"]").siblings('input').each(function(i,n){
                                $(this).prop("checked",false);
                            });
                        }
                    })
                });


                $("input[name='childrenid']").each(function(i,n){
                    $(this).on('click',function(){
                        if(this.checked){
                            var len = $(this).siblings("input[name='childrenid']").length;
                            var checkLen =0;
                            $(this).siblings("input[name='childrenid']").each(function(ii,nn){
                                if(this.checked){
                                    checkLen++;
                                }
                            });
                            if(len==checkLen){
                                $("input[name='parentid'][value='0']").siblings('input[value='+$(this).siblings("input[name='parentid']").val()+']')
                                        .prop("checked",true);
                            }
                        } else{
                            $("input[name='parentid'][value='0']").siblings('input[value='+$(this).siblings("input[name='parentid']").val()+']')
                                    .prop("checked",false);
                        }
                    });
                });

            });
        </script>

    </ul>
</div>
