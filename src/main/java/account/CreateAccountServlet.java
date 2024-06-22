package account;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import database.DataBaseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateAccountServlet
 */
@WebServlet("/CreateAccountServlet")
public class CreateAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * アカウント作成ページ(createAccount.jsp)に遷移
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/WEB-INF/classes/account/createAccount.jsp").forward(request, response);
	}

	/**
	 * createAccount.jspから情報をもらいcreateAccountResult.jspへ遷移
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//createAccount.jspに入力されたデータを変数に格納
		String userName = request.getParameter("userName");
		String passWord = request.getParameter("passWord");
		String displayName = request.getParameter("displayName");

		//入力が抜けている所を登録ページで指摘。
		if (userName == null || userName.isEmpty() ||displayName == null || displayName.isEmpty() || passWord == null || passWord.isEmpty()) {
			request.setAttribute("errorMessage", "項目を正しく入力してください");
			request.getRequestDispatcher("/WEB-INF/classes/account/createAccount.jsp").forward(request, response);
			return;
		}

		//データベースに接続してユーザー登録情報を入れる
		try (Connection conn = DataBaseUtil.getConnection()) {

			// ユーザーネームがすでに存在するかをチェック
			String checkSql = "SELECT COUNT(*) AS count FROM accountdata WHERE userName = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
				checkStmt.setString(1, userName);
				try (ResultSet rs = checkStmt.executeQuery()) {
					rs.next();
					int count = rs.getInt("count");
					if (count > 0) {
						// 重複エラー
						request.setAttribute("errorMessage", "そのユーザーIDはすでに登録されています");
						request.getRequestDispatcher("/WEB-INF/classes/account/createAccount.jsp").forward(request, response);
						return;
					}
				}
			}
			
			// 表示名がすでに存在するかをチェック
			String checkNameSql = "SELECT COUNT(*) AS count FROM accountdata WHERE displayName = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkNameSql)) {
				checkStmt.setString(1, displayName);
				try (ResultSet rs = checkStmt.executeQuery()) {
					rs.next();
					int count = rs.getInt("count");
					if (count > 0) {
						// 重複エラー
						request.setAttribute("errorMessage", "その表示名はすでに使用されています");
						request.getRequestDispatcher("/WEB-INF/classes/account/createAccount.jsp").forward(request, response);
						return;
					}
				}
			}
			
			 // パスワードをハッシュ化
            String hashedPassword = BCrypt.hashpw(passWord, BCrypt.gensalt());
			
			
			//ユーザーを登録
			String sql = "INSERT INTO accountdata (userName, passWord,displayName) VALUES (?, ?,?)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, userName);
				stmt.setString(2, hashedPassword);
				stmt.setString(3, displayName);
				stmt.executeUpdate();
				
				// ログインページに戻る時にユーザーIDだけセッションに保存
				request.getSession().setAttribute("createdUserName", userName);

				// 登録成功の場合
				request.getRequestDispatcher("/WEB-INF/classes/account/createAccountSuccess.jsp").forward(request, response);
			}
		} catch (SQLException e) {

			throw new ServletException("Database access error.", e);
		}
	}

}
