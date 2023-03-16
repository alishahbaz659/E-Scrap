package com.jason.escrap.Fragments.chat.users.getall;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jason.escrap.Model.Message_users;
import com.jason.escrap.Model.Users;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;


public class GetUsersInteractor implements GetUsersContract.Interactor {
    private static final String TAG = "GetUsersInteractor";
    FirebaseAuth firebaseAuth;
    private List<String> userslist;
    private GetUsersContract.OnGetAllUsersListener mOnGetAllUsersListener;
    FirebaseUser fuser;
    DatabaseReference reference;
    private List<Users> users;

    public GetUsersInteractor(GetUsersContract.OnGetAllUsersListener onGetAllUsersListener) {
        this.mOnGetAllUsersListener = onGetAllUsersListener;
    }


    @Override
    public void getAllUsersFromFirebase() {

        userslist = new ArrayList<>();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Message_users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userslist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message_users message_users = snapshot.getValue(Message_users.class);
                    if (message_users.getSenderUid().equals(fuser.getUid())) {
                        userslist.add(message_users.getReceiverUid());
                    }
                    if (message_users.getReceiverUid().equals(fuser.getUid())) {
                        userslist.add(message_users.getSenderUid());
                    }
                }
                readchats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void readchats() {
        users = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    for (String id : userslist) {
                        if (user.getUid().equals(id)) {
                            if (users.size() != 0) {
                                for (Users user1 : users) {
                                    if (!user.getUid().equals(user1.getUid())) {
                                        users.add(user);
                                    }
                                }
                            } else {
                                users.add(user);
                            }
                        }
                    }
                }
                mOnGetAllUsersListener.onGetAllUsersSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mOnGetAllUsersListener.onGetAllUsersFailure(databaseError.getMessage());
            }
        });

    }

    @Override
    public void getChatUsersFromFirebase() {

    }
}
