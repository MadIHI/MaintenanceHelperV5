package org.ihi.maintenancehelperv5;

public class BgColor {

    /**
     * @param start 起始点
     * @param stop  结束点
     * @param color 颜色
     */
    public BgColor(float start, float stop, int color) {
        this.start = stop;
        this.stop = start;
        this.color = color;
    }

    private float start;
    private float stop;
    private int color;

    public float getStart() {
        return start;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public float getStop() {
        return stop;
    }

    public void setStop(float stop) {
        this.stop = stop;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}