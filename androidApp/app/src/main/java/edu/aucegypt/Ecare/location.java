package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class location extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        final FirebaseAuth mAuth;
        final TextView loc = (TextView)findViewById(R.id.location1);
        final String uid;
        final Button button = findViewById(R.id.hist);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        uid = bundle.getString("uid");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ipsHistory.class).putExtra("uid",uid);;
                startActivity(intent);
            }
        });
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                DataSnapshot st1 = dataSnapshot.child(uid).child("Location").child("Kitchen").child("Value");
                DataSnapshot st2 = dataSnapshot.child(uid).child("Location").child("Bathroom").child("Value");
                if(String.valueOf(st1.getValue()).equals("1")){
                    loc.setText("Kitchen");
                }
                else if(String.valueOf(st2.getValue()).equals("1")){
                    loc.setText("Bathroom");
                }
                else{
                    loc.setText("Unknown Location");
                }

//                assert post != null;
                Log.e("firebase", String.valueOf(st1));
                Log.e("firebase", String.valueOf(st2));
//                Log.e("firebase", post.kitchen);
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("firebase", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}