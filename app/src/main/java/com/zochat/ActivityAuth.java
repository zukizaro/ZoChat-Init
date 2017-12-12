package com.zochat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityAuth extends AppCompatActivity {


    private TextView tvRegister;
    private FirebaseAuth mAuth;
    private EditText etEmail, etPass;
    private Button btnDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        init();

        mAuth = FirebaseAuth.getInstance();


        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityAuth.this, ActivitySignUp.class);
                startActivity(intent);
                finish();

            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(etEmail.getText() + "", etPass.getText() + "");
            }
        });
    }

    private void signIn(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(ActivityAuth.this, "signInWithEmail:failed" + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(ActivityAuth.this, ActivityFriend.class);
                            startActivity(intent);
                            finish();
                        }
                        // ...
                    }
                });
    }

    private void init() {
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
    }
}
