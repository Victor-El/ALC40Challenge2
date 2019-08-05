package com.victor.alc40challenge2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class AddTravel extends AppCompatActivity {
    private EditText mDestEditText, mDescEditText, mAmountEditText;
    private Button mSelectImage;
    private ImageView mImageView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private FirebaseStorage mStorage;
    private StorageReference mStoragRef;

    private final int RESULT_LOAD_IMAGE = 1;
    private Uri picsURI;

    private String imageUploadLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStorage = FirebaseStorage.getInstance();
        mStoragRef = mStorage.getReference();

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        mDestEditText = findViewById(R.id.destination_edit_text);
        mAmountEditText = findViewById(R.id.amount_edit_text);
        mDescEditText = findViewById(R.id.description_edit_text);
        mSelectImage = findViewById(R.id.add_image_btn);
        mImageView = findViewById(R.id.image_upload_view);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }

    private void uploadInfo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("dest", mDestEditText.getText().toString().trim());
        map.put("desc", mDescEditText.getText().toString().trim());
        map.put("amount", mAmountEditText.getText().toString().trim());
        map.put("link", imageUploadLink);

        mRef.push().setValue(map);
    }

    private String uploadImage() {
        if (!(picsURI == null)) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading ...");
            progressDialog.show();

            StorageReference ref = mStoragRef.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(picsURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddTravel.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                imageUploadLink = task.getResult().toString();
                                uploadInfo();
                            }
                        }
                    });
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddTravel.this, "Failure: " + e.toString(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int)progress+"%");
                }
            });
        } else {
            Toast.makeText(this, "No Image to Upload", Toast.LENGTH_SHORT).show();
        }
        return imageUploadLink;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            picsURI = data.getData();
            mImageView.setImageURI(picsURI);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cancel_travel) {
            finish();
        } else if(item.getItemId() == R.id.action_save_travel) {
            uploadImage();
        }
        return true;
    }
}
