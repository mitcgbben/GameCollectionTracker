package com.example.bennettmitchell_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddGame extends AppCompatActivity {
    private TextView screenLabel;
    private EditText searchBar;
    private Button searchButton;
    private RecyclerView gameList;
    private Button blankTemplateButton;
    private Button backButton;
    private TextView errorField;

    private GameListAdapter adapter;
    private List<Game> games;

    @Override
    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.add_game);

        screenLabel = findViewById(R.id.topLabel);

        // search //
        searchBar = findViewById(R.id.searchBar);
        errorField = findViewById(R.id.errorMessage);

        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener((View v) -> {
            // api call to IGDB //
            try {
                String query = searchBar.getText().toString();
//                Log.i("plink", query);

                if (!query.isEmpty()) {
                    games = APICaller.searchAPI(query);
                    errorField.setText("");
            /*
            games = new ArrayList<>();
            Game imgTest = new Game("Capybara");
            imgTest.setBoxArt(APICaller.getImageFromWeb(this, "https://oldschool.runescape.wiki/images/thumb/Capybara.png/800px-Capybara.png?5cf43"));
            games.add(imgTest);
            imgTest = new Game("meowmroweprm");
            imgTest.setBoxArt(APICaller.getImageFromWeb(this, "https://preview.redd.it/please-give-us-a-capybara-pet-v0-mysxaaq2tvpc1.png?auto=webp&s=7e194d38558abd1b5ad972705e2f7bcc10c11a26"));
            games.add(imgTest);
            games.add(new Game("plink"));
            games.add(new Game("Puyo Puyo"));
            games.add(new Game("Amogus"));
             */

                    if (games != null) {
                        // put the games onto the screen
                        adapter = new GameListAdapter(games, this, GameListAdapter.Destinations.GAMEFORM);
                        gameList.setLayoutManager(new LinearLayoutManager(this));
                        gameList.setAdapter(adapter);
                        if (games.isEmpty()){
                            errorField.setText(R.string.noGamesFound);
                        }

                    }
                    else{
                        errorField.setText(R.string.nullList);
                    }
                }
                else{
                    errorField.setText(R.string.entryError);
                }
            }
            catch (APICaller.ConnectionException e){
                errorField.setText(R.string.connectionError);
            }
        });

        gameList = findViewById(R.id.resultList);

        blankTemplateButton = findViewById(R.id.blankTemplateButton);
        blankTemplateButton.setOnClickListener((View v) -> {
            Intent form = new Intent(this, GameForm.class);
            startActivity(form);

        });
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener((View v) -> {
            this.finish(); // end the screen
        });
    }
}
