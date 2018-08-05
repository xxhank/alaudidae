package com.lrx.alaudidae;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.GsonBuilder;
import com.lrx.alaudidae.Model.BookResponse;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RecyclerView _recyclerView;
    private RecyclerView.Adapter _adapter;
    private RecyclerView.LayoutManager _layoutManager;

    BridgeWebView _webView;
    Button _parseButton;

    private Gson _gson;
    BookResponse _response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _gson = new Gson();
        try {
            InputStream books = getAssets().open("books2.json");
            String bookJSONString = IOUtils.toString(books, "UTF-8");
            _response = _gson.fromJson(bookJSONString, BookResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        _webView = findViewById(R.id.webView);
        _webView.getSettings().setJavaScriptEnabled(true);
        _webView.setWebContentsDebuggingEnabled(true);


        //_webView.loadUrl("file:///android_asset/index.html");

        _webView.setDefaultHandler(new DefaultHandler());

        _webView.setWebChromeClient(new WebChromeClient() {
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                //mUploadMessage = uploadMsg;
                //pickFile();
            }
        });

        _webView.loadUrl("file:///android_asset/index.html");

        _webView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.i("handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }
        });

        _webView.callHandler("parse", "http://www.mailnovel.com/", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                Logger.d(data);
            }
        });

        _parseButton = findViewById(R.id.parse_button);
        _parseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _webView.callHandler("parse", "http://www.mailnovel.com/", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Logger.d(data);
                    }
                });
            }
        });
        _recyclerView =  findViewById(R.id.my_recycler_view);
        _recyclerView.setHasFixedSize(true);

        _layoutManager = new

                LinearLayoutManager(this);
        _recyclerView.setLayoutManager(_layoutManager);

        _adapter = new

                MyAdapter(_response.books);
        _recyclerView.setAdapter(_adapter);
        _recyclerView.addItemDecoration(new

                DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        _recyclerView.addOnItemTouchListener(new

                RecyclerTouchListener(this, _recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Single Click on position :" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Long press on position :" + position, Toast.LENGTH_LONG).show();
            }
        }));

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<BookResponse.Book> books;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView titleView;
            public TextView countView;
            public TextView briefView;

            public ViewHolder(View itemView) {
                super(itemView);
                titleView = itemView.findViewById(R.id.book_title);
                countView = itemView.findViewById(R.id.book_subscription_count);
                briefView = itemView.findViewById(R.id.book_brief);
            }
        }

        MyAdapter(List<BookResponse.Book> books) {
            this.books = books;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            BookResponse.Book book = books.get(position);
            holder.titleView.setText(book.title);
            holder.countView.setText("" + book.subscriptionsCount);
            holder.briefView.setText(book.brief);
        }

        @Override
        public int getItemCount() {
            return books.size();
        }
    }
}
