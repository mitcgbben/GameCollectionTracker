package com.example.bennettmitchell_final.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bennettmitchell_final.GameID;
import com.example.bennettmitchell_final.R;
import com.example.bennettmitchell_final.Status;

import java.util.ArrayList;

// to display the items in the spinner
public class SpinnerAdapter extends ArrayAdapter<GameID> {
    public SpinnerAdapter(Context c, ArrayList<GameID> list){
        super(c, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int pos, View convertView, ViewGroup parent){
        return initView(pos, convertView, parent);
    }

    // inflator of the segments of the spinner
    private View initView(int position, View convertView, ViewGroup parent){
        // inflate it if it doesnt exist yet
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }

        // put the name of the status/platform
        TextView nameView = convertView.findViewById(R.id.itemName);
        GameID currentStat = getItem(position);

        if (currentStat != null){
            nameView.setText(currentStat.getName());
        }
        return convertView;
    }
}
