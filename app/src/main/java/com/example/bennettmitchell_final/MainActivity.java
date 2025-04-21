package com.example.bennettmitchell_final;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button addGameButton;
    private TextView noGames;

    // game recycle view //
//    private static String plink = "plink";
    private DBManager dbMan;
    private List<Game> games;
    private GameListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbMan = new DBManager(this);
//        games = new ArrayList<>();
        // test games //
        /*
        games.add(new Game("plink"));
        games.add(new Game("Puyo Puyo"));
        games.add(new Game("Amogus"));
        Game test = new Game("Wii Chess");
        test.setReleaseDate("01/01/2009");
        games.add(test);
        */
        
        recyclerView = findViewById(R.id.gameList);
        noGames = findViewById(R.id.noGamesLabel);

        showGames();

        // theres a scroll to position method


        addGameButton = findViewById(R.id.addGameButton);
        addGameButton.setOnClickListener((View v) -> {
            Intent addGame = new Intent(this, AddGame.class);
            startActivity(addGame);
        });
    }

    @Override
    public void onRestart(){
        Log.d("plink", "on restart");
        showGames();
        super.onRestart();
    }
    // add overflow menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // get the option menu option that is selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId(); // get the id of the option

        // idk why switch doesnt work here
        if (id == R.id.settingsMenu) {
            SettingsMenu dialog = new SettingsMenu();
            dialog.show(getSupportFragmentManager(), "settings");
            return true;
        }
        else if (id == R.id.aboutMenu){
            AlertDialog.Builder builder = new AlertDialog.Builder(this); // create the buidler to display the message
            builder.setMessage(R.string.aboutText);
            builder.setTitle(R.string.aboutTitle);
            // actually show it
            AlertDialog alert = builder.create();
            alert.show();
        }
        else if(id == R.id.refresh){
            Log.i("plink", "refresh requested");
            games = dbMan.getGames();
            showGames(); // fully refreshes the list
            // isnt the most efficient but it will always show the new content
        }
        return super.onOptionsItemSelected(item); // mrow
    }

    public void showGames(){
        games = dbMan.getGames();
        // there are definately better ways to do this
        // this is like the first thing to optimize
        //
        adapter = new GameListAdapter(games, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (games.isEmpty()){
            noGames.setText(getText(R.string.noGames));
        }
        else{
            noGames.setText("");
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        dbMan.close();
    }
}