package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class editCaregiver extends AppCompatActivity {

    TextView userName;
    TextView userImg;
    EditText status;
    EditText phone;
    String uStatus;
    String uPhone;
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    public static final int GET_FROM_GALLERY = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_caregiver);
        final Button edit = findViewById(R.id.change_pp);
        final Button submit = findViewById(R.id.submit);
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        userName = findViewById(R.id.userName2);
        status = findViewById(R.id.edit_status);
        phone = findViewById(R.id.edit_phone_number);
        DocumentReference docRef = db.collection("users").document(String.valueOf(mAuth.getCurrentUser().getUid()));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        JSONObject userDetail = new JSONObject(document.getData());
                        try {
                            userName.setText(userDetail.getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
                        ),
                        GET_FROM_GALLERY
                );
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uStatus = status.getText().toString();  //uName has the name
                uPhone = phone.getText().toString();  //uName has the name
                edit_status();
            }
        });
    }

    public void edit_status() {
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        DocumentReference ref = db.collection("users").document(String.valueOf(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()));
        ref
                .update("status", uStatus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        edit_phone();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public void edit_phone() {
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        DocumentReference ref = db.collection("users").document(String.valueOf(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()));
        ref
                .update("phone", uPhone)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Intent intent = new Intent(editCaregiver.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
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