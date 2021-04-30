package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class caregiverOutsideProfile extends AppCompatActivity {
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    TextView userName;
    final ArrayList<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_outside_profile);
        userName = findViewById(R.id.userName2);
        listenForUsers();
    }

    public void listenForUsers() {
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        DocumentReference docRef = db.collection("users").document(String.valueOf(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        JSONObject userDetail = new JSONObject(Objects.requireNonNull(document.getData()));
                        try {
                            userName.setText(userDetail.getString("name"));
                            items.add("email: " + userDetail.getString("email"));
                            items.add("Phone Number: " + userDetail.getString("phone"));
                            items.add("Status: " + userDetail.getString("relation"));
                            System.out.println(items);
                            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(caregiverOutsideProfile.this, R.layout.patient_details,items);
                            ListView listView = (ListView) findViewById(R.id.list2);
                            listView.setAdapter(itemsAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}