package com.avs.app.gomarket;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.signin.internal.SignInClientImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getName();
    private FirebaseAuth firebaseAuth;
    private SignInClient signInClient;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        signInClient = Identity.getSignInClient(getApplicationContext());

        TextInputEditText textLoginEmail = findViewById(R.id.editTextLoginEmail);
        TextInputEditText textLoginPassword = findViewById(R.id.editTextLoginPassword);
        forgotPassword = findViewById(R.id.forgot_password);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "done", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot,null);
                TextView emailBox  = dialogView.findViewById(R.id.emailBox);

                builder.setView(dialogView);
                builder.show();
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String resetEmail = emailBox.getText().toString();

                        if (resetEmail.isEmpty()){
                            Toast.makeText(LoginActivity.this, "Enter your registered email", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        firebaseAuth.sendPasswordResetEmail(resetEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Check your Email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(LoginActivity.this, "Unable to send,Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

//                if (dialog.getWindow() != null){
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//                }
//                dialog.show();

                //builder.setView(dialogView);
               // builder.show();
               //AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
               // View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot,null);
               // EditText emailBox = dialogView.findViewById(R.id.emailBox);

                //builder.setView(dialogView);
               // AlertDialog dialog = builder.create();

//                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String userEmail = emailBox.getText().toString();
//
//                        if (userEmail.isEmpty()){
//                            Toast.makeText(LoginActivity.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
//                            return;
//                        }

//                        firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()){
//                                    Toast.makeText(LoginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
//                                    dialog.dismiss();
//
//                                }else {
//                                    Toast.makeText(LoginActivity.this, "Unable to send,Failed!", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });

                  //  }
              //  });
//                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                if (dialog.getWindow() != null){
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//                }
//                dialog.show();
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = textLoginEmail.getText().toString();
                String password = textLoginPassword.getText().toString();

                if (email.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Log.i(TAG,"UserLogin : Successfully");
                                    Toast.makeText(LoginActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();

                                }else{
                                    Log.i(TAG,"UserLogin : Failed");
                                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        findViewById(R.id.btnSignInWithGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetSignInIntentRequest signInIntentRequest = GetSignInIntentRequest.builder()
                        .setServerClientId(getString(R.string.web_client_id)).build();

                Task<PendingIntent> signInIntent = signInClient.getSignInIntent(signInIntentRequest);
                signInIntent.addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
                    @Override
                    public void onSuccess(PendingIntent pendingIntent) {

                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest
                                .Builder(pendingIntent).build();
                        signInLauncher.launch(intentSenderRequest);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });


        findViewById(R.id.textView7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    //SignIn With Google
    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken,null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(authCredential);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void updateUI(FirebaseUser user){
        if (user != null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void handleSignInResult(Intent intent){
        try {
            SignInCredential signInCredential = signInClient.getSignInCredentialFromIntent(intent);
            String idToken = signInCredential.getGoogleIdToken();
            firebaseAuthWithGoogle(idToken);

        }catch (ApiException e){
            Log.e(TAG,e.getMessage());
        }
    }

    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    handleSignInResult(o.getData());
                }
            });

}