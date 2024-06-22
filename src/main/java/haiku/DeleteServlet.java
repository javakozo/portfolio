package haiku;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.DataBaseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeleteServlet
 */
@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 選ばれた俳句を削除
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//俳句IDを取得
		String haikuID = request.getParameter("haikuID");
		//接続
		Connection conn = null;
		try {
			//データベースへ接続
			conn = DataBaseUtil.getConnection();
			//SQL作成
			String sql = "DELETE FROM haikudata WHERE haikuID = ?";			
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, haikuID);
			stmt.executeUpdate();
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
		//再度リスト生成
		response.sendRedirect(request.getContextPath() + "/ListHaikuServlet");
//		request.getRequestDispatcher("/ListHaikuServlet").forward(request, response);
	}

}
