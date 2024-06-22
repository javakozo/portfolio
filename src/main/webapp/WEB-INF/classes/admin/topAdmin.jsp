<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="database.DataBaseUtil" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css">
<title>管理者ページ</title>
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
				<a href="ListHaikuServlet">俳句管理</a>
			</div>
			<div class="link">
				<a href="ListAccountServlet">アカウント管理</a>
			</div>
			<div class="link">
				<a href="ListThemeServlet">テーマ管理</a>
			</div>
		</div>
	</div><!-- END leftarea -->
	
	
	<!-- 右：コンテンツ -->
	<div class="rightarea">
		メインコンテンツ※お知らせ等
	</div><!-- END rightarea -->
	
</div><!-- END page-container -->

</body>
</html>