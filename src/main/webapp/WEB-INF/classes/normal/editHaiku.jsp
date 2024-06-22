<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="java.util.List"%>
<%@ page import="haiku.Haiku"%>
<%@ page import="database.DataBaseUtil" %>
<%
// リクエストパラメータから俳句IDを取得
int haikuID = Integer.parseInt(request.getAttribute("haikuID").toString());

// データベースから俳句を取得
Haiku haiku = DataBaseUtil.getHaikuById(haikuID);

// 俳句の内容と設定を取得
String haikuWork = haiku.getHaikuWork();
String visibility = haiku.getVisibility();
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="style.css">
<meta charset="UTF-8">
<title>編集画面</title>
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
	
		<div>
		    <div class="title">俳句編集</div>
		    
		    <div  class="post-form">
			    <form action="EditServlet" method="post">
			        <input type="hidden" name="haikuID" value="<%=haikuID%>">
			        <div>
			            <label for="haikuWork">俳句:</label><br>
			            <input type="text" id="haikuWork" name="haikuWork" value="<%=haikuWork%>" required>
			        </div>
			        <div style="margin-top:20px">
			            <label for="visibility">公開設定:</label><br>
			            <select id="visibility" name="visibility">
			                <option value="公開" <%=visibility.equals("公開") ? "selected" : ""%>>公開</option>
			                <option value="非公開" <%=visibility.equals("非公開") ? "selected" : ""%>>非公開</option>
			            </select>
			        </div>
			        <div style="margin-top:50px"></div>
			        <button type="submit">更新</button>
			    </form>
		
			    <!-- 俳句削除フォーム -->
				<form action="DeleteServlet" method="post">
				    <input type="hidden" name="haikuID" value="<%= haikuID %>">
				    <!-- 削除ボタン -->
				    <button type="submit">削除</button>
				</form>
				
				<!-- 戻るボタン -->
			    <button onclick="history.back()">戻る</button>

    		</div>

		</div>


	</div><!-- END rightarea -->

</div><!-- END page-container -->

</body>
</html>