package com.example.bennettmitchell_final;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DeprecatedSinceApi;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {
    // static constants for the destination parameter
    public static class Destinations{
        public static final int DETAILS = 0;
        public static final int GAMEFORM = 1;
    }
    // the tabs that will be displayed
    // legally a wrapper class
    public static class GameViewHolder extends RecyclerView.ViewHolder{
        // TODO: define the contents here
        private final TextView titleLabel;
        private final ImageView boxArt;
        private final ImageView platformIcon;
        private final ImageView statusIcon;

        public GameViewHolder(@NonNull View view){
            super(view);
            //view.setOnClickListener((View v) -> {});
            titleLabel = view.findViewById(R.id.gameTitle);
            boxArt = view.findViewById(R.id.boxArt);
            platformIcon = view.findViewById(R.id.platformIcon);
            statusIcon = view.findViewById(R.id.statusIcon);
        }
        // getters for all the contents of each view //
        public TextView getTitleLabel(){
            return titleLabel;
        }
        public ImageView getBoxArt(){return boxArt;}
        public ImageView getPlatformIcon(){return platformIcon;}
        public ImageView getStatusIcon(){return statusIcon;}
    }

    // items to be displayed
    private List<Game> games;
    private Context context;
    private int destination;
    public GameListAdapter(List<Game> gameList, Context context){
        this.games = gameList;
        this.context = context;
        this.destination = Destinations.DETAILS;
    }
    public GameListAdapter(List<Game> gameList, Context context, int destination){
        this(gameList, context);
        this.destination = destination;
    }

    // create new views
    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext()); // create layout inflater to display it
        View view = li.inflate(R.layout.game_list_item, viewGroup, false);
        return new GameViewHolder(view);
    }
    @Override
    public void onBindViewHolder(GameViewHolder viewHolder, final int position){
        Game gameItem = games.get(position);
        Resources res = viewHolder.itemView.getResources();


        viewHolder.itemView.setOnClickListener((View v) -> {
            Log.i("plink", "Clicked on item " + position);
            Intent intent;
            if (destination == Destinations.DETAILS) {
                // coming from main menu
                intent = new Intent(context.getApplicationContext(), GameDetails.class);
            }
            else{
                // going to game form
                intent = new Intent(context.getApplicationContext(), GameForm.class);
            }

            intent.putExtra("Game", gameItem);
            context.startActivity(intent);
        });

        String titleText = res.getString(R.string.titleDisplay);
        titleText = String.format(titleText, gameItem.getTitle(), gameItem.getYear());
        viewHolder.getTitleLabel().setText(titleText);
        viewHolder.getBoxArt().setImageBitmap(gameItem.getBoxArtDisplay());

        // do not show the icons if showing from add game
        // TODO : show platform and status icons
        if (destination == Destinations.GAMEFORM){
            viewHolder.getPlatformIcon().setImageDrawable(null);
            viewHolder.getStatusIcon().setImageDrawable(null);
        }
//        viewHolder.getBoxArt().setImageDrawable(gameItem.getImageFromWeb(viewHolder.itemView.getContext()));
    }
    // legally required
    @Override
    public int getItemCount(){
        return games.size();
    }
}
