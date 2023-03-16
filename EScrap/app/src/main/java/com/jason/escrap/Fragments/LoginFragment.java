package com.jason.escrap.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jason.escrap.Database.Registration;
import com.jason.escrap.Model.Alert;
import com.jason.escrap.R;
import com.jason.escrap.Utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment {
    View view;
    @BindView(R.id.btn_go_back)
    TextView btn_go_back;
    @BindView(R.id.btn_gmail_signin)
    SignInButton google_signIn;
    private static final int RC_SIGN_IN = 101;
    private CallbackManager mcallbackManager;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    Alert alert;
    Registration registration;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.edt_userEmail)
    EditText edt_userEmail;
    @BindView(R.id.edt_password)
    EditText edt_password;
    ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_login_fragment, container, false);
        ButterKnife.bind(this, view);
        alert = new Alert(view.getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        mcallbackManager = CallbackManager.Factory.create();
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        registration = new Registration(view.getContext());
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
        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment bf = new AuthenticationFragment();
                fragmentTransaction.replace(R.id.intro_activity_container, bf);
                fragmentTransaction.commit();
            }
        });

        google_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserEmail = edt_userEmail.getText().toString();
                String UserPassword = edt_password.getText().toString();
                if (isValid(UserEmail, UserPassword)) {
                    pDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(UserEmail, UserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                registration.checkEmailverification(firebaseAuth);
                                pDialog.dismiss();
                            } else {
                                alert.error_alert("Invalid login", "Please enter correct email and password");
                                pDialog.dismiss();
                            }
                        }
                    });
                }   
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                registration.firebaseloginwithGoogle(firebaseAuth, account.getIdToken());
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }


    }

    public boolean isValid(String userEmail, String userPassword) {
        if (userEmail.isEmpty() && userPassword.isEmpty()) {
            alert.error_alert("Error", "Please enter login details");
            return false;
        } else if (userEmail.isEmpty()) {
            alert.error_alert("Error", "Email should not be empty");
            return false;
        } else if (!userEmail.matches(Utils.emailPattern)) {
            alert.error_alert("Error", "Please enter valid email address");
            return false;
        } else if (userPassword.isEmpty()) {
            alert.error_alert("Error", "Please enter password");
            return false;
        }
        return true;
    }


}
