package com.jason.escrap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jason.escrap.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthenticationFragment extends Fragment {
    @BindView(R.id.btn_join_us)
    Button btn_join_us;
    View view;
    @BindView(R.id.btn_goto_login)
    Button btn_goto_login;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_authentication_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_join_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment bf = new UserTypeFragment();
                fragmentTransaction.replace(R.id.intro_activity_container, bf);
                fragmentTransaction.commit();

            }
        });
        btn_goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment bf = new LoginFragment();
                fragmentTransaction.replace(R.id.intro_activity_container, bf);
                fragmentTransaction.commit();
            }
        });
    }
}
