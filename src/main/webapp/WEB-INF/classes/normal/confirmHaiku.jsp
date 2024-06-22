<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="database.DataBaseUtil" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Yuji+Syuku&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Mochiy+Pop+One&display=swap" rel="stylesheet">
<link rel="stylesheet" href="style.css">
<title>投稿確認</title>
<!-- 一般 -->
</head>
<body>
<%
String displayName = DataBaseUtil.getColumnValue((int)session.getAttribute("sessionAccountID"),"accountdata", "displayName");
%>
	
<div class="page-container">

	<!-- leftarea -->
	<div class="leftarea">
		<div>
			<div class="image">
				<img src="image/title.png" width="150" height="150">
			</div>
			<div class="user">
				<%= displayName %>さん
			</div>
			<div class="logout">
				<a href="LogoutServlet">ログアウト</a>
			</div>
		</div>
		<div>
			<div class="menu-title">
				メニュー
			</div>
			<div class="link">
				<a href="PostHaikuServlet">俳句投稿</a>
			</div>
			<div class="link">
				<a href="ListHaikuServlet">俳句管理</a>
			</div>
			<div class="link">
				<a href="ListHaikuServlet?open=value">俳句鑑賞</a>
			</div>
			<div class="link">
				<a href="LoginServlet">トップに戻る</a>
			</div>
		</div>
	</div><!-- END leftarea -->
	
	
	<!-- 右：コンテンツ -->
	<div class="rightarea">
	
	<div>
		
		<div class="title">俳句確認</div>
		
		<div class="conf-haiku">
			<div class="c2"><%=request.getAttribute("haikuWork")%></div>
			<div class="data">
				<div class="c3">投稿者：</div>
				<div class="c4"><%=request.getAttribute("displayName")%></div>
				<div class="c3">お題：</div>
				<div class="c4"><%=request.getParameter("theme")%></div>
				<div class="c3">投稿日時：</div>
				<div class="c4"><%=request.getAttribute("formattedDate")%></div>
			</div>
		</div>
		
		<div class="data2">
			<div class="c5">
				公開設定 : 
			</div>
			<div class="c5">
				<%=request.getAttribute("visibility")%>
			</div>
		</div>
		<div class="data2">
			<div class="modify">
				<button onclick="history.back()">修正する</button>
			</div>
			<div class="post">
				<form action="PostHaikuServlet" method="post">
					<input type="hidden" name="haikuWork" value="<%=request.getParameter("haikuWork")%>">
					<input type="hidden" name="visibility" value="<%=request.getParameter("visibility")%>">
					<button type="submit">投稿</button>
				</form>
			</div>
		</div>
	</div>
	</div><!-- END rightarea -->
	
</div><!-- END page-container -->

</body>
</html>