package com.fahim.newapp.holder;

public class BookHolder {
    public int id;
    public int subjectid;
    public int standardid;
    public String bookname;
    public String booklink;
    public int viewCount;

    public BookHolder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public int getStandardid() {
        return standardid;
    }

    public void setStandardid(int standardid) {
        this.standardid = standardid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBooklink() {
        return booklink;
    }

    public void setBooklink(String booklink) {
        this.booklink = booklink;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
