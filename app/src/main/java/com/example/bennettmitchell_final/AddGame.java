package com.example.bennettmitchell_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class AddGame extends AppCompatActivity {
    private TextView screenLabel;
    private EditText searchBar;
    private Button searchButton;
    private RecyclerView gameList;
    private Button blankTemplateButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.add_game);

        screenLabel = findViewById(R.id.topLabel);

        // search //
        searchBar = findViewById(R.id.searchBar);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener((View v) -> {
            // TODO search
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
