package com.jason.escrap.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jason.escrap.Model.Alert;
import com.jason.escrap.R;
import com.jason.escrap.Utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpFragment extends Fragment {
    View view;
    @BindView(R.id.btn_go_next)
    Button btn_go_next;
    @BindView(R.id.edt_userName)
    EditText edt_user_name;
    @BindView(R.id.edt_userEmail)
    EditText edt_user_email;
    @BindView(R.id.edt_password)
    EditText edt_user_password;
    @BindView(R.id.edt_confirm_password)
    EditText edt_confirm_password;
    @BindView(R.id.txt_go_back)
    TextView btn_go_back;
    @BindView(R.id.btn_gmail_signin)
    SignInButton btn_gmail_signin;
    Alert alert;
    SharedPreferences.Editor editor;
    GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 101;
    private CallbackManager mcallbackManager;
    FirebaseAuth firebaseAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_signup_fragment, container, false);
        ButterKnife.bind(this, view);
        alert = new Alert(view.getContext());
        editor = view.getContext().getSharedPreferences("userInfo", getContext().MODE_PRIVATE).edit();
        firebaseAuth = FirebaseAuth.getInstance();

        mcallbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        btn_go_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edt_user_name.getText().toString();
                String userEmail = edt_user_email.getText().toString();
                String userPassword = edt_user_password.getText().toString();
                String userConfirmPasswrod = edt_confirm_password.getText().toString();
                if (isValid(userName, userEmail, userPassword, userConfirmPasswrod)) {
                    editor.putString("userName", userName);
                    editor.putString("userEmail", userEmail);
                    editor.putString("userPassword", userPassword);
                    editor.putString("googleSignin", "no");
                    editor.commit();

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment bf = new AddressFragment();
                    fragmentTransaction.replace(R.id.intro_activity_container, bf);
                    fragmentTransaction.commit();
                }
            }
        });

        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment bf = new UserTypeFragment();
                fragmentTransaction.replace(R.id.intro_activity_container, bf);
                fragmentTransaction.commit();
            }
        });

        btn_gmail_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    public boolean isValid(String userName, String Email, String Password, String confirmPassword) {
        if (userName.isEmpty() && Email.isEmpty() && Password.isEmpty() && confirmPassword.isEmpty()) {
            alert.error_alert("Error", "Please enter all details");
            return false;
        } else if (userName.isEmpty()) {
            alert.error_alert("Error", "Please enter name");
            return false;
        } else if (Email.isEmpty()) {
            alert.error_alert("Error", "Please enter email");
            return false;
        } else if (!Email.matches(Utils.emailPattern)) {
            alert.error_alert("Error", "Please enter valid email address");
            return false;
        } else if (Password.isEmpty()) {
            alert.error_alert("Error", "Please enter password");
            return false;
        } else if (Password.length() < 8) {
            alert.error_alert("Error", "Password length should be minimum eight");
            return false;
        } else if (!Password.matches(confirmPassword)) {
            alert.error_alert("Error", "Password doesn't match");
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                editor.putString("userName", account.getDisplayName());
                editor.putString("userEmail", account.getEmail());
                editor.putString("googleToken", account.getIdToken());
                editor.putString("googleSignin", "yes");
                editor.commit();


                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment bf = new AddressFragment();
                fragmentTransaction.replace(R.id.intro_activity_container, bf);
                fragmentTransaction.commit();

            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
