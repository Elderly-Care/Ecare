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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class signupCaregiver extends AppCompatActivity {
    private static final String TAG = "AuthenticatedAddition";
    FirebaseUser user;
    Button signUp ;
    Button clickHere ;
    FirebaseAuth fAuth;
    EditText name  ;
    EditText email ;
    EditText password ;
    EditText number ;
    EditText status ;
    String uName ;
    String uEmail ;
    String uPassword ;
    String uNumber ;
    String uStatus ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_caregiver);
        signUp = (Button) findViewById(R.id.signup);
        clickHere = (Button) findViewById(R.id.clickhere);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        number = findViewById(R.id.PhoneNumber);
        status = findViewById(R.id.status);


        // countrySpinner.setOnItemSelectedListener(this);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uName = name.getText().toString();  //uName has the name
                uEmail = email.getText().toString().trim(); // uEmail has the email
                uPassword = password.getText().toString().trim(); // uPassword has the Password
                uNumber = number.getText().toString().trim(); // uNumber has the Number
                uStatus = status.getText().toString().trim(); // uNumber has the Number

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
//                    Thread.sleep(5000);
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
                            user.put("email", uEmail);
                            user.put("name", uName);
                            user.put("date", new java.util.Date());
                            user.put("phone", uNumber);
                            user.put("registerType", "Caregiver");
                            user.put("relation", uStatus);

                            db.collection("users")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .set(user);
                            Intent intent = new Intent(signupCaregiver.this, login.class);
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(signupCaregiver.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });



    }


    public void openMainActivity() {
        Intent intent = new Intent(signupCaregiver.this, login.class);
        startActivity(intent);
    }


}

