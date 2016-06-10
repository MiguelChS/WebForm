package mc185249.webforms;

/**
 * Created by jn185090 on 6/8/2016.
 */
public class Information {

    int imageId;
    String title;
    String deltaTime;
    String currentState;

    public Information() {
    }

    public Information(int imageId, String title,String deltaTime,String currentState) {

        this.imageId = imageId;
        this.title = title;
        this.deltaTime = deltaTime;
        this.currentState = currentState;
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

    public String getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(String deltaTime) {
        this.deltaTime = deltaTime;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
}
