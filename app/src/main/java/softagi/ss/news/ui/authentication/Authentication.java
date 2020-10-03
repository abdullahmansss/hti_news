package softagi.ss.news.ui.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import softagi.ss.news.R;
import softagi.ss.news.models.UserModel;
import softagi.ss.news.ui.login.LoginActivity;
import softagi.ss.news.ui.main.MainActivity;
import softagi.ss.news.utils.Constants;

public class Authentication extends AppCompatActivity
{
    EditText nameField,emailField,passwordField,confirmField,phoneField,codeField;
    TextView already;
    Button registerBtn, goBtn;

    FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String hhh;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        if(auth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        setContentView(R.layout.activity_authentication);

        initViews();
    }

    private void initViews()
    {
//        progressDialog = new ProgressDialog(getApplicationContext());
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setCancelable(false);

        nameField = findViewById(R.id.name_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        confirmField = findViewById(R.id.confirm_password_field);
        phoneField = findViewById(R.id.phone_field);
        codeField = findViewById(R.id.code_field);

        registerBtn = findViewById(R.id.register_btn);
        goBtn = findViewById(R.id.go_btn);

        already = findViewById(R.id.login_screen_txt);

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPhoneNumberWithCode(hhh, codeField.getText().toString());
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = nameField.getText().toString();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String confirmPassword = confirmField.getText().toString();
                String phone = phoneField.getText().toString();

                if(name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty())
                {
                    Toast.makeText(Authentication.this, "enter a valid data", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!confirmPassword.equals(password))
                {
                    Toast.makeText(Authentication.this, "password don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                //progressDialog.show();
                authWithEmail(name, email, password, phone);
                //authWithPhone(phone);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential)
            {

            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                Toast.makeText(Authentication.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token)
            {
                hhh = verificationId;
                Toast.makeText(Authentication.this, verificationId, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void authWithPhone(String phone)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+2" + phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(Authentication.this, task.getResult().getUser().getUid(), Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(Authentication.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void authWithEmail(final String name, final String email, String password, final String phone)
    {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                //progressDialog.dismiss();

                if(task.isSuccessful())
                {
                    createUser(name, email, phone, task.getResult().getUser().getUid());
                } else
                    {
                        Toast.makeText(Authentication.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private void createUser(String name, String email, String phone, final String uid)
    {
        UserModel userModel = new UserModel(uid, name, email, phone, "");
        databaseReference.child("Users").child(uid).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Constants.saveUid(Authentication.this, uid);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else
                {
                    Toast.makeText(Authentication.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}