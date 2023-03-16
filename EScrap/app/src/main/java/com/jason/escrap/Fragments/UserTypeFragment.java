package com.jason.escrap.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.jason.escrap.Model.Alert;
import com.jason.escrap.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserTypeFragment extends Fragment {
    View view;
    @BindView(R.id.btn_go_next)
    Button btn_go_next;
    @BindView(R.id.t1_productOwner)
    CheckBox t1_productOwner;
    @BindView(R.id.t2_productSeller)
    CheckBox t2_productSeller;
    @BindView(R.id.t3_general)
    CheckBox t3_general;
    @BindView(R.id.btn_go_back)
    Button btn_go_back;
    Alert alert;
    String t1_status, t2_status, t3_status;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_user_type_fragment, container, false);
        ButterKnife.bind(this, view);
        alert = new Alert(view.getContext());
        editor = view.getContext().getSharedPreferences("userInfo", view.getContext().MODE_PRIVATE).edit();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_go_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {

                    if (t1_productOwner.isChecked()) {
                        t1_status = "1";
                    } else {
                        t1_status = "0";
                    }

                    if (t2_productSeller.isChecked()) {
                        t2_status = "1";
                    } else {
                        t2_status = "0";
                    }

                    if (t3_general.isChecked()) {
                        t3_status = "1";
                    } else {
                        t3_status = "0";
                    }

                    showSignupFragment();
                }

            }
        });
        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment bf = new AuthenticationFragment();
                fragmentTransaction.replace(R.id.intro_activity_container, bf);
                fragmentTransaction.commit();

            }
        });

    }


    public void showSignupFragment() {
        editor.putString("product_owner", t1_status);
        editor.putString("product_seller", t2_status);
        editor.putString("general", t3_status);
        editor.commit();

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment bf = new SignUpFragment();
        fragmentTransaction.replace(R.id.intro_activity_container, bf);
        fragmentTransaction.commit();

    }


    public boolean isValid() {
        if (!t1_productOwner.isChecked() && !t2_productSeller.isChecked() && !t3_general.isChecked()) {
            alert.error_alert("Error", "Please choose at-least one type");
            return false;
        }
        return true;
    }
}
