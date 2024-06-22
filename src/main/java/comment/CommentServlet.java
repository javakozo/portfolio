package comment;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import database.DataBaseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CommentServlet
 */
@WebServlet("/CommentServlet")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentServlet() {

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String haikuID = request.getParameter("haikuID");
        String accountID = request.getParameter("accountID");
        String commentText = request.getParameter("comment_text");
        Timestamp date = Timestamp.valueOf(request.getParameter("date"));
        
     // コメントオブジェクトを生成し、引数を指定してインスタンス化します
        Comment comment = new Comment(0, Integer.parseInt(haikuID), Integer.parseInt(accountID), commentText,date);
        
        try {
            DataBaseUtil.insertComment(comment);
            // コメントが正常に挿入された場合の処理を記述します
            response.sendRedirect("ListHaikuServlet?open=value"); // 例えば、リダイレクトして俳句リストを再表示します
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "コメントを挿入できませんでした");
            request.getRequestDispatcher("/WEB-INF/classes/normal/listOpen.jsp").forward(request, response);
        }
    }

}
