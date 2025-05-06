package com.example.bennettmitchell_final.activities;

import static java.lang.Math.abs;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bennettmitchell_final.GameID;
import com.example.bennettmitchell_final.Status;
import com.example.bennettmitchell_final.model.DBManager;
import com.example.bennettmitchell_final.Game;
import com.example.bennettmitchell_final.R;
import com.example.bennettmitchell_final.model.Database;

import java.util.ArrayList;

public class GameDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GestureDetector.OnGestureListener {

    private Button backButton;

    private Game game;
//    private DBManager dbMan;

    private TextView title;
    private TextView developer;
    private TextView publisher;
    private ImageView boxArt;
    private ImageView platformLogo;
    private Spinner statusCombo;
    private TextView description;
    private EditText userNotes;

    private SpinnerAdapter adapter;
    private ArrayList<GameID> statuses;

    public GestureDetector gDetector;
    private static int MIN_Y_SWIPE = 0;
    private static int MAX_Y_SWIPE = 400;

    private static int MIN_X_SWIPE = -1000;
    private static int MAX_X_SWIPE = -50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_details);
        Log.i("plink", "Got to Game Details");

        gDetector = new GestureDetector(this, this);
        // ! Do i need to change the context?

        // get game from extras

        Bundle bundle = getIntent().getExtras();
        game = bundle.getParcelable("Game");

        //Log.i("plink", "unbundled");
        // get all the controls
        title = findViewById(R.id.gameTitleYear);
        developer = findViewById(R.id.gameDeveloper);
        publisher = findViewById(R.id.gamePublisher);
        boxArt = findViewById(R.id.gameBoxArt);
        statusCombo = findViewById(R.id.gameStatusCombo);
        platformLogo = findViewById(R.id.gamePlatformIcon);
        description = findViewById(R.id.gameDescription);
        userNotes = findViewById(R.id.gameUserNotes);




        // display the information //
        String dispTitle = getString(R.string.titleDisplay);
        dispTitle = String.format(dispTitle, game.getTitle(), game.getYear());
        title.setText(dispTitle);
        developer.setText(String.format(getString(R.string.developer), game.getDeveloper()));
        publisher.setText(String.format(getString(R.string.publisher), game.getPublisher()));
        userNotes.setText(game.getUserNotes());
        description.setText(game.getDescription());
        description.setMovementMethod(new ScrollingMovementMethod());
//        userNotes.setText(game.getGameID() + " ");
        boxArt.setImageBitmap(game.getBoxArtDisplay());

        Log.i("Database", "Status: " + game.getGameStatus().getName() + " | Console: " + game.getPlatform().getName());
        platformLogo.setImageBitmap(game.getPlatform().getDisplayIcon());

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener((View v) -> {
            this.finish(); // end the screen
        });

        userNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
//                Log.i("TextWatch", "after");

                String text = s.toString();
//                Log.i("TextWatch", text);
                game.setUserNotes(text);
                DBManager.updateGame(game);
            }
        });

        // spinner :D //
        statuses = (ArrayList<GameID>) DBManager.getGameIDs(Database.Tables.STATUSES);
        adapter = new SpinnerAdapter(this, statuses);
        statusCombo.setAdapter(adapter);
        statusCombo.setOnItemSelectedListener(this);
        statusCombo.setSelection(DBManager.findGameID(statuses, game.getGameStatus()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.game_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.edit){
            Intent gameForm = new Intent(this, GameForm.class);
            gameForm.putExtra("Action", true);
            gameForm.putExtra("Game", game);
            startActivity(gameForm);
        }
        else if (id == R.id.delete){
            DBManager.deleteGame(game.getGameID());
            Log.i("plink", "Delete Game Clicked for " + game.getTitle());
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("Action", "Delete");
            intent.putExtra("gameID", game.getGameID());
            this.finish();
        }
        return true;
    }
    @Override
    protected void onRestart(){
        // on page restart, reload the same page
        super.onRestart();
        Log.d("plink", "onrestart");
        Intent intent = getIntent();
        intent.putExtra("Game", DBManager.getGame(game.getGameID()));
        finish();
        startActivity(intent);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //DBManager.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Status newStatus = (Status) statuses.get(position);
        game.setGameStatus(newStatus);
        DBManager.updateGame(game); // this line was commented out in the video

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
