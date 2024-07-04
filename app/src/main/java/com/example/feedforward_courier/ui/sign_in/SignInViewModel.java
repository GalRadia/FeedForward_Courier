package com.example.feedforward_courier.ui.sign_in;

import androidx.lifecycle.ViewModel;

import com.example.feedforward_courier.interfacea.ApiCallback;
import com.example.feedforward_courier.models.server.user.UserBoundary;
import com.example.feedforward_courier.utils.Repository;

public class SignInViewModel extends ViewModel {
    private Repository repository;

    public SignInViewModel() {
        repository = Repository.getInstance();

    }

    public void logIn(String email, ApiCallback<UserBoundary> userBoundaryApiCallback) {
        repository.getUser(email, userBoundaryApiCallback);
    }



    public void signUp(String email, String username, String avatar, ApiCallback<UserBoundary> callback) {
        repository.createUser(email, username, avatar, callback);

    }

    public void updateProfile(UserBoundary user, String id) {
        user.setUserName(id);
        repository.updateUser(user);
    }




}
