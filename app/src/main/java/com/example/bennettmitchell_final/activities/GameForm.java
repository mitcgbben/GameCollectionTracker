package com.example.bennettmitchell_final.activities;

import static java.lang.Math.abs;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bennettmitchell_final.GameID;
import com.example.bennettmitchell_final.Platform;
import com.example.bennettmitchell_final.Status;
import com.example.bennettmitchell_final.model.DBManager;
import com.example.bennettmitchell_final.Game;
import com.example.bennettmitchell_final.R;
import com.example.bennettmitchell_final.model.Database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GameForm extends AppCompatActivity implements GestureDetector.OnGestureListener{

    private TextView topLabel; // for changing to "Edit Game"
    private EditText titleEdit;
    private EditText releaseDateEdit;
    private EditText developerEdit;
    private EditText publisherEdit;
    private Spinner statusCombo;
    private Spinner platformCombo;
    private EditText descriptionEdit;

    private Button backButton;
    private Button submitButton;

    private SpinnerAdapter statusAdapter;
    private SpinnerAdapter platformAdapter;
    private ArrayList<GameID> platforms;
    private ArrayList<GameID> statuses;

    public GestureDetector gDetector;
    private static int MIN_Y_SWIPE = 0;
    private static int MAX_Y_SWIPE = 400;

    private static int MIN_X_SWIPE = -1000;
    private static int MAX_X_SWIPE = -50;


//    private DBManager dbMan;

    private boolean action = false;
    private Game importedGame;
    private byte[] importedBoxArt = null;

    @Override
    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.game_form);

        importedGame = null;
//        dbMan = new DBManager(this);
        // :3
        topLabel = findViewById(R.id.topLabel);
        titleEdit = findViewById(R.id.titleEdit);
        releaseDateEdit = findViewById(R.id.releaseDateEdit);
        developerEdit = findViewById(R.id.developerEdit);
        publisherEdit = findViewById(R.id.publisherEdit);

        statusCombo = findViewById(R.id.statusCombo);
        platformCombo = findViewById(R.id.platformCombo);

        descriptionEdit = findViewById(R.id.descriptionEdit);

        backButton = findViewById(R.id.backButton);
        submitButton = findViewById(R.id.submitButton);

        // fill status and platform spinners
        statuses = (ArrayList<GameID>) DBManager.getGameIDs(Database.Tables.STATUSES);
        statusAdapter = new SpinnerAdapter(this, statuses);

        platforms = (ArrayList<GameID>) DBManager.getGameIDs(Database.Tables.PLATFORMS);
        platformAdapter = new SpinnerAdapter(this, platforms);

        statusCombo.setAdapter(statusAdapter);

        platformCombo.setAdapter(platformAdapter);

        gDetector = new GestureDetector(this, this);

        // get bundle from prior screen //
        Bundle bundle = getIntent().getExtras();
        // if no extras were passed in (blank template)
        if (bundle != null){
            // get game //
            importedGame = bundle.getParcelable("Game");
            // change the top text //
            action = bundle.getBoolean("Action", false);
            if (action){
                topLabel.setText(R.string.editGameLabel);
//                editGameID = game.getGameID(); // store this for later

                // jump to preselected status
                Status importedStatus =  importedGame.getGameStatus();
                if (importedStatus != null) {
                    int statusNum = DBManager.findGameID(statuses, importedGame.getGameStatus());
                    statusCombo.setSelection(statusNum);
                }
                Status importedPlatform = importedGame.getPlatform();
                if (importedPlatform != null){
                    int platformNum = DBManager.findGameID(platforms, importedGame.getPlatform());
                    platformCombo.setSelection(platformNum);
                }
            }
            else{
                topLabel.setText(R.string.addGameLabel);
            }

            // autofill the game information
            titleEdit.setText(importedGame.getTitle());
            releaseDateEdit.setText(importedGame.getReleaseDate());
            developerEdit.setText(importedGame.getDeveloper());
            publisherEdit.setText(importedGame.getPublisher());
            descriptionEdit.setText(importedGame.getDescription());


            importedBoxArt = importedGame.getBoxArt();

        }
        else{
            topLabel.setText(R.string.addGameLabel);
        }


        // button click listeners //
        backButton.setOnClickListener((View v) ->{
            this.finish();
        });
        submitButton.setOnClickListener((View v) ->{
            // TODO : Validate data and then add to database
            try{
                Game newGame = buildGame();
                Log.d("Game", newGame.toString());
                if (!action){
                    // Add game
                    DBManager.insertGame(newGame);
                    // displays toast that states the game was added successfully
                    Toast.makeText(this, String.format(getString(R.string.formSuccess), getString(R.string.formAdd)), Toast.LENGTH_LONG).show();
                    this.finish();
                }
                else{
                    DBManager.updateGame(newGame);

                    Toast.makeText(this, String.format(getString(R.string.formSuccess), getString(R.string.formEdit)), Toast.LENGTH_SHORT).show();
                    this.finish();
                }

            }
            catch (Exception e){
                Log.e("plink", e.toString());
                Toast.makeText(this, R.string.formError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Game buildGame(){
        String title = titleEdit.getText().toString();
        String releaseDate = releaseDateEdit.getText().toString();
        // TODO : release date format
        String developer = developerEdit.getText().toString();
        String publisher = publisherEdit.getText().toString();
        String description = descriptionEdit.getText().toString();

        Status status = (Status) statuses.get(statusCombo.getSelectedItemPosition());
        Status platform = (Status) platforms.get(platformCombo.getSelectedItemPosition());

        Log.i("Database", platforms.get(platformCombo.getSelectedItemPosition()).getName() + " " + platform.getName());
        Log.i("Database", statuses.get(statusCombo.getSelectedItemPosition()).getName() + " " + status.getName());
        String userNotes = "";
//        byte[] boxArt = null;
        int gameID = 0;
        String releaseDateF = "1/1/2000";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try{
            // parse so the user can enter any dumb stuff
            Date release = sdf.parse(releaseDate);
            releaseDateF = sdf.format(release);
            Log.i("plink", releaseDateF);
        } catch (ParseException e) {
            releaseDateF = "1/1/2000";
        }


        if (action){
            userNotes = importedGame.getUserNotes();
            importedBoxArt = importedGame.getBoxArt();
            gameID = importedGame.getGameID();
//            boxArt = importedGame.getBoxArt();
        }
//        boxArt = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
        Game tempGame = new Game(title, releaseDateF, importedBoxArt, developer, publisher, description, userNotes, status, platform, gameID);
        return tempGame;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
//        DBManager.close();
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {return false;}
    @Override
    public void onShowPress(@NonNull MotionEvent e) {}
    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {return false;}
    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {return false;}
    @Override
    public void onLongPress(@NonNull MotionEvent e) {}
    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        float deltaY = abs(e1.getY() - e2.getY());
        float deltaX = e1.getX() - e2.getX();

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
