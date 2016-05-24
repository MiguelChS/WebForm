package models;

import android.graphics.Bitmap;

/**
 * Created by jn185090 on 5/20/2016.
 */
public class File{
    String name,blob;
    private Bitmap bitmap;

    public File(String name, String blob,Bitmap bitmap1) {
        this.name = name;
        this.blob = blob;
        this.bitmap = bitmap1;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlob() {
        return blob;
    }

    public void setBlob(String blob) {
        this.blob = blob;
    }
}
