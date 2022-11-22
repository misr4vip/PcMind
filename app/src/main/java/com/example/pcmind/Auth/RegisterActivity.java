package com.example.pcmind.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pcmind.Clients.MainActivity;
import com.example.pcmind.Model.MyUser;
import com.example.pcmind.Model.User;
import com.example.pcmind.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    EditText name,email,mobile,pwd,repwd;
    TextView signin;
    Button signup;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String userName,userEmail,userMobile,userPwd,userRePwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.etFullNameRegisterActivity);
        email = findViewById(R.id.etUserNameRegisterActivity);
        mobile = findViewById(R.id.etMobileRegisterActivity);
        pwd = findViewById(R.id.etPasswordRegisterActivity);
        repwd = findViewById(R.id.etRePasswordRegisterActivity);
        signin = findViewById(R.id.tvloginRegisterActivity);
        signup = findViewById(R.id.btnRegisterRegisterActivity);
         database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        signin.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        });

        signup.setOnClickListener(view -> {

            userName = name.getText().toString();
            userEmail = email.getText().toString();
            userMobile = mobile.getText().toString();
            userPwd = pwd.getText().toString();
            userRePwd = repwd.getText().toString();
            HashMap<Boolean,String> Validate = Validation();
            if (Validate.isEmpty())
            {
                auth.createUserWithEmailAndPassword(userEmail,userPwd).addOnCompleteListener(this::onComplete);
            }else{

               Validate.forEach((error,errorString) ->{

                   if (error)
                   {
                       Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
                   }
               });
            }

        });

    }

    private HashMap<Boolean,String> Validation()
    {

        HashMap<Boolean,String> process = new HashMap<>();
        if (userName.isEmpty() || userEmail.isEmpty() || userPwd.isEmpty() || userRePwd.isEmpty())
        {
            process.put(true,"Non Optional Can not be Empty");
        }
        if (userPwd.compareTo(userRePwd) != 0)
        {
            process.put(true,"Password and Repassed Not Equal");
        }

        return process;
    }

    private void onComplete(Task<AuthResult> AuthResult) {
        if (AuthResult.isSuccessful()) {
            DatabaseReference ref = database.getReference();
            String id = AuthResult.getResult().getUser().getUid().toString();

            MyUser user = new MyUser(id,userName,userEmail,userMobile,false,true);
            ref.child("Users").child(id).setValue(user).addOnCompleteListener(result -> {
                if (result.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User Added Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Error Occurred Add new User To db", Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Toast.makeText(getApplicationContext(), "Error Occurred in Authentication", Toast.LENGTH_SHORT).show();
        }
    }
}