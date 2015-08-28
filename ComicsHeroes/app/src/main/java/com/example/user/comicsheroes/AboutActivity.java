package com.example.user.comicsheroes;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Red_byte on 07.08.2015.
 */
public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textDescription = (TextView) findViewById(R.id.textView);
        TextView textName = (TextView) findViewById(R.id.nameHero);
        SimpleDraweeView heroFace = (SimpleDraweeView) findViewById(R.id.image_about);
        heroFace.setImageURI(Uri.parse(getIntent().getExtras().getString("urlImage")));
        textDescription.setText(getIntent().getExtras().getString("description"));
        textName.setText(getIntent().getExtras().getString("nameHero"));
    }
}
