package com.example.android.news1;

public class News {

    private String mNewsUrl;
    private String mNewsSection;
    private String mNewsTitle;
    private String mNewsPublishedDate;
    private String mNewsWebTitle;
    private String mImageUrl;

    public News(String section,String title,String url,String publishedDate,String webTitle,String imgUrl){
        mNewsSection = section;
        mNewsTitle = title;
        mNewsUrl = url;
        mNewsPublishedDate = publishedDate;
        mNewsWebTitle = webTitle;
        mImageUrl = imgUrl;
    }

    public String getNewsWebTitle() {
        return mNewsWebTitle;
    }
    public String getNewsSection() {
        return mNewsSection;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    public String getNewsTitle() {
        return mNewsTitle;
    }
    public String getNewsUrl() {
        return mNewsUrl;
    }
    public String getNewsPublishedDate() {
        return mNewsPublishedDate;
    }
}
