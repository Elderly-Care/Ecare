package edu.aucegypt.Ecare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class postsAdapter extends ArrayAdapter {
    ArrayList<Item> buttonList = new ArrayList<Item>();
    private patientProfile context;

    public postsAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        buttonList = objects;
        this.context = (patientProfile) context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
//        notifyDataSetChanged();
        if(convertView==null)
            v = inflater.inflate(R.layout.posts, null, true);
        TextView textView = (TextView) v.findViewById(R.id.userName);
        TextView textView2 = (TextView) v.findViewById(R.id.senderName);
        TextView post = (TextView) v.findViewById(R.id.post);
        ImageView imageView = (ImageView) v.findViewById(R.id.userImg);
        textView.setText(buttonList.get(position).getbuttonName());
        textView2.setText(buttonList.get(position).getbuttonRecName());
        post.setText(buttonList.get(position).getbuttonPost());
        imageView.setImageResource(buttonList.get(position).getbuttonImage());
        return v;
    }
}
