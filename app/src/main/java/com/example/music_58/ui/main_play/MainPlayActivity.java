package com.example.music_58.ui.main_play;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.music_58.R;
import com.example.music_58.ui.search.SearchActivity;

public class MainPlayActivity extends AppCompatActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, MainPlayActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_play);
    }
}
