package haiku;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import comment.Comment;
import database.DataBaseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 俳句の管理に関するクラス
 * 閲覧、削除、編集
 */
@WebServlet("/ListHaikuServlet")
public class ListHaikuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//各メニューから俳句リストが呼ばれた時
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//絞り込みの選択用
        List<Integer> years = DataBaseUtil.getHaikuYears();
        List<Integer> months = DataBaseUtil.getHaikuMonths();
        List<Integer> days = DataBaseUtil.getHaikuDays();
        List<String> themes = DataBaseUtil.getThemes();
        
        // リクエスト属性に設定
        request.setAttribute("years", years);
        request.setAttribute("months", months);
        request.setAttribute("days", days);
        request.setAttribute("themes", themes);
        request.setAttribute("isfilter", "false");
		// "open" パラメーターが存在するかどうかを確認
		boolean isOpen = request.getParameter("open") != null;

		if (isOpen) {
			List<Haiku> ohaikus = getOpenHaikus();
			request.setAttribute("haikus", ohaikus);
			for (Haiku haiku : ohaikus) {
                List<Comment> comments;
				try {
					comments = DataBaseUtil.getCommentsByHaikuID(haiku.getHaikuID());
					request.setAttribute("comments_" + haiku.getHaikuID(), comments);
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
                
            }
 
			request.getRequestDispatcher("/WEB-INF/classes/normal/listOpen.jsp").forward(request, response);

		} else {

			//セッションからアカウントIDを取得
			int sessionAccountID = (int)request.getSession().getAttribute("sessionAccountID");

			//取得したアカウントIDからroleを取得
			String role = DataBaseUtil.getColumnValue(sessionAccountID, "accountdata", "role");
			
			//コメントリストを取得
			List<Haiku> haikus = getAllHaikus(sessionAccountID);
			for (Haiku haiku : haikus) {
                List<Comment> comments;
				try {
					comments = DataBaseUtil.getCommentsByHaikuID(haiku.getHaikuID());
					request.setAttribute("comments_" + haiku.getHaikuID(), comments);
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
                
            }

			// ロールに基づいて適切なページにフォワード
			if ("admin".equals(role)) {
				request.setAttribute("haikus", getAllHaikus());
				request.getRequestDispatcher("/WEB-INF/classes/admin/listAdmin.jsp").forward(request, response);

			} else {

				request.setAttribute("haikus", getAllHaikus(sessionAccountID));
				request.getRequestDispatcher("/WEB-INF/classes/normal/listNormal.jsp").forward(request, response);

			}
		}

	}

	/**
	 * 絞り込みを受けて返す
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

        List<Integer> years = DataBaseUtil.getHaikuYears();
        List<Integer> months = DataBaseUtil.getHaikuMonths();
        List<Integer> days = DataBaseUtil.getHaikuDays();
        List<String> themes = DataBaseUtil.getThemes();
        
     // サーブレットの doPost メソッド内での処理例

        request.setAttribute("years", years);
        request.setAttribute("months", months);
        request.setAttribute("days", days);
        request.setAttribute("themes", themes);
        
		//アカウントID,roleをセッションから取得
		int sessionAccountID = (int) request.getSession().getAttribute("sessionAccountID");
		String sessionRole = DataBaseUtil.getColumnValue(sessionAccountID, "accountdata", "role");

		// 絞り込み条件を取得
		String haikuID = request.getParameter("haikuID");
		String accountID = request.getParameter("accountID");
		String themeID = request.getParameter("themeID");
		String visibility = request.getParameter("visibility");
		String userName = request.getParameter("userName");
		String displayName = request.getParameter("displayName");
		String role = request.getParameter("role");
		String theme = request.getParameter("theme");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		String day = request.getParameter("day");

        request.setAttribute("isfilter", "true");
		
		//管理者はaccountID,userIDも取得
		request.setAttribute("haikus", getFilteredHaikus(
				haikuID, accountID, themeID, visibility, userName, displayName, role, theme, year, month, day));

		// "open" パラメーターが存在するかどうかを確認
		boolean isOpen = request.getParameter("open") != null;

		if (isOpen) {
			request.getRequestDispatcher("/WEB-INF/classes/normal/listOpen.jsp").forward(request, response);

		} else {
			// ロールに基づいて適切なページにフォワード
			if ("admin".equals(sessionRole)) {
				request.getRequestDispatcher("/WEB-INF/classes/admin/listAdmin.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/WEB-INF/classes/normal/listNormal.jsp").forward(request, response);
			}
		}

	}

	//メソッド
	//=============================================================================================//
	
	/**
	 * 絞り込みのメソッド
	 * 一旦全部絞り込み項目入れて、必要に応じて各ページで出力を決める
	 * @param haikuID 俳句ID
	 * @param accountID アカウントID
	 * @param themeID テーマID
	 * @param visibility 可視性
	 * @param userID ユーザーID
	 * @param displayName 表示名
	 * @param role ロール
	 * @param theme テーマ
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return 絞り込まれた俳句のリスト
	 */
	private List<Haiku> getFilteredHaikus(
	        String haikuID, String accountID, String themeID, String visibility, String userName, String displayName,
	        String role, String theme, String year, String month, String day) {
	    List<Haiku> haikus = new ArrayList<>();
	    Connection conn = null;
	    try {
	        conn = DataBaseUtil.getConnection();

	        StringBuilder sqlBuilder = new StringBuilder("""
	                SELECT haikuID, h.accountID, h.themeID, haikuWork, postDate, visibility, userName, displayName, role, theme
	                FROM haikudata h
	                JOIN themedata t ON h.themeID = t.themeID
	                JOIN accountdata a ON h.accountID = a.accountID
	                WHERE 1=1  
	                """);

	        if (haikuID != null && !haikuID.isEmpty()) {
	            sqlBuilder.append("AND haikuID = ? ");
	        }
	        if (accountID != null && !accountID.isEmpty()) {
	            sqlBuilder.append("AND h.accountID = ? ");
	        }
	        if (themeID != null && !themeID.isEmpty()) {
	            sqlBuilder.append("AND h.themeID = ? ");
	        }
	        if (visibility != null && !visibility.isEmpty()) {
	            sqlBuilder.append("AND visibility = ? ");
	        }
	        if (userName != null && !userName.isEmpty()) {
	            sqlBuilder.append("AND userName = ? ");
	        }
	        if (displayName != null && !displayName.isEmpty()) {
	            sqlBuilder.append("AND displayName = ? ");
	        }
	        if (role != null && !role.isEmpty()) {
	            sqlBuilder.append("AND role = ? ");
	        }
	        if (theme != null && !theme.isEmpty()) {
	            sqlBuilder.append("AND theme = ? ");
	        }
	        if (year != null && !year.isEmpty()) {
	            sqlBuilder.append("AND YEAR(postDate) = ? ");
	        }
	        if (month != null && !month.isEmpty()) {
	            sqlBuilder.append("AND MONTH(postDate) = ? ");
	        }
	        if (day != null && !day.isEmpty()) {
	            sqlBuilder.append("AND DAY(postDate) = ? ");
	        }

	        PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString());

	        int paramIndex = 1;

	        if (haikuID != null && !haikuID.isEmpty()) {
	            stmt.setString(paramIndex++, haikuID);
	        }
	        if (accountID != null && !accountID.isEmpty()) {
	            stmt.setString(paramIndex++, accountID);
	        }
	        if (themeID != null && !themeID.isEmpty()) {
	            stmt.setString(paramIndex++, themeID);
	        }
	        if (visibility != null && !visibility.isEmpty()) {
	            stmt.setString(paramIndex++, visibility);
	        }
	        if (userName != null && !userName.isEmpty()) {
	            stmt.setString(paramIndex++, userName);
	        }
	        if (displayName != null && !displayName.isEmpty()) {
	            stmt.setString(paramIndex++, displayName);
	        }
	        if (role != null && !role.isEmpty()) {
	            stmt.setString(paramIndex++, role);
	        }
	        if (theme != null && !theme.isEmpty()) {
	            stmt.setString(paramIndex++, theme);
	        }
	        if (year != null && !year.isEmpty()) {
	            stmt.setString(paramIndex++, year);
	        }
	        if (month != null && !month.isEmpty()) {
	            stmt.setString(paramIndex++, month);
	        }
	        if (day != null && !day.isEmpty()) {
	            stmt.setString(paramIndex++, day);
	        }

	        ResultSet rs = stmt.executeQuery();

	        
	        while (rs.next()) {
	            Haiku haiku = new Haiku();
	            haiku.setHaikuID(rs.getInt("haikuID"));
	            haiku.setAccountID(rs.getInt("accountID"));
	            haiku.setThemeID(rs.getInt("themeID"));
	            haiku.setHaikuWork(rs.getString("haikuWork"));
	            haiku.setPostDate(rs.getDate("postDate"));
	            haiku.setVisibility(rs.getString("visibility"));
	            haiku.setUserName(rs.getString("userName"));
	            haiku.setDisplayName(rs.getString("displayName"));
	            haiku.setRole(rs.getString("role"));
	            haiku.setTheme(rs.getString("theme"));
	            haikus.add(haiku);
	        }
	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return haikus;
	}


	/**
	 * 登録俳句データ全取得
	 * データリストを返す
	 * @return
	 */
	private List<Haiku> getAllHaikus() {
		List<Haiku> haikus = new ArrayList<>();

		Connection conn = null;
		try {
			conn = DataBaseUtil.getConnection();
			//クエリの作成
			String sql = """
					SELECT haikuID,h.accountID,h.themeID,haikuWork,postDate,visibility,userName,displayName,role,theme
					FROM haikudata h
					JOIN themedata t ON h.themeID = t.themeID
					JOIN accountdata a ON h.accountID = a.accountID
					""";
			//クエリの準備
			PreparedStatement stmt = conn.prepareStatement(sql);
			//クエリの実行
			ResultSet rs = stmt.executeQuery();
			//データがある限りデータをsetしてhaikusにadd
			while (rs.next()) {
				Haiku haiku = new Haiku();
				haiku.setHaikuID(rs.getInt("haikuID"));
				haiku.setHaikuWork(rs.getString("haikuWork"));
				haiku.setAccountID(rs.getInt("h.accountID"));
				haiku.setUserName(rs.getString("userName"));
				haiku.setDisplayName(rs.getString("displayName"));
				haiku.setThemeID(rs.getInt("h.themeID"));
				haiku.setTheme(rs.getString("theme"));
				haiku.setVisibility(rs.getString("visibility"));
				haiku.setRole(rs.getString("role"));
				haiku.setPostDate(rs.getDate("postDate"));
				haikus.add(haiku);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return haikus;
	}
	
	/**
	 * ログインユーザーの作成した俳句のみを表示するメソッド
	 * @param accountID アカウントID
	 * @return 指定されたアカウントIDに関連する俳句のリスト
	 */
	private List<Haiku> getAllHaikus(int accountID) {
	    List<Haiku> haikus = new ArrayList<>();

	    Connection conn = null;
	    try {
	        conn = DataBaseUtil.getConnection();
	        // 特定のアカウントIDに関連する俳句のみを取得するクエリ
	        String sql = """
	            SELECT haikuID, h.accountID, h.themeID, haikuWork, postDate, visibility, userName, displayName, role, theme
	            FROM haikudata h
	            JOIN themedata t ON h.themeID = t.themeID
	            JOIN accountdata a ON h.accountID = a.accountID
	            WHERE h.accountID = ?
	        """;
	        // クエリの準備
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setInt(1, accountID);
	        // クエリの実行
	        ResultSet rs = stmt.executeQuery();
	        // データがある限りデータをsetしてhaikusにadd
	        while (rs.next()) {
	            Haiku haiku = new Haiku();
	            haiku.setHaikuID(rs.getInt("haikuID"));
	            haiku.setAccountID(rs.getInt("accountID"));
	            haiku.setThemeID(rs.getInt("themeID"));
	            haiku.setHaikuWork(rs.getString("haikuWork"));
	            haiku.setPostDate(rs.getDate("postDate"));
	            haiku.setVisibility(rs.getString("visibility"));
	            haiku.setUserName(rs.getString("userName"));
	            haiku.setDisplayName(rs.getString("displayName"));
	            haiku.setRole(rs.getString("role"));
	            haiku.setTheme(rs.getString("theme"));
	            haikus.add(haiku);
	        }
	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return haikus;
	}

	
	/**
	 * 公開設定が”公開”のみ表示する
	 * @param accountID アカウントID
	 * @return 指定されたアカウントIDに関連する俳句のリスト
	 */
	private List<Haiku> getOpenHaikus() {
	    List<Haiku> haikus = new ArrayList<>();

	    Connection conn = null;
	    try {
	        conn = DataBaseUtil.getConnection();
	        // 公開された俳句のみ
	        String sql = """
	            SELECT haikuID, h.accountID, h.themeID, haikuWork, postDate, visibility, userName, displayName, role, theme
	            FROM haikudata h
	            JOIN themedata t ON h.themeID = t.themeID
	            JOIN accountdata a ON h.accountID = a.accountID
	            WHERE visibility = '公開'
	        """;
	        // クエリの準備
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        // クエリの実行
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            Haiku haiku = new Haiku();
	            haiku.setHaikuID(rs.getInt("haikuID"));
	            haiku.setHaikuWork(rs.getString("haikuWork"));
	            haiku.setPostDate(rs.getDate("postDate"));
	            haiku.setVisibility(rs.getString("visibility"));
	            haiku.setDisplayName(rs.getString("displayName"));
	            haiku.setTheme(rs.getString("theme"));
	            haikus.add(haiku);
	        }

	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return haikus;
	}

}
