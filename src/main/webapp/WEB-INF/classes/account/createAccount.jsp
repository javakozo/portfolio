<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Yuji+Syuku&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Mochiy+Pop+One&display=swap" rel="stylesheet">
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css">
<title>新規登録</title>
</head>
<body>
<div class="top-container">
	<div style="height:100px"></div>
	<!-- コンテンツブロック -->
	<div class="createcontainer">
			<!-- タイトル -->
		<div class="title">
			新規登録
		</div>
		<div style="height:30px"></div>
		<!-- 新規登録 -->
		<div class="createblock">
			<form action="CreateAccountServlet" method="post">
			<div>
				ユーザーネーム
			</div>
			<div>
				<input type="text" name="userName" placeholder="半角英数" required>
			</div>
			<div>
				パスワード
			</div>
			<div>
				<input type="text" name="passWord" placeholder="半角英数" required>
			</div>
			<div>
				表示名
			</div>
			<div>
				<input type="text" name="displayName" placeholder="１６文字まで" required>
			</div>
			<div class="errorbox">
	           <!-- エラーメッセージの表示 -->
	           <% if (request.getAttribute("errorMessage") != null) { %>
	              <p><%=request.getAttribute("errorMessage")%></p>
	           <% } %>
			</div>
			<button type="submit">登録</button>
			</form>
			<div>
				<form action="index.jsp">
					<button type="submit">戻る</button>
				</form>
			</div>	
		</div>
	</div>
</div>
</body>
</html>