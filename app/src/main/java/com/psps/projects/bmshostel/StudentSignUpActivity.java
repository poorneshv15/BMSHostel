package com.psps.projects.bmshostel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentSignUpActivity extends AppCompatActivity {

    EditText nameEt,usnEt,passwordEt;
    Button signupBtn;
    TextView emailTv;
    String email;
    CircleImageView profilePic;
    final  static String EMAIL="EMAIL";
    final String TAG="StudentSignUpActivity";
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private final int PICK_IMAGE_REQUEST = 1;
    private final int RESULT_CROP = 400;
    Uri contentUri=null;
    // Write a message to the database
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);
        initialize_views();
        Intent intent=getIntent();
        email=intent.getStringExtra(StudentSignUpActivity.EMAIL);
        emailTv.setText(email);
        mAuth=FirebaseAuth.getInstance();
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtils.class);
                startActivityForResult(gallery_Intent, PICK_IMAGE_REQUEST);

            }
        });

    }

    protected void initialize_views(){
        profilePic=(CircleImageView)findViewById(R.id.profilePicIv);
        emailTv=(TextView) findViewById(R.id.emailTv);
        nameEt=(EditText)findViewById(R.id.sNameEt);
        usnEt=(EditText)findViewById(R.id.sUsnEt);
        passwordEt=(EditText)findViewById(R.id.sPasswordEt);
        signupBtn=(Button)findViewById(R.id.signupBtn);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if(resultCode == Activity.RESULT_OK){
                //perform Crop on the Image Selected from Gallery
                performCrop(data.getStringExtra("picturePath"));
            }
        }

        if (requestCode == RESULT_CROP ) {
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                profilePic.setImageBitmap(selectedBitmap);
//                profilePic.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            contentUri= Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    //Onclick is set in the xml part itself
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
                            FirebaseUser user=task.getResult().getUser();
                            UserProfileChangeRequest request=new UserProfileChangeRequest
                                    .Builder()
                                    .setDisplayName(nameEt.getText().toString())
                                    .setPhotoUri(contentUri)
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
                            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                            newStudent student=new newStudent(nameEt.getText().toString(),email,usnEt.getText().toString());
                            myRef.child(user.getUid()).setValue(student);
                            user.sendEmailVerification();
                            mAuth.signOut();
                            startActivity(new Intent(StudentSignUpActivity.this,LoginActivity.class));
                            finish();

                        }

                        // ...
                    }
                });
    }

}
