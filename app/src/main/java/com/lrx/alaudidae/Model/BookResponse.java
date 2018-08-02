package com.lrx.alaudidae.Model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.List;

public class BookResponse {
    int status;
    int message;

    @SerializedName("data")
    List<Book> books;

    class Book {
        @SerializedName("bookid")
        String id;
        String title;
        String brief;
        @SerializedName("subscription_count")
        int subscriptionsCount;
    }
}

