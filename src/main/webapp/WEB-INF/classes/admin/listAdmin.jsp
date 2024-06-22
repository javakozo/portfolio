<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="haiku.Haiku"%>
<%@ page import="java.util.Comparator"%>
<%@ page import="database.DataBaseUtil" %>
<%
    String isfilter = (String) request.getAttribute("isfilter");
    if (isfilter == null) {
        isfilter = "false"; // 初期値は非表示として設定
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css">
<script>
	function initializeFilterForm() {
	    var form = document.getElementById("select-outer");
	    var button = document.getElementById("filter-button");
	
	    // JSPから取得したisfilterの値をJavaScript変数に埋め込む
	    var isfilter = '<%= isfilter %>';
	
	    // isfilterの値に基づいてフォームの表示を設定する
	    if (isfilter === "true") {
	        form.style.display = "block"; // フォームを表示
	        button.innerHTML = "絞り込みを閉じる"; // ボタンのテキストを変更
	    } else {
	        form.style.display = "none"; // フォームを非表示
	        button.innerHTML = "絞り込みを表示する"; // ボタンのテキストを変更
	    }
	}
		
	// ページのロード完了時にinitializeFilterForm()関数を呼び出す
	window.onload = function() {
	    initializeFilterForm();
	};

	function toggleFilterForm() {
        var form = document.getElementById("select-outer");
        var button = document.getElementById("filter-button");
	     
        if (form.style.display === "none" || form.style.display === "") {
            form.style.display = "block";
            button.innerHTML = "絞り込みを閉じる";
        } else {
            form.style.display = "none";
            button.innerHTML = "絞り込みを表示する";
        }
    }

    // 絞り込み条件をリセットしてフォームを送信する関数
    function resetAndSubmit() {
        document.getElementById("haikuID").value = "";
        document.getElementById("accountID").value = "";
        document.getElementById("userName").value = "";
        document.getElementById("year").value = "";
        document.getElementById("month").value = "";
        document.getElementById("day").value = "";
        document.getElementById("visibility").value = "";
        document.getElementById("theme").value = "";
        // filterForm → submit → servlet
        document.getElementById("filterForm").submit();
    }
</script>
<title>俳句リスト（管理者）</title>
</head>
<body>
<%
String displayName = DataBaseUtil.getColumnValue((int)session.getAttribute("sessionAccountID"),"accountdata", "displayName");
List<Integer> years = (List<Integer>) request.getAttribute("years");
List<Integer> months = (List<Integer>) request.getAttribute("months");
List<Integer> days = (List<Integer>) request.getAttribute("days");
List<String> themes = (List<String>) request.getAttribute("themes");
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
	
		<div class="title">投稿俳句一覧</div>
		
		<!-- 絞り込み機能（管理者用） -->
		
		<!-- 絞り込みボタン -->
		<button id="filter-button" class="filter-button" onclick="toggleFilterForm()">絞り込みを表示する</button>
		
		
		<div class="select-outer" id="select-outer">
			<form id="filterForm" action="ListHaikuServlet" method="post">

			<div class="select-area">
				<div class="area1">
					<div class="select-block">
						<label for="haikuID">俳句ID</label>
						<input type="text" id="haikuID" name="haikuID" value="">
					</div>
					<div class="select-block">
						<label for="accountID">アカウントID</label>
						<input type="text" id="accountID" name="accountID" value="">
					</div>
					<div class="select-block">
						<label for="userName">ユーザーネーム</label>
						<input type="text" id="userName" name="userName" value="">
					</div>
					<div class="select-block">
					    <label for="theme">お題</label>
					    <select id="theme" name="theme">
					        <option value="">選択してください</option>
					        <%
					        for (String theme : themes) {
					        %>
					            <option value="<%= theme %>"><%= theme %></option>
					        <%
					        }
					        %>
					    </select>
					</div>   					
				</div>
				<div class="area1">
					<div class="select-block">
						<label for="visibility">公開設定</label>
					    <select id="visibility" name="visibility">
					        <option value="">選択してください</option>
					        <option value="公開">公開</option>
					        <option value="非公開">非公開</option>
					    </select>
					</div>
					<div class="select-block">
						<label for="role">権限</label>
						<select id="role" name="role">
						    <option value="">選択してください</option>
					        <option value="normal">normal</option>
					        <option value="prime">prime</option>
				        </select>
					</div>
                        <div class="select-block">
                            <label for="year">年</label>
                            <select id="year" name="year">
                                <option value="">選択してください</option>
                                <%
                                for (Integer year : years) {
                                %>
                                    <option value="<%= year %>"><%= year %></option>
                                <%
                                }
                                %>
                            </select>
                        </div>
                        <div class="select-block">
                            <label for="month">月</label>
                            <select id="month" name="month">
                                <option value="">選択してください</option>
                                <%
                                for (Integer month : months) {
                                %>
                                    <option value="<%= month %>"><%= month %></option>
                                <%
                                }
                                %>
                            </select>
                        </div>
                        <div class="select-block">
                            <label for="day">日</label>
                            <select id="day" name="day">
                                <option value="">選択してください</option>
                                <%
                                for (Integer day : days) {
                                %>
                                    <option value="<%= day %>"><%= day %></option>
                                <%
                                }
                                %>
                            </select>
                        </div>                        

				</div>
				<div class="area2">
					<button type="submit">絞り込み</button>
					<!-- リセットボタン -->
					<button type="button" onclick="resetAndSubmit()">リセット</button>
				</div>
				</form>
			</div>
		</div>
		
		
		<!-- 俳句リスト -->
		<div class="adminlist">	
			<table>
				<thead style="font-size:small;">
					<th>俳句ID</th>
					<th>俳句</th>
					<th>アカウントID</th>
					<th>ユーザーネーム</th>
					<th>表示名</th>
					<th>公開設定</th>
					<th>お題</th>
					<th>権限</th>
					<th>投稿日</th>
					<th>操作</th>
				</thead>
			<!--=================================================================================-->
			<tbody>
			<%
			List<Haiku> haikus = (List<Haiku>) request.getAttribute("haikus");
			if (haikus == null || haikus.isEmpty()) {
			%>
				<p>まだ投稿がありません</p>
			<%
			} else {
				haikus.sort(Comparator.comparing(haiku -> haiku.getHaikuID()));
				for (Haiku haiku : haikus) {
			%>
				<tr>
					<td class="c1"><%=haiku.getHaikuID()%></td>
					<td class="c2"><%=haiku.getHaikuWork()%></td>
					<td class="c5"><%=haiku.getAccountID()%></td>
					<td class="c3"><%=haiku.getUserName()%></td>
					<td class="c3"><%=haiku.getDisplayName()%></td>
					<td class="c4"><%=haiku.getVisibility()%></td>
					<td class="c4"><%=haiku.getTheme()%></td>
					<td class="c4"><%=haiku.getRole()%></td>
					<td class="c5"><%=haiku.getPostDate()%></td>
					<td>
						<!-- 削除ボタン -->
						<form action="DeleteServlet" method="post">
							<!-- 削除対象HaikuIDを送信 -->
							<input type="hidden" name="haikuID" value="<%=haiku.getHaikuID()%>">
							<button type="submit">削除</button>
						</form>
					</td>
				</tr>
			</tbody>
			<!--=================================================================================-->
			<%
			}// for
			}// if
			%>
			</table>
		</div><!-- END 俳句リスト -->
	</div><!-- END rightarea -->
</div><!-- END page-container -->


</body>
</html>