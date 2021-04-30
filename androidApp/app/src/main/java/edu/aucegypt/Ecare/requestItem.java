package edu.aucegypt.Ecare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class requestItem extends BaseAdapter {
    Context context;
    String animals;
    LayoutInflater inflter;

    public requestItem(Context applicationContext, String animals) {
        this.context = applicationContext;
        this.animals = animals;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_request_item, null);
        TextView textView = (TextView) view.findViewById(R.id.name);
        textView.setText(animals);
        return view;
    }
}


//        ArrayList<Item> buttonList = new ArrayList<Item>();
//
//        public requestItem(Context context, int textViewResourceId, ArrayList objects) {
//            super(context, textViewResourceId, objects);
//            buttonList = objects;
//        }
//
//        @Override
//        public int getCount() {
//            return super.getCount();
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//
//            View v = convertView;
//            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = inflater.inflate(R.layout.activity_request_item, null);
//            TextView textView = (TextView) v.findViewById(R.id.name);
//            textView.setText(buttonList.get(position).getbuttonUid());
//            return v;
//
//        }
//    }

