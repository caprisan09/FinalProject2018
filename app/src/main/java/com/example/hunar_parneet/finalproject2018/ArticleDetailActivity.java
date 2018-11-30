package com.example.hunar_parneet.finalproject2018;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import java.util.UUID;


public class ArticleDetailActivity extends AppCompatActivity{
    private static  final String EXTRA_CARD = "com.example.hunar_parneet.finalproject2018";

    public static Intent newIntent(Context packageContext, UUID id ){
        Intent intent = new Intent(packageContext, ArticleDetailActivity.class);
        intent.putExtra(EXTRA_CARD, id);
        return intent;
    }

    protected Fragment createFragment(){
        UUID cardId = (UUID) getIntent().getSerializableExtra(EXTRA_CARD);
        return FragmentNewsDetail.newInstance(cardId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
