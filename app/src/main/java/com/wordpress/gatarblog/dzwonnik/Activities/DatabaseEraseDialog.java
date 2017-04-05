package com.wordpress.gatarblog.dzwonnik.Activities;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Creating dialog box for ask user is he sure about delete all alarm states.
 */
public class DatabaseEraseDialog extends DialogFragment {

    EntityDelete entityDelete;

    void setEntityDelete(EntityDelete entityDelete){
        this.entityDelete = entityDelete;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Usunięcie wszystkich pozycji jest nieodwracalne!")
                .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkEntityDeleteReference();
                        entityDelete.eraseDatabaseEntities();
                    }
                })
                .setNegativeButton("Powrót", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    private void checkEntityDeleteReference() throws NullPointerException{
        if (entityDelete == null)  throw new NullPointerException("You have to use setEntityDelete to give reference to object with database delete method!!!");
    }

    /**
     * Activity, which want to show this dialog must implement this interface.
     */
    interface EntityDelete {
        void eraseDatabaseEntities();
    }
}

