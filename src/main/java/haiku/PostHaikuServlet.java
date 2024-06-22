package haiku;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import database.DataBaseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PostHaiku
 */
@WebServlet("/PostHaikuServlet")
public class PostHaikuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 俳句投稿画面への遷移
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("/WEB-INF/classes/normal/postHaiku.jsp").forward(request, response);
		
	}

	/**
	 * 俳句投稿画面から俳句データベースに格納
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // セッションからaccountIDを取得
        Integer accountID = (Integer) request.getSession().getAttribute("sessionAccountID");
        
    	// accountIDがない場合はログイン画面にリダイレクト
        if (accountID == null) {
        	System.out.print("セッション切れ");
            response.sendRedirect("index.jsp"); 
            return;
        }
        

        // confirmhaikudataから取得
        //俳句本文
        String haikuWork = request.getParameter("haikuWork");
        //公開、非公開設定
        String visibility = request.getParameter("visibility");
        
        // 現在の日付を取得
        Timestamp postDate = Timestamp.valueOf(LocalDateTime.now());
        
        // データベースに挿入
        try (Connection conn = DataBaseUtil.getConnection()) {
        	String sql = "INSERT INTO haikudata (accountID, haikuWork, postDate, visibility) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, accountID);
                pstmt.setString(2, haikuWork);
                pstmt.setTimestamp(3, postDate);
                pstmt.setString(4, visibility);
                pstmt.executeUpdate();
            }

            // 投稿画面にフォワードする
            request.getRequestDispatcher("/WEB-INF/classes/normal/successPost.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error", e);
        }
        
		
	}

}
