package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class requests extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    requestItem myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        final ArrayList<String> items = new ArrayList<>();

        Button decline = findViewById(R.id.decline);
        Button accept = findViewById(R.id.accept);

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
                                if(stat == 0)
                                    items.add(requid);
                        }
                        if (doc.getType() == DocumentChange.Type.REMOVED) {
                            Long stat = doc.getDocument().getLong("status");
                            String requid = doc.getDocument().getString("requestUid");
                            if(stat == 0)
                                items.add(requid);
                        }
                        if (doc.getType() == DocumentChange.Type.MODIFIED) {
                            Long stat = doc.getDocument().getLong("status");
                            String requid = doc.getDocument().getString("requestUid");
                            if(stat == 0)
                                items.add(requid);
                            else
                                items.remove(requid);
                        }
                    }
                    getUser(items);
//                    ListView l1=(ListView)findViewById(R.id.requestList);
//                    l1.setAdapter(new dataListAdapter(items));
            }
        });
    }

    public void getUser(ArrayList<String> uid){
        final ArrayList<String> items = new ArrayList<>();
        final ArrayList<String> Uid = new ArrayList<>();
//        setContentView(R.layout.activity_request_item);
        if(uid.size() == 0) {
            ListView l1 = (ListView) findViewById(R.id.requestList);
            l1.setAdapter(new dataListAdapter(items, Uid));
        }
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
                                    items.add(userDetail.getString("name"));
                                    Uid.add(userDetail.getString("uid"));
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
                        ListView l1 = (ListView) findViewById(R.id.requestList);
                        l1.setAdapter(new dataListAdapter(items, Uid));
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }

    class dataListAdapter extends BaseAdapter {
        ArrayList<String> Title;
        ArrayList<String> uid;

        dataListAdapter() {
            Title = null;
            uid = null;
        }

        public dataListAdapter(ArrayList<String> text, ArrayList<String> Uid) {
            Title = text;
            uid = Uid;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return Title.size();
        }

        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            final FirebaseAuth mAuth;
            mAuth = FirebaseAuth.getInstance();

            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.activity_request_item, parent, false);
            TextView title;
            Button decline = row.findViewById(R.id.decline);
            Button accept = row.findViewById(R.id.accept);
            title = (TextView) row.findViewById(R.id.name);
            final Map<String, Object> request = new HashMap<>();
            title.setText(Title.get(position));
            decline.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final CollectionReference ref = db.collection("users/" + mAuth.getCurrentUser().getUid() +"/requests");
                    ref.whereEqualTo("uid", uid.get(position))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Integer> map = new HashMap<>();
                                        map.put("status", 2);
                                        ref.document(uid.get(position)).set(map, SetOptions.merge());
                                        Log.d(TAG, "DocumentSnapshot successfully updated! uid: ");
                                    }
                                    else {
                                        Log.d(TAG, "DocumentSnapshot WAS NOT successfully updated!");
                                    }
                                }
                            });
                }
            });
            accept.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final CollectionReference ref = db.collection("users/" + mAuth.getCurrentUser().getUid() +"/requests");
                    final String uidDoc = uid.get(position);
                    ref.whereEqualTo("uid", uidDoc)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                            Map<String, Integer> map = new HashMap<>();
                                            map.put("status", 1);
                                            ref.document(uid.get(position)).set(map, SetOptions.merge());
                                            request.put("requestUid",mAuth.getCurrentUser().getUid());
                                            request.put("status", 1);
                                            db.collection("users")
                                                    .document(uidDoc)
                                                    .collection("requests").document(mAuth.getCurrentUser().getUid())
                                                    .set(request);
                                        Log.d(TAG, "DocumentSnapshot successfully updated! uid: ");
                                    }
                                    else {
                                        Log.d(TAG, "DocumentSnapshot WAS NOT successfully updated!");
                                    }
                                }
                            });
                }
            });

            return (row);
        }
    }

}