package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
import java.util.Map;
import java.util.Objects;

public class healthData extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    final ArrayList<String> days = new ArrayList<String>();
    final ArrayList<Double> avg = new ArrayList<Double>();
    GraphView graphView;
    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_data);
        final ArrayList<Item> list = new ArrayList<Item>();
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final BarChart barChart = (BarChart) findViewById(R.id.barchart1);
        final BarChart barChart2 = (BarChart) findViewById(R.id.barchart2);
        final BarChart barChart3 = (BarChart) findViewById(R.id.barchart3);
        final ArrayList<BarEntry> entries = new ArrayList<>();
        final BarDataSet bardataset = new BarDataSet(entries, "Cells");

        final ArrayList<String> labels = new ArrayList<String>();
        db.collection("users").document(String.valueOf(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            JSONObject userDetail = new JSONObject((Map) Objects.requireNonNull(document.get("weekSteps")));
                            JSONArray key = userDetail.names();
                            assert key != null;
                            for(int i = 0; i < key.length(); ++i){
                                String keys = null;
                                try {
                                    keys = key.getString(i);
                                    days.add(keys);
                                    Double value = userDetail.getDouble(keys);
                                    avg.add(value);
                                    entries.add(new BarEntry(i,value.floatValue()));
                                    labels.add(keys);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            BarDataSet barDataSet = new BarDataSet(entries, "Calories");
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
                            xAxis = barChart2.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

                            barDataSet = new BarDataSet(entries, "Sleep");
                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(16f);
                            barData = new BarData(barDataSet);
                            barChart2.setFitBars(true);
                            barChart2.setData(barData);
                            barChart2.getDescription().setText("Bar Chart Heart Rate");
                            barChart2.animateY(2000);

                            barChart2.animateY(5000);

                            barDataSet = new BarDataSet(entries, "Step Count");
                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(16f);
                            xAxis = barChart3.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

                            barData = new BarData(barDataSet);
                            barChart3.setFitBars(true);
                            barChart3.setData(barData);
                            barChart3.getDescription().setText("Bar Chart Heart Rate");
                            barChart3.animateY(2000);

                            barChart3.animateY(5000);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}