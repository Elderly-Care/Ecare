package edu.aucegypt.Ecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class reportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        reportAdapter myAdapter;
        final String[] uid = new String[1];

        final ArrayList<Item> list = new ArrayList<Item>();
        ListView grid = (ListView) findViewById(R.id.histList);

        list.add(new Item("Week History"));
        list.add(new Item("Month History"));
        list.add(new Item("Year History"));

        myAdapter = new reportAdapter(this, R.layout.report_items, list);
        grid.setAdapter(myAdapter);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        uid[0] = bundle.getString("uid");
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = list.get(position);
                    Bundle bundle = getIntent().getExtras();
                    assert bundle != null;
                    uid[0] = bundle.getString("uid");
                    Intent i = new Intent(reportActivity.this, history.class);
                    bundle = new Bundle();
                    bundle.putString("uid", uid[0]);
                    i.putExtras(bundle);
                    startActivity(i);
            }
        });

    }
}