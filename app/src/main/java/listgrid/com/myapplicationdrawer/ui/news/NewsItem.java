package listgrid.com.myapplicationdrawer.ui.news;

public class NewsItem {
private String gambar;
private String title, desc;
private String id;

    public NewsItem(String gambar, String title, String desc) {
        this.gambar = gambar;
        this.title = title;
        this.desc = desc;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
