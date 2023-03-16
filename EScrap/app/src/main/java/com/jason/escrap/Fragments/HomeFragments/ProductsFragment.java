package com.jason.escrap.Fragments.HomeFragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jason.escrap.Adapter.PostedProductsAdapter;
import com.jason.escrap.Model.Products;
import com.jason.escrap.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsFragment extends Fragment {
    public static ProductsFragment newInstance() {
        ProductsFragment fragment = new ProductsFragment();
        return fragment;
    }

    @BindView(R.id.searchView_products)
    SearchView searchView;
    @BindView(R.id.dogs_recylerview)
    RecyclerView dogs_recycler_view;
    @BindView(R.id.txt_filter)
    TextView txt_filter;
    FirebaseAuth firebaseAuth;
    ProgressDialog pDialog;
    ArrayList<Products> productsList;
    PostedProductsAdapter postedDogAdapter;
    View view;
    SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dogs_layout, container, false);
        ButterKnife.bind(this, view);
        productsList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        sharedPreferences = getContext().getSharedPreferences("filter_details", Context.MODE_PRIVATE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        postedDogAdapter = new PostedProductsAdapter(view.getContext(), productsList);
        dogs_recycler_view.setLayoutManager(new GridLayoutManager(getContext(), 1));
        dogs_recycler_view.setAdapter(postedDogAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                postedDogAdapter.getFilter().filter(newText);
                return false;
            }
        });
        txt_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_filter_dialog();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        pDialog.show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productsList.clear();
                String uid = firebaseAuth.getUid();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Products products = ds.getValue(Products.class);
                    if (!products.getUid().equals(uid)) {
                        productsList.add(products);
                    }
                }
                pDialog.dismiss();
                postedDogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void show_filter_dialog() {

        //binding the dialog box design
        final Dialog dialog = new Dialog(getContext());
        //we don't want the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.filter_layout_ticket);
        dialog.setCancelable(true);
        dialog.show();

//        CheckBox adoption_availability = dialog.findViewById(R.id.checkbox_availability_adoption);
//        CheckBox breed_availability = dialog.findViewById(R.id.checkbox_availability_breed);
//        EditText breed_type = dialog.findViewById(R.id.edt_breedType);
//        EditText area_radius = dialog.findViewById(R.id.edt_area_radius);
//        EditText dog_age = dialog.findViewById(R.id.edt_age);
//        RadioButton male_gender = dialog.findViewById(R.id.radio_male);
//        RadioButton female_gender = dialog.findViewById(R.id.radio_female);
//        Button apply_filter = dialog.findViewById(R.id.btn_save_filter);
//        TextView reset_choice = dialog.findViewById(R.id.reset_choices);
//
//
//        //checking shared preferences for previous filters
//        if (sharedPreferences.getString("dog_adoption_status", "").equals("yes")) {
//            adoption_availability.setChecked(true);
//        } else if (sharedPreferences.getString("dog_adoption_status", "").equals("no")) {
//            adoption_availability.setChecked(false);
//        }
//
//        if (sharedPreferences.getString("dog_breed_status", "").equals("yes")) {
//            breed_availability.setChecked(true);
//        } else if (sharedPreferences.getString("dog_breed_status", "").equals("no")) {
//            breed_availability.setChecked(false);
//        }
//        if (sharedPreferences.contains("breedtype")) {
//            String breedType = sharedPreferences.getString("breedtype", "");
//            breed_type.setText(breedType);
//        }
//        if (sharedPreferences.contains("gender")) {
//            if (sharedPreferences.getString("gender", "").equals("male")) {
//                male_gender.setChecked(true);
//            } else if (sharedPreferences.getString("gender", "").equals("female")) {
//                female_gender.setChecked(true);
//            }
//        }
//        if (sharedPreferences.contains("age")) {
//            dog_age.setText(sharedPreferences.getString("age", ""));
//        }
//        dialog.show();
//        reset_choice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                male_gender.setChecked(false);
//                female_gender.setChecked(false);
//            }
//        });
//
//        apply_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String adoption_availability_status, breed_availability_status, gender;
//                if (adoption_availability.isChecked()) {
//                    adoption_availability_status = "yes";
//                } else {
//                    adoption_availability_status = "no";
//                }
//                if (breed_availability.isChecked()) {
//                    breed_availability_status = "yes";
//                } else {
//                    breed_availability_status = "no";
//                }
//                if (male_gender.isChecked()) {
//                    gender = "male";
//                } else if (female_gender.isChecked()) {
//                    gender = "female";
//                } else {
//                    gender = "no_selection";
//                }
//
//                postedDogAdapter.performFiltering(adoption_availability_status, breed_availability_status, breed_type.getText().toString(),
//                        dog_age.getText().toString(), gender);
//                dialog.dismiss();
//            }
//        });


    }

}
