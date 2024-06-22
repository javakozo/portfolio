package account;

public class Account {
    private int accountID;
    private String userName;
	private String displayName;
	private String role;
	
	public int getAccountID() {
		return accountID;
	}
	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userID) {
		this.userName = userID;
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

}
