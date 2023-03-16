package com.jason.escrap.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jason.escrap.Model.Alert;
import com.jason.escrap.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactInfoFragment extends Fragment {
    View view;
    @BindView(R.id.btn_go_next)
    Button btn_go_next;
    @BindView(R.id.edt_phone)
    EditText edt_phone_no;
    @BindView(R.id.edt_email)
    EditText edt_email;
    SharedPreferences.Editor editor;
    Alert alert;
    SharedPreferences sharedPreferences;
    @BindView(R.id.txt_differentEmail)
    TextView txt_different_email;
    @BindView(R.id.btn_go_back)
    Button btn_go_back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_contact_info_fragment, container, false);
        ButterKnife.bind(this, view);
        alert = new Alert(view.getContext());
        editor = view.getContext().getSharedPreferences("userInfo", getContext().MODE_PRIVATE).edit();
        sharedPreferences = getContext().getSharedPreferences("userInfo", getContext().MODE_PRIVATE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String userEmail = sharedPreferences.getString("userEmail", "N/A");
        edt_email.setText(userEmail);
        edt_email.setEnabled(false);

        txt_different_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_email.setEnabled(true);
            }
        });
        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment bf = new AddressFragment();
                fragmentTransaction.replace(R.id.intro_activity_container, bf);
                fragmentTransaction.commit();
            }
        });


        btn_go_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = edt_phone_no.getText().toString();
                String email=edt_email.getText().toString();
                if (isValid(phoneNo,email)) {
                    editor.putString("userPhoneNo", phoneNo);
                    editor.putString("userEmail",email);
                    editor.commit();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment bf = new IntroductionFragment();
                    fragmentTransaction.replace(R.id.intro_activity_container, bf);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    public boolean isValid(String phoneNo,String email) {
        if (phoneNo.isEmpty()) {
            alert.error_alert("Error", "Please enter phone number");
            return false;
        }else if(email.isEmpty()){
            alert.error_alert("Error","Please enter email address");
            return false;
        }
        return true;
    }
}
