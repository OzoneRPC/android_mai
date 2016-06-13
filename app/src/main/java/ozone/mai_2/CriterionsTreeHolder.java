package ozone.mai_2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
    private Window currentWindow;
    public CriterionsTreeHolder(Context context, Window window){
        super(context);
        this.currentContext = context;
        this.currentWindow = window;
    }
    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        final RelativeLayout currentLayout = (RelativeLayout)currentWindow.findViewById(R.id.main_relative_layout);
        final LayoutInflater inflater = LayoutInflater.from(currentContext);
        final View view = inflater.inflate(R.layout.tree_item, null, false);
        final EditText criterion_add_text = (EditText)view.findViewById(R.id.criterion_add_text);
        //final TextView criterion_text = (TextView)view.findViewById(R.id.criterion_text);
        final TextView add = (TextView)view.findViewById(R.id.add);
        final TextView delete = (TextView)view.findViewById(R.id.delete);

        criterion_add_text.setText("Критерий");

        /*Random r = new Random();
        criterion_add_text.setId(r.nextInt(80 - 65) + 65);*/

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                node.getParent().deleteChild(node);
                node.getViewHolder().getTreeView().expandLevel(node.getParent().getLevel());
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout.LayoutParams curParams = (RelativeLayout.LayoutParams) criterion_add_text.getLayoutParams();

                CriterionsTreeHolder.IconTreeItem crItemIcon = new CriterionsTreeHolder.IconTreeItem();
                TreeNode newNode = new TreeNode(crItemIcon).setViewHolder(new CriterionsTreeHolder(currentContext, currentWindow));

                View alternativeNode = newNode.getViewHolder().getView();

                EditText alternativeText = (EditText)alternativeNode.findViewById(R.id.criterion_add_text);

                Button add = (Button)alternativeNode.findViewById(R.id.add);

                if(node.getLevel() > 1){
                    add.setVisibility(View.INVISIBLE);
                }

                RelativeLayout.LayoutParams newParams = (RelativeLayout.LayoutParams)alternativeText.getLayoutParams();
                newParams.leftMargin = curParams.leftMargin + 30;
                alternativeText.setLayoutParams(newParams);
                alternativeText.setText("Альтернатива");
                node.addChild(newNode);

                node.getViewHolder().getTreeView().expandLevel(node.getLevel());


            }
        });

        /*criterion_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*v.setVisibility(View.INVISIBLE);
                criterion_add_text.setVisibility(View.VISIBLE);
                editTextFocus(criterion_add_text);*//*
            }
        });*/
        /*criterion_add_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (criterion_add_text.getVisibility() == View.VISIBLE && !criterion_add_text.isFocused()) {
                    if (criterion_add_text.getText().length() > 0) {
                        CharSequence text = criterion_add_text.getText();
                        criterion_text.setText(text);
                    }
                    criterion_text.setVisibility(View.VISIBLE);
                    criterion_add_text.setVisibility(View.INVISIBLE);
                }

            }
        });*/

        return view;
    }
    /*private void editTextFocus(EditText v){
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }*/
    public static class IconTreeItem {
        public int icon;
        public String text;
    }
}