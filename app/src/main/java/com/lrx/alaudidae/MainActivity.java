package com.lrx.alaudidae;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private RecyclerView               _recyclerView;
    private RecyclerView.Adapter       _adapter;
    private RecyclerView.LayoutManager _layoutManager;

    private String[] _dataset = new String[]{"1", "2", "3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _recyclerView = findViewById(R.id.my_recycler_view);
        _recyclerView.setHasFixedSize(true);

        _layoutManager = new LinearLayoutManager(this);
        _recyclerView.setLayoutManager(_layoutManager);

        _adapter = new MyAdapter(_dataset);
        _recyclerView.setAdapter(_adapter);
        _recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        _recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, _recyclerView, new
                RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(MainActivity.this, "Single Click on position        :" + position,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Toast.makeText(MainActivity.this, "Long press on position :" + position,
                                Toast.LENGTH_LONG).show();
                    }
                }));

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] _dataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView _textView;

            public ViewHolder(View itemView) {
                super(itemView);
                _textView = itemView.findViewById(R.id.book_title);
            }
        }

        MyAdapter(String[] dataset) {
            _dataset = dataset;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder._textView.setText(_dataset[position]);
        }

        @Override
        public int getItemCount() {
            return _dataset.length;
        }
    }
}
