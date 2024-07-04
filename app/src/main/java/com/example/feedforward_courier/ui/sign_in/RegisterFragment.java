package com.example.feedforward_courier.ui.sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.feedforward_courier.databinding.FragmentRegisterBinding;
import com.example.feedforward_courier.interfacea.ApiCallback;
import com.example.feedforward_courier.models.server.object.ObjectBoundary;
import com.example.feedforward_courier.models.server.object.ObjectId;
import com.example.feedforward_courier.models.server.user.UserBoundary;
import com.example.feedforward_courier.models.server.user.UserSession;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.Arrays;


public class RegisterFragment extends Fragment {
    private SignInViewModel signInViewModel;
    private FragmentRegisterBinding binding;
    private TextInputEditText emailEditText;
    private TextInputEditText usernameEditText;
    private TextInputEditText phoneEditText;
    private TextInputEditText AddressEditText;
    private ExtendedFloatingActionButton registerButton;
    private PlacesClient placesClient;
    private LatLng latLng;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        findViews();
        return root;


    }

    private void findViews() {
        emailEditText = binding.registerEmail;
        usernameEditText = binding.registerName;
        registerButton = binding.registerFinishBTN;
        phoneEditText = binding.associationPhone;
        AddressEditText = binding.associationAddress;
        initViess();
    }

    private void initViess() {
        // initializePlaces();
        AddressEditText.setEnabled(false);
        registerButton.setOnClickListener(v -> {
            if (!validate()) {
                return;
            }
            String email = emailEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String avatar = "DEFAULT_AVATAR";
            signInViewModel.signUp(email, username, avatar, new ApiCallback<UserBoundary>() {
                @Override
                public void onSuccess(UserBoundary userBoundary) {
                    Toast.makeText(getActivity(), "User created successfully", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getActivity(), "Email allready exists", Toast.LENGTH_SHORT).show();
                    Log.e("RegisterFragment", "onError: " + error);

                }
            });

        });
    }

    private boolean validate() {
        if (TextUtils.isEmpty(emailEditText.getText().toString())) {
            emailEditText.setError("Email is required");
            return false;
        }
        if (TextUtils.isEmpty(usernameEditText.getText().toString())) {
            usernameEditText.setError("Username is required");
            return false;
        }
        if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
            phoneEditText.setError("Phone is required");
            return false;
        }
        if (TextUtils.isEmpty(AddressEditText.getText().toString())) {
            AddressEditText.setError("Address is required");
            return false;
        }
        return true;
    }


}

