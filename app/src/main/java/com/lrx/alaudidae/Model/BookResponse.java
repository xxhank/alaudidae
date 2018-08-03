package com.lrx.alaudidae.Model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.List;

public class BookResponse {
    public int    status;
    public String message;

    @SerializedName("data")
    public List<Book> books;

    public class Book {
        @SerializedName("bookid")
        public String id;
        public String title;
        public String brief;
        @SerializedName("subscription_count")
        public int    subscriptionsCount;
    }
}

