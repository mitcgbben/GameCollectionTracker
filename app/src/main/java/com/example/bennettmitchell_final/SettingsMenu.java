package com.example.bennettmitchell_final;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SettingsMenu extends DialogFragment {
//    private DBManager dbMan;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflation = getActivity().getLayoutInflater();
        // display
        View dialogView = inflation.inflate(R.layout.settings_menu, null);

        Button puyo = dialogView.findViewById(R.id.puyo);
        puyo.setOnClickListener((View v) -> {
            APICaller.test();
        });
        Button authTest = dialogView.findViewById(R.id.authTest);
        authTest.setOnClickListener((View v) -> APICaller.searchAPI("among us"));


        builder.setView(dialogView);
        builder.setMessage("Settings");
        return builder.create();
    }
}
