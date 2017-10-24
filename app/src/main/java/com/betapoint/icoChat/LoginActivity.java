package com.betapoint.icoChat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;


public class LoginActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private FirebaseAuth _auth;

    public static final String TAG = "sign in:::";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // TODO: Grab an instance of FirebaseAuth
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.valueOf("DEBUG"));
        _auth = FirebaseAuth.getInstance();

    }

    // Executed when Sign in button pressed
    public void signInExistingUser(View v)   {
        // TODO: Call attemptLogin() here
        attemptLogin();

    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.betapoint.icoChat.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    // TODO: Complete the attemptLogin() method
    private void attemptLogin() {

        String emailAddr = mEmailView.getText().toString();
        String passAddr = mPasswordView.getText().toString();

        if ((emailAddr.length() == 0) || (passAddr.length() == 0)) {
            return;
        }
        else {
            CharSequence msg = "login in progress";
            Toast loginToast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
            loginToast.show();

            _auth.signInWithEmailAndPassword(emailAddr, passAddr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "sign in returned, login was " + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Log.d(TAG, "the sign in was not successful " + task.getException());

                        showErrorDialog("There was an error logging you in");
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), MainChatActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }
            });
        }

        // TODO: Use FirebaseAuth to sign in with email & password


    }

    private void showErrorDialog(String msg) {
        AlertDialog.Builder alDialog = new AlertDialog.Builder(this);
        alDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alDialog.setPositiveButton(android.R.string.ok, null);
        alDialog.setTitle("Oops");
        alDialog.setMessage(msg);
        alDialog.show();
    }

    // TODO: Show error on screen with an alert dialog



}