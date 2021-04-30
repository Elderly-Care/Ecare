package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.inappmessaging.FirebaseInAppMessagingClickListener;
import com.google.firebase.inappmessaging.model.Action;
import com.google.firebase.inappmessaging.model.CampaignMetadata;
import com.google.firebase.inappmessaging.model.InAppMessage;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.ml.modeldownloader.CustomModel;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;
import com.pusher.pushnotifications.PushNotifications;


import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "123";
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    TextView userName;
    String registerType;
    userAdapter myAdapter;
    private static final int RC_SIGN_IN = 123;
//    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userName = findViewById(R.id.userName);
        listenForUsers();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Map<String, Object> tokenMap = new HashMap<>();
                        tokenMap.put("token", token);
                        db.collection("FCMtoken")
                                .document("1")
                                .set(tokenMap);

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        PushNotifications.start(getApplicationContext(), "661a95d4-7499-47b3-9e3f-5a03827fad36");
        PushNotifications.addDeviceInterest("hello");



    }

    public void popUp(ArrayList<String> homeList){
//            db.collection("users/TzV04aBQlhTn8CN7w07Ub1p9k7G2")
//                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable QuerySnapshot value,
//                                            @Nullable FirebaseFirestoreException e) {
//                            if (e != null) {
//                                Log.w(TAG, "Listen failed.", e);
//                                return;
//                            }
//                            assert value != null;
//                            for (DocumentChange doc : value.getDocumentChanges()) {
//                                if (doc.getType() == DocumentChange.Type.ADDED) {
//                                    Long fell = doc.getDocument().getLong("fell");
//                                    if (fell == 1)
//                                        startActivity(new Intent(MainActivity.this, notification.class));
//                                }
//                                if (doc.getType() == DocumentChange.Type.REMOVED) {
//                                    Long fell = doc.getDocument().getLong("fell");
//                                    if (fell == 1)
//                                        startActivity(new Intent(MainActivity.this, notification.class));
//                                }
//                                if (doc.getType() == DocumentChange.Type.MODIFIED) {
//                                    Long fell = doc.getDocument().getLong("fell");
//                                    if (fell == 1)
//                                        startActivity(new Intent(MainActivity.this, notification.class));
//                                }
//                            }
//                        }
//                    });
        final DocumentReference docRef = db.collection("users").document("TzV04aBQlhTn8CN7w07Ub1p9k7G2");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    if(snapshot.getLong("fell") == 1){
//                        if(counter == 0)
                            startActivity(new Intent(MainActivity.this, notification.class));
//                        counter = 1;
                    }
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    public void getRequest(){
        final ArrayList<String> homeList = new ArrayList<>();
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        db.collection("users/" + mAuth.getCurrentUser().getUid() +"/requests")
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
                                Long stat = doc.getDocument().getLong("status");
                                String requid = doc.getDocument().getString("requestUid");
                                if(stat == 1)
                                    homeList.add(requid);
                            }
                            if (doc.getType() == DocumentChange.Type.REMOVED) {
                                Long stat = doc.getDocument().getLong("status");
                                String requid = doc.getDocument().getString("requestUid");
                                if(stat == 1)
                                    homeList.add(requid);
                            }
                            if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                Long stat = doc.getDocument().getLong("status");
                                String requid = doc.getDocument().getString("requestUid");
                                if(stat == 1)
                                    homeList.add(requid);
                                else
                                    homeList.remove(requid);
                            }
                        }
                        getUser(homeList);
                        popUp(homeList);
//                    ListView l1=(ListView)findViewById(R.id.requestList);
//                    l1.setAdapter(new dataListAdapter(items));
                    }
                });
    }
    public void getUser(ArrayList<String> uid){
        final ArrayList<String> items = new ArrayList<>();
        final ArrayList<String> Uid = new ArrayList<>();
        final ArrayList<Item> list = new ArrayList<>();
        final ListView grid = (ListView) findViewById(R.id.caregivers);
//        setContentView(R.layout.activity_request_item);
        for(String uids : uid) {
            Query docRef = db.collection("users").whereEqualTo("uid", uids);
            docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                JSONObject userDetail = new JSONObject(Objects.requireNonNull(document.getData()));
                                try {
                                    list.add(new Item(userDetail.getString("name"), userDetail.getString("email"), R.drawable.profile));
//                                    items.add(userDetail.getString("name"));
//                                    Uid.add(userDetail.getString("uid"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Log.d(TAG, "DocumentSnapshot data: " + userDetail.getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        }
//                        ListView l1 = (ListView) findViewById(R.id.caregivers);
//                        l1.setAdapter(new myAdapter(items, Uid));
                        myAdapter = new userAdapter(MainActivity.this, R.layout.activity_main, list);
                        grid.setAdapter(myAdapter);
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            if(registerType.equals("Caregiver")) {
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Item item = list.get(position);
                        String email = item.getbuttonEmail();
                        Intent i = new Intent(MainActivity.this, patientProfile.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("email", email);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });
            }
            else{
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Item item = list.get(position);
                        String email = item.getbuttonEmail();
                        Intent i = new Intent(MainActivity.this, userProfile.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("email", email);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });
            }
        }
    }

    public void listenForUsers() {
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        final FloatingActionButton fab2 = findViewById(R.id.floatingActionButton3);
        final Button emergencyButton = findViewById(R.id.emergencyButton);
        //String text = "View Profile";
        final TextView textView = (TextView)findViewById(R.id.button2);
        //textView.setOnClickListener(new View.OnClickListener());
        //final Button button = findViewById(R.id.button2);
        final Button edit = findViewById(R.id.button3);
        fab.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.INVISIBLE);


        emergencyButton.setVisibility(View.INVISIBLE);
        final DocumentReference docRef = db.collection("users").document(String.valueOf(mAuth.getCurrentUser().getUid()));
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot document,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (document != null && document.exists()) {
                    JSONObject userDetail = new JSONObject(Objects.requireNonNull(document.getData()));
                    try {
                        userName.setText(userDetail.getString("name"));
                        registerType = userDetail.getString("registerType");
                        getRequest();
                        if(registerType.equals("Caregiver")) {
                            fab.setVisibility(View.VISIBLE);
                            fab.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(MainActivity.this, addPatient.class);
                                    startActivity(i);
                                }
                            });
                            textView.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent(MainActivity.this, caregiverOutsideProfile.class);
                                    startActivity(intent);
                                }
                            });
                            edit.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent(MainActivity.this, editCaregiver.class);
                                    startActivity(intent);
                                }
                            });
                        }
                        else {
                            emergencyButton.setVisibility(View.VISIBLE);
                            emergencyButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(MainActivity.this, notification.class));
                                }
                            });
                            fab.setVisibility(View.GONE);
                            textView.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent(MainActivity.this, patientProfile.class).putExtra("email",mAuth.getCurrentUser().getEmail());
                                    startActivity(intent);
                                }
                            });
                            fab2.setVisibility(View.VISIBLE);
                            fab2.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent(MainActivity.this, requests.class);
                                    startActivity(intent);
                                }
                            });
                            edit.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent(MainActivity.this, editPatient.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    Log.d(TAG, "Current data: " + document.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }

                }
        });
    }
}
