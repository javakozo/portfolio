<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="database.DataBaseUtil" %>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Yuji+Syuku&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Mochiy+Pop+One&display=swap" rel="stylesheet">
<link rel="stylesheet" href="style.css">
<title>投稿画面</title>
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
	
	
	<!-- rightarea -->
	<div class="rightarea">
	
		<div class="title">
			俳句投稿
		</div>
	
		<div class="post-form">	
			<!-- 俳句作成 -->
			<form action="ConfirmHaikuServlet" method="post">
				<!-- 俳句 -->
				<div class="c1">
					俳句
				</div>
				<div>
					<input type="text" name="haikuWork" required>
				</div>
				<!-- 題材 -->
				<div class="c1">
					お題
				</div>
				<select name="theme">
				<%
					List<String> themes = DataBaseUtil.getThemes();
					for (String theme : themes) {
				%>
				<option value="<%=theme%>" ><%=theme%></option>
				<%
				}
				%>
				</select>
				<!-- 公開設定 -->
				<div class="c1">
					公開設定
				</div>
				<select name="visibility">
						<option value="公開">公開</option>
						<option value="非公開">非公開</option>
				</select>
				<div class="c1">
					<button type="submit">確認</button>
				</div>
			</form>
		</div>
	</div><!-- END rightarea -->
	
</div><!-- END page-container -->

</body>
</html>