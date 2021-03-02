package com.sample.kakao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";

    private View loginButton,logoutButton;
    private TextView nickName;
    private ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login);
        logoutButton=findViewById(R.id.logout);
        nickName = findViewById(R.id.nickname);
        profileImage=findViewById(R.id.profile);

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken != null){

                }
                if(throwable != null){

                }
                updateKakaoLoginUi();
                return null;
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LoginClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)){
                    LoginClient.getInstance().loginWithKakaoTalk(MainActivity.this, callback);
                        }

                else {
                    LoginClient.getInstance().loginWithKakaoAccount(MainActivity.this,callback );
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        updateKakaoLoginUi();
                        return null;
                    }
                });

            }
        });

        updateKakaoLoginUi();
    }


        private void updateKakaoLoginUi(){ //ㅋㅏ카오 로그인 돼있는지 확인
            UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                @Override
                public Unit invoke(User user, Throwable throwable) {
                    if(user != null) {

                        Log.i(TAG,"invoke: id =" +user.getId());
                        Log.i(TAG,"invoke: email =" +user.getKakaoAccount().getEmail());
                        Log.i(TAG,"invoke: nickname =" +user.getKakaoAccount().getProfile().getNickname());
                        Log.i(TAG,"invoke: gender =" +user.getKakaoAccount().getGender());
                        Log.i(TAG,"invoke: invoke =" +user.getKakaoAccount().getAgeRange());

                        nickName.setText(user.getKakaoAccount().getProfile().getNickname());
                        //user.getKakaoAccount().getProfile().getThumbnailImageUrl();
                        Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(profileImage);


                        loginButton.setVisibility(View.GONE);
                        logoutButton.setVisibility(View.VISIBLE);
                    }
                    else{
                        nickName.setText(null);
                        profileImage.setImageBitmap(null);
                        loginButton.setVisibility(View.VISIBLE);
                        logoutButton.setVisibility(View.GONE);


                    }
                    return null;
                }
            }); //로그인 정보 얻어오기 {
                


    }
}