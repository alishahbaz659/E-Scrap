package com.jason.escrap.Fragments.chat.users.getall;



import com.jason.escrap.Model.Users;

import java.util.List;


public interface GetUsersContract {
    interface View {
        void onGetAllUsersSuccess(List<Users> users);

        void onGetAllUsersFailure(String message);

        void onGetChatUsersSuccess(List<Users> users);

        void onGetChatUsersFailure(String message);
    }

    interface Presenter {
        void getAllUsers();

        void getChatUsers();
    }

    interface Interactor {
        void getAllUsersFromFirebase();

        void getChatUsersFromFirebase();
    }

    interface OnGetAllUsersListener {
        void onGetAllUsersSuccess(List<Users> users);

        void onGetAllUsersFailure(String message);
    }

    interface OnGetChatUsersListener {
        void onGetChatUsersSuccess(List<Users> users);

        void onGetChatUsersFailure(String message);
    }
}
