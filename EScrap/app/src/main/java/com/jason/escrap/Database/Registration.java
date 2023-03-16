package com.jason.escrap.Database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.anirudh.locationfetch.EasyLocationFetch;
import com.anirudh.locationfetch.GeoLocationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jason.escrap.Activity.HomeActivity;
import com.jason.escrap.Fragments.LoginFragment;

import com.jason.escrap.Model.Alert;
import com.jason.escrap.Model.Users;
import com.jason.escrap.R;
import com.jason.escrap.Utils.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Registration {
    Context context;
    DatabaseReference databaseReference;
    Alert alert;
    ProgressDialog pDialog;
    SharedPreferences.Editor editor;
    GeoLocationModel geoLocationModel;
    Double longitude,latitude;
    public Registration(Context context) {
        this.context = context;
        alert = new Alert(context);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        editor = context.getSharedPreferences("userInfo", context.MODE_PRIVATE).edit();
        geoLocationModel = new EasyLocationFetch(context, Utils.MapKey).getLocationData();
    }


    public void register_user(FirebaseAuth firebaseAuth, String name, String email, String password, String address, String phoneno, String description, String uid,
                              String product_owner, String product_seller, String general) {
        pDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (ContextCompat.checkSelfPermission(context,
                                            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                        longitude=geoLocationModel.getLongitude();
                                        latitude=geoLocationModel.getLattitude();
                                    }


                                    databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getUid());
                                    Users users = new Users(name, email, address, phoneno, description, firebaseAuth.getUid(), product_owner, product_seller, general, latitude, longitude);
                                    databaseReference.setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            pDialog.dismiss();
                                            //done here
                                            alert.success_alert("Success", "Successfully registered, Please verify your email");
                                            show_login_fragment();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pDialog.dismiss();
                                        }
                                    });
                                } else {
                                    pDialog.dismiss();
                                    alert.error_alert("Error", "Registration failed,Please try again");
                                }
                            }
                        });
                    }
                } else {
                    pDialog.dismiss();
                    alert.error_alert("Error", "Email already registered");
                }

            }
        });

    }

    public void show_login_fragment() {
        FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment bf = new LoginFragment();
        fragmentTransaction.replace(R.id.intro_activity_container, bf);
        fragmentTransaction.commit();
    }

    public void firebaseAuthWithGoogle(FirebaseAuth firebaseAuth, String name, String email, String password, String address, String phoneno, String description, String uid,
                                       String dog_owner, String dog_breeder, String trainer, String token) {
        pDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                if (isNewUser) {
                    if (task.isSuccessful()) {
                        if (ContextCompat.checkSelfPermission(context,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            longitude=geoLocationModel.getLongitude();
                            latitude=geoLocationModel.getLattitude();
                        }
                        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getUid());
                        Users users = new Users(name, email, address, phoneno, description, firebaseAuth.getUid(), dog_owner, dog_breeder, trainer, latitude, longitude);

                        databaseReference.setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pDialog.dismiss();
                                alert.success_alert("Success", "Successfully registered");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pDialog.dismiss();
                                alert.error_alert("Error", "Registration failed,Please try again");
                            }
                        });
                    } else {
                        pDialog.dismiss();
                        alert.error_alert("Error", "Registration failed,Please try again");
                    }
                } else {
                    pDialog.dismiss();
                    context.startActivity(new Intent(context.getApplicationContext(), HomeActivity.class));
                }
            }
        });
    }

    public void firebaseloginwithGoogle(FirebaseAuth firebaseAuth, String token) {
        pDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                if (isNewUser) {
                    if (task.isSuccessful()) {
                        if (ContextCompat.checkSelfPermission(context,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            longitude=geoLocationModel.getLongitude();
                            latitude=geoLocationModel.getLattitude();
                        }
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getUid());
                        Users users = new Users(user.getDisplayName(), user.getEmail(), "", "", "", firebaseAuth.getUid(), "", "", "", latitude, longitude);
                        databaseReference.setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pDialog.dismiss();
                                context.startActivity(new Intent(context.getApplicationContext(), HomeActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pDialog.dismiss();
                                alert.error_alert("Error", "Registration failed,Please try again");
                            }
                        });
                    } else {
                        pDialog.dismiss();
                        alert.error_alert("Error", "Registration failed,Please try again");
                    }
                } else {
                    pDialog.dismiss();
                    context.startActivity(new Intent(context.getApplicationContext(), HomeActivity.class));
                }
            }
        });
    }

    public void checkEmailverification(FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();
        if (emailflag) {
            //show new activity
            pDialog.dismiss();
            context.startActivity(new Intent(context.getApplicationContext(), HomeActivity.class));
        } else {
            pDialog.dismiss();
            alert.error_alert("Verification failed", "Please verify your email");
            firebaseAuth.signOut();
        }
    }

}
