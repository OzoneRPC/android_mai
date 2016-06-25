package ozone.mai_2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;



/**
 * Created by Ozone on 04.05.2016.
 */
public class AddCriterionDialog extends DialogFragment{
    int type_alternative = 0;
    int type_criterion = 1;
    int nodeId;
    private AddProjectActivity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        nodeId = args.getInt("nodeId");
        activity = (AddProjectActivity) getActivity();
        String title = "Выберите тип элемента";
        String message = "Выберите тип добавляемого элемента: связанный критерий или альтернатива";
        String criterion = "Критерий";
        String alternative = "Альтернатива";
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);  // заголовок
        builder.setMessage(message); // сообщение
        builder.setPositiveButton(criterion, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.addTreeItem(nodeId, type_criterion);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(alternative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.addTreeItem(nodeId, type_alternative);
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);

        return builder.create();
    }
}
