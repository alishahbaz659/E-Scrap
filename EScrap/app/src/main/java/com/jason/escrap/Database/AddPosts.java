package com.jason.escrap.Database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.anirudh.locationfetch.EasyLocationFetch;
import com.anirudh.locationfetch.GeoLocationModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jason.escrap.Model.Alert;
import com.jason.escrap.Model.Products;
import com.jason.escrap.Utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class AddPosts {
    Context context;
    DatabaseReference databaseReference;
    Alert alert;
    ProgressDialog pDialog;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    GeoLocationModel geoLocationModel;
    Double longitude,latitude;

    public AddPosts(Context context) {
        this.context = context;
        alert = new Alert(context);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        geoLocationModel = new EasyLocationFetch(context, Utils.MapKey).getLocationData();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void upload_new_post(String type, String caption, String producttype, String manufacturingDate, String Expirydate,
                                String briefhistory, String warranty, String lendingavailability, String lendingavailability_price,
                                String saleavailability, String saleavailabilityprice, Uri imageurl, String uid, FirebaseAuth firebaseAuth) {
        pDialog.show();
        String key = FirebaseDatabase.getInstance().getReference("Posts").push().getKey();
        final StorageReference imagerefrence = storageReference.child("Postspic").child(key).child("Product Pic");
        UploadTask uploadTask = imagerefrence.putFile(imageurl);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagerefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (ContextCompat.checkSelfPermission(context,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                            longitude=geoLocationModel.getLongitude();
                            latitude=geoLocationModel.getLattitude();
                        }
                        String uploadedimageurl = uri.toString();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Posts").child(key);
                        Products products = new Products(type, caption, producttype, manufacturingDate, Expirydate, briefhistory, warranty,lendingavailability,
                                lendingavailability_price, saleavailability, saleavailabilityprice, uploadedimageurl, uid,latitude,longitude);
                        databaseReference.setValue(products).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pDialog.dismiss();
                                Toast.makeText(context, "Posted Successfully", Toast.LENGTH_SHORT).show();
                                ((Activity) context).finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pDialog.dismiss();
                                Toast.makeText(context, "fail" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Uploading Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
