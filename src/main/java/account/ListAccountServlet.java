package account;

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

/**
 * Servlet implementation class ListAccountServlet
 */
@WebServlet("/ListAccountServlet")
public class ListAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // テーマリストを取得
        List<Account> accountIDs = getAccounts();
        
        
        // リクエストにテーマリストをセット
        request.setAttribute("accountIDs", accountIDs);
        
        // JSPにフォワード
        request.getRequestDispatcher("/WEB-INF/classes/admin/listAccount.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
    private List<Account> getAccounts() {
        List<Account> accountlist = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DataBaseUtil.getConnection();

            // テーマを取得するSQLクエリ
            String sql = "SELECT * FROM accountdata";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
    				Account account = new Account();
    				account.setAccountID(rs.getInt("accountID"));
    				account.setUserName(rs.getString("userName"));
    				account.setDisplayName(rs.getString("displayName"));
    				account.setRole(rs.getString("role"));
    				accountlist.add(account);
    	
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
        return accountlist;
    }

}
