package login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import database.DataBaseUtil;
import image.ImagePost;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//メニュー等から戻ってきた時にセッションから適切なメニューへ。
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
        try {
            List<ImagePost> posts = DataBaseUtil.getAllImagePosts();
            request.setAttribute("posts", posts);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to retrieve posts");
        }
		HttpSession session = request.getSession(false);
		if (session != null) {
			int resessionAccountID = (int) session.getAttribute("sessionAccountID");
			String role = DataBaseUtil.getColumnValue(resessionAccountID, "accountdata", "role");

			if (role != null) {
				if ("admin".equals(role)) {
					request.getRequestDispatcher("/WEB-INF/classes/admin/topAdmin.jsp").forward(request, response);
				} else if ("normal".equals(role)) {
					request.getRequestDispatcher("/WEB-INF/classes/normal/topNormal.jsp").forward(request, response);
				} else {
					response.sendRedirect("index.jsp");
				}
			} else {
				response.sendRedirect("index.jsp");
			}
		}
	}

	/**
	 * 最初のログイン時にID,PASSから各メニューへ
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
        try {
            List<ImagePost> posts = DataBaseUtil.getAllImagePosts();
            request.setAttribute("posts", posts);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to retrieve posts");
        }

		// フォームから送信されたデータを取得	
		String userName = request.getParameter("userName");
		String passWord = request.getParameter("passWord");

		try (Connection conn = DataBaseUtil.getConnection()) {
			//SQL用意
			String sql = "SELECT * FROM accountdata WHERE userName = ?";
			//SQLセット
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, userName);

				try (ResultSet rs = stmt.executeQuery()) {

					if (rs.next()) {
						String storedHash = rs.getString("passWord");

						if (BCrypt.checkpw(passWord, storedHash)) {
							//データベースのaccountIDを拾う
							int sessionAccountID = rs.getInt("accountID");

							//セッションにログインユーザーのaccountIDのみ保存
							HttpSession session = request.getSession();
							session.setAttribute("sessionAccountID", sessionAccountID);

							// データベースから関連するroleを取得
							String srole = DataBaseUtil.getColumnValue(sessionAccountID, "accountdata", "role");
							
							// 管理者であるかどうかを判定し、isAdminフラグをセット
							boolean isAdmin = "admin".equals(srole);
							session.setAttribute("isAdmin", isAdmin);
							
							//ロール毎に遷移
							if ("admin".equals(srole)) {
								request.getRequestDispatcher("/WEB-INF/classes/admin/topAdmin.jsp").forward(request,
										response);//管理者
							} else if ("normal".equals(srole)) {
								request.getRequestDispatcher("/WEB-INF/classes/normal/topNormal.jsp").forward(request,
										response);//一般

							}

						} else {
							// ログイン失敗なら遷移せずに、最初のページにエラーメッセージを送る
							request.setAttribute("errorMessage", "ユーザー名またはパスワードが違います");
							request.getRequestDispatcher("index.jsp").forward(request, response);

						}

					} else {
						request.setAttribute("errorMessage", "ユーザー名またはパスワードが違います");
						request.getRequestDispatcher("index.jsp").forward(request, response);
					}

				}
			}
		} catch (SQLException e) {
			throw new ServletException("Database access error.", e);
		}
	}
}