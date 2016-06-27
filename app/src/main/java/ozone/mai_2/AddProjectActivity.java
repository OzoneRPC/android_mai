package ozone.mai_2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Ozone on 03.05.2016.
 */
public class AddProjectActivity extends AppCompatActivity {

    private ProjectControl control;
    private TreeNode crRoot;
    public static TreeNode crParent;
    private JsonObject criterionsList;

    public static HashMap<Integer, TreeNode> crNodes;
    public static List<String> altNames;
    public static LinkedHashMap<Integer, TreeNode> altNodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);
        setupParent(findViewById(R.id.add_project_layout));
        control = new ProjectControl(this);

        crNodes = new HashMap<>();
        criterionsList = new JsonObject();
        altNames = new ArrayList<>();
        altNodes = new LinkedHashMap<>();
        crRoot = TreeNode.root();
        DefaultTreeHolder defItemCriterions = new DefaultTreeHolder(this, getWindow());
        DefaultTreeHolder.IconTreeItem item = new DefaultTreeHolder.IconTreeItem();
        item.text = "Критерии";


        crParent = new TreeNode(item).setViewHolder(defItemCriterions);

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

                control.saveProject(project_name.getText().toString(), project_objective.getText().toString(), crParent);

                Intent intent = new Intent("android.intent.action.CHOOSE_JUDGMENT");
                String name = project_name.getText().toString();
                intent.putExtra("project_name", name);
                startActivityForResult(intent, 1);
            }
        });

    }


    public void addCriterion(String name){

        CriterionsTreeHolder.IconTreeItem crItemIcon = new CriterionsTreeHolder.IconTreeItem();
        TreeNode newNode = new TreeNode(crItemIcon).setViewHolder(new CriterionsTreeHolder(this));


        CriterionsTreeHolder holder = (CriterionsTreeHolder)newNode.getViewHolder();

        holder.setName(name);

        crNodes.put(holder.getValues().id, newNode);

        crParent.addChildren(newNode);

        String crId = ((CriterionsTreeHolder)newNode.getViewHolder()).getValues().id+"";
        criterionsList.add(crId, new JsonPrimitive(name));
        AndroidTreeView tree = crParent.getViewHolder().getTreeView();
        tree.expandLevel(newNode.getLevel());
    }
    public void addAlternative(Integer crId, TreeNode node, String name, boolean hasParent){

        /*TreeNode cr = AddProjectActivity.crNodes.get(crId);

        CriterionsTreeHolder holder = (CriterionsTreeHolder) node.getViewHolder();

        if(!hasParent) {
            altNodes.put(holder.getValues().id, node);
            altNames.add(name);
        }

        AndroidTreeView tree = cr.getViewHolder().getTreeView();
        tree.expandLevel(node.getLevel());*/
    }
    protected void setupParent(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    getCurrentFocus().clearFocus();
                    hideSoftKeyboard();
                    return false;
                }
            });
        }
        //If a layout container, iterate over children
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupParent(innerView);
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }



}
