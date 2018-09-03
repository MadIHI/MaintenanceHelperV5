package org.ihi.maintenancehelperv5;

import android.graphics.drawable.Drawable;

public class Data {
    //private int imgId;
    private String content;
    private Drawable _drawable;

    public Data() {
    }

    public Data(String content, Drawable drawable) {
        //this.imgId = imgId;
        this.content = content;
        this._drawable = drawable;
    }

    /*public int getImgId() {
        return imgId;
    }*/

    public String getContent() {
        return content;
    }

    public Drawable getDrawable() {
        return _drawable;
    }

    /*public void setImgId(int imgId) {
        this.imgId = imgId;
    }*/

    public void setContent(String content) {
        this.content = content;
    }
}