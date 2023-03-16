package com.jason.escrap.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jason.escrap.Model.Users;
import com.jason.escrap.R;

public class ProductDetailsActivity extends AppCompatActivity {
    TextView post_caption, product_type, manufacturing_date, available_lending, available_sale, contact,
            lending_price, sale_price, post_owner_name, post_owner_type_owner, post_owner_type_seller, post_owner_distance,
            brief_history;
    ImageView post_image;
    Button btn_request;
    String val_post_caption, val_product_type, val_manufacturingDate, val_available_lending, val_available_sale,
            val_lending_price, val_sale_price,
            val_brief_history, image_url, ownerId;
    String owner_email, ownername;
    CheckBox t1_lending, t1_owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        bind_views();
        Intent intent = getIntent();
        val_post_caption = intent.getStringExtra("post_caption");
        val_product_type = intent.getStringExtra("product_type");
        val_manufacturingDate = intent.getStringExtra("manufacturingDate");
        val_available_lending = intent.getStringExtra("available_lending");
        val_available_sale = intent.getStringExtra("available_sale");
        val_lending_price = intent.getStringExtra("lending_price");
        val_sale_price = intent.getStringExtra("sale_price");
        val_brief_history = intent.getStringExtra("brief_intro");
        image_url = intent.getStringExtra("image_url");
        ownerId = intent.getStringExtra("owner_id");
        get_owner_email(ownerId);

        post_caption.setText(val_post_caption);
        product_type.setText(val_product_type);
        manufacturing_date.setText(val_manufacturingDate);
        if (val_available_lending.equalsIgnoreCase("Yes")) {
            available_lending.setVisibility(View.VISIBLE);
        } else {
            available_lending.setVisibility(View.GONE);
        }
        if (val_available_sale.equalsIgnoreCase("Yes")) {
            available_sale.setVisibility(View.VISIBLE);
        } else {
            available_sale.setVisibility(View.GONE);
        }

        lending_price.setText("Breed $" + val_lending_price);
        sale_price.setText("Adoption $" + val_sale_price);

        Glide.with(this).load(image_url)
                .into(post_image);
        brief_history.setText(val_brief_history);
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!t1_lending.isChecked() && !t1_owner.isChecked()) {
                    Toast.makeText(ProductDetailsActivity.this, "Please select request type", Toast.LENGTH_SHORT).show();
                } else {
                    ChatActivity.startActivity(ProductDetailsActivity.this,
                            owner_email,
                            ownerId,
                            "", ownername);
                }
            }
        });


    }

    public void bind_views() {
        post_image = findViewById(R.id.post_image);
        post_caption = findViewById(R.id.txt_post_caption);
        product_type = findViewById(R.id.post_product_type);
        manufacturing_date = findViewById(R.id.post_product_manufacturing_date);
        available_lending = findViewById(R.id.post_txt_availableforlending);
        available_sale = findViewById(R.id.post_txt_availableforsale);
        contact = findViewById(R.id.txt_post_contact);
        lending_price = findViewById(R.id.txt_post_lending_price);
        sale_price = findViewById(R.id.txt_post_sale_price);
        post_owner_name = findViewById(R.id.txt_post_owner_name);
        post_owner_type_owner = findViewById(R.id.posted_user_product_owner);
        post_owner_distance = findViewById(R.id.posted_user_distance);
        post_owner_type_seller = findViewById(R.id.posted_user_product_seller);
        brief_history = findViewById(R.id.txt_brief_history);
        btn_request = findViewById(R.id.btn_request);
        t1_lending = findViewById(R.id.t1_lending);
        t1_owner = findViewById(R.id.t2_owner);
    }


    public void get_owner_email(String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                owner_email = users.getEmail();
                ownername = users.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
