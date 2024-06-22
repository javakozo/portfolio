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
<link rel="stylesheet" href="style.css">
<title>登録成功</title>
</head>
<body>
<div class="top-container">
	<div style="height:30px"></div>
	<!-- コンテンツブロック -->
	<div class="createcontainer">
			<!-- タイトル -->
		<div class="title">
		登録成功
		</div>

		<div class="createblock">
			<form action="index.jsp">
			<input type="hidden" name="userName" value="${sessionScope.createdUserName}">
			<button type="submit">ログインページへ</button>
			</form>
		</div>
		
	</div>
</div>
</body>
</html>