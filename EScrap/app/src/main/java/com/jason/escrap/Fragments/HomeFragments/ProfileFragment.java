package com.jason.escrap.Fragments.HomeFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jason.escrap.Activity.AddPostActivity;
import com.jason.escrap.Adapter.MyProductPostAdapter;
import com.jason.escrap.Model.Products;
import com.jason.escrap.Model.Users;
import com.jason.escrap.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @BindView(R.id.btn_add_post)
    Button btn_add_post;
    @BindView(R.id.my_posts_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.user_profile_name)
    TextView user_profile_name;
    @BindView(R.id.user_product_owner)
    TextView userprofile_owner;
    @BindView(R.id.user_product_seller)
    TextView userprofile_seller;
    @BindView(R.id.user_general)
    TextView userprofile_genral;
    @BindView(R.id.user_profile_email)
    TextView userprofile_email;
    @BindView(R.id.user_profile_phone)
    TextView userprofile_phone;
    @BindView(R.id.user_profile_address)
    TextView userprofile_address;
    @BindView(R.id.user_profile_brief_intro)
    TextView userprofile_brief_intro;

    MyProductPostAdapter myDogPostAdapter;
    ArrayList<Products> productsList;
    View view;
    FirebaseAuth firebaseAuth;
    ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_layout, container, false);
        ButterKnife.bind(this, view);
        productsList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), AddPostActivity.class));
            }
        });
        myDogPostAdapter = new MyProductPostAdapter(view.getContext(), productsList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(myDogPostAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        pDialog.show();

        //getting profile info
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if (snapshot.exists()) {
                    user_profile_name.setText(users.getName());
                    if (users.getProductowner().equals("1")) {
                        userprofile_owner.setText("Product Owner");
                    } else {
                        userprofile_owner.setText("");
                    }
                    if (users.getProductseller().equals("1")) {
                        userprofile_seller.setText(",Product Seller");
                    } else {
                        userprofile_seller.setText("");
                    }
                    if (users.getGeneral().equals("1")) {
                        userprofile_genral.setText(",General customer");
                    } else {
                        userprofile_genral.setText("");
                    }

                    userprofile_email.setText(users.getEmail());
                    if(!users.getPhoneno().isEmpty()){
                        userprofile_phone.setText(users.getPhoneno());
                    }else {
                        userprofile_phone.setText("N/A");
                    }

                    if (!users.getAddress().isEmpty()) {
                        userprofile_address.setText(users.getAddress());
                    } else {
                        userprofile_address.setText("N/A");
                    }
                    if (!users.getDescription().isEmpty()) {
                        userprofile_brief_intro.setText(users.getDescription());
                    } else {
                        userprofile_brief_intro.setText("N/A");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Query query = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("uid").equalTo(firebaseAuth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productsList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Products products = ds.getValue(Products.class);
                    productsList.add(products);
                }
                pDialog.dismiss();
                myDogPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
