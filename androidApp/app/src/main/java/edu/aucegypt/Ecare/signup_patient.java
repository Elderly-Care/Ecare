package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;
//import org.json.JSONObject;
//import org.json.JSONObject;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class signup_patient extends AppCompatActivity {
    private static final String TAG = "AuthenticatedAddition";
    FirebaseUser user;
    Button signUp ;
    Button clickHere ;
    FirebaseAuth fAuth;
    EditText name  ;
    EditText email ;
    EditText password ;
    EditText number ;
    EditText age ;
    EditText gender ;
    EditText weight ;
    EditText height ;
    EditText health ;
    EditText date ;
    String uName ;
    String uEmail ;
    String uPassword ;
    String uNumber ;
    String uAge ;
    String uGender ;
    String uWeight ;
    String uHeight ;
    String uHealth ;
    String uDate ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_patient);
        signUp = (Button) findViewById(R.id.signup);
        clickHere = (Button) findViewById(R.id.clickhere);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        number = findViewById(R.id.PhoneNumber);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        health = findViewById(R.id.health);
        date = findViewById(R.id.date);


        // countrySpinner.setOnItemSelectedListener(this);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uName = name.getText().toString();  //uName has the name
                uEmail = email.getText().toString().trim(); // uEmail has the email
                uPassword = password.getText().toString().trim(); // uPassword has the Password
                uNumber = number.getText().toString().trim(); // uNumber has the Number
                uAge = age.getText().toString().trim(); // uNumber has the Number
                uGender = gender.getText().toString().trim(); // uNumber has the Number
                uWeight = weight.getText().toString().trim(); // uNumber has the Number
                uHeight = height.getText().toString().trim(); // uNumber has the Number
                uDate = date.getText().toString().trim(); // uNumber has the Number
                uHealth = health.getText().toString().trim(); // uNumber has the Number

                user = FirebaseAuth.getInstance().getCurrentUser();

                if (TextUtils.isEmpty(uName)) {
                    name.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(uEmail)) {
                    email.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(uPassword)) {
                    password.setError("Password is required");
                    return;
                }
                if (uPassword.length() < 6) {
                    password.setError("Password must be atleast 6 characters");
                    return;
                }
                if (TextUtils.isEmpty(uNumber)) {
                    number.setError("Phone number is required");
                    return;
                }

                getUserDetails();
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
            }

        });


        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();

            }
        });
    }
    public void getUserDetails() {
        final FirebaseAuth mAuth;
        final FirebaseUser[] user1 = new FirebaseUser[1];
//        Date date = new Date(70, 1, 1);
//        final float diff = date.getTime();
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(uEmail, uPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Map<String, Object> user = new HashMap<>();
                            user.put("uid", mAuth.getCurrentUser().getUid());
                            user.put("name", uName);
                            user.put("email", uEmail);
                            user.put("date", new java.util.Date());
                            user.put("phone", uNumber);
                            user.put("registerType", "Patient");
                            user.put("weight", uWeight);
                            user.put("height", uHeight);
                            user.put("age", uAge);
                            user.put("dateOfBirth", uDate);
                            user.put("gender", uGender);
                            user.put("healthCondition", uHealth);

                            db.collection("users")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .set(user);
                            Intent intent = new Intent(signup_patient.this, login.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(signup_patient.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



    public void openMainActivity() {
        Intent intent = new Intent(signup_patient.this, login.class);
        startActivity(intent);
    }


}