package com.renbasi.quotloud.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.renbasi.quotloud.Main.Homescreen;
import com.renbasi.quotloud.Main.ResetPasswordActivity;
import com.renbasi.quotloud.R;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail,inputPassword;
    private Button loginButton,registerButton,resetpassswordButton;
    private ProgressDialog loginprogress;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        loginprogress = new ProgressDialog(this);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);
        registerButton = (Button) findViewById(R.id.register);
        resetpassswordButton = (Button) findViewById(R.id.resetpassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checklogin();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerIntent);

            }
        });
        resetpassswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerIntent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerIntent);

            }
        });


    }
    public final static boolean isValidEmail(CharSequence target)
    {
        if (TextUtils.isEmpty(target))
        {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void checklogin() {

        String Email_id = inputEmail.getText().toString().trim();
        String Password = inputPassword.getText().toString().trim();

        CharSequence temp_emilID=Email_id;//here username is the your edittext object...
        if(!isValidEmail(temp_emilID))
        {
            inputEmail.requestFocus();
            inputEmail.setError("Enter a valid emailid");

        }
        else
        {
            if(!TextUtils.isEmpty(Email_id) && !TextUtils.isEmpty(Password)){

                loginprogress.setMessage("Checking Login....");
                loginprogress.show();
                mAuth.signInWithEmailAndPassword(Email_id,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            loginprogress.dismiss();
                            checkUserExist();

                        }
                        else {
                            loginprogress.dismiss();
                            Toast.makeText(LoginActivity.this,"Error in login",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }


        }

    }

    private void checkUserExist() {

        final String User_id = mAuth.getCurrentUser().getUid();
        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(User_id)){

                    Intent mainIntent = new Intent(LoginActivity.this, Homescreen.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                }else
                {
                    Toast.makeText(LoginActivity.this,"You need to setup your account",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
