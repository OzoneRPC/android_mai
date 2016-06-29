package ozone.mai_2;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.Random;

/**
 * Created by Ozone on 27.05.2016.
 */
public class CriterionsTreeHolder extends TreeNode.BaseNodeViewHolder<CriterionsTreeHolder.IconTreeItem>{
    private Context currentContext;
    public String nodeType;
    public int index = 1;

    public  HolderValues values;

    public String name;
    public CriterionsTreeHolder(Context context){
        super(context);
        this.currentContext = context;
    }
    @Override
    public View createNodeView(final TreeNode node, final IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(currentContext);
        final View view = inflater.inflate(R.layout.tree_item, null, false);
        final EditText nodeEditText = (EditText)view.findViewById(R.id.criterion_add_text);
        final TextView add = (TextView)view.findViewById(R.id.add);
        final TextView delete = (TextView)view.findViewById(R.id.delete);
        final TextView nodeNameText = (TextView)view.findViewById(R.id.node_name_view);

        nodeEditText.setText(R.string.criterion);

        Random r = new Random();


        if(values == null){
            values = new HolderValues();
            values.id = AddProjectActivity.id++;
        }


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                node.getParent().deleteChild(node);
                node.getViewHolder().getTreeView().expandLevel(node.getParent().getLevel());
                index--;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("id", values.id);
                Activity activity = (Activity) currentContext;
                FragmentManager manager = activity.getFragmentManager();
                AddAlternativeDialog dialog = new AddAlternativeDialog();
                dialog.setArguments(args);
                dialog.show(manager, "dialog");
            }
        });
        nodeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //getTreeView().collapseAll();
                if(!hasFocus){

                    values.name = ((TextView)v).getText().toString();
                    nodeNameText.setText(((TextView)v).getText().toString());
                    v.setVisibility(View.INVISIBLE);
                    nodeNameText.setVisibility(View.VISIBLE);


                    for(TreeNode crNode : AddProjectActivity.crParent.getChildren()){

                        for(TreeNode altNode : crNode.getChildren()){
                            EditText  altnameEdit = (EditText)altNode.getViewHolder().getView().findViewById(R.id.criterion_add_text);
                            TextView nodeNameText = (TextView)altNode.getViewHolder().getView().findViewById(R.id.node_name_view);
                            String viewString = altnameEdit.getText().toString();
                            String valueString = ((CriterionsTreeHolder)altNode.getViewHolder()).getValues().name;
                            if(!(altnameEdit.getText().toString().equals(((CriterionsTreeHolder)altNode.getViewHolder()).getValues().name))){
                                altnameEdit.setText(((CriterionsTreeHolder)altNode.getViewHolder()).getValues().name);
                                nodeNameText.setText(((CriterionsTreeHolder)altNode.getViewHolder()).getValues().name);
                            }
                        }
                    }
                }
            }
        });
        nodeNameText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.setVisibility(View.INVISIBLE);
                nodeEditText.setText(((TextView)v).getText());
                nodeEditText.setVisibility(View.VISIBLE);
                nodeEditText.requestFocus();
                return true;
            }
        });


        return view;
    }
    public void setName(String name){
        EditText text = (EditText)this.getView().findViewById(R.id.criterion_add_text);
        TextView nameView = (TextView)this.getView().findViewById(R.id.node_name_view);
        nameView.setText(name);
        values.name = name;
    }
    public void setId(int id){
        values.id = id;
    }
    public void setValues(int id, String name){
        if(this.values == null){
            this.values = new HolderValues();
        }
        this.values.id = id;
        this.values.name = name;
        EditText text = (EditText)this.getView().findViewById(R.id.criterion_add_text);
        TextView nameView = (TextView)this.getView().findViewById(R.id.node_name_view);
        text.setText(name);
        nameView.setText(name);
    }
    public HolderValues getValues(){
        return this.values;
    }

    public void setValues(HolderValues newValues){
        this.values = newValues;
        EditText text = (EditText)this.getView().findViewById(R.id.criterion_add_text);
        TextView nameView = (TextView)this.getView().findViewById(R.id.node_name_view);
        nameView.setText(newValues.name);
        text.setText(newValues.name);
    }

    public static class IconTreeItem {
        public int icon;
        public String text;
    }
}
class HolderValues{
    Integer id;
    String name;
}