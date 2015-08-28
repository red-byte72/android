package com.example.user.comicsheroes;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.service.voice.VoiceInteractionSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    ArrayList<Hero> arrayHero;
    GridAdapter adapter;
    //HelpFunc hF = new HelpFunc();
    Hero hero = new Hero(" "," "," ");
    boolean flag = false;
    private DatabaseHeroes mDatabaseHeroes = new DatabaseHeroes(this, "databaseheroes.db", null, 1);
    final String fBASE_URL = "http://gateway.marvel.com/v1/public/characters?";
    int offsetGlobal = 0;
    ContentValues values = new ContentValues();
    int end = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        final SQLiteDatabase mSqLiteDatabase = mDatabaseHeroes.getWritableDatabase();

        /*mDatabaseHeroes = new DatabaseHeroes(this, "databaseheroes.db", null, 1);
        mSqLiteDatabase = mDatabaseHeroes.getWritableDatabase();
        */

        arrayHero = new ArrayList<Hero>();
        if(isNetworkConnected()) {
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = sendMarvelQueue(fBASE_URL, offsetGlobal, mSqLiteDatabase);
            queue.add(jsObjRequest);
        }else{
            //mSqLiteDatabase.delete("heroes",null,null);
            Cursor cursor = mSqLiteDatabase.query("heroes", new String[]{
                            DatabaseHeroes.HERO_NAME_COLUMN, DatabaseHeroes.URL_COLUMN, DatabaseHeroes.DESCRIPTION_COLUMN},
                    null, null, null, null, null);
            //cursor.moveToFirst();
            cursor.moveToPosition(-1);
            String urli = "";
            String name = "";
            String description = "";
            while (cursor.moveToNext()){
                urli = cursor.getString(cursor.getColumnIndex(mDatabaseHeroes.URL_COLUMN));
                name = cursor.getString(cursor.getColumnIndex(mDatabaseHeroes.HERO_NAME_COLUMN));
                description = cursor.getString(cursor.getColumnIndex(mDatabaseHeroes.DESCRIPTION_COLUMN));
                arrayHero.add(new Hero(urli,name,description));
            }
            GridView gridViewHero = (GridView) findViewById(R.id.gridView);
          //  gridViewHero.setClickable(true);
            gridViewHero.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                    intent.putExtra("urlImage", arrayHero.get(position).getHeroFace());
                    intent.putExtra("description", arrayHero.get(position).getHeroInfo());
                    intent.putExtra("nameHero", arrayHero.get(position).getHeroName());
                    startActivity(intent);
                }

            });

            adapter = new GridAdapter(MainActivity.this, arrayHero);
            gridViewHero.setAdapter(adapter);
            cursor.close();

        }


    }

    JsonObjectRequest sendMarvelQueue(String BASE_URL, final int offset, final SQLiteDatabase mSqLiteDatabase){
        return new JsonObjectRequest(Request.Method.GET, HelpFunc.marvelUrl(BASE_URL,offset), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                hero = gson.fromJson(response.toString(),Hero.class);

                String urlImageMarvelHero = "http://i-gbi.ru/uss-img/now_uss_eshop_cover_200x150.png";
                String nameHero=" ";
                String infoHero=" ";
                Cursor cursor = mSqLiteDatabase.query("heroes", new String[]{
                                DatabaseHeroes.HERO_NAME_COLUMN, DatabaseHeroes.URL_COLUMN, DatabaseHeroes.DESCRIPTION_COLUMN},
                        null, null, null, null, null);
                cursor.moveToPosition(offset);

                for(int i = 0; i<hero.getData().getLimit(); i++) {
                    if (hero.getData().getResults()[i].getThumbnail() != null) {
                        urlImageMarvelHero = hero.getData().getResults()[i].getThumbnail().getPath() + "."
                                + hero.getData().getResults()[i].getThumbnail().getExtension();
                    } else
                        urlImageMarvelHero = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg";
                    nameHero = hero.getData().getResults()[i].getName();
                    infoHero = hero.getData().getResults()[i].getDescription();
                    arrayHero.add(new Hero(urlImageMarvelHero, nameHero, infoHero));
                    Log.d("777", "offset " + offset);
                    Log.d("777", "offsetGlob "+offsetGlobal);
                    if(cursor.getCount() == offset) {
                        values.put(DatabaseHeroes.HERO_NAME_COLUMN, nameHero);
                        values.put(DatabaseHeroes.URL_COLUMN, urlImageMarvelHero);
                        values.put(DatabaseHeroes.DESCRIPTION_COLUMN, infoHero);
                        mSqLiteDatabase.insert("heroes", null, values);

                    }
                }
                cursor.close();
                GridView gridView = (GridView)findViewById(R.id.gridView);
                gridView.setClickable(true);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                        intent.putExtra("urlImage", arrayHero.get(position).getHeroFace().toString());
                        intent.putExtra("nameHero", arrayHero.get(position).getHeroName());
                        intent.putExtra("description", arrayHero.get(position).getHeroInfo());
                        startActivity(intent);
                    }
                });

                gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        // Log.d("777", "onSkroll");
                        int endScreen = firstVisibleItem + visibleItemCount;
                     /*   if ((totalItemCount == endScreen) && (totalItemCount != 0))
                            adapter.notifyDataSetChanged();*/
                        if (offsetGlobal <= totalItemCount) {
                            if ((totalItemCount == endScreen) && (totalItemCount != 0)) {
                                offsetGlobal += 100;
                                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                                JsonObjectRequest jsObjRequest = sendMarvelQueue(fBASE_URL, offsetGlobal, mSqLiteDatabase);
                                queue.add(jsObjRequest);
                                adapter.notifyDataSetChanged();
                                end = endScreen;
                            }
                        }
                    }

                });

                        adapter = new GridAdapter(MainActivity.this, arrayHero);
                gridView.setAdapter(adapter);
                gridView.setSelection(end);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }
}
