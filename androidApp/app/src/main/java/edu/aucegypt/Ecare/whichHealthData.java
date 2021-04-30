package edu.aucegypt.Ecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

public class whichHealthData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_which_health_data);
        final String[] uid = new String[1];
        homeAdapter myAdapter;
        final ArrayList<Item> homeList=new ArrayList<Item>();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        uid[0] = bundle.getString("uid");
        GridView grid = (GridView) findViewById(R.id.homeGrid);
        homeList.add(new Item("Heart Rate",R.drawable.heart, "Hi Mrs. Natasha, Please don't forget to take your daily vitamins"));
        homeList.add(new Item("Steps",R.drawable.ic_baseline_directions_walk_24, "Hi Mrs. Natasha, Please don't forget to take your daily vitamins"));
        homeList.add(new Item("Calories",R.drawable.fire, "Hi Mrs. Natasha, Please don't forget to take your daily vitamins"));
        homeList.add(new Item("Sleep",R.drawable.ic_baseline_single_bed_24, "Hi Mrs. Natasha, Please don't forget to take your daily vitamins"));
//        homeList.add(new Item("My Connections",R.drawable.friends));
//        homeList.add(new Item("Requests",R.drawable.notification));
//        homeList.add(new Item("Store",R.drawable.store));
//        homeList.add(new Item("Studio",R.drawable.studio2));
//        homeList.add(new Item("Interested Buyers",R.drawable.interestedbuyers));
//        homeList.add(new Item("E-Learning",R.drawable.learn2));
//        homeList.add(new Item("About",R.drawable.about, "Hi Mrs. Natasha, Please don't forget to take your daily vitamins"));
//        homeList.add(new Item("Settings",R.drawable.settings2));
//        homeList.add(new Item("Sign Out",R.drawable.signout, "Hi Mrs. Natasha, Please don't forget to take your daily vitamins"));

        myAdapter = new homeAdapter(this, R.layout.home_view_items, homeList);
        grid.setAdapter(myAdapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = homeList.get(position);
                if(item.getbuttonName().equals("Heart Rate")) {
                    Bundle bundle = getIntent().getExtras();
                    assert bundle != null;
                    uid[0] = bundle.getString("uid");
                    Intent i = new Intent(whichHealthData.this, history.class);
                    bundle = new Bundle();
                    bundle.putString("uid", uid[0]);
                    bundle.putString("data", "heart");
                    i.putExtras(bundle);
                    startActivity(i);
                }
                else if(item.getbuttonName().equals("Steps")) {
                    Bundle bundle = getIntent().getExtras();
                    assert bundle != null;
                    uid[0] = bundle.getString("uid");
                    Intent i = new Intent(whichHealthData.this, history.class);
                    bundle = new Bundle();
                    bundle.putString("uid", uid[0]);
                    bundle.putString("data", "steps");
                    i.putExtras(bundle);
                    startActivity(i);
                }
                else if(item.getbuttonName().equals("Calories")) {
                    Bundle bundle = getIntent().getExtras();
                    assert bundle != null;
                    uid[0] = bundle.getString("uid");
                    Intent i = new Intent(whichHealthData.this, history.class);
                    bundle = new Bundle();
                    bundle.putString("uid", uid[0]);
                    bundle.putString("data", "calories");
                    i.putExtras(bundle);
                    startActivity(i);
                }
                else if(item.getbuttonName().equals("Sleep")) {
                    Bundle bundle = getIntent().getExtras();
                    assert bundle != null;
                    uid[0] = bundle.getString("uid");
                    Intent i = new Intent(whichHealthData.this, history.class);
                    bundle = new Bundle();
                    bundle.putString("uid", uid[0]);
                    bundle.putString("data", "sleep");
                    i.putExtras(bundle);
                    startActivity(i);
                }
//                else if (item.getbuttonName().equals("Store")) {
//                    Intent intent = new Intent(getApplicationContext(), Inst_list.class);
//                    startActivity(intent);
//                }
//                else if (item.getbuttonName().equals("Search For Musicians")) {
//                    Intent intent = new Intent(getApplicationContext(), SearchConnections.class);
//                    startActivity(intent);
//                }
//                else if (item.getbuttonName().equals("Sign Out")) {
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(intent);
//                }
//                else if (item.getbuttonName().equals("My Connections")) {
//                    Intent intent = new Intent(getApplicationContext(), MyConnections.class);
//                    startActivity(intent);
//                }
//                else if (item.getbuttonName().equals("Requests")) {
//                    Intent intent = new Intent(getApplicationContext(), RequestsActivity.class);
//                    startActivity(intent);
//                }
//                else if (item.getbuttonName().equals("Interested Buyers")) {
//                    Intent intent = new Intent(getApplicationContext(), interested_users.class);
//                    startActivity(intent);
//                }
//                else if (item.getbuttonName().equals("About")){
//                    AlertDialog alert = new AlertDialog.Builder(home_page.this)
//                            .setTitle("Group number 1")
//                            .setMessage ("Names: Abdelrahman Rezk, Marwan El-Toukhy, Mohamed El-Naggar, Mohamed El-Shenawy")
//                            .setIcon(R.drawable.nosloganlogo)
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .create();
//                    alert.show();
//                }

            }
        });
//        historyAdapter myAdapter;
//        final String[] uid = new String[1];
//
//        final ArrayList<Item> list = new ArrayList<Item>();
//        ListView grid = (ListView) findViewById(R.id.list4);
//        list.add(new Item("Heart Rate"));
//        list.add(new Item("Steps"));
//        myAdapter = new historyAdapter(this, R.layout.activity_which_health_data, list);
//        grid.setAdapter(myAdapter);
//        Bundle bundle = getIntent().getExtras();
//        assert bundle != null;
//        uid[0] = bundle.getString("uid");
//        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Item item = list.get(position);
//                //if(item.getbuttonName().equals("Heart Rate")){
//                Bundle bundle = getIntent().getExtras();
//                assert bundle != null;
//                uid[0] = bundle.getString("uid");
//                Intent i = new Intent(whichHealthData.this, history.class);
//                bundle = new Bundle();
//                bundle.putString("uid", uid[0]);
//                i.putExtras(bundle);
//                startActivity(i);
//                //}
//            }
//        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}