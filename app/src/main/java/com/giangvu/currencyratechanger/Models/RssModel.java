package com.giangvu.currencyratechanger.Models;

public class RssModel {
    private String Title;
    private String Link;
    private String Description;
    public  RssModel(){}

    public RssModel(String title, String link, String description) {
        Title = title;
        Link = link;
        Description = description;
    }

    @Override
    public String toString() {
        return "RssModel{" +
                "Title='" + Title + '\'' +
                ", Link='" + Link + '\'' +
                ", Description='" + Description + '\'' +
                '}';
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
