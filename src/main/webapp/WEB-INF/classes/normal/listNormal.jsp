<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="haiku.Haiku"%>
<%@ page import="database.DataBaseUtil" %>
<%@ page import="comment.Comment" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Mochiy+Pop+One&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Yuji+Syuku&display=swap" rel="stylesheet">
<title>俳句リスト</title>
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
		
		<div class="title"><%= displayName %><span>さんの</span>俳句一覧</span></div>
		
		<!-- 絞り込み機能（一般） -->
		<div></div>
		
		<!-- 俳句リスト -->
		<!--=================================================================================-->
		
		<%
		List<Haiku> haikus = (List<Haiku>) request.getAttribute("haikus");
		if (haikus == null || haikus.isEmpty()) {
		%>	
			<p>まだ投稿がありません</p>
		<%
		} else {
			for (Haiku haiku : haikus) {
		%>
		<div class="haiku-block">
			<div class="c2"><%=haiku.getHaikuWork()%></div>
			<div class="c3">
				<div>お題：</div>
				<div class="t1"><%=haiku.getTheme()%></div>
				<div>公開設定：</div>
				<div class="t1"><%=haiku.getVisibility()%></div>
				<!-- 編集 -->
				<div style="margin-right:10px">
					<form action="EditServlet" method="get">
						<input type="hidden" name="haikuID" value="<%=haiku.getHaikuID()%>">
						<button type="submit">編集</button>
					</form>
				</div>
				
				<!-- コメント投稿、表示ボタン -->
				<div>
	 			    <button type="button" onclick="showCommentForm('<%= haiku.getHaikuID() %>')">コメント（<%= DataBaseUtil.getCommentCount(haiku.getHaikuID()) %>）</button>
				</div>
			</div>
			
			<!-- コメントフォームと既存のコメント（コメントボタンを押したら表示 -->
			<div id="commentsAndForm_<%= haiku.getHaikuID() %>" style="display: none;">
			    <!-- コメントフォーム -->
			    <div id="commentForm_<%= haiku.getHaikuID() %>">
			        <form action="CommentServlet" method="post">
						<textarea name="comment_text" class="comment-textbox" rows="4" cols="50"></textarea>
			            <input type="hidden" name="haikuID" value="<%= haiku.getHaikuID() %>">
			            <input type="hidden" name="accountID" value="<%= session.getAttribute("sessionAccountID") %>">
						<input type="hidden" name="date" value="<%= new java.sql.Timestamp(System.currentTimeMillis()) %>">
			            <button type="submit">コメント投稿</button>
			        </form>
			        <%-- コメントエラーメッセージを表示 --%>
  					<div style="color: red;">
                        <% Object errorMessage = request.getAttribute("errorMessage");
                           out.print(errorMessage != null ? errorMessage : ""); %>
                    </div>
			    </div><!-- END コメントフォーム -->
			
			    <!-- 既存のコメント -->
			    <div id="existingComments_<%= haiku.getHaikuID() %>">
					<%
					    List<Comment> comments = (List<Comment>) request.getAttribute("comments_" + haiku.getHaikuID());
					    if (comments != null && !comments.isEmpty()) {
					        for (Comment comment : comments) {
					            if (comment.getHaikuID() == haiku.getHaikuID()) {
					%>
					<div class="come2">
					    <!-- 既存のコメントを表示 -->
					    <div><hr></div>
						<div class="left-column">
							<%= DataBaseUtil.getColumnValue(comment.getAccountID(), "accountdata", "displayName") %>
						</div>
						<div class="right-column">
							<pre><%= comment.getCommentText() %></pre>
						</div>
						<div class="last-column">
							 <%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(comment.getDate()) %>
						</div>
					</div>
					<%
					            }
					        }
					    } else {
					%>
					<p>コメントはまだありません。</p>
					<%
					    }
					%>
			    </div><!-- END 既存のコメント -->
		
			</div><!-- END コメントフォームと既存のコメント -->
		</div>
		<!--=================================================================================-->
		<%
		}
		}
		%>
		
		
	</div><!-- END rightarea -->
</div><!-- END page-container -->
<script>
    function showCommentForm(haikuID) {
        var commentsAndForm = document.getElementById('commentsAndForm_' + haikuID);
        if (commentsAndForm.style.display === 'none') {
            commentsAndForm.style.display = 'block';
        } else {
            commentsAndForm.style.display = 'none';
        }
    }
</script>
</body>
</html>