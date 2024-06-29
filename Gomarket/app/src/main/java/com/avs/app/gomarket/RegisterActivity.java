package com.avs.app.gomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.avs.app.gomarket.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG= RegisterActivity.class.getName();

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        TextInputEditText register_fname = findViewById(R.id.textInputEditFname);
        TextInputEditText register_lname = findViewById(R.id.textInputEditLname);
        TextInputEditText register_email = findViewById(R.id.textInputEditEmail);
        TextInputEditText register_password = findViewById(R.id.textInputEditPassword);

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname = register_fname.getText().toString();
                String lname = register_lname.getText().toString();
                String email = register_email.getText().toString();
                String password = register_password.getText().toString();

                if (fname.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Please enter your first name",Toast.LENGTH_LONG).show();
                    return;
                }
                if (lname.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Please enter your last name",Toast.LENGTH_LONG).show();
                    return;
                }
                if (email.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Please enter your email",Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Please enter your password",Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.length() < 6){
                    Toast.makeText(RegisterActivity.this,"Password length must be grater than 6 letter",Toast.LENGTH_LONG).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){

                                    UserModel userModel = new UserModel(fname,lname,email,password);
                                    String id = task.getResult().getUser().getUid();
                                    firebaseDatabase.getReference().child("Users").child(id).setValue(userModel);

                                    Log.i(TAG,"createUserWithEmailAndPassword : Success");
                                    Toast.makeText(RegisterActivity.this,"Registration Successfully!",Toast.LENGTH_LONG).show();

                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    user.sendEmailVerification();
                                    Toast.makeText(RegisterActivity.this, "Please verify your email!", Toast.LENGTH_LONG).show();

                                    findViewById(R.id.rContainer).setVisibility(View.INVISIBLE);
                                    updateUI(user);

                                }else{
                                    Log.i(TAG,"createUserWithEmailAndPassword : Failed");
                                    Toast.makeText(RegisterActivity.this,"Registration Failed!",Toast.LENGTH_LONG).show();
                                }

                            }
                        });

            }
        });

        findViewById(R.id.textView16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

    }

    private void updateUI(FirebaseUser user){
        if (user != null){

            if (user.isEmailVerified()){
                Toast.makeText(RegisterActivity.this, "Please verify your email!", Toast.LENGTH_SHORT).show();
                return;
            }

            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();

        }
    }

}