<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="database.DataBaseUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="image.ImagePost" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Yuji+Syuku&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Mochiy+Pop+One&display=swap" rel="stylesheet">
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css">
<title>トップページ</title>
<!-- 一般 -->
</head>
<body>
<%
String displayName = DataBaseUtil.getColumnValue((int)session.getAttribute("sessionAccountID"),"accountdata", "displayName");
List<ImagePost> posts = (List<ImagePost>) request.getAttribute("posts");
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
	
	
	<!-- 右：コンテンツ --><!-- 画像掲示板 -->
	<div class="rightarea">
		
		<!-- タイトル -->
  		<div class="title">画像投稿</div>
		
		<!-- 投稿フォーム -->
  		<div class="imageform">
  			<div class="c1">
	  			<form action="PostImageServlet" method="post" enctype="multipart/form-data">
			    	<input type="file" name="file">
			    	<input type="text" name="imageText" placeholder="画像の説明を入力してください">
			    	<input type="hidden" name="postTime" value="<%= new java.util.Date() %>">
			    	<input type="hidden" name="postAccountID" value="<%= ((HttpSession) request.getSession(false)).getAttribute("sessionAccountID") %>">
				    <button type="submit">投稿</button>			    
	  			</form>
 			</div>
 		    <%-- エラーメッセージがあれば表示 --%>
		    <div style="margin-left:20px">
			    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
			    <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
		    	    <p style="color: red;"><%= errorMessage %></p>
		    	<% } %>
			</div>  			
  		</div>
  		
		
		<!-- 掲示板 -->
		<div class="imagelist">
		    <% for(ImagePost post : posts) { %>
		    	<div class="imagebox">
		            <div>
		            	<img src="<%= "upimages/" + post.getImageUrl() %>" alt="投稿画像" class="postimage">
		           	</div>
		            <div class="name">
		            	<%= DataBaseUtil.getColumnValue((int) post.getAccountID(), "accountdata", "displayName") %>
		           	</div>
		            <div class="des">
		            	<%= post.getImageText() != null ? post.getImageText() : "" %>
		           	</div>
					<div class="time">
						投稿日時: <%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(post.getTime()) %>
					</div>
				</div>
		    <% } %>
		</div>
  		
	</div><!-- END rightarea -->
	
</div><!-- END page-container -->



</body>
</html>