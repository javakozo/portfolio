package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import comment.Comment;
import haiku.Haiku;
import image.ImagePost;

/**
 * データベース接続の為のクラス
 * インポートして使う。
 */
public class DataBaseUtil {
    private static final String URL = "jdbc:mysql://database-1.cris22a023tv.ap-northeast-1.rds.amazonaws.com:3306/haiku";
    private static final String USER = "admin"; // データベースのユーザー名
    private static final String PASSWORD = "Servgo0714"; // データベースのパスワード

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接続
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /**
     * 指定されたIDのHaikudataを取得するメソッド
     * @param haikuID 俳句のID
     * @return Haiku オブジェクト
     * @throws SQLException
     */
    public static Haiku getHaikuById(int haikuID) throws SQLException {
        Haiku haiku = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;


        conn = DataBaseUtil.getConnection();
        //俳句IDからデータを取得
		String sql = """
				SELECT haikuID,h.accountID,h.themeID,haikuWork,postDate,visibility,userName,displayName,role,theme
				FROM haikudata h
				JOIN themedata t ON h.themeID = t.themeID
				JOIN accountdata a ON h.accountID = a.accountID
				WHERE haikuID = ?
				""";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, haikuID);
        rs = stmt.executeQuery();
        

        if (rs.next()) {
            haiku = new Haiku();
            haiku.setHaikuID(rs.getInt("haikuID"));
			haiku.setAccountID(rs.getInt("accountID"));
			haiku.setUserName(rs.getString("userName"));
			haiku.setHaikuWork(rs.getString("haikuWork"));
			haiku.setPostDate(rs.getDate("postDate"));
			haiku.setVisibility(rs.getString("visibility"));
			haiku.setDisplayName(rs.getString("displayName"));
			haiku.setRole(rs.getString("role"));
			haiku.setTheme(rs.getString("theme"));
            // 必要に応じて他のフィールドも設定
        }

        if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (conn != null) {
            conn.close();
        }

        return haiku;
    }
    
    /**
     * 俳句の情報を更新するメソッド
     * 
     * @param haikuID    俳句のID
     * @param haikuWork  俳句の内容
     * @param visibility 俳句の公開設定
     * @return 更新が成功したかどうかを示す真偽値
     * @throws SQLException SQL例外
     */
    public static boolean updateHaiku(int haikuID, String haikuWork, String visibility)
            throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataBaseUtil.getConnection();
            String sql = "UPDATE haikudata SET haikuWork = ?, visibility = ? WHERE haikuID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, haikuWork);
            stmt.setString(2, visibility);
            stmt.setInt(3, haikuID);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // 更新が成功したかどうかを返す
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    
    /**
     * セッションに保存してあるaccountIDからテーブル名を指定して特定のカラムのデータを呼び出す
     * @param accountID
     * @param columnName
     * @return
     */
    public static String getColumnValue(int accountID, String tableName,String columnName) {
        String columnValue = "";
        Connection conn = null;
        try {
            conn = DataBaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT " + columnName + " FROM "+ tableName + " WHERE accountID = ?");
            stmt.setInt(1, accountID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                columnValue = rs.getString(columnName);
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
        return columnValue;
    }
    
    // データベースからテーマデータを取得するメソッド
    public static List<String> getThemes() {
        List<String> themes = new ArrayList<>();
        Connection conn = null;
        try {
            conn = getConnection();
            String sql = "SELECT theme FROM themedata";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                themes.add(rs.getString("theme"));
            }
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
    
    public static void insertComment(Comment comment) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataBaseUtil.getConnection();
            String sql = "INSERT INTO commentdata (haikuID, accountID, comment_text, date) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, comment.getHaikuID());
            stmt.setInt(2, comment.getAccountID());
            stmt.setString(3, comment.getCommentText());
            stmt.setTimestamp(4, new Timestamp(comment.getDate().getTime()));
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    
    /**
     * 指定された俳句IDに関連するコメントをデータベースから取得します。
     * 
     * @param haikuID 俳句ID
     * @return コメントのリスト
     * @throws SQLException
     */
    public static List<Comment> getCommentsByHaikuID(int haikuID) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM commentdata WHERE haikuID = ? ORDER BY commentID DESC";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, haikuID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int commentID = resultSet.getInt("commentID");
                    int accountID = resultSet.getInt("accountID");
                    String commentText = resultSet.getString("comment_text");
                    Timestamp date = resultSet.getTimestamp("date");
                    
                    Comment comment = new Comment(commentID, haikuID, accountID, commentText,date);
                    comments.add(comment);
                }
            }
        }

        return comments;
    }
    
    /**
     * コメント数を数える
     * @param haikuID
     * @return
     * @throws SQLException
     */
    public static int getCommentCount(int haikuID) throws SQLException {
        int commentCount = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT COUNT(*) AS count FROM commentdata WHERE haikuID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, haikuID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                commentCount = rs.getInt("count");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        
        return commentCount;
    }
    
    public static List<Integer> getHaikuYears() {
        List<Integer> years = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT DISTINCT YEAR(postDate) AS year FROM haikudata ORDER BY year DESC")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                years.add(rs.getInt("year"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return years;
    }

    public static List<Integer> getHaikuMonths() {
        List<Integer> months = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT DISTINCT MONTH(postDate) AS month FROM haikudata ORDER BY month")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                months.add(rs.getInt("month"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return months;
    }
    
    public static List<Integer> getHaikuDays() {
        List<Integer> days = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT DISTINCT DAY(postDate) AS day FROM haikudata ORDER BY day")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                days.add(rs.getInt("day"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }
    
    public static void saveImageInfo(String fileName, Timestamp postTime, int accountId, String imageText) throws SQLException {
        String sql = "INSERT INTO imageposts (accountID, imageUrl, imageText, time) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setString(2, fileName);
            pstmt.setString(3, imageText);
            pstmt.setTimestamp(4, postTime);
            pstmt.executeUpdate();
        }
    }
    
    public static List<ImagePost> getAllImagePosts() throws SQLException {
        List<ImagePost> posts = new ArrayList<>();
        String query = "SELECT * FROM imageposts";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ImagePost post = new ImagePost();
                post.setId(rs.getInt("id"));
                post.setAccountID(rs.getInt("accountID"));
                post.setImageUrl(rs.getString("imageUrl"));
                post.setImageText(rs.getString("imageText"));
                post.setTime(rs.getTimestamp("time"));
                posts.add(post);
            }
        }
        return posts;
    }    
   

    
}
