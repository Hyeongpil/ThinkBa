package com.example.kwan.thinkba.model;

/**
 * Created by hp on 2016. 7. 19..
 */
public class ArchiveModel {
    private String name;
    private String detail;
    private int thumbnail;
    private boolean complete;

    public ArchiveModel(){}

    public ArchiveModel(String name, String detail, int thumbnail, boolean complete) {
        this.name = name;
        this.detail = detail;
        this.thumbnail = thumbnail;
        this.complete = complete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean getComplete() {return complete;}

    public void setComplete(boolean complete) {this.complete = complete;}
}
