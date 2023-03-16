package com.jason.escrap.Fragments;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.anirudh.locationfetch.EasyLocationFetch;
import com.anirudh.locationfetch.GeoLocationModel;
import com.jason.escrap.Model.Alert;
import com.jason.escrap.R;
import com.jason.escrap.Utils.Utils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressFragment extends Fragment {
    View view;
    @BindView(R.id.btn_go_next)
    Button btn_go_next;
    @BindView(R.id.edt_address)
    EditText edt_address;
    Alert alert;
    SharedPreferences.Editor editor;
    @BindView(R.id.btn_go_back)
    Button btn_go_back;
    GeoLocationModel geoLocationModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_address_fragment, container, false);
        ButterKnife.bind(this, view);
        alert = new Alert(view.getContext());
        editor = view.getContext().getSharedPreferences("userInfo", getContext().MODE_PRIVATE).edit();
        geoLocationModel = new EasyLocationFetch(view.getContext(), Utils.MapKey).getLocationData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            edt_address.setText(geoLocationModel.getAddress());
        }

        btn_go_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edt_address.getText().toString();
                if (isValid(address)) {
                    editor.putString("userAddress", address);
                    editor.commit();

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment bf = new ContactInfoFragment();
                    fragmentTransaction.replace(R.id.intro_activity_container, bf);
                    fragmentTransaction.commit();
                }

            }
        });
        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment bf = new SignUpFragment();
                fragmentTransaction.replace(R.id.intro_activity_container, bf);
                fragmentTransaction.commit();
            }
        });
    }

    public boolean isValid(String address) {
        if (address.isEmpty()) {
            alert.error_alert("Error", "Please enter address");
            return false;
        }
        return true;
    }
}
