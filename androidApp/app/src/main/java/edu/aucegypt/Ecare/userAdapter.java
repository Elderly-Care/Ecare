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

public class userAdapter extends ArrayAdapter {
    ArrayList<Item> buttonList = new ArrayList<Item>();

    public userAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        buttonList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.user_item, null);
        TextView textView = (TextView) v.findViewById(R.id.textView2);
        TextView textView2 = (TextView) v.findViewById(R.id.email2);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView4);
        textView.setText(buttonList.get(position).getbuttonName());
        textView2.setText(buttonList.get(position).getbuttonEmail());
        imageView.setImageResource(buttonList.get(position).getbuttonImage());
        return v;
//        View v = convertView;
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        v = inflater.inflate(R.layout.home_view_items, null);
//        TextView textView = (TextView) v.findViewById(R.id.textView);
//        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
//        textView.setText(buttonList.get(position).getbuttonName());
//        imageView.setImageResource(buttonList.get(position).getbuttonImage());
//        return v;

    }
}