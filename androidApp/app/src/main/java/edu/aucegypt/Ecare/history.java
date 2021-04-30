package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class history extends AppCompatActivity {
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    TextView userName;
    final ArrayList<String> items = new ArrayList<>();
    final ArrayList<String> days = new ArrayList<String>();
    final ArrayList<Integer> avg = new ArrayList<Integer>();
    String uid;
    String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        uid = bundle.getString("uid");
        data = bundle.getString("data");
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
                        switch (data) {
                            case "heart":
                                userDetail = new JSONObject((Map) Objects.requireNonNull(document.get("heartRate")));
                                break;
                            case "steps":
                                userDetail = new JSONObject((Map) Objects.requireNonNull(document.get("steps")));
                                break;
                            case "sleep":
                                userDetail = new JSONObject((Map) Objects.requireNonNull(document.get("sleep")));
                                break;
                            default:
                                userDetail = new JSONObject((Map) Objects.requireNonNull(document.get("calories")));
                                break;
                        }
                        JSONArray key = userDetail.names();
                        assert key != null;
                        for (int i = 0; i < key.length(); ++i) {
                            String keys = null;
                            try {
                                keys = key.getString(i);

                                days.add(keys);
                                int value = userDetail.getInt(keys);
                                if(data.equals("sleep"))
                                    value = value/60;
                                avg.add(value);
                                items.add(keys + ":       " + value);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(history.this, R.layout.patient_details,items);
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
}