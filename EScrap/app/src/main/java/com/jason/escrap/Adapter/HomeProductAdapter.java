package com.jason.escrap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jason.escrap.Activity.ProductDetailsActivity;
import com.jason.escrap.Model.Products;
import com.jason.escrap.Model.Users;
import com.jason.escrap.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<Products> productsList;
    ArrayList<Products> exampleproductslist;
    SharedPreferences.Editor editor;

    public HomeProductAdapter(Context context, ArrayList<Products> productsList) {
        this.context = context;
        this.productsList = productsList;
        this.exampleproductslist = productsList;
        editor = context.getSharedPreferences("filter_details", Context.MODE_PRIVATE).edit();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.home_dog_posts_layout_ticket, null, true);
        return new HomeProductAdapter.MyViewHolder(listViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.post_caption.setText(exampleproductslist.get(position).getCaption());
        holder.dog_type.setText(exampleproductslist.get(position).getProducttype());
        Glide.with(context).load(exampleproductslist.get(position).getImageurl())
                .into(holder.post_image);

        get_owner_details(exampleproductslist.get(position).getUid(), holder, position);

        //if user click on the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("post_caption", exampleproductslist.get(position).getCaption());
                intent.putExtra("product_type", exampleproductslist.get(position).getType());
                intent.putExtra("manufacturingDate", exampleproductslist.get(position).getManufacturingdate());
                intent.putExtra("available_lending", exampleproductslist.get(position).getLendavailability());
                intent.putExtra("available_sale", exampleproductslist.get(position).getSaleavailability());
                intent.putExtra("lending_price", exampleproductslist.get(position).getLendavailability_price());
                intent.putExtra("sale_price", exampleproductslist.get(position).getSaleavailabilityprice());
                intent.putExtra("brief_intro", exampleproductslist.get(position).getBriefhistory());
                intent.putExtra("image_url", exampleproductslist.get(position).getImageurl());
                intent.putExtra("owner_id", exampleproductslist.get(position).getUid());

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return exampleproductslist.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    exampleproductslist = productsList;
                } else {
                    ArrayList<Products> filteredList = new ArrayList<>();
                    for (Products name : productsList) {
                        if (name.getCaption().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(name);
                        }
                        exampleproductslist = filteredList;
                    }
                }
                FilterResults results = new FilterResults();
                results.values = exampleproductslist;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                exampleproductslist = (ArrayList<Products>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void performFiltering(String adoption_availability, String breed_availability, String breed_type,
                                 String dog_age, String gender) {
        if (adoption_availability.equals("no") && breed_availability.equals("no") &&
                breed_type.equals("") &&
                dog_age.equals("") && gender.equals("no_selection")
        ) {
            exampleproductslist = productsList;
            editor.remove("dog_adoption_status");
            editor.remove("dog_breed_status");
            editor.remove("gender");
            editor.remove("breedtype");
            editor.remove("age");
            editor.commit();
        } else {
            ArrayList<Products> filteredList = new ArrayList<>();
            for (Products dog : productsList) {
                if (adoption_availability.equals("yes")) {
                    if (!filteredList.contains(dog)) {
                        if (dog.getLendavailability().toLowerCase().equals(adoption_availability)) {
                            filteredList.add(dog);
                        }
                    }
                    editor.putString("dog_adoption_status", "yes");

                } else {
                    editor.remove("dog_adoption_status");
                }

                if (breed_availability.equals("yes")) {
                    if (!filteredList.contains(dog)) {
                        if (dog.getLendavailability().toLowerCase().equals(breed_availability)) {
                            filteredList.add(dog);
                        }
                    }
                    editor.putString("dog_breed_status", "yes");
                } else {
                    editor.remove("dog_breed_status");
                }


                if (dog.getType().toLowerCase().equals(breed_type.toLowerCase())) {
                    if (!filteredList.contains(dog)) {
                        filteredList.add(dog);
                    }
                }
                editor.putString("breedtype", breed_type);

                if (dog.getManufacturingdate().toLowerCase().equals(gender.toLowerCase())) {
                    if (!filteredList.contains(dog)) {
                        filteredList.add(dog);
                    }
                }
                editor.putString("gender", gender);

                //For dog age
                if (!dog_age.isEmpty()) {
                    int dog_entered_age = Integer.parseInt(dog_age);
                    if (!filteredList.contains(dog)) {
                        int age = Integer.parseInt(dog.getManufacturingdate());
                        if (age <= dog_entered_age) {
                            filteredList.add(dog);
                        }
                    }
                    editor.putString("age", dog_entered_age + "");
                }
                editor.commit();
                exampleproductslist = filteredList;
            }
        }
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView post_caption, dog_type, post_owner_distance;
        ImageView post_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            post_image = itemView.findViewById(R.id.post_image);
            post_caption = itemView.findViewById(R.id.txt_post_caption);
            dog_type = itemView.findViewById(R.id.post_dog_type);
            post_owner_distance = itemView.findViewById(R.id.posted_user_distance);
        }
    }

    public void get_owner_details(String uid, MyViewHolder holder, int poition) {
        //getting profile info
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if (snapshot.exists()) {
                    getKmFromLatLong(users.getLattitude(), users.getLongitude(), productsList.get(poition).getLattitude(), productsList.get(poition).getLongitude(), holder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static double getKmFromLatLong(double lat1, double lng1, double lat2, double lng2, MyViewHolder holder) {
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        double distanceInMeters = loc1.distanceTo(loc2);
        holder.post_owner_distance.setText("" + Math.round(distanceInMeters / 1000) + " km away");
        return distanceInMeters / 1000;
    }
}
