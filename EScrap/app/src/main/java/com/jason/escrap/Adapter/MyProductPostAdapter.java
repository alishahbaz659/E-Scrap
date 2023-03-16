package com.jason.escrap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jason.escrap.Model.Products;
import com.jason.escrap.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyProductPostAdapter extends RecyclerView.Adapter<MyProductPostAdapter.MyViewHolder> {
    Context context;
    ArrayList<Products> productsList;

    public MyProductPostAdapter(Context context, ArrayList<Products> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.product_posts_layout_ticket, null, true);
        return new MyProductPostAdapter.MyViewHolder(listViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.post_caption.setText(productsList.get(position).getCaption());
        holder.product_type.setText(productsList.get(position).getType());
        holder.manufacturing_date.setText("Manufacturing Date("+productsList.get(position).getManufacturingdate()+")");
        if (productsList.get(position).getLendavailability().equalsIgnoreCase("Yes")) {
            holder.available_lending.setVisibility(View.VISIBLE);
        } else {
            holder.available_lending.setVisibility(View.GONE);
        }
        if (productsList.get(position).getSaleavailability().equalsIgnoreCase("Yes")) {
            holder.available_sale.setVisibility(View.VISIBLE);
        } else {
            holder.available_sale.setVisibility(View.GONE);
        }

        holder.lending_price.setText("Lending Fee $" + productsList.get(position).getLendavailability_price());
        holder.sale_price.setText("Sale Fee $" + productsList.get(position).getSaleavailabilityprice());

        Glide.with(context).load(productsList.get(position).getImageurl())
                .into(holder.post_image);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView post_caption, product_type, manufacturing_date, available_lending, available_sale, contact,
                lending_price, sale_price;
        ImageView post_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            post_image=itemView.findViewById(R.id.post_image);
            post_caption = itemView.findViewById(R.id.txt_post_caption);
            product_type = itemView.findViewById(R.id.post_product_type);
            manufacturing_date = itemView.findViewById(R.id.post_product_manufacturing_date);
            available_lending = itemView.findViewById(R.id.post_txt_availableforlending);
            available_sale = itemView.findViewById(R.id.post_txt_availableforsale);
            contact = itemView.findViewById(R.id.txt_post_contact);
            lending_price = itemView.findViewById(R.id.txt_post_lending_price);
            sale_price = itemView.findViewById(R.id.txt_post_sale_price);
        }
    }
}
