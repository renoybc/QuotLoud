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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.renbasi.quotloud.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputPassword,inputFirst_Name,inputLast_Name,inputEmail_id,inputMobile_Number,inputConfirm_Password;
    private Button btnSignUp;
    private ProgressDialog registerProgress;
    private FirebaseAuth mauth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get Firebase auth instance
        mauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        registerProgress = new ProgressDialog(this);


        btnSignUp = (Button) findViewById(R.id.regpg_btn_reg);
        inputEmail_id = (EditText) findViewById(R.id.regpg_email);
        inputPassword = (EditText) findViewById(R.id.regpg_password);
        inputFirst_Name = (EditText) findViewById(R.id.regpg_first_name);
        inputLast_Name = (EditText) findViewById(R.id.regpg_last_name);
        inputMobile_Number = (EditText) findViewById(R.id.regpg_mob_no);
        inputConfirm_Password = (EditText) findViewById(R.id.regpg_re_password);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String First_Name = inputFirst_Name.getText().toString().trim();
                final String Last_Name = inputLast_Name.getText().toString().trim();
                String Email_id = inputEmail_id.getText().toString().trim();
                final String Mobile_Number = inputMobile_Number.getText().toString().trim();
                String Password = inputPassword.getText().toString().trim();
                String Confirm_Password = inputConfirm_Password.getText().toString().trim();

                if (!TextUtils.isEmpty(First_Name) && !TextUtils.isEmpty(Last_Name) && !TextUtils.isEmpty(Email_id) && !TextUtils.isEmpty(Mobile_Number) && !TextUtils.isEmpty(Password) && !TextUtils.isEmpty(Confirm_Password)){

                    Toast.makeText(RegisterActivity.this,"some",Toast.LENGTH_SHORT).show();
                    registerProgress.setMessage("Signing Up.....");
                    registerProgress.show();


                    mauth.createUserWithEmailAndPassword(Email_id ,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                String User_id = mauth.getCurrentUser().getUid();

                                DatabaseReference Current_User_db = mDatabase.child(User_id);
                                Current_User_db.child("First_Name").setValue(First_Name);
                                Current_User_db.child("Last_Name").setValue(Last_Name);
                                Current_User_db.child("Image").setValue("defult");
                                Current_User_db.child("Mobile_Number").setValue(Mobile_Number);

                                registerProgress.dismiss();

                                Intent mIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mIntent);
                            }

                        }
                    });

                }
            }
        });


    }


}
