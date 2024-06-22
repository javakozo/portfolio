package theme;

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

@WebServlet("/DeleteThemeServlet")
public class DeleteThemeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // リクエストパラメータから削除するテーマのIDを取得
        int themeId = Integer.parseInt(request.getParameter("themeId"));

        // テーマを削除
        deleteTheme(themeId);

        // リダイレクトしてリフレッシュ
        response.sendRedirect(request.getContextPath() + "/ListThemeServlet");
    }

    private void deleteTheme(int themeId) {
        Connection conn = null;
        try {
            conn = DataBaseUtil.getConnection();

            // テーマを削除するSQLクエリ
            String sql = "DELETE FROM themedata WHERE themeID = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, themeId);
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
    }
}
