package ozone.mai_2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.unnamed.b.atv.model.TreeNode;


/**
 * Created by Ozone on 04.05.2016.
 */
public class AddCriterionDialog extends DialogFragment{

    private AddProjectActivity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = (AddProjectActivity) getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.add_criterion_dialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_criterion_dialog, null);

        builder.setTitle("Добавить критерий");  // заголовок
        builder.setMessage("Введите название критерия"); // сообщение]


        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText crNameEdit = (EditText) getDialog().findViewById(R.id.dialog_criterion_name);
                String name = crNameEdit.getText().toString();

                activity.addCriterion(name);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);

        return builder.create();
    }
}
