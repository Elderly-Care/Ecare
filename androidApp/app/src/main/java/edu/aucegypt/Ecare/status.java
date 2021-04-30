package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class status extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    final ArrayList<String> days = new ArrayList<String>();
//    final ArrayList<Double> avg = new ArrayList<Double>();
    GraphView graphView;
    LineGraphSeries<DataPoint> series;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        final ArrayList<Item> list = new ArrayList<Item>();
        statusAdapter myAdapter;
        final String[] uid = new String[1];
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final BarChart barChart = (BarChart) findViewById(R.id.barchart);
        final ArrayList<BarEntry> entries = new ArrayList<>();
        final Bundle[] bundle = {getIntent().getExtras()};
        assert bundle[0] != null;
        uid[0] = bundle[0].getString("uid");
        final BarDataSet bardataset = new BarDataSet(entries, "Cells");

//        final ArrayList<String> labels = new ArrayList<String>();

        ListView grid = (ListView) findViewById(R.id.list);
        list.add(new Item("Check Location",R.drawable.location));
        list.add(new Item("Generate a report",R.drawable.report));
        //list.add(new Item("View Health Data",R.drawable.ic_baseline_data_usage_24));

        myAdapter = new statusAdapter(this, R.layout.status_item, list);
        grid.setAdapter(myAdapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Item item = list.get(position);
                if(item.getbuttonName().equals("Check Location")) {
                    Intent i = new Intent(status.this, location.class);
                    bundle[0] = new Bundle();
                    bundle[0].putString("uid", uid[0]);
                    i.putExtras(bundle[0]);
                    startActivity(i);
                }
                if(item.getbuttonName().equals("Generate a report")){
                    Intent i = new Intent(status.this, whichHealthData.class);
                    bundle[0] = new Bundle();
                    bundle[0].putString("uid", uid[0]);
                    i.putExtras(bundle[0]);
                    startActivity(i);
                }
//                if(item.getbuttonName().equals("View Health Data")){
//
//                    Intent i = new Intent(status.this, healthData.class);
//                    bundle[0] = new Bundle();
//                    bundle[0].putString("uid", uid[0]);
//                    i.putExtras(bundle[0]);
//                    startActivity(i);
//                }
        }
        });

        db.collection("users").document(Objects.requireNonNull(uid[0]))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            JSONObject userDetail = new JSONObject((Map) Objects.requireNonNull(document.get("hourlyHR")));
                            JSONObject newuser;
                            JSONArray key = userDetail.names();
                            assert key != null;  
                            for(int i = 0; i < key.length(); ++i){
                                String keys = null;
                                try {
                                    keys = key.getString(i);
                                    days.add(keys);
                                    double value = userDetail.getDouble(keys);
//                                    avg.add(value);
                                    entries.add(new BarEntry(i, (float) value));
//                                    labels.add(keys);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            BarDataSet barDataSet = new BarDataSet(entries, "Heart Rate");
                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(16f);
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

                            BarData barData = new BarData(barDataSet);
                            barChart.setFitBars(true);
                            barChart.setData(barData);
                            barChart.getDescription().setText("Bar Chart Heart Rate");
                            barChart.animateY(2000);

                            barChart.animateY(5000);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}