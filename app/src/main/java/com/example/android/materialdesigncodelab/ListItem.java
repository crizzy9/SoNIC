package com.example.android.materialdesigncodelab;

/**
 * Created by ALCHEMi$T on 05-02-2016.
 */

public class ListItem {

    private String headline;
    private String reporterName;
    private String upvotes;
    private String downvotes;
    private String date;
    private String url;
    private String postid;
    private String type;
    private String value;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getUpvotes(){
        return upvotes;
    }

    public void setUpvotes(String upvotes){
        this.upvotes = upvotes;
    }

    public String getDownvotes(){
        return downvotes;
    }

    public void setDownvotes(String downvotes){
        this.downvotes = downvotes;
    }

    public String getPostid(){
        return postid;
    }

    public void setPostId(String postid){
        this.postid = postid;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "[ headline=" + headline + ", reporter Name=" + reporterName + " , date=" + date + "]";
    }
}
