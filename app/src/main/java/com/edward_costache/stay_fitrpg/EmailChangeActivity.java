package com.edward_costache.stay_fitrpg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Edward Costache
 */
public class EmailChangeActivity extends AppCompatActivity {
    private EditText editTxtNewEmail, editTxtCurrentPassword;
    private Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_change);
        initViews();

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTxtNewEmail.getText().toString();
                String password = editTxtCurrentPassword.getText().toString();

                if (password.isEmpty())      //make sure password is not empty
                {
                    editTxtCurrentPassword.setError("Please enter a password!");
                    editTxtCurrentPassword.requestFocus();
                    return;
                }

                if (email.isEmpty())        //make sure email is not empty
                {
                    editTxtNewEmail.setError("Please enter an email address!");
                    editTxtNewEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())       //make sure the email matches a normal email pattern i.e name@exmaple.com
                {
                    editTxtNewEmail.setError("Please enter a valid new email address!");
                    editTxtNewEmail.requestFocus();
                }

                changeEmail(email, password);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * A function for validating the user before trying to change their email address
     * @param email The email address they want to change to
     * @param password The current password of their account
     */
    private void changeEmail(String email, String password) {
        //get the current user logged in and update their email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), password);
        //check if they have entered the current current password by attempting to re-authenticate them using the old password they have provided
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updateEmail(email)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EmailChangeActivity.this, "Email successfully changed!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EmailChangeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        editTxtNewEmail.setText("");
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    //current password is incorrect
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EmailChangeActivity.this, "You old password is not correct!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * A function for initializing the Views
     */
    private void initViews() {
        editTxtNewEmail = findViewById(R.id.emailChangeEditTxtEmail);
        editTxtCurrentPassword = findViewById(R.id.emailChangeEditTextTextPassword);
        btnChange = findViewById(R.id.emailChangeBtnChange);
    }
}