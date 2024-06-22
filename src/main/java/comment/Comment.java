package comment;

import java.sql.Timestamp;
import java.util.Date;

public class Comment {
    private int commentID;
    private int haikuID;
    private int accountID;
    private String commentText;
    private Timestamp date;

    // コンストラクタ
    public Comment(int commentID, int haikuID, int accountID, String commentText,Timestamp date) {
        this.commentID = commentID;
        this.haikuID = haikuID;
        this.accountID = accountID;
        this.commentText = commentText;
        this.date = date;
    }

    // ゲッターとセッター
    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getHaikuID() {
        return haikuID;
    }

    public void setHaikuID(int haikuID) {
        this.haikuID = haikuID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
    
	public Date getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
}

