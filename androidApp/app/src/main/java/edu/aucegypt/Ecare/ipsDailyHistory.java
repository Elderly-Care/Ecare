package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ipsDailyHistory extends AppCompatActivity {
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    TextView userName;
    final ArrayList<String> items = new ArrayList<>();
    final ArrayList<String> days = new ArrayList<String>();
    final ArrayList<Double> avg = new ArrayList<Double>();
    String uid;
    String loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ips_daily_history);
        ipsAdapter myAdapter;
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        uid = bundle.getString("uid");
        loc = bundle.getString("location");
        listenForUsers();
    }

    public void listenForUsers() {
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        DocumentReference docRef = db.collection("history").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        JSONObject userDetail;
                        if (loc.equals("kitchen")) {
                            userDetail = new JSONObject((Map) Objects.requireNonNull(document.get("Kitchen")));
                        } else {
                            userDetail = new JSONObject((Map) Objects.requireNonNull(document.get("Bathroom")));
                        }
                        JSONArray key = userDetail.names();
                        assert key != null;
                        for (int i = 0; i < key.length(); ++i) {
                            String keys = null;
                            try {
                                keys = key.getString(i);

                                days.add(keys);
                                JSONObject value = (JSONObject) userDetail.get(keys);
                                Object num = value.get("Num_Entries");
                                double time = value.getDouble("Total_Time");
                                time = time / 60;
                                double roundedTwoDigitsX = Math.round(time * 10) / 10.0;
                                //JSONArray key2 = (JSONArray) value.keys();
//                                Object value2 = userDetail.get(keys);
                                //avg.add(value);
                                items.add(keys + "        " + num + "                     " + roundedTwoDigitsX);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(ipsDailyHistory.this, R.layout.patient_details, items);
                            ListView listView = (ListView) findViewById(R.id.list3);
                            listView.setAdapter(itemsAdapter);
                        }
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}