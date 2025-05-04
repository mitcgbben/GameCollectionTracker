package com.example.bennettmitchell_final.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.bennettmitchell_final.Platform;
import com.example.bennettmitchell_final.R;
import com.example.bennettmitchell_final.Status;
import com.example.bennettmitchell_final.model.DBManager;
import com.example.bennettmitchell_final.model.Database;

public class SettingsMenu extends DialogFragment {
//    private DBManager dbMan;
    private Button statusButton;
    private EditText statusField;
    private Button platformButton;
    private EditText platformField;

    private Button resetButton;
    private Button doneButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflation = getActivity().getLayoutInflater();
        // display
        View dialogView = inflation.inflate(R.layout.settings_menu, null);
        // define the controls //
        statusField = dialogView.findViewById(R.id.newStatusField);
        statusButton = dialogView.findViewById(R.id.newStatusButton);

        statusButton.setOnClickListener((View v) -> {
            String status = statusField.getText().toString();
            if (!status.isEmpty()){
                DBManager.addGameID(Database.Tables.STATUSES, new Status(status));
                statusField.setText("");
                Toast.makeText(getContext(), R.string.statusAdded, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), R.string.emptyStatus, Toast.LENGTH_SHORT).show();
            }
        });
        platformField = dialogView.findViewById(R.id.newPlatformField);
        platformButton = dialogView.findViewById(R.id.newPlatformButton);

        platformButton.setOnClickListener((View v) -> {
            String platform = platformField.getText().toString();
            if (!platform.isEmpty()){
                DBManager.addGameID(Database.Tables.PLATFORMS, new Status(platform));
                platformField.setText("");
                Toast.makeText(getContext(), R.string.platformAdded, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), R.string.emptyPlatform, Toast.LENGTH_SHORT).show();
            }});

        resetButton = dialogView.findViewById(R.id.resetButton); // danger danger danger
        doneButton = dialogView.findViewById(R.id.exitButton);

        // goodnight
        doneButton.setOnClickListener((View v) -> {this.dismiss();});

        resetButton.setOnClickListener((View v) ->{
            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getContext());
            confirmBuilder.setTitle(R.string.alert);
            confirmBuilder.setMessage(R.string.confirmMessage);

            confirmBuilder.setNegativeButton(R.string.no, (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.dismiss();
            });
            confirmBuilder.setPositiveButton(R.string.yes, (DialogInterface.OnClickListener) (dialog, which) ->{
                DBManager.reset();
                Toast.makeText(getContext(), R.string.databaseCleared, Toast.LENGTH_LONG).show();
                dialog.dismiss();
                this.dismiss();
            });
            AlertDialog confirm = confirmBuilder.create();
            confirm.show();


        });



        /*
        Button puyo = dialogView.findViewById(R.id.puyo);
        puyo.setOnClickListener((View v) -> {
            APICaller.test();
        });
        Button authTest = dialogView.findViewById(R.id.authTest);
        authTest.setOnClickListener((View v) -> {
            try {
                APICaller.searchAPI("among us");
            }
            catch (APICaller.ConnectionException e){
                Log.e("API", e.toString());
            }
        });
         */

        builder.setView(dialogView);
        builder.setMessage("Settings");
        return builder.create();
    }
}
