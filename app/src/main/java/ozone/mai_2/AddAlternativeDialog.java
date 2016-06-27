package ozone.mai_2;

        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.app.DialogFragment;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.RelativeLayout;
        import android.widget.Spinner;
        import android.widget.TextView;

        import com.unnamed.b.atv.model.TreeNode;
        import com.unnamed.b.atv.view.AndroidTreeView;

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

        builder.setTitle("Добавить альтернативу");  // заголовок
        builder.setMessage("Добавьте существующую, либо укажите название новой"); // сообщение]


        View view = inflater.inflate(R.layout.add_alternative_dialog, null);


        final  Spinner spinner = (Spinner)view.findViewById(R.id.altSpinner);
        RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.select_add_type);
        final RadioButton addNew = (RadioButton)view.findViewById(R.id.radio_add_new);
        final RadioButton addExist = (RadioButton)view.findViewById(R.id.radio_add_exist);
        final EditText altNameEdit = (EditText)view.findViewById(R.id.dialog_alternative_name);

        if(AddProjectActivity.altNames.size() > 0){

            spinner.setVisibility(View.INVISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, AddProjectActivity.altNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);
            // заголовок
            spinner.setPrompt("Title");
            //spinner.setSelection(0);

            addNew.toggle();

            altNameEdit.setVisibility(View.VISIBLE);

            addNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spinner.setVisibility(View.INVISIBLE);
                    altNameEdit.setVisibility(View.VISIBLE);
                    v.setSelected(true);
                    addExist.setSelected(false);
                }
            });
            addExist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(true);
                    spinner.setVisibility(View.VISIBLE);
                    altNameEdit.setVisibility(View.INVISIBLE);
                    addNew.setSelected(false);
                }
            });
        }else{
            radioGroup.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) altNameEdit.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            altNameEdit.setLayoutParams(params);
            altNameEdit.setVisibility(View.VISIBLE);
        }
        builder.setView(view);

        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TreeNode newNode = null;
                String name = "";
                CriterionsTreeHolder.IconTreeItem crItemIcon = new CriterionsTreeHolder.IconTreeItem();
                TreeNode cr = AddProjectActivity.crNodes.get(crId);
                if(addExist.isSelected()){//!!!!!!
                    long selected_id = spinner.getSelectedItemId();

                    int i = 0;
                    int nodeKey = -1;
                    for (Integer key : AddProjectActivity.altNodes.keySet()){

                        if(i == (int)selected_id){
                            nodeKey = key;
                            break;
                        }
                        i++;
                    }

                    if(nodeKey != -1) {
                        CriterionsTreeHolder existHolder = ((CriterionsTreeHolder) AddProjectActivity.altNodes.get(nodeKey).getViewHolder());
                        newNode = new TreeNode(crItemIcon).setViewHolder(new CriterionsTreeHolder(getActivity()));
                        ((CriterionsTreeHolder) newNode.getViewHolder()).setValues(existHolder.getValues());
                        cr.addChild(newNode);
                    }
                }else{
                    CriterionsTreeHolder.IconTreeItem crItem = new CriterionsTreeHolder.IconTreeItem();
                    newNode = new TreeNode(crItem).setViewHolder(new CriterionsTreeHolder(getActivity()));
                    newNode.getViewHolder().getView();
                    EditText altNameEdit = (EditText)getDialog().findViewById(R.id.dialog_alternative_name);
                    name = altNameEdit.getText().toString();

                    cr.addChild(newNode);

                    CriterionsTreeHolder holder = (CriterionsTreeHolder)newNode.getViewHolder();

                    holder.setName(name);
                    AddProjectActivity.altNodes.put(holder.getValues().id, newNode);
                    AddProjectActivity.altNames.add(name);
                }

                EditText editTextName = (EditText)newNode.getViewHolder().getView().findViewById(R.id.criterion_add_text);
                TextView nodeNameTextView = (TextView)newNode.getViewHolder().getView().findViewById(R.id.node_name_view);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) editTextName.getLayoutParams();
                params.leftMargin = 25;
                nodeNameTextView.setLayoutParams(params);
                editTextName.setLayoutParams(params);

                Button buttonAdd = (Button)newNode.getViewHolder().getView().findViewById(R.id.add);
                buttonAdd.setVisibility(View.INVISIBLE);


                AndroidTreeView tree = cr.getViewHolder().getTreeView();
                tree.expandLevel(newNode.getLevel());

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
