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
				ユーザー名
			</div>
			<div>
				<input type="text" name="userName" placeholder="半角英数のみ。先頭文字は英文字のみ" pattern="[a-zA-Z][a-zA-Z0-9]*" required title="先頭文字は英字で、半角英数字のみ使用できます。">
			</div>
			<div>
				パスワード
			</div>
			<div>
				<input type="text" name="passWord" placeholder="英数字4文字以上" pattern="[a-zA-Z0-9]{4,}" required title="半角英数字のみ使用できます。4文字以上入力してください。">
			</div>
			<div>
				表示名
			</div>
			<div>
				<input type="text" name="displayName" placeholder="１６文字まで" maxlength="16" required>
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