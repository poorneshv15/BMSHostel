package com.psps.projects.bmshostel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ProviderQueryResult;

public class Signup_ResetpassActivity extends AppCompatActivity {

    public static final String RESET_OR_CREATE ="RESET",TITLE="TITLE";
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    //FALSE=CREATE , TRUE=RESET
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__resetpass);
        Intent intent =getIntent();
        final boolean re_or_cr=intent.getBooleanExtra(RESET_OR_CREATE,false);
        getSupportActionBar().setTitle(intent.getStringExtra(TITLE));
        mAuth=FirebaseAuth.getInstance();
        Log.d("Signup_ResetpassActivit","onCreate");
        final ImageButton goIb=(ImageButton)findViewById(R.id.goIb);
        final EditText emailEt=(EditText)findViewById(R.id.emailInput);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        goIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String email=emailEt.getText().toString();
                if(re_or_cr){
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("RESET PASSWORD", "Email sent.");
                                        startActivity(new Intent(Signup_ResetpassActivity.this,LoginActivity.class));
                                        finish();
                                    }
                                    else{
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Signup_ResetpassActivity.this, "FAILED : "+((FirebaseAuthException) task.getException()).getErrorCode(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    //Toast.makeText(Signup_ResetpassActivity.this,"Reset mail sent to\n"+email,Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                            if(task.isSuccessful()){
                                ///////// getProviders() will return size 1. if email ID is available.
                                try{
                                    if(task.getResult().getProviders().size()!=0){
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Signup_ResetpassActivity.this, "ACCOUNT EXISTS ON EMAIL "+email, Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Intent intent=new Intent(Signup_ResetpassActivity.this,StudentSignUpActivity.class);
                                        intent.putExtra(StudentSignUpActivity.EMAIL,email);
                                        startActivity(intent);
                                    }
                                }
                                catch(NullPointerException e){
                                    Toast.makeText(Signup_ResetpassActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }

            }
        });


    }


    public void signUp(View v){
        Log.d("Signup_ResetpassActivit","signUp");
        startActivity(new Intent(Signup_ResetpassActivity.this,HosteliteSignupActivity.class));
    }
}
