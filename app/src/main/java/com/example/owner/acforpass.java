package com.example.owner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class acforpass extends AppCompatActivity {
    Button forback,forchngepassword;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acforpass);
        forback=(Button)findViewById(R.id.forgetback);
        editText=(EditText)findViewById(R.id.resetemail);
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=(ProgressBar)findViewById(R.id.progress);
        forchngepassword=(Button)findViewById(R.id.forgetchngepass);
        forback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(acforpass.this,signin.class);
                startActivity(intent);
            }
        });
        forchngepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.sendPasswordResetEmail(editText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(acforpass.this);
                            builder.setMessage("\t\tEmail Send Successfully:) \n\nPLEASE check your email to change your password:)").setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int id) {
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int id) {
                                            Toast.makeText(acforpass.this,"Please Check your Email",Toast.LENGTH_LONG).show();
                                        }
                                    });
                            final AlertDialog alert = builder.create();
                            alert.show();
                            progressBar.setVisibility(View.GONE);
                        }else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(acforpass.this);
                            builder.setMessage("Email Not Send!\n" + task.getException().getMessage()).setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int id) {
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int id) {
                                        }
                                    });
                            final AlertDialog alert = builder.create();
                            alert.show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
                //Toast.makeText(acforpass.this,"Chk Email for Reset Password",Toast.LENGTH_LONG).show();
            }
        });
    }
}
