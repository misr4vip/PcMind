package com.example.pcmind.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pcmind.Clients.MainActivity;
import com.example.pcmind.Model.MyUser;
import com.example.pcmind.Model.User;
import com.example.pcmind.R;
import com.example.pcmind.admin.AdminMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText userName , passowrd ;
    TextView register;
    Button login;
    String userEmail,pwd;
    FirebaseAuth auth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = findViewById(R.id.etUserNameLoginActivity);
        passowrd = findViewById(R.id.etPasswordLoginActivity);
        register = findViewById(R.id.tvRegisterLoginActivity);
        login = findViewById(R.id.btnLoginLoginActivity);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
         userEmail = userName.getText().toString();
         pwd = passowrd.getText().toString();
        register.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext() , RegisterActivity.class);
            startActivity(intent);
        });

        login.setOnClickListener(view -> {
            /// Auth With Firebase
            userEmail = userName.getText().toString();
            pwd = passowrd.getText().toString();
            HashMap<Boolean,String> Validate = Validation();
            if(Validate.isEmpty())
            {
                auth.signInWithEmailAndPassword(userEmail,pwd).addOnCompleteListener(getAuthResultOnCompleteListener());
            }
        });
    }
    @NonNull
    private OnCompleteListener<AuthResult> getAuthResultOnCompleteListener() {
        return authResult -> {
            if (authResult.isSuccessful()) {
                String id = authResult.getResult().getUser().getUid().toString();
                DatabaseReference ref = database.getReference().child("Users").child(id);
                ref.get().addOnCompleteListener(snapShot -> {
                    if (snapShot.isSuccessful())
                    {
//                        User.setId(snapShot.getResult().child("id").getValue().toString());
//                        User.setName(snapShot.getResult().child("name").getValue().toString());
//                        User.setEmail(snapShot.getResult().child("email").getValue().toString());
//                        User.setMobile(snapShot.getResult().child("mobile").getValue().toString());
//                        User.setConnected(snapShot.getResult().child("connected").exists());
//                        User.setAdmin(snapShot.getResult().child("admin").exists());
                        MyUser user = snapShot.getResult().getValue(MyUser.class);
                        User.setId(user.getId());
                        Log.e("TAG", "onClick: " + User.isConnected());
                        if (!user.isAdmin()) {
                            // User
                            Log.e("TAG", "getAuthResultOnCompleteListener: " + user.isAdmin() );
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Admin
                            Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                            startActivity(intent);
                        }
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Error No Internet Access Client Is offline ", Toast.LENGTH_SHORT).show();
                    }
                    
                });

            }
        };
    }

    private HashMap<Boolean,String> Validation()
    {
        HashMap<Boolean,String> process = new HashMap<>();
        String errorsString = "";
        Boolean errorBool = false;
        if (userEmail.isEmpty() || pwd.isEmpty() )
        {
            process.put(true,"Non Optional Can not be Empty");
        }
        return process;
    }
}