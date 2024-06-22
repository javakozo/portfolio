package haiku;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import database.DataBaseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ConfirmHaikuServlet")
public class ConfirmHaikuServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // confirmHaiku.jspから送信されたデータを取得する
        String haikuWork = request.getParameter("haikuWork");
        String visibility = request.getParameter("visibility");
        String theme = request.getParameter("theme"); // テーマを取得
        
        // セッションからaccountIDを取得
        HttpSession session = request.getSession();
        
        //displayNameを取得
        int sessionAccountID = (int) session.getAttribute("sessionAccountID");
        String displayName = DataBaseUtil.getColumnValue(sessionAccountID,"accountdata", "displayName");

        
        // 現在の日時を取得
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(currentDate);
        
        // リクエストにデータをセットしてconfirmHaiku.jspにフォワードする
        request.setAttribute("haikuWork", haikuWork);
        request.setAttribute("visibility", visibility);
        request.setAttribute("displayName", displayName);
        request.setAttribute("formattedDate", formattedDate);
        request.setAttribute("theme", theme); // テーマをリクエストにセット
        request.getRequestDispatcher("/WEB-INF/classes/normal/confirmHaiku.jsp").forward(request, response);
    }
}
