<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<#assign actionExtension = "html"> 
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>${table.remarks}列表</title>
	</head>
	<body>
		<!-- 不要滚动条 -->
		<div class="pageHeader" style="overflow-x:hidden">
		<div class="pageHeader">
			<form id="searchForm" onsubmit="return navTabSearch(this);" action="${table.sqlName}/list.html" method="post" rel="pagerForm">
			<div class="searchBar">
				<table class="searchContent">
					<tr>
						<#list table.columns as column>
						<td>
							<#if column.remarks != ''>${column.remarks}<#else>${column.columnNameLower}</#if>: <input type="text" name="${column.columnNameLower}" class="<#if column.isDateTimeColumn>date<#else>textInput</#if><#if column.nullable> required</#if>" <#if column.isDateTimeColumn>readonly="readonly"</#if> value="${r"$"}{query.${column.columnNameLower}}"/>
						</td>
						</#list>
					</tr>
				</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
					<!--<li><a class="button" href="demo_page6.html" target="dialog" mask="true" title="查询框"><span>高级检索</span></a></li>-->
				</ul>
			</div>
			</div>
			</form>
		</div>
		<div class="pageContent">
			<div class="panelBar">
				<ul class="toolBar">
					<!-- height 26px  -->
					<li><a class="add" href="${jspFileBasePath}/add.jsp" target="dialog"><span>添加</span></a></li>
					<#list table.columns as column>
					<#if column.pk>
					<li><a class="edit" href="${table.sqlName}/info.html?${column.columnNameLower}={${column.columnNameLower}}" target="dialog"><span>编辑</span></a></li>
					<!--<li><a class="delete" href="${table.sqlName}/delete.html?${column.columnNameLower}={${column.columnNameLower}}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>-->
					<li><a title="确实要删除这些记录吗?" target="selectedTodo" rel="items" href="${table.sqlName}/delete.html" class="delete"><span>批量删除</span></a></li>
				    </#if>
					</#list>
					<!--
					<li class="line">line</li>
					<li><a class="icon" href="/${table.sqlName}/expExcel.html" target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出EXCEL</span></a></li>
					-->
				</ul>
			</div>
			<table class="table" width="100%" layoutH="138">
				<thead>
					<tr>
						<!-- 不给宽带自适应 -->
						<th align="center"><input type="checkbox" class="checkboxCtrl" group="items" /></th>
						<#list table.columns as column>
						<th align="center"><#if column.remarks != ''>${column.remarks}<#else>${column.columnNameLower}</#if></th>
						</#list>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${r"$"}{page.result}">
						<#list table.columns as column>
						<#if column.pk>
						<tr target="${column.columnNameLower}" rel="${r"$"}{item.${column.columnNameLower}}">
				        <td><label><input type="checkbox" name="items" rel="${r"$"}{item.${column.columnNameLower}}" value="${r"$"}{item.${column.columnNameLower}}" /></label></td>
				        </#if>
						<td>${r"$"}{item.${column.columnNameLower}}</td>
						</#list>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<!-- 分页 -->
			<form id="pagerForm" method="post" action="${table.sqlName}/list.html" rel="searchForm">
				<input type="hidden" name="pageNum" value="1" /> <!--【必须】value=1可以写死-->
				<input type="hidden" name="numPerPage" value="${r"$"}{page.numPerPage}" /> <!--【可选】每页显示多少条-->
			</form>
			<%@ include file="../../../js/page.js" %>
		</div>
	</body>
</html>