package com.avs.app.gomarket.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avs.app.gomarket.LoginActivity;
import com.avs.app.gomarket.R;
import com.avs.app.gomarket.models.ViewAllModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DetailedActivity extends AppCompatActivity {
    TextView quantity;
    int totalQuantity = 1;
    int totalPrice = 0;
    ImageView detailedImg;
    TextView price,rating,description;
    Button addToCart;
    ImageView addItem,removeItem;
    Toolbar d_toolbar;
    ViewAllModel viewAllModel = null;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    private static final String TAG = DetailedActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        //d_toolbar = findViewById(R.id.detailed_toolbar);
        //setSupportActionBar(d_toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof ViewAllModel){
            viewAllModel = (ViewAllModel) object;
        }

        quantity = findViewById(R.id.quantity);
        detailedImg = findViewById(R.id.detailed_img);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);

        price = findViewById(R.id.detailed_price);
        rating = findViewById(R.id.detailed_rating);
        description = findViewById(R.id.detailed_dec);

        if (viewAllModel != null){
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailedImg);
            rating.setText(viewAllModel.getRating());
            description.setText(viewAllModel.getDescription());
            price.setText("Price Rs."+viewAllModel.getPrice()+".00/kg");

            totalPrice = viewAllModel.getPrice() * totalQuantity;

            if (viewAllModel.getType().equals("egg")){
                price.setText("Price Rs."+viewAllModel.getPrice()+".00/ 1");
                totalPrice = viewAllModel.getPrice() * totalQuantity;
            }
            if (viewAllModel.getType().equals("milk")){
                price.setText("Price Rs."+viewAllModel.getPrice()+".00/L");
                totalPrice = viewAllModel.getPrice() * totalQuantity;
            }

        }

        addToCart = findViewById(R.id.add_to_cart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addedToCart();
//                String saveCurrentDate,saveCurrentTime;
//                Calendar calForDate = Calendar.getInstance();
//
//                SimpleDateFormat currentDate = new SimpleDateFormat("MM,dd,yyyy");
//                saveCurrentDate = currentDate.format(calForDate.getTime());
//
//                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//                saveCurrentTime = currentTime.format(calForDate.getTime());
//
//                final HashMap<String,Object> cartMap = new HashMap<>();
//                cartMap.put("productName",viewAllModel.getName());
//                cartMap.put("productPrice",price.getText().toString());
//                cartMap.put("currentDate",saveCurrentDate);
//                cartMap.put("currentTime",saveCurrentDate);
//                cartMap.put("totalQuantity",quantity.getText().toString());
//                cartMap.put("totalPrice",totalPrice);
//
//                firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
//                        .set(cartMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(DetailedActivity.this, "Success", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(DetailedActivity.this, "Please login", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(DetailedActivity.this, LoginActivity.class));
//                            }
//                        });

            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalQuantity < 10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice = viewAllModel.getPrice() * totalQuantity;
                }

            }
        });
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice = viewAllModel.getPrice() * totalQuantity;
                }

            }
        });

    }

    private void addedToCart(){
        String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy.MM.dd");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("productName",viewAllModel.getName());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added to a Cart", Toast.LENGTH_SHORT).show();
                        //finish();
                        Log.i(TAG,"Success");
                        finish();
                    }
                });
    }

}