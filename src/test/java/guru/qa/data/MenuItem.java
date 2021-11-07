package guru.qa.data;

public enum MenuItem {
    CODE("Code"),
    ISSUES("Issues"),
    PULL_REQUESTS("Pull requests"),
    DISCUSSIONS("Discussions");

    private String content;

    MenuItem(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
