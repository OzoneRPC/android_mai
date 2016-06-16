package ozone.mai_2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;


import java.io.IOException;


/**
 * Created by Ozone on 04.05.2016.
 */
public class AddProjectDialog extends DialogFragment{
    private final static int MIN_CRITERIONS_COUNT = 1;
    private final static int MIN_ALTERNATIVES_COUNT = 1;
    /*private int criterionsCount = 0;
    private int alternativesCount = 0;
    /*//*private List<View> criterionsListView;
    private List<View> alternativesListView;*/
    private ProjectControl control;
    private TreeNode crRoot;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //projects = getActivity().getSharedPreferences(KEY_PROJECTS, 0);
        control = new ProjectControl(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        Activity activity = getActivity();

        Context context = activity;

        final View view = inflater.inflate(R.layout.add_project, null);

        builder.setView(view);

        crRoot = TreeNode.root();

        DefaultTreeHolder defItemCriterions = new DefaultTreeHolder(context, activity.getWindow());
        DefaultTreeHolder.IconTreeItem item = new DefaultTreeHolder.IconTreeItem();
        item.text = "Критерии";


        TreeNode crParent = new TreeNode(item).setViewHolder(defItemCriterions);

        crRoot.addChild(crParent);

        final AndroidTreeView tView = new AndroidTreeView(getActivity(), crRoot);

        RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.add_project_tree_layout);

        layout.addView(tView.getView());



        builder.setPositiveButton("Далее", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditText project_name = (EditText)view.findViewById(R.id.projectName);
                EditText project_objective = (EditText)view.findViewById(R.id.projectObjective);

                control.saveProject(project_name.getText().toString(),
                        project_objective.getText().toString(), crRoot.getChildren().get(0));


                AddProjectDialog dialogNew = new AddProjectDialog();
                dialogNew.show(getFragmentManager(), "dlg2");
                /*Intent intent = new Intent("android.intent.action.CHOOSE_JUDGMENT");
                intent.putExtras(b);
                startActivity(intent);*/
            }
        });

        builder.setNeutralButton("Сохранить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
