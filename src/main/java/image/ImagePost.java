package image;

import java.sql.Timestamp;

public class ImagePost {
    private int id;
    private int accountID;
    private String imageUrl;
    private String imageText;
    private Timestamp time;

    public ImagePost() {
    }
    
    public ImagePost(int id, int accountID, String imageUrl, String imageText, Timestamp time) {
        this.id = id;
        this.accountID = accountID;
        this.imageUrl = imageUrl;
        this.imageText = imageText;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
