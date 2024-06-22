package haiku;

import java.util.Date;

/**
 * Haiku
 */
public class Haiku {
	private int haikuID;
    private int accountID;
    private int themeID;
	private String haikuWork;
	private Date postDate;
	private String visibility;
	private String userName;
	private String displayName;
	private String role;
	private String theme;
	
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
	
	public int getThemeID() {
		return themeID;
	}

	public void setThemeID(int themeID) {
		this.themeID = themeID;
	}
	
	
	public String getHaikuWork() {
		return haikuWork;
	}

	public void setHaikuWork(String haikuWork) {
		this.haikuWork = haikuWork;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	
	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@Override
	public String toString() {
	    return "Haiku [haikuID=" + haikuID + ", accountID=" + accountID + ", themeID=" + themeID 
	            + ", haikuWork=" + haikuWork + ", postDate=" + postDate + ", visibility=" + visibility 
	            + ", userName=" + userName + ", displayName=" + displayName + ", role=" + role + ", theme=" + theme + "]";
	}







}
