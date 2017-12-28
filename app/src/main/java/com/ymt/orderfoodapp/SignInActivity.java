package com.ymt.orderfoodapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ymt.orderfoodapp.common.Common;
import com.ymt.orderfoodapp.model.User;

public class SignInActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone=(MaterialEditText) findViewById(R.id.edtPhone);
        edtPassword=(MaterialEditText) findViewById(R.id.edtPassword);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);

        //Innit Firebase
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog=new ProgressDialog(SignInActivity.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Check if user not exist in database
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            //Get User information
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                //Toast.makeText(SignInActivity.this, "Sign in Succsessfully !!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(SignInActivity.this,  HomeActivity.class);
                                Common.currentUser=user;
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignInActivity.this, "Wrong Password !!", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            mDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "User not exist in database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
