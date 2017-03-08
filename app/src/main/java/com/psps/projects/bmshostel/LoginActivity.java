package com.psps.projects.bmshostel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity  implements
        GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText emailEt,passwordEt;
    private GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN=11;
    final String TAG="MAIN ACTIVITY";
    ProgressBar progressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt=(EditText)findViewById(R.id.emailEt);
        passwordEt=(EditText)findViewById(R.id.passwordEt);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        findViewById(R.id.forgotPassTv).setOnClickListener(this);
        findViewById(R.id.createAccTv).setOnClickListener(this);
        findViewById(R.id.signInBtn).setOnClickListener(this);
        findViewById(R.id.googleSignInBtn).setOnClickListener(this);


        new GoogleInit().execute();
        // Configure Google Sign In

        mAuth = FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    DatabaseReference checkwarden= FirebaseDatabase.getInstance().getReference("wardens");
                    checkwarden.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                            Log.d(TAG, "User name: " + user.getDisplayName() + ", email " + user.getEmail());
                            //Check if the user is warden or not
                            SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);
                            if(dataSnapshot.exists()){
                                Log.d(TAG," The user is warden");
                                preferences.edit().putBoolean("student",false).apply();
                                startActivity(new Intent(LoginActivity.this,WardenHomeActivity.class));
                                finish();
                            }
                            else{
                                Log.d(TAG," The user is student");
                                if(user.isEmailVerified()){
                                    preferences.edit().putBoolean("student",true).apply();
                                    startActivity(new Intent(LoginActivity.this,StudentHomeActivity.class));
                                    finish();
                                }
                                else
                                    Toast.makeText(LoginActivity.this, "Please verify your email and come back!", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "Failed to read value.", databaseError.toException());
                        }
                    });
                }

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void signInWithPassword(View v){

            String email,password;
            email=emailEt.getText().toString();
            password=passwordEt.getText().toString();
            if(!(email.isEmpty() && password.isEmpty()))
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email,password )
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("LOGIN ACTIVITY", "signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w("LOGIN ACTIVITY", "signInWithEmail:failed", task.getException());
                                    Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                            Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }

                                // ...
                            }
                        });


    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("LOGIN ACTIVITY", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LOGIN ACTIVITY", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if(!task.isSuccessful()) {
                            Log.w("LOGIN ACTIVITY", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private class GoogleInit extends AsyncTask  {

        @Override
        protected Object doInBackground(Object[] params) {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.web_client_id))
                    .requestEmail()
                    .build();
            // [END config_signin]
            mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                    .enableAutoManage(LoginActivity.this /* FragmentActivity */, LoginActivity.this/* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgotPassTv:
                Intent resetIntent =new Intent(LoginActivity.this,Signup_ResetpassActivity.class);
                resetIntent.putExtra(Signup_ResetpassActivity.RESET_OR_CREATE,true);
                resetIntent.putExtra(Signup_ResetpassActivity.TITLE,"RESET PASSWORD");
                startActivity(resetIntent);
                break;
            case R.id.createAccTv:
                Intent createAccIntent =new Intent(LoginActivity.this,Signup_ResetpassActivity.class);
                createAccIntent.putExtra(Signup_ResetpassActivity.RESET_OR_CREATE,false);
                createAccIntent.putExtra(Signup_ResetpassActivity.TITLE,"SIGN UP");
                startActivity(createAccIntent);
                break;
            case R.id.signInBtn:
                //validate();
                signInWithPassword(v);
                break;
            case R.id.googleSignInBtn:
                Log.d("LOGIN ACTIVITY", "signInWithGoogle");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
        }
    }

    private void validate() {

    }
}
