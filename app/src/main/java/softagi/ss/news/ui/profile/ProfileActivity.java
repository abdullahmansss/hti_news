package softagi.ss.news.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import softagi.ss.news.R;
import softagi.ss.news.models.UserModel;
import softagi.ss.news.ui.authentication.Authentication;
import softagi.ss.news.ui.main.MainActivity;
import softagi.ss.news.utils.Constants;

public class ProfileActivity extends AppCompatActivity
{
    TextView uidTxt, nameTxt, emailTxt, phoneTxt;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    CircleImageView circleImageView;
    Uri selectedImage;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
    }

    private void initViews()
    {
        uidTxt = findViewById(R.id.uid_txt);
        nameTxt = findViewById(R.id.name_txt);
        emailTxt = findViewById(R.id.email_txt);
        phoneTxt = findViewById(R.id.phone_txt);
        circleImageView = findViewById(R.id.profile_image);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        databaseReference.child("Users").child(Constants.getUid(ProfileActivity.this)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userModel = snapshot.getValue(UserModel.class);

                uidTxt.setText(userModel.getuId());
                nameTxt.setText(userModel.getName());
                emailTxt.setText(userModel.getEmail());
                phoneTxt.setText(userModel.getPhone());
                Picasso.get().load(userModel.getImage()).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 250);

//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 250);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 250 && resultCode == RESULT_OK)
        {
            selectedImage = data.getData();
            Picasso.get().load(selectedImage).into(circleImageView);

            uploadImage(selectedImage);
        }
    }

    private void uploadImage(Uri selectedImage)
    {
        UploadTask uploadTask;

        final StorageReference ref = storageReference.child("images/" + selectedImage.getLastPathSegment());
        uploadTask = ref.putFile(selectedImage);

        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                if (!task.isSuccessful())
                {
                    Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                Uri downloadUri = task.getResult();

                String selectedImageUrl = downloadUri.toString();

                createUser(userModel.getName(), userModel.getEmail(), userModel.getPhone(), userModel.getuId(), selectedImageUrl);
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception exception)
            {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUser(String name, String email, String phone, final String uid, String image)
    {
        UserModel userModel = new UserModel(uid, name, email, phone, image);
        databaseReference.child("Users").child(uid).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {

                } else
                {
                }
            }
        });
    }
}