package mc185249.webforms;

/**
 * Created by jn185090 on 6/8/2016.
 */
public class Information {

    int imageId;
    String title;

    public Information() {
    }

    public Information(int imageId, String title) {

        this.imageId = imageId;
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
