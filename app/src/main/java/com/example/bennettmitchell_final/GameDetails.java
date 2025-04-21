package com.example.bennettmitchell_final;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameDetails extends AppCompatActivity {

    private Button backButton;

    private Game game;
    private DBManager dbMan;

    private TextView title;
    private TextView developer;
    private TextView publisher;
    private ImageView boxArt;
    private ImageView platformLogo;
    private Spinner statusCombo;
    private TextView description;
    private EditText userNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_details);
        Log.i("plink", "Got to Game Details");
        dbMan = new DBManager(this);

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



        // TODO : description, platform logo

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener((View v) -> {
            this.finish(); // end the screen
        });
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
            dbMan.deleteGame(game.getGameID());
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

        Intent intent = getIntent();
        intent.putExtra("Game", dbMan.getGame(game.getGameID()));
        finish();
        startActivity(intent);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        dbMan.close();
    }
}
