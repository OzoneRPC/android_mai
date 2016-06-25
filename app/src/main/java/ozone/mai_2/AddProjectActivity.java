package ozone.mai_2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;


import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Ozone on 03.05.2016.
 */
public class AddProjectActivity extends AppCompatActivity {
    private final static int MIN_CRITERIONS_COUNT = 1;
    private final static int MIN_ALTERNATIVES_COUNT = 1;
    private int criterionsCount = 0;
    private int alternativesCount = 0;
    /*private List<View> criterionsListView;
    private List<View> alternativesListView;*/
    private ProjectControl control;
    private TreeNode crRoot;
    private JsonObject projectTree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);

        control = new ProjectControl(this);

        projectTree = new JsonObject();

        crRoot = TreeNode.root();

        DefaultTreeHolder defItemCriterions = new DefaultTreeHolder(this, getWindow());
        DefaultTreeHolder.IconTreeItem item = new DefaultTreeHolder.IconTreeItem();
        item.text = "Критерии";


        final TreeNode crParent = new TreeNode(item).setViewHolder(defItemCriterions);

        crRoot.addChild(crParent);

        final AndroidTreeView tView = new AndroidTreeView(this, crRoot);


        RelativeLayout layout = (RelativeLayout)findViewById(R.id.add_project_tree_layout);

        layout.addView(tView.getView());

        final Button btnSave = (Button) findViewById(R.id.save);
        final Button btnSaveAndContinue = (Button) findViewById(R.id.saveAndContinue);

        btnSaveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText project_name = (EditText)findViewById(R.id.projectName);
                EditText project_objective = (EditText)findViewById(R.id.projectObjective);

                projectTree = control.TreeNodeToJson(crParent);

                control.saveProject(project_name.getText().toString(),
                        project_objective.getText().toString(), crRoot.getChildren().get(0), projectTree);

                Intent intent = new Intent("android.intent.action.CHOOSE_JUDGMENT");
                String name = project_name.getText().toString();
                intent.putExtra("project_name", name);
                startActivityForResult(intent, 1);
            }
        });

    }
    public void addTreeItem(int nodeId, int type){
        TreeNode currentNode = null;
        for(TreeNode node : crRoot.getChildren().get(0).getChildren()){
            if(nodeId == node.getId()){
                currentNode = node;
            }
        }
        if(currentNode != null) {
            CriterionsTreeHolder.IconTreeItem crItemIcon = new CriterionsTreeHolder.IconTreeItem();
            TreeNode newNode = new TreeNode(crItemIcon).setViewHolder(new CriterionsTreeHolder(this, getWindow()));

            CriterionsTreeHolder newNodeHolder = (CriterionsTreeHolder) newNode.getViewHolder();
            View nodeView = newNode.getViewHolder().getView();


            EditText text = (EditText) nodeView.findViewById(R.id.criterion_add_text);

            Button add = (Button) nodeView.findViewById(R.id.add);
            add.setGravity(Gravity.RIGHT);
            add.setVisibility(View.INVISIBLE);


            RelativeLayout.LayoutParams newParams = (RelativeLayout.LayoutParams) text.getLayoutParams();
            text.setLayoutParams(newParams);
            if (type == 0) {
                newNodeHolder.nodeType = control.nodeType_alternative;
                text.setText("Альтернатива");
                newParams.leftMargin = 100;
                currentNode.addChild(newNode);
            } else {
                newNodeHolder.nodeType = control.nodeType_criterion;
                newParams.leftMargin = 50;
                text.setText("Связанный критерий");
                List<TreeNode> altNodes = new ArrayList<>();
                for(TreeNode node : currentNode.getChildren()){
                    CriterionsTreeHolder nodeViewHolder = (CriterionsTreeHolder)node.getViewHolder();
                    if(nodeViewHolder.nodeType == control.nodeType_alternative){
                        altNodes.add(node);
                    }
                }
                for(TreeNode node : altNodes){
                    currentNode.deleteChild(node);
                }
                currentNode.addChild(newNode);
                currentNode.addChildren(altNodes);
            }
            currentNode.getViewHolder().getTreeView().expandLevel(currentNode.getLevel());
        }
    }
}
