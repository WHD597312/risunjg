package com.risun.jg.pojo;

/**
 * Created by whd on 2018/1/16.
 */

public class Ad {
    private int iconResId;
    private int intro;

    public Ad(int iconResId){
        this.iconResId=iconResId;
    }
    public Ad(int iconResId, int intro) {
        super();
        this.iconResId = iconResId;
        this.intro = intro;
    }
    public int getIconResId() {
        return iconResId;
    }

    public int getIntro() {
        return intro;
    }
}
