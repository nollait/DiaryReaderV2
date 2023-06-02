package com.example.diaryreader;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private int imageId;
    private String title;
    private String author;
    private String review;

    public Book(int imageId, String title, String author) {
        this.imageId = imageId;
        this.title = title;
        this.author = author;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    // Реализация Parcelable

    protected Book(Parcel in) {
        imageId = in.readInt();
        title = in.readString();
        author = in.readString();
        review = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageId);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(review);
    }
}