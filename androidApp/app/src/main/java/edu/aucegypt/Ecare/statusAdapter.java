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

public class statusAdapter extends ArrayAdapter {
    ArrayList<Item> buttonList = new ArrayList<Item>();
    private status context;

    public statusAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        buttonList = objects;
        this.context = (status) context;
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
        if(convertView==null)
            v = inflater.inflate(R.layout.status_item, null, true);
        TextView textView = (TextView) v.findViewById(R.id.imageName);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageButton);
        textView.setText(buttonList.get(position).getbuttonName());
        imageView.setImageResource(buttonList.get(position).getbuttonImage());
        return v;
    }
}
