package com.xiankunz.myflickrsearcher.model;

import java.util.List;

public class Photos {
    private int page;       // 1
    private int pages;      // 5641
    private int perpage;    // 100
    private int total;      // 564078
    private List<Photo> photo = null;  //

    public List<Photo> getPhotoList() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }
}
