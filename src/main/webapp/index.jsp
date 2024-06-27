<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css">
<title>吟花トップページ</title>
</head>
<body>

<%
// ユーザー名がnullの場合、デフォルト値を設定
String createdUserName = (request.getParameter("createdUserName") != null) ? request.getParameter("createdUserName") : "";
%>

<div class="top-container">

    <div class="top-title">
		<img src="image/title.png">
	</div>
    
    <div class="top-box">
        <!-- ログイン -->
        <div class="box">
            <form action="LoginServlet" method="post">
                <div class="t1">
                    登録済みの方はコチラ
                </div>
                <div class="t2">
                    ユーザー名
                </div>
                <div>
                    <input type="text" name="userName" value="<%= createdUserName %>" placeholder="半角英数" required>
                </div>
                <div class="t2">
                    パスワード
                </div>
                <div>
                    <input type="password" name="passWord" placeholder="半角英数" required>
                </div>
                <div class="errorbox">
                    <% if (request.getAttribute("errorMessage") != null) { %>
                        <p><%=request.getAttribute("errorMessage")%></p>
                    <% } %>
                </div>
                <div>
                    <button type="submit">ログイン</button>
                </div>
            </form>
        </div>
    
        <!-- 新規作成 -->
        <div class="box">
            <form action="CreateAccountServlet" method="get">
                <div>
                    ご登録がお済でない方
                </div>
                <div>
                    <button type="submit">新規作成</button>
                </div>
            </form>
        </div>
        
        
    </div>


</div>

</body>
</html>
