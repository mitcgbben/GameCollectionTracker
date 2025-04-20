package com.example.bennettmitchell_final;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {

    // the tabs that will be displayed
    // legally a wrapper class
    public static class GameViewHolder extends RecyclerView.ViewHolder{
        // TODO: define the contents here
        private final TextView titleLabel;
        private final ImageView boxArt;

        public GameViewHolder(@NonNull View view){
            super(view);
            //view.setOnClickListener((View v) -> {});
            titleLabel = view.findViewById(R.id.gameTitle);
            boxArt = view.findViewById(R.id.boxArt);
        }
        // getters for all the contents of each view //
        public TextView getTitleLabel(){
            return titleLabel;
        }
        public ImageView getBoxArt(){return boxArt;}
    }

    // items to be displayed
    private List<Game> games;
    private Context context;
    public GameListAdapter(List<Game> gameList, Context context){
        this.games = gameList;
        this.context = context;
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
            Intent gameDetails = new Intent(context.getApplicationContext(), GameDetails.class);
            gameDetails.putExtra("Game", gameItem);
            context.startActivity(gameDetails);
        });

        String titleText = res.getString(R.string.titleDisplay);
        titleText = String.format(titleText, gameItem.getTitle(), gameItem.getYear());
        viewHolder.getTitleLabel().setText(titleText);
//        viewHolder.getBoxArt().setImageDrawable(gameItem.getImageFromWeb(viewHolder.itemView.getContext()));
    }
    // legally required
    @Override
    public int getItemCount(){
        return games.size();
    }
}
