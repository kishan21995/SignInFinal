package com.app.signinfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout prof_section;
    private Button Signout;
    private SignInButton Signin;
    private TextView Name,Email;
    private ImageView Pro_pic;
    private GoogleApiClient googleApiClient;
    private  static final int REQ_CODE=9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prof_section=(LinearLayout) findViewById(R.id.prof_section);
        Signout=(Button) findViewById(R.id.btn_logout);
        Signin=(SignInButton) findViewById(R.id.btn_signin);
        Name=(TextView) findViewById(R.id.name);
        Email=(TextView)findViewById(R.id.email);
        Pro_pic=(ImageView) findViewById(R.id.pro_pic);
        Signin.setOnClickListener(this);
        Signout.setOnClickListener(this);
        prof_section.setVisibility(View.GONE);
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_signin:
                signin();
                break;

            case R.id.btn_logout:
                signout();
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private  void signin(){

        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);

    }
    private  void signout(){

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });

    }
    private  void handleResult(GoogleSignInResult result){


        if(result.isSuccess()){

            GoogleSignInAccount account=result.getSignInAccount();
            String name=account.getDisplayName();
            String email=account.getEmail();
            String img_url=account.getPhotoUrl().toString();
            Name.setText(name);
            Email.setText(email);
            Glide.with(this).load(img_url).into(Pro_pic);
            updateUI(true);

            }else {


            updateUI(false);
        }
        }

    private void updateUI(boolean isLogin){

        if(isLogin){
            prof_section.setVisibility(View.VISIBLE);
            Signin.setVisibility(View.GONE);
        }else {

            prof_section.setVisibility(View.GONE);
            Signin.setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CODE){

            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }

    }
}
