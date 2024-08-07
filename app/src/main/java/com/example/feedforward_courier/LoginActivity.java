package com.example.feedforward_courier;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.feedforward_courier.interfacea.ApiCallback;
import com.example.feedforward_courier.models.Courier;
import com.example.feedforward_courier.models.StatusOfCourier;
import com.example.feedforward_courier.models.server.object.ObjectBoundary;
import com.example.feedforward_courier.models.server.user.NewUserBoundary;
import com.example.feedforward_courier.models.server.user.RoleEnum;
import com.example.feedforward_courier.models.server.user.UserBoundary;
import com.example.feedforward_courier.models.server.user.UserSession;

import com.example.feedforward_courier.utils.Repository;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    private Button btnSignIn, btnSignUp;
    private Repository apiRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        apiRepository = Repository.getInstance();
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnSignIn.setOnClickListener(view -> showSignInDialog());

        btnSignUp.setOnClickListener(view -> showSignUpDialog());
    }

    private void showSignInDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.sign_in));

        final EditText inputEmail = new EditText(this);
        inputEmail.setHint("Email");
        inputEmail.setMaxLines(1);

        builder.setView(inputEmail);

        builder.setPositiveButton(getString(R.string.sign_in), null);

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button signInButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            signInButton.setOnClickListener(view -> {
                String email = inputEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
                    setErrorDrawable(inputEmail);
                    Toast.makeText(LoginActivity.this, getString(R.string.please_enter_a_valid_email), Toast.LENGTH_SHORT).show();
                } else {
                    clearErrorDrawable(inputEmail);
                    dialog.dismiss();
                    signIn(email);
                }
            });
        });

        dialog.show();
    }

    private void showSignUpDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View customView = inflater.inflate(R.layout.dialog_sign_up, null);

        final EditText inputEmail = customView.findViewById(R.id.input_email);
        final EditText inputName = customView.findViewById(R.id.input_name);
        final EditText inputPhone = customView.findViewById(R.id.input_phone);

        Button btnSignUp = customView.findViewById(R.id.btn_sign_up);
        Button btnCancel = customView.findViewById(R.id.btn_cancel);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(customView)
                .create();

        btnSignUp.setOnClickListener(view -> {
            String email = inputEmail.getText().toString().trim();
            String name = inputName.getText().toString().trim();
            String phone = inputPhone.getText().toString().trim();

            boolean isValid = true;

            if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
                setErrorDrawable(inputEmail);
                Toast.makeText(LoginActivity.this, getString(R.string.please_enter_a_valid_email), Toast.LENGTH_SHORT).show();
                isValid = false;
            } else {
                clearErrorDrawable(inputEmail);
            }
            if (TextUtils.isEmpty(name)) {
                setErrorDrawable(inputName);
                Toast.makeText(LoginActivity.this, getString(R.string.name_cannot_be_empty), Toast.LENGTH_SHORT).show();
                isValid = false;
            } else {
                clearErrorDrawable(inputName);
            }
            if (TextUtils.isEmpty(phone)) {
                setErrorDrawable(inputPhone);
                Toast.makeText(LoginActivity.this, getString(R.string.phone_cannot_be_empty), Toast.LENGTH_SHORT).show();
                isValid = false;
            } else {
                clearErrorDrawable(inputPhone);
            }

            if (isValid) {
                signUp(email, name, phone);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private boolean isValidEmail(CharSequence email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void signIn(String email) {
        apiRepository.getUser(email, new ApiCallback<UserBoundary>() {
            @Override
            public void onSuccess(UserBoundary userBoundary) {
                String objectId = userBoundary.getUserName();
                apiRepository.getSpecificObject(UserSession.getInstance().getSUPERAPP(), objectId, UserSession.getInstance().getSUPERAPP(), email, new ApiCallback<ObjectBoundary>() {
                    @Override
                    public void onSuccess(ObjectBoundary objectBoundary) {
                        if ("Courier".equals(objectBoundary.getType())) {
                            Toast.makeText(LoginActivity.this, getString(R.string.user_logged_in_successfully), Toast.LENGTH_SHORT).show();
                            UserSession.getInstance().setUser(userBoundary);
                            UserSession.getInstance().setCourier(new Courier(objectBoundary));
                            navigateToMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.user_is_not_a_courier_owner), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(LoginActivity.this, getString(R.string.failed_to_verify_user_role) + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, getString(R.string.failed_to_login) + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUp(String email, String name, String phone) {
        NewUserBoundary newUserBoundary = new NewUserBoundary();
        newUserBoundary.setUsername(name);
        newUserBoundary.setEmail(email);
        newUserBoundary.setRole(RoleEnum.SUPERAPP_USER);
        newUserBoundary.setAvatar("default_avatar");

        apiRepository.createUser(newUserBoundary, new ApiCallback<UserBoundary>() {
            @Override
            public void onSuccess(UserBoundary userResult) {
                Toast.makeText(LoginActivity.this, getString(R.string.user_created_successfully), Toast.LENGTH_SHORT).show();
                Courier courier = new Courier();
                courier.setCourierEmail(email);
                courier.setCourierName(name);
                courier.setCourierPhone(phone);
                courier.setStatus(StatusOfCourier.AVIALABLE); // Assuming you have a status field

                ObjectBoundary objectBoundary = courier.toObjectBoundary();

                apiRepository.createObject(objectBoundary, new ApiCallback<ObjectBoundary>() {
                    @Override
                    public void onSuccess(ObjectBoundary result) {
                        Toast.makeText(LoginActivity.this,getString(R.string.courier_created_successfully), Toast.LENGTH_SHORT).show();
                        UserBoundary updatedUser = userResult;
                        updatedUser.setUserName(result.getObjectId().getId());
                        updatedUser.setRole(RoleEnum.MINIAPP_USER);
                        apiRepository.updateUser(UserSession.getInstance().getSUPERAPP(),email, updatedUser, new ApiCallback<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                UserSession.getInstance().setUser(userResult);
                                UserSession.getInstance().setCourier(new Courier(result));
                                navigateToMainActivity();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(LoginActivity.this, getString(R.string.failed_to_update_user) + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(LoginActivity.this, getString(R.string.failed_to_create_courier) + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, getString(R.string.failed_to_create_user) + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setErrorDrawable(EditText editText) {
        Drawable errorDrawable = ContextCompat.getDrawable(this, R.drawable.ic_error);
        errorDrawable.setBounds(0, 0, errorDrawable.getIntrinsicWidth(), errorDrawable.getIntrinsicHeight());
        editText.setCompoundDrawables(null, null, errorDrawable, null);
    }

    private void clearErrorDrawable(EditText editText) {
        editText.setCompoundDrawables(null, null, null, null);
    }
}
