package com.example.bennettmitchell_final;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameForm extends AppCompatActivity {

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

    private DBManager dbMan;

    private boolean action = false;
    private Game importedGame;

    @Override
    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.game_form);

        importedGame = null;
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
            }
            else{
                topLabel.setText(R.string.addGameLabel);
            }

            // get the game information
            titleEdit.setText(importedGame.getTitle());
            releaseDateEdit.setText(importedGame.getReleaseDate());
            developerEdit.setText(importedGame.getDeveloper());
            publisherEdit.setText(importedGame.getPublisher());
            // TODO : Set the combo boxes

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
                throw(new RuntimeException("hi"));


            }
            catch (Exception e){
                Log.e("plink", e.toString());
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
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
        int statusID = statusCombo.getSelectedItemPosition();
        int platformID = platformCombo.getSelectedItemPosition();

        String userNotes = "";
        byte[] boxArt = null;
        int gameID = 0;

        if (action){
            userNotes = importedGame.getUserNotes();
            boxArt = importedGame.getBoxArt();
            gameID = importedGame.getGameID();
        }

        Game tempGame = new Game(title, releaseDate, boxArt, developer, publisher, description, userNotes, statusID, platformID, gameID);
        return tempGame;
    }
}
