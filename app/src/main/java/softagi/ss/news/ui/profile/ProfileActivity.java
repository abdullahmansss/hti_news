package softagi.ss.news.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import softagi.ss.news.R;
import softagi.ss.news.models.UserModel;
import softagi.ss.news.utils.Constants;

public class ProfileActivity extends AppCompatActivity
{
    TextView uidTxt, nameTxt, emailTxt, phoneTxt;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Users").child(Constants.getUid(ProfileActivity.this)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                UserModel userModel = snapshot.getValue(UserModel.class);

                uidTxt.setText(userModel.getuId());
                nameTxt.setText(userModel.getName());
                emailTxt.setText(userModel.getEmail());
                phoneTxt.setText(userModel.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}