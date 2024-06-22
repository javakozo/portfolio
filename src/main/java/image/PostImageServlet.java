package image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import database.DataBaseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/PostImageServlet")
@MultipartConfig
public class PostImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String UPLOAD_DIR = "upimages";

    public PostImageServlet() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ImagePost> posts = DataBaseUtil.getAllImagePosts();
            request.setAttribute("posts", posts);
            request.getRequestDispatcher("/WEB-INF/classes/normal/topNormal.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to retrieve posts");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uploadPath = request.getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        Part filePart = request.getPart("file");
        
        if (filePart == null || filePart.getSize() == 0) {
            // エラーメッセージを設定
            String errorMessage = "画像ファイルを選択してください。";
            request.setAttribute("errorMessage", errorMessage);

            // 全ての投稿を取得して "posts" 属性に設定
            List<ImagePost> posts;
			try {
				posts = DataBaseUtil.getAllImagePosts();
				request.setAttribute("posts", posts);
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
            // 元の投稿フォームに戻る
            request.getRequestDispatcher("/WEB-INF/classes/normal/topNormal.jsp").forward(request, response);
            return;
        }
        
        
        try {

            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String filePath = uploadPath + File.separator + fileName;
            File file = new File(filePath);
            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            Timestamp postTime = new Timestamp(System.currentTimeMillis());
            int accountId = Integer.parseInt(request.getParameter("postAccountID"));
            String imageText = request.getParameter("imageText");

            DataBaseUtil.saveImageInfo(fileName, postTime, accountId, imageText);
            
            // 全ての投稿を取得して "posts" 属性に設定
            List<ImagePost> posts = DataBaseUtil.getAllImagePosts();
            request.setAttribute("posts", posts);

            request.getRequestDispatcher("/WEB-INF/classes/normal/topNormal.jsp").forward(request, response);
        } catch (SQLException | IOException | ServletException e) {
            e.printStackTrace();
        }
    }
}
