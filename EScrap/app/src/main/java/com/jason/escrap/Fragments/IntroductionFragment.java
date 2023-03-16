package com.jason.escrap.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.jason.escrap.Database.Registration;
import com.jason.escrap.Model.Alert;
import com.jason.escrap.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroductionFragment extends Fragment {
    View view;
    SharedPreferences.Editor editor;
    Alert alert;
    SharedPreferences sharedPreferences;
    @BindView(R.id.edt_description)
    EditText edt_description;
    @BindView(R.id.btn_go_next)
    Button btn_go_next;
    Registration registration;
    FirebaseAuth firebaseAuth;
    String userName, userEmail, userPassword, userAddress, userContact, userDescription, product_owner, product_seller;
    String general, google_signin_check,google_token;
    @BindView(R.id.btn_go_back)
    Button btn_go_back;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_introduction_fragment, container, false);
        ButterKnife.bind(this, view);
        alert = new Alert(view.getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        editor = view.getContext().getSharedPreferences("userInfo", getContext().MODE_PRIVATE).edit();
        sharedPreferences = getContext().getSharedPreferences("userInfo", getContext().MODE_PRIVATE);
        registration = new Registration(view.getContext());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_go_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = edt_description.getText().toString();
                if (isValid(description)) {
                    editor.putString("userDescription", description);
                    editor.commit();

                    userName = sharedPreferences.getString("userName", "N/A");
                    userEmail = sharedPreferences.getString("userEmail", "N/A");
                    userPassword = sharedPreferences.getString("userPassword", "N/A");
                    userAddress = sharedPreferences.getString("userAddress", "N/A");
                    userContact = sharedPreferences.getString("userPhoneNo", "N/A");
                    userDescription = sharedPreferences.getString("userDescription", "N/A");
                    product_owner = sharedPreferences.getString("product_owner", "N/A");
                    product_seller = sharedPreferences.getString("product_seller", "N/A");
                    general = sharedPreferences.getString("general", "N/A");
                    google_signin_check = sharedPreferences.getString("googleSignin", "N/A");
                    google_token=sharedPreferences.getString("googleToken","N/A");

                    if (google_signin_check.equals("no")) {
                        registration.register_user(firebaseAuth, userName, userEmail, userPassword, userAddress, userContact, userDescription, firebaseAuth.getUid(), product_owner, product_seller, general);
                    } else if(google_signin_check.equals("yes")){
                        //do google sign in
                        registration.firebaseAuthWithGoogle(firebaseAuth, userName, userEmail, userPassword, userAddress, userContact, userDescription, firebaseAuth.getUid(), product_owner, product_seller, general,google_token);

                    }
                }

            }
        });
        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment bf = new ContactInfoFragment();
                fragmentTransaction.replace(R.id.intro_activity_container, bf);
                fragmentTransaction.commit();
            }
        });

    }

    public boolean isValid(String description) {
        if (description.isEmpty()) {
            alert.error_alert("Error", "Please provide brief description");
            return false;
        }
        return true;
    }


}
