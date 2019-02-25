package com.xiankunz.myflickrsearcher.model;

public class Photo {
    private int farm;       // 1
    private int height_s;   // 240
    private String id;      // 33094387050
    private int isfamily;   // 0
    private int isfriend;   // 0
    private int ispublic;   // 1
    private String owner;   // 29314320@N07
    private String secret;  // 89019909cc
    private int server;     // 667
    private String title;   // Stanley T. 3-16-2017
    private String url_s;   // https://farm1.staticflickr.com/667/33094387050_89019909cc_m.jpg
    private int width_s;    // 180

    public int getHeight_s() {
        return height_s;
    }

    public void setHeight_s(int height_s) {
        this.height_s = height_s;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl_s() {
        return url_s;
    }

    public void setUrl_s(String url_s) {
        this.url_s = url_s;
    }

    public int getWidth_s() {
        return width_s;
    }

    public void setWidth_s(int width_s) {
        this.width_s = width_s;
    }
}
