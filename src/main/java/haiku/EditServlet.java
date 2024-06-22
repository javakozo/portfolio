package haiku;

import java.io.IOException;
import java.sql.SQLException;

import database.DataBaseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 俳句の編集
 */
@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 編集画面editHaiku.jspへ
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int haikuID = Integer.parseInt(request.getParameter("haikuID"));
		request.setAttribute("haikuID", haikuID);
		request.getRequestDispatcher("/WEB-INF/classes/normal/editHaiku.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// フォームから送信されたデータを取得
		int haikuID = Integer.parseInt(request.getParameter("haikuID"));
		String haikuWork = request.getParameter("haikuWork");
		String visibility = request.getParameter("visibility");

		// データベースの俳句を更新
		try {
			boolean success = DataBaseUtil.updateHaiku(haikuID, haikuWork, visibility);
			if (success) {
				// 更新成功したら編集画面にリダイレクト
				response.sendRedirect(request.getContextPath() + "/ListHaikuServlet");
			} else {
				// 更新失敗したらエラーページにリダイレクト
				response.sendRedirect("errorPage.jsp");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// SQLエラーが発生したらエラーページにリダイレクト
			response.sendRedirect("errorPage.jsp");
		}
	}

}
