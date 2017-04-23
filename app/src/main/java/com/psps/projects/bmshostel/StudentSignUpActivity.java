package com.psps.projects.bmshostel;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import firebaseclasses.NewStudentFirebase;
import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

public class StudentSignUpActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1,CROP_IMAGE=2;
    EditText nameEt,usnEt,passwordEt;
    Button signupBtn;
    TextView emailTv;
    String email;
    File actualImage,compressedImage;
    final  static String EMAIL="EMAIL";
    final String TAG="StudentSignUpActivity";
    CircleImageView profilePic;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    File image;
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
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        mAuth=FirebaseAuth.getInstance();

    }

    protected void initialize_views(){
        profilePic=(CircleImageView)findViewById(R.id.profilePicIv);
        emailTv=(TextView) findViewById(R.id.roomNoTv);
        nameEt=(EditText)findViewById(R.id.sNameEt);
        usnEt=(EditText)findViewById(R.id.sUsnEt);
        passwordEt=(EditText)findViewById(R.id.sPasswordEt);
        signupBtn=(Button)findViewById(R.id.signupBtn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK){
            try {
                actualImage = FileUtil.from(this, data.getData());
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(data.getData(), "image/*");

                intent.putExtra("crop", "true");
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scaleUpIfNeeded", true);
                intent.putExtra("return-data", true);

                startActivityForResult(intent, CROP_IMAGE);

            } catch (ActivityNotFoundException e) {
                Log.d(TAG,e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode==CROP_IMAGE && resultCode==RESULT_OK){
            new AsyncTask<Intent,Void,String>() {
                @Override
                protected void onPostExecute(String val) {
                    super.onPostExecute(val);
                    profilePic.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));
                    Log.d(TAG,"Compression after "+String.format("Size : %s", getReadableFileSize(compressedImage.length())));

                }

                @Override
                protected String doInBackground(Intent[] params) {
                     image = null;
                    try {
                        Log.d(TAG,"Compression Before "+String.format("Size : %s", getReadableFileSize(actualImage.length())));
                        // get the returned data
                        Bundle extras = params[0].getExtras();

                        // get the cropped bitmap
                        Bitmap photo = extras.getParcelable("data");
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        String path = MediaStore.Images.Media.insertImage(StudentSignUpActivity.this.getContentResolver(), photo, null, null);
                        Log.d(TAG,path);
                         image = FileUtil.from(StudentSignUpActivity.this,Uri.parse(path));
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(image));
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    compressedImage = new Compressor.Builder(StudentSignUpActivity.this)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .build()
                            .compressToFile(image);

                    return null;
                }
            }.execute(data);

        }
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
                            final FirebaseUser user=task.getResult().getUser();

                            if(compressedImage!=null)
                                uploadDataWithDp(user);
                            else
                                uploadDataWithNoDp(user);

                            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());


                        }

                        // ...
                    }

                    private void uploadDataWithNoDp(final FirebaseUser user) {
                        final NewStudentFirebase student = new NewStudentFirebase(nameEt.getText().toString(), email, usnEt.getText().toString());
                        UserProfileChangeRequest request = new UserProfileChangeRequest
                                .Builder()
                                .setDisplayName(nameEt.getText().toString())
                                .build();
                        user.updateProfile(request)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        myRef.child(user.getUid()).setValue(student);
                                        user.sendEmailVerification();
                                        Intent intent = new Intent(StudentSignUpActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                        Log.d(TAG, "User Name updated.");
                                    }
                                });
                    }

                    private void uploadDataWithDp(final FirebaseUser user) {
                        Log.d(TAG,"Compressed Image Path:"+ image.getAbsolutePath());
                        Uri file = Uri.fromFile(new File(String.valueOf(image)));
                        StorageReference picRef = FirebaseStorage.getInstance().getReference().child("profilePic/"+user.getEmail());
                        StorageMetadata picDetails= new StorageMetadata.Builder()
                                .setContentType("image/"+image.getAbsolutePath().substring(image.getAbsolutePath().lastIndexOf(".")))
                                .setCustomMetadata("name",nameEt.getText().toString())
                                .build();
                        UploadTask uploadTask=picRef.putFile(file,picDetails);
                        Log.d(TAG,"Image type: "+image.getAbsolutePath().substring(image.getAbsolutePath().lastIndexOf(".")));
                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains image metadata such as size, content-type, and download URL.
                                //noinspection VisibleForTests
                                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                UserProfileChangeRequest request=new UserProfileChangeRequest
                                        .Builder()
                                        .setDisplayName(nameEt.getText().toString())
                                        .setPhotoUri(downloadUrl)
                                        .build();
                                user.updateProfile(request)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    NewStudentFirebase student;
                                                    if (downloadUrl != null) {
                                                        student=new NewStudentFirebase(nameEt.getText().toString(),email,usnEt.getText().toString(),downloadUrl.toString());
                                                    }
                                                    else {
                                                        student=new NewStudentFirebase(nameEt.getText().toString(),email,usnEt.getText().toString());
                                                    }

                                                    myRef.child(user.getUid()).setValue(student);
                                                    user.sendEmailVerification();
                                                    Intent intent=new Intent(StudentSignUpActivity.this,LoginActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                    Log.d(TAG, "User Name updated.");
                                                }
                                            }
                                        });
                            }
                        });

                    }
                });
    }
    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
