package com.example.gamepuzzlepursuit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText inputUsername,inputEmail,inputPassword,inputConformPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConformPassword = findViewById(R.id.inputConformPassword);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(RegisterActivity.this);

        Button btnRigister = findViewById(R.id.btnRegister);
        btnRigister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkCrendedentiatls();
            }
        });

        TextView haveAccout = findViewById(R.id.haveAccount);
        haveAccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    //check input register
    private void checkCrendedentiatls() {
        String username = inputUsername.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String conformPassword = inputConformPassword.getText().toString();

        if(username.isEmpty() || username.length()<7){
            showError(inputUsername,"Tên người dùng không hợp lệ!");
        }
        else if(email.isEmpty() || !email.contains("@")){
            showError(inputEmail,"Email không hợp lệ!");
        }
        else if(password.isEmpty() || password.length()<7){
            showError(inputPassword,"Mật khẩu phải hơn 6 ký tự!");
        }
        else if(conformPassword.isEmpty() || !conformPassword.equals(password)){
            showError(inputUsername,"Nhập lại mật khẩu không đúng!");
        }
        else {
            mLoadingBar.setTitle("Đang đăng ký");
            mLoadingBar.setMessage("Vui lòng đợi, kiểm tra thông tin xác thực của bạn!");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void showError(EditText input,String s) {
        input.setError(s);
        input.requestFocus();
    }
}
