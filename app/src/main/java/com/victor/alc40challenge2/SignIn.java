package com.victor.alc40challenge2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText mEmailEditText, mPasswordEditText;
    private Button mSignInBtn;
    private TextView mSwitchTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setTitle("Sign In");

        mAuth = FirebaseAuth.getInstance();

        mEmailEditText = findViewById(R.id.email_sign_in_field);
        mPasswordEditText = findViewById(R.id.password_sign_in_field);
        mSignInBtn = findViewById(R.id.email_sign_in_button);
        mSwitchTextView = findViewById(R.id.sign_up_switch_btn);

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(mEmailEditText.getText().toString().trim(),
                        mPasswordEditText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignIn.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignIn.this, DisplayTravels.class));
                        } else {
                            Toast.makeText(SignIn.this, "Sign In Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mSwitchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });
    }
}
