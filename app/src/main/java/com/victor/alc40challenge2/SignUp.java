package com.victor.alc40challenge2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText mEmailEditText, mPasswordEditText;
    private Button mSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");

        mEmailEditText = findViewById(R.id.email_sign_up_field);
        mPasswordEditText = findViewById(R.id.pass_sign_up_field);
        mSignUpBtn = findViewById(R.id.email_btn_sign_up);

        mAuth = FirebaseAuth.getInstance();

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(mEmailEditText.getText().toString().trim(),
                        mPasswordEditText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this, DisplayTravels.class));
                        } else {
                            Toast.makeText(SignUp.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
