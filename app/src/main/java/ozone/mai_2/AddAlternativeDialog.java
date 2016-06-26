package ozone.mai_2;

        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.app.DialogFragment;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.RelativeLayout;
        import android.widget.Spinner;

        import com.unnamed.b.atv.model.TreeNode;

        import java.util.ArrayList;
        import java.util.List;

        import static android.R.attr.data;

/**
 * Created by Ozone on 26.06.2016.
 */

public class AddAlternativeDialog extends DialogFragment {
    private AddProjectActivity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = (AddProjectActivity) getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final int crId = getArguments().getInt("id");
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle("Добавить альтернатив");  // заголовок
        builder.setMessage("Добавьте существующую, либо укажите название новой"); // сообщение]


        View view = inflater.inflate(R.layout.add_alternative_dialog, null);

        final  Spinner spinner = (Spinner)view.findViewById(R.id.altSpinner);

        if(AddProjectActivity.altNames.size() > 0){

            spinner.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, AddProjectActivity.altNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);
            // заголовок
            spinner.setPrompt("Title");
            //spinner.setSelection(0);
        }

        builder.setView(view);

        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TreeNode newNode;
                String name = "";
                CriterionsTreeHolder.IconTreeItem crItemIcon = new CriterionsTreeHolder.IconTreeItem();

                if(AddProjectActivity.altNames.size() > 0){
                    long selected_id = spinner.getSelectedItemId();
                    TreeNode existNode = AddProjectActivity.altNodes.get(AddProjectActivity.altIds.get((int)selected_id));

                    newNode = new TreeNode(crItemIcon).setViewHolder(existNode.getViewHolder());
                    name = ((EditText)newNode.getViewHolder().getView().findViewById(R.id.criterion_add_text)).getText().toString();
                }else{

                    EditText altNameEdit = (EditText)getDialog().findViewById(R.id.dialog_alternative_name);
                    name = altNameEdit.getText().toString();
                    newNode = new TreeNode(crItemIcon).setViewHolder(new CriterionsTreeHolder(getActivity(), getActivity().getWindow()));
                    EditText edit = (EditText)newNode.getViewHolder().getView().findViewById(R.id.criterion_add_text);
                    edit.setText(name);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) edit.getLayoutParams();

                    params.leftMargin = 25;
                    edit.setLayoutParams(params);
                    Button add = (Button)newNode.getViewHolder().getView().findViewById(R.id.add);
                    add.setVisibility(View.INVISIBLE);

                }


                activity.addAlternative(crId, newNode, name);


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
