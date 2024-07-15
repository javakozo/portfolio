<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="database.DataBaseUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="theme.Theme" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css">
<title>テーマ</title>
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
	
	
	<!-- 右：コンテンツ -->
	<div class="rightarea">

			<div class="themelist">
				<table>
					<thead>
						<th class="c1">ThemeID</th>
						<th>お題</th>
					</thead>
<!-- ============================================================================== -->
					<tbody>
			<%
            List<Theme> themeList = (List<Theme>)request.getAttribute("themes"); 
            if (themeList != null && !themeList.isEmpty()) { 
                for (Theme theme : themeList) { 
            %>
					<tr>
						<td class="c1"><%= theme.getThemeID() %></td>
						<td class="c2"><%= theme.getThemeName() %></td>
					</tr>
					<% 
                }
            } else {
            %>
					<tr>
						<td colspan="2">テーマがありません。</td>
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