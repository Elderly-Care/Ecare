package edu.aucegypt.Ecare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class reportAdapter extends ArrayAdapter {
    ArrayList<Item> buttonList = new ArrayList<Item>();
    private reportActivity context;

    public reportAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        buttonList = objects;
        this.context = (reportActivity) context;
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
            v = inflater.inflate(R.layout.report_items, null, true);
        TextView textView = (TextView) v.findViewById(R.id.histName);
        textView.setText(buttonList.get(position).getbuttonUid());
        return v;

    }
}


