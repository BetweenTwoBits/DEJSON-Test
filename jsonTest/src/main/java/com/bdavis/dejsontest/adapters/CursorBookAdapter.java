package com.bdavis.dejsontest.adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.bdavis.dejsontest.data.DatabaseHandler;
import com.bdavis.dejsontest.jsontest.R;
import com.koushikdutta.ion.Ion;

public class CursorBookAdapter extends SimpleCursorAdapter {

    private Context context;
    private ViewHolder holder;
    private DatabaseHandler dbHandler = new DatabaseHandler(context);
    private int layout;

    public CursorBookAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);

        holder = new ViewHolder();
        holder.title = (TextView) v.findViewById(R.id.title);
        holder.author = (TextView) v.findViewById(R.id.author);
        holder.cover = (ImageView) v.findViewById(R.id.cover);

        v.setTag(holder);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        holder = (ViewHolder) view.getTag();

        int titleCol = cursor.getColumnIndex(dbHandler.getColumnNameBookTitle());
        String title = cursor.getString(titleCol);

        int authorCol = cursor.getColumnIndex(dbHandler.getColumnNameBookAuthor());
        String author = cursor.getString(authorCol);

        int imageUrlCol = cursor.getColumnIndex(dbHandler.getColumnNameBookImageUrl());
        String imageURL = cursor.getString(imageUrlCol);



        holder.title.setText(title);

        if(!TextUtils.isEmpty(author)) {
            holder.author.setText("Author: " + author);
            holder.author.setVisibility(View.VISIBLE);
        } else {
            holder.author.setText("");
            holder.author.setVisibility(View.GONE);
        }


        Ion.with(context)
                .load(imageURL)
                .withBitmap()
                .placeholder(R.drawable.ic_launcher)
                .intoImageView(holder.cover);
    }

    private static class ViewHolder {
        public TextView title;
        public ImageView cover;
        public TextView author;
    }
}
