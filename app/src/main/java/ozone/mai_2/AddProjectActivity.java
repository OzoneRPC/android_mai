package ozone.mai_2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;


import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;


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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);
        control = new ProjectControl(this);
        final EditText edit_name = (EditText)findViewById(R.id.projectName);
        final EditText edit_obj = (EditText)findViewById(R.id.projectObjective);
        final Button btn_save = (Button)findViewById(R.id.save);
        TreeNode crRoot = TreeNode.root();

        DefaultTreeHolder defItemCriterions = new DefaultTreeHolder(this, getWindow());
        DefaultTreeHolder.IconTreeItem item = new DefaultTreeHolder.IconTreeItem();
        item.text = "Критерии";

        TreeNode crParent = new TreeNode(item).setViewHolder(defItemCriterions);

        crRoot.addChild(crParent);

        final AndroidTreeView tView = new AndroidTreeView(this, crRoot);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.add_project_tree_layout);

        layout.addView(tView.getView());

        RelativeLayout main_layout = (RelativeLayout)findViewById(R.id.main_layout);
        main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentFocus() instanceof EditText) {
                    getCurrentFocus().clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }
}
