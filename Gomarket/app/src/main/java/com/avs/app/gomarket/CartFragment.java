package com.avs.app.gomarket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avs.app.gomarket.activities.PlacedOrderActivity;
import com.avs.app.gomarket.adapters.MyCartAdapter;
import com.avs.app.gomarket.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;
    TextView overTotalAmount;
    ProgressBar progressBar;
    Button buyNow;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = root.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        overTotalAmount = root.findViewById(R.id.overTotalAmount);
        recyclerView.setVisibility(View.GONE);
        buyNow = root.findViewById(R.id.buy_now);

//        LocalBroadcastManager.getInstance(getActivity())
//                .registerReceiver(mMessageReceiver,new IntentFilter("MyTotalAmount"));

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(),cartModelList);
        recyclerView.setAdapter(cartAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                                String documentId = documentSnapshot.getId();

                                MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                                cartModel.setDocumentId(documentId);

                                cartModelList.add(cartModel);
                                cartAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }

                            calculateTotalAmount(cartModelList);

                        }
                    }
                });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlacedOrderActivity.class);
                intent.putExtra("itemList", (Serializable) cartModelList);
                startActivity(intent);
            }
        });

        return root;
    }

    private void calculateTotalAmount(List<MyCartModel> cartModelList) {

        double totalAmount = 0.0;
        for (MyCartModel myCartModel : cartModelList){
            totalAmount += myCartModel.getTotalPrice();
        }

        overTotalAmount.setText("Total Amount:Rs."+totalAmount+"0");
    }

//    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            int totalBill = intent.getIntExtra("totalAmount",0);
//            overTotalAmount.setText("Total Bill : Rs."+totalBill+".00");
//        }
//    };

}