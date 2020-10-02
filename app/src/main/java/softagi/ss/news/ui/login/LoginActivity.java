package softagi.ss.news.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import softagi.ss.news.R;
import softagi.ss.news.ui.authentication.Authentication;
import softagi.ss.news.ui.main.MainActivity;
import softagi.ss.news.utils.Constants;

public class LoginActivity extends AppCompatActivity {
    EditText emailField,passwordField;
    Button loginBtn;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews()
    {
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);

        loginBtn = findViewById(R.id.login_btn);

        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if(email.isEmpty() || password.isEmpty() )
                {
                    Toast.makeText(LoginActivity.this, "enter a valid data", Toast.LENGTH_SHORT).show();
                    return;
                }

                authWithEmail(email, password);
            }
        });
    }

    private void authWithEmail(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                //progressDialog.dismiss();

                if(task.isSuccessful())
                {
                    Constants.saveUid(LoginActivity.this, task.getResult().getUser().getUid());

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    Toast.makeText(LoginActivity.this, task.getResult().getUser().getUid(), Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}