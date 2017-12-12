package com.zochat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zochat.models.Friend;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivitySignUp extends AppCompatActivity {

    EditText etEmail, etPass;
    Button btnDk;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference dataRef;

    String listuid = "";
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dataRef = database.getReference("listuser");
        myRef = database.getReference("datachat");

        btnDk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser(etEmail.getText() + "", etPass.getText() + "");
            }
        });
        /*dataRef.child("listuid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listuid = (dataSnapshot.getValue() == null)? "" : dataSnapshot.getValue().toString();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    private void signUpUser(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(ActivitySignUp.this, task.getException() + "",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ActivitySignUp.this, task.getResult() + "",
                                    Toast.LENGTH_SHORT).show();
                            addInfoUser();

                        }
                        // ...
                    }
                });
    }



    private void init() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
        btnDk = (Button) findViewById(R.id.btnDangKy);
    }

    private void addInfoUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("TAG user sign up", user.getUid().toString());
        myRef.child(user.getUid()).setValue(new Friend(null, user.getEmail().toString()));
        dataRef.child(user.getUid()).setValue(new Friend(null, user.getEmail().toString()));

        /*if (listuid.equals("")) {
            dataRef.child("listuid").setValue(user.getUid());
        } else {
            dataRef.child("listuid").setValue(listuid + ";" + user.getUid());
        }*/

        Intent intent = new Intent(ActivitySignUp.this, ActivityDetailUser.class);
        startActivity(intent);
        finish();
    }

}
