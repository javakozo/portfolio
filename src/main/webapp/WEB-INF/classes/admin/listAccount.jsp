<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Comparator"%>
<%@ page import="database.DataBaseUtil" %>
<%@ page import="haiku.Haiku"%>
<%@ page import="account.Account"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css">
<title>アカウントリスト(管理者)</title>
</head>
<body>
<%
String displayName = DataBaseUtil.getColumnValue((int)session.getAttribute("sessionAccountID"),"accountdata", "displayName");
%>
    <%
    // セッションから管理者フラグを取得
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    
 // 管理者でない場合はトップページなどへリダイレクト
    if (isAdmin == null || !isAdmin) {
        response.sendRedirect(request.getContextPath() + "/LoginServlet");
        return;
    }
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
	
	
	<!-- rightarea -->

	<div class="rightarea">

		<div class="title">アカウントリスト（管理）</div>
		
		
		<div class="accountlist">
		<table>
	        <thead>
	            <th>アカウントID</th>
	            <th>ユーザーネーム</th>
	            <th>表示名</th>
	            <th>権限</th>
	        </thead>
			<!-- ============================================================================== -->
			<tbody>
			<%
			List<Account> accountList = (List<Account>)request.getAttribute("accountIDs"); 
			if (accountList != null && !accountList.isEmpty()) { 
			for (Account account : accountList) { 
			%>
			<tr>
				<td class="c1"><%= account.getAccountID() %></td>
				<td class="c2"><%= account.getUserName() %></td>
				<td class="c3"><%= account.getDisplayName() %></td>
				<td class="c4"><%= account.getRole() %></td>
			</tr>
			<% 
			}
			} else {
			%>
			<tr>
				<td colspan="4">アカウントがありません。</td>
			</tr>
			<% } %>
			</tbody>
			<!-- ============================================================================== -->
    </table>
		
		</div>
	</div><!-- END rightarea -->
</div><!-- END page-container -->

</body>
</html>