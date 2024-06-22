package theme;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DataBaseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ListThemeServlet")
public class ListThemeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // テーマリストを取得
        List<Theme> themes = getThemes();
        
        // リクエストにテーマリストをセット
        request.setAttribute("themes", themes);
        
        // JSPにフォワード
        request.getRequestDispatcher("/WEB-INF/classes/admin/listTheme.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // リクエストパラメータからテーマを取得
        String themeName = request.getParameter("theme");

        // テーマが空でない場合は追加
        if (themeName != null && !themeName.isEmpty()) {
            addTheme(themeName);
        }

        // テーマリストを取得
        List<Theme> themes = getThemes();

        // リクエストにテーマリストをセット
        request.setAttribute("themes", themes);

        // JSPにフォワード
        request.getRequestDispatcher("/WEB-INF/classes/admin/listTheme.jsp").forward(request, response);
    }

    private List<Theme> getThemes() {
        List<Theme> themes = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DataBaseUtil.getConnection();

            // テーマを取得するSQLクエリ
            String sql = "SELECT * FROM themedata";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // テーマをリストに追加
            while (rs.next()) {
                Theme theme = new Theme(rs.getInt("themeID"), rs.getString("theme"));
                themes.add(theme);
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
        return themes;
    }
    
    private void addTheme(String theme) {
        Connection conn = null;
        try {
            conn = DataBaseUtil.getConnection();

            // テーマを追加するSQLクエリ
            String sql = "INSERT INTO themedata (theme) VALUES (?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, theme);
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
