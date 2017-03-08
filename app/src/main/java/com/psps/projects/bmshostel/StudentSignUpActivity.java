package com.psps.projects.bmshostel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentSignUpActivity extends AppCompatActivity {

    EditText nameEt,usnEt,passwordEt;
    Button signupBtn;
    TextView emailTv;
    String email;
    final  static String EMAIL="EMAIL";
    final String TAG="StudentSignUpActivity";
    FirebaseAuth mAuth;
    boolean signout=false;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);
        initialize_views();
        Intent intent=getIntent();
        email=intent.getStringExtra(StudentSignUpActivity.EMAIL);
        emailTv.setText(email);

        mAuth=FirebaseAuth.getInstance();
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                String name=nameEt.getText().toString(),usn=usnEt.getText().toString();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && !name.isEmpty()) {
                    // User is signed in
                    String uid=user.getUid();
                    UserProfileChangeRequest request=new UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(name)
                            .build();
                    user.updateProfile(request)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User Name updated.");
                                    }
                                }
                            });
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + uid);
                    newStudent student=new newStudent(usn);
                    myRef.child(uid).setValue(student);
                    user.sendEmailVerification();
                } else {
                    // User is signed out
                    if(signout)
                        progressDialog.setMessage("Account Created");
                        Log.d(TAG, "onAuthStateChanged:signed_out");

                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    protected void initialize_views(){
        emailTv=(TextView) findViewById(R.id.emailTv);
        nameEt=(EditText)findViewById(R.id.sNameEt);
        usnEt=(EditText)findViewById(R.id.sUsnEt);
        passwordEt=(EditText)findViewById(R.id.sPasswordEt);
        signupBtn=(Button)findViewById(R.id.signupBtn);
    }

    public void signUp(View v){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        final String password= passwordEt.getText().toString();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(StudentSignUpActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(StudentSignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                                        Toast.makeText(StudentSignUpActivity.this, R.string.auth_failed,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        signout=true;
                                        mAuth.signOut();
                                        Toast.makeText(StudentSignUpActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(StudentSignUpActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(StudentSignUpActivity.this,LoginActivity.class));
                                        finish();
                                    }
                                }
                            });
                        }

                        // ...
                    }
                });
    }

}
