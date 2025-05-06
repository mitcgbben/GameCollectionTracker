package com.example.bennettmitchell_final.activities;

import static java.lang.Math.abs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bennettmitchell_final.model.APICaller;
import com.example.bennettmitchell_final.Game;
import com.example.bennettmitchell_final.R;

import java.util.List;

public class AddGame extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private TextView screenLabel;
    private EditText searchBar;
    private Button searchButton;
    private RecyclerView gameList;
    private Button blankTemplateButton;
    private Button backButton;
    private TextView errorField;

    private GameListAdapter adapter;
    private List<Game> games;

    public GestureDetector gDetector;
    private static int MIN_Y_SWIPE = 0;
    private static int MAX_Y_SWIPE = 400;

    private static int MIN_X_SWIPE = -1000;
    private static int MAX_X_SWIPE = -50;

    @Override
    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.add_game);

        screenLabel = findViewById(R.id.topLabel);
        gDetector = new GestureDetector(this, this);

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

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return false;
    }
    @Override
    public void onShowPress(@NonNull MotionEvent e) {}
    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }
    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }
    @Override
    public void onLongPress(@NonNull MotionEvent e) {}

    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        float deltaY = abs(e1.getY() - e2.getY());
        float deltaX = e1.getX() - e2.getX();
        Log.i("plink", deltaX + "");

        if ((deltaY >= MIN_Y_SWIPE && deltaY <= MAX_Y_SWIPE) && (deltaX >= MIN_X_SWIPE && deltaX <= MAX_X_SWIPE)) {
            this.finish();
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
