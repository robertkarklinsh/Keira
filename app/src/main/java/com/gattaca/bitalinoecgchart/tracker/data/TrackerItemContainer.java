package com.gattaca.bitalinoecgchart.tracker.data;

/**
 * Created by Artem on 28.08.2016.
 */
public abstract class TrackerItemContainer {
    int icon;
    String blackText;
    String grayText;
    boolean visible = false;

    public abstract int getType();

    public TrackerItemContainer(){

    }

    public TrackerItemContainer(String blackText, String grayText, int icon) {
        this.grayText = grayText;
        this.blackText = blackText;
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBlackText() {
        return blackText;
    }

    public void setBlackText(String blackText) {
        this.blackText = blackText;
    }

    public String getGrayText() {
        return grayText;
    }

    public void setGrayText(String grayText) {
        this.grayText = grayText;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public TrackerItemContainer(int icon, String blackText, String grayText, boolean visible) {

        this.icon = icon;
        this.blackText = blackText;
        this.grayText = grayText;
        this.visible = visible;
    }
}
