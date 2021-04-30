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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.security.Provider;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class login extends AppCompatActivity {

    EditText uEmail, uPassword;
    Button uLogin, uSignup;
    TextView messageofLogin;
    // ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uEmail = findViewById(R.id.email);
        uPassword = findViewById(R.id.password);
        fAuth= FirebaseAuth.getInstance();
        //   progressBar = findViewById(R.id.progressBar);
        uSignup = findViewById(R.id.signup);
        uLogin= findViewById(R.id.login);
        messageofLogin=(TextView) findViewById(R.id.messageofLogin);

        uSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();

            }
        });

        uLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = uEmail.getText().toString().trim();
                String password = uPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    uEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    uPassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    uPassword.setError("Password must be atleast 6 characters");
                    return;
                }
                // progressBar.setVisibility(View.GONE);

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(login.this, "Logged in Successfully", Toast.LENGTH_LONG).show();
                            messageofLogin.setText("Login Successful!");
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(login.this, "Error!!" + task.getException().getMessage(), Toast.LENGTH_LONG);
                            messageofLogin.setText("The details you entered does not match any account ");
                            //   progressBar.setVisibility(View.GONE);
                        }

                    }


                });

            }
        });
    }

    private void openSignUpActivity() {
        Intent intent= new Intent(login.this, caregiverOrDoctor.class);
        startActivity(intent);

    }
}