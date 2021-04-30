package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class userProfile extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    TextView userName;
    String email;
    EditText message;
    String uMessage;
    String name;
    String recName;
    String uidPat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userName = findViewById(R.id.userName);
        message = findViewById(R.id.editTextTextMultiLine);
        final ArrayList<Item> list = new ArrayList<Item>();
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        email = bundle.getString("email");
        final ImageButton post = findViewById(R.id.imageButton2);
        final Button button6 = findViewById(R.id.button6);
        final Button button7 = findViewById(R.id.button7);
        final Button button8 = findViewById(R.id.button8);
        postsAdapter myAdapter;
        listenForUsers();
        final Map<String, Object> map = new HashMap<>();
        post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uMessage = message.getText().toString();  //uName has the name
                map.put("uidSender", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                map.put("nameSender", name);
                map.put("nameReceiver", recName);
                map.put("message", uMessage);
                map.put("date", new java.util.Date());
                db.collection("users")
                        .document("TzV04aBQlhTn8CN7w07Ub1p9k7G2")
                        .collection("posts").document()
                        .set(map);
                message.setText("");
                Toast.makeText(userProfile.this, "Message Sent!", Toast.LENGTH_LONG).show();
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uMessage = message.getText().toString();  //uName has the name
                map.put("uidSender", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                map.put("nameSender", name);
                map.put("nameReceiver", recName);
                map.put("message", "I'm Feeling Better!");
                map.put("date", new java.util.Date());
                db.collection("users")
                        .document("TzV04aBQlhTn8CN7w07Ub1p9k7G2")
                        .collection("posts").document()
                        .set(map);
                message.setText("");
                Toast.makeText(userProfile.this, "Message Sent!", Toast.LENGTH_LONG).show();
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uMessage = message.getText().toString();  //uName has the name
                map.put("uidSender", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                map.put("nameSender", name);
                map.put("nameReceiver", recName);
                map.put("message", "I Took My Medicine!");
                map.put("date", new java.util.Date());
                db.collection("users")
                        .document("TzV04aBQlhTn8CN7w07Ub1p9k7G2")
                        .collection("posts").document()
                        .set(map);
                message.setText("");
                Toast.makeText(userProfile.this, "Message Sent!", Toast.LENGTH_LONG).show();
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uMessage = message.getText().toString();  //uName has the name
                map.put("uidSender", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                map.put("nameSender", name);
                map.put("nameReceiver", recName);
                map.put("message", "I'm Feeling Tired!");
                map.put("date", new java.util.Date());
                db.collection("users")
                        .document("TzV04aBQlhTn8CN7w07Ub1p9k7G2")
                        .collection("posts").document()
                        .set(map);
                message.setText("");
                Toast.makeText(userProfile.this, "Message Sent!", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void listenForUsers() {
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        Query docRef = db.collection("users").whereEqualTo("email",email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            JSONObject userDetail = new JSONObject(document.getData());
                            try {
                                userName.setText(userDetail.getString("name"));
                                recName = userDetail.getString("name");
                                uidPat = userDetail.getString("uid");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        docRef = db.collection("users").whereEqualTo("uid",mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            JSONObject userDetail = new JSONObject(document.getData());
                            try {
                                name = userDetail.getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
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