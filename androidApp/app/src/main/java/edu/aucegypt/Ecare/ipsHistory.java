package edu.aucegypt.Ecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class ipsHistory extends AppCompatActivity {
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ips_history);
        ipsAdapter myAdapter;
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        uid = bundle.getString("uid");
        final ArrayList<Item> homeList=new ArrayList<Item>();

        GridView grid = (GridView) findViewById(R.id.ipsGrid);
        homeList.add(new Item("Kitchen",R.drawable.cutlery, " "));
        homeList.add(new Item("Bathroom",R.drawable.toilet, " "));

        myAdapter = new ipsAdapter(this, R.layout.ips_item, homeList);
        grid.setAdapter(myAdapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = homeList.get(position);
                if(item.getbuttonName().equals("Kitchen")) {
                    Intent intent = new Intent(getApplicationContext(), ipsDailyHistory.class).putExtra("uid", uid).putExtra("location", "kitchen");
                    startActivity(intent);
                }
                else if(item.getbuttonName().equals("Bathroom")) {
                    Intent intent = new Intent(getApplicationContext(), ipsDailyHistory.class).putExtra("uid", uid).putExtra("location", "bathroom");
                    startActivity(intent);
                }
            }
        });
    }
}