package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class patientOutsideProfile extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    TextView userName;
    final ArrayList<String> items = new ArrayList<>();
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_outside_profile);
        userName = findViewById(R.id.userName2);
        listenForUsers();

    }

    public void listenForUsers() {
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        email = bundle.getString("email");
        final Button add = findViewById(R.id.add);

        Query docRef = db.collection("users").whereEqualTo("email", email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            JSONObject userDetail = new JSONObject(Objects.requireNonNull(document.getData()));
                            try {
                                userName.setText(userDetail.getString("name"));
                                items.add("email: " + userDetail.getString("email"));
                                items.add("age: " + userDetail.getString("age"));
                                items.add("gender: " + userDetail.getString("gender"));
                                items.add("weight: " + userDetail.getString("weight"));
                                items.add("height: " + userDetail.getString("height"));
                                items.add("health condition: " + userDetail.getString("healthCondition"));
                                addPatient(userDetail.getString("uid"));
                                System.out.println(items);
                                ArrayAdapter itemsAdapter = new ArrayAdapter<String>(patientOutsideProfile.this, R.layout.patient_details, items);
                                ListView listView = (ListView) findViewById(R.id.list2);
                                listView.setAdapter(itemsAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                }else{
                        Log.d(TAG, "get failed with ", task.getException());
                    }
            }
        });
    }

    public void addPatient(final String uid){
        final Button add = findViewById(R.id.add);
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String[] name = new String[1];
        final Map<String, Object> request = new HashMap<>();
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                request.put("requestUid",mAuth.getCurrentUser().getUid());
                request.put("status", 0);
                db.collection("users")
                        .document(uid)
                        .collection("requests").document(mAuth.getCurrentUser().getUid())
                        .set(request);
                Toast.makeText(patientOutsideProfile.this, "Request Sent!", Toast.LENGTH_LONG).show();
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