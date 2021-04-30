package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.google.firebase.database.ValueEventListener;

public class patientProfile extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    TextView userName;
    String uid;
    public static final int GET_FROM_GALLERY = 3;
    EditText message;
    String uMessage;
    String email;
    String name;
    String recName;
    String uidPat;
    String uidCare;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        final Button button = findViewById(R.id.button5);
        final FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        userName = findViewById(R.id.userName);
        message = findViewById(R.id.editTextTextMultiLine);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        email = bundle.getString("email");
        final ImageButton post = findViewById(R.id.imageButton2);
        listenForUsers();
        final Map<String, Object> map = new HashMap<>();
        //Log.d(TAG, uidPat);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Query docRef = db.collection("users").whereEqualTo("email", email);
                docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    JSONObject userDetail = new JSONObject(document.getData());
                                    try {
                                        Intent i = new Intent(patientProfile.this, status.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("uid", userDetail.getString("uid"));
                                        i.putExtras(bundle);
                                        startActivity(i);
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
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(
                        new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
                        ),
                        GET_FROM_GALLERY
                );
            }
        });
    }

    public void posts(){
        final postsAdapter myAdapter;
        final ArrayList<Item> list = new ArrayList<Item>();
        final FirebaseAuth mAuth;
//        LayoutInflater inflater = getLayoutInflater();
//        View postView = inflater.inflate(R.layout.posts, null);
        mAuth = FirebaseAuth.getInstance();
        final ImageButton post = findViewById(R.id.imageButton2);
        final Map<String, Object> map = new HashMap<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        final TextView textView =  (TextView) postView.findViewById(R.id.next);
//        textView.setVisibility(View.GONE);
        post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uMessage = message.getText().toString();  //uName has the name
                map.put("uidSender", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                map.put("nameSender", name);
                map.put("nameReceiver", recName);
                map.put("message", uMessage);
                map.put("date", new java.util.Date());
                db.collection("users")
                        .document(uidPat)
                        .collection("posts").document()
                        .set(map);
                message.setText("");
            }
        });
        ListView grid = (ListView) findViewById(R.id.list);

        myAdapter = new postsAdapter(this, R.layout.posts, list);
        grid.setAdapter(myAdapter);
        db.collection("users/TzV04aBQlhTn8CN7w07Ub1p9k7G2/posts").orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        assert value != null;
                        for (DocumentChange doc : value.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                String nameSender = doc.getDocument().getString("nameSender");
                                String nameReceiver = doc.getDocument().getString("nameReceiver");
                                String mess = doc.getDocument().getString("message");
                                assert nameReceiver != null;
                                if(nameReceiver.equals(nameSender)) {
                                    list.add(0,(new Item(nameSender, R.drawable.profile, mess)));
//                                    textView.setVisibility(View.GONE);
                                }
//                                JSONObject userDetail = new JSONObject((Map) Objects.requireNonNull(doc.getDocument()));
                                else {
                                    nameSender = nameSender + " > ";
                                    list.add(0, (new Item(nameSender, nameReceiver, R.drawable.profile, mess)));
                                    //textView.setVisibility(View.VISIBLE);
                                }
                                myAdapter.notifyDataSetChanged();
                                //Log.d(TAG, "DocumentSnapshot data: " + doc.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        }
                    }
                });

    }

    public void listenForUsers() {
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        db.collection("users").whereEqualTo("email", email)
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                assert value != null;
                    for (DocumentChange doc : value.getDocumentChanges())  {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String name2 = doc.getDocument().getString("name");
                            String uid2 = doc.getDocument().getString("uid");
                                userName.setText(name2);
                                recName = name2;
                                //name = userDetail.getString("name");
                                uidPat = uid2;
                                //posts();
                        }
                        if (doc.getType() == DocumentChange.Type.MODIFIED) {
                            String name2 = doc.getDocument().getString("name");
                            String uid2 = doc.getDocument().getString("uid");
                            userName.setText(name2);
                            recName = name2;
                            //name = userDetail.getString("name");
                            uidPat = uid2;
                            //posts();
                        }
                        if (doc.getType() == DocumentChange.Type.REMOVED) {
                            String name2 = doc.getDocument().getString("name");
                            String uid2 = doc.getDocument().getString("uid");
                            userName.setText(name2);
                            recName = name2;
                            //name = userDetail.getString("name");
                            uidPat = uid2;
                            //posts();
                        }
                        else {
                            Log.d(TAG, "No such document");
                        }
                    }
            }
        });
        db.collection("users").whereEqualTo("uid", mAuth.getCurrentUser().getUid())
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                assert value != null;
                for (DocumentChange doc : value.getDocumentChanges())  {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String name3 = doc.getDocument().getString("name");
                        name = name3;
                        posts();
                    }
                    if (doc.getType() == DocumentChange.Type.MODIFIED) {
                        String name3 = doc.getDocument().getString("name");
                        name = name3;
                        posts();
                    }
                    if (doc.getType() == DocumentChange.Type.REMOVED) {
                        String name3 = doc.getDocument().getString("name");
                        name = name3;
                        posts();
                    }
                    else {
                            Log.d(TAG, "No such document");
                        }
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