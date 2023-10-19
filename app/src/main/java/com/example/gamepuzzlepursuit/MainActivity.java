package com.example.gamepuzzlepursuit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView fullname = findViewById(R.id.fullname);
        final TextView email = findViewById(R.id.emailId);
        final Button signOutBtn = findViewById(R.id.signOutBtn);
        final ImageView img = findViewById(R.id.imgaccout);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        //get details for signed in user
        final String getFullName = googleSignInAccount.getDisplayName();
        final  String getEmail = googleSignInAccount.getEmail();


        email.setText("Email:" + getEmail);
        fullname.setText("Full name:" + getFullName);
        Picasso.with(this).load(googleSignInAccount.getPhotoUrl()).into(img);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign out
                googleSignInClient.signOut();

                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}