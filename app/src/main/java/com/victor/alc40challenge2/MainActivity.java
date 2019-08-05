package com.victor.alc40challenge2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button mButtonSignIn;
    private Button mButtonGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.app_display_app);

        mButtonSignIn = findViewById(R.id.button_sign_with_e_and_p);
        mButtonGoogle = findViewById(R.id.btn_sign_in_with_google);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "No Auth", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, mAuth.toString(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, DisplayTravels.class));
        }

        mButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GoogleSignIn.class));
            }
        });

        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignIn.class));
            }
        });
    }
}
