package io.github.shivams112.echat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    // Constants
    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";

    // TODO: Add member variables here:
    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView =  findViewById(R.id.register_email);
        mPasswordView =  findViewById(R.id.register_password);
        mConfirmPasswordView =  findViewById(R.id.register_confirm_password);
        mUsernameView =  findViewById(R.id.register_username);

        // Keyboard sign in action
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        // TODO: Get hold of an instance of FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public void signUpFinal(View view){
            attemptRegistration();
    }

    private void attemptRegistration() {

        // Reset errors displayed in the form.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if(!mConfirmPasswordView.getText().toString().equals(mPasswordView.getText().toString()))
        {
            mConfirmPasswordView.setError(getString(R.string.password_not_match));
            focusView = mConfirmPasswordView;
            cancel = true;

        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        else if(!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.password_too_small));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            fireBaseUser();


        }
    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Add own logic to check for a valid password (minimum 6 characters)
        if(password.length()<6)
        return false;

        else
            return true;
    }

    // TODO: Create a Firebase user
       public void fireBaseUser(){
        String email = mEmailView.getText().toString();
        String pass  = mPasswordView.getText().toString();
        Log.d("Echat" , "Email : "+email +"\n Password: "+pass);
        mFirebaseAuth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Echat" , "User registered successfully "+ task.isSuccessful());
                if(!task.isSuccessful()){
                    Log.d("Echat" ,"Failed");
                    alertDialogue("Registration Failed !");
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    saveDisplayName();
                   // FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    //updateUI(user);
                    Intent intent = new Intent(RegisterActivity.this , LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });

       }

    // TODO: Save the display name to Shared Preferences
    private void saveDisplayName(){
        String user = mUsernameView.getText().toString();
        SharedPreferences preferences = getSharedPreferences(CHAT_PREFS ,0);
        preferences.edit().putString(DISPLAY_NAME_KEY ,user).apply();
    }


    // TODO: Create an alert dialog to show in case registration failed
    private void alertDialogue(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok ,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
