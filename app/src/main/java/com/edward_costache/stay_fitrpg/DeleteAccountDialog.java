package com.edward_costache.stay_fitrpg;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * Code adapted from Coding in Flow, YouTube channel https://www.youtube.com/watch?v=ARezg1D9Zd0
 */
public class DeleteAccountDialog extends AppCompatDialogFragment{
    private EditText editTxtPassword;
    private DialogDeleteAccountListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_delete_ccount_dialog, null);

        editTxtPassword = view.findViewById(R.id.dialogDeleteAccountPassword);

        builder.setView(view)
                .setTitle("Validation")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("DELETE!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = editTxtPassword.getText().toString();
                        listener.validate(password, editTxtPassword);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogDeleteAccountListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogDeleteAccountListener");
        }
    }

    public interface DialogDeleteAccountListener{
        void validate(String password, EditText editTxtPassword);
    }
}