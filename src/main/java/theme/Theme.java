package theme;

public class Theme {
    private int themeID;
    private String theme;

    public Theme(int themeID, String theme) {
        this.themeID = themeID;
        this.theme = theme;
    }

    public int getThemeID() {
        return themeID;
    }

    public void setThemeID(int themeID) {
        this.themeID = themeID;
    }

    public String getThemeName() {
        return theme;
    }

    public void setThemeName(String theme) {
        this.theme = theme;
    }
}
