package com.avs.app.gomarket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avs.app.gomarket.models.UserModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class ProfileFragment extends Fragment {
    ImageView profileImg;
    TextInputEditText firstName,lastName,email,mobile,password,addressLine1,addressLine2;
    //Button update;
    //Button logout;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        profileImg = root.findViewById(R.id.profile_img);
        firstName = root.findViewById(R.id.profile_fname);
        lastName = root.findViewById(R.id.profile_lname);
        email = root.findViewById(R.id.profile_email);
        password = root.findViewById(R.id.profile_password);
        //mobile = root.findViewById(R.id.profile_mobile);
       // addressLine1 = root.findViewById(R.id.profile_address1);
       // addressLine2 = root.findViewById(R.id.profile_address2);
        //update = root.findViewById(R.id.update);

        DatabaseReference databaseReference = database.getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid());
        databaseReference.child("profileImg")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                              //UserModel userModel = snapshot.getValue(UserModel.class);

                                if (snapshot.exists()){
                                    String profileUrl = snapshot.getValue(String.class);
                                    loadProfilePicture(profileUrl);
//                                    Glide.with(getContext())
//                                            .load(profileUrl)
//                                            .placeholder(R.drawable.user)
//                                            .error(R.drawable.user)
//                                            .into(profileImg);
                                    //profileImg.setImageResource(R.drawable.user);
                                    //lastName.setText("done");
                                   // UserModel userModel = snapshot.getValue(UserModel.class);
                                   // Glide.with(getContext()).load(userModel.getProfileImg()).into(profileImg);
                                }else{

                                    //UserModel userModel = snapshot.getValue(UserModel.class);
                                    //Glide.with(getContext()).load(userModel.getProfileImg()).into(profileImg);
                                    //firstName.setText("hello");

                                    //Toast.makeText(getContext(), "No profile pic", Toast.LENGTH_SHORT).show();
                                    //Glide.with(getContext()).load(R.drawable.user).into(profileImg);
                                    profileImg.setImageResource(R.drawable.user);

                                }

                                //firstName.setText(userModel.getUserFname());
                                //lastName.setText(userModel.getUserLname());
                                //email.setText(userModel.getUserEmail());
                                //password.setText(userModel.getUserPassword());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserModel userModel = snapshot.getValue(UserModel.class);
                                firstName.setText(userModel.getUserFname());
                                lastName.setText(userModel.getUserLname());
                                email.setText(userModel.getUserEmail());
                                email.setEnabled(false);
                                password.setText(userModel.getUserPassword());
                                password.setEnabled(false);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);
            }
        });

//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateUserProfile();
//            }
//        });

//        logout = root.findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                GoToLogin();
//
//            }
//        });
//
       return root;
    }

    private void loadProfilePicture(String profileUrl) {
        Glide.with(getContext())
                .load(profileUrl)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .centerCrop()
                .into(profileImg);
    }

//    private void GoToLogin() {
//        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        startActivity(intent);
//    }

//    private void updateUserProfile() {
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData() != null){
            Uri profileUri = data.getData();
            profileImg.setImageURI(profileUri);

            final StorageReference reference = storage.getReference().child("profile_picture")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
//                    Task<Uri> downloadUrl = reference.getDownloadUrl();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profileImg").setValue(uri.toString());
                            Toast.makeText(getContext(), "Profile image uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }

    }

//    private void loadProfilePicture(String profileUrl) {
//        Glide.with(this)
//                .load(profileImg)
//                .placeholder(R.drawable.user) // Placeholder image while loading
//                .error(R.drawable.user) // Error image if loading fails
//                .into(profileImg);
//    }
}