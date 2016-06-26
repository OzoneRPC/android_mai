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
    private Window currentWindow;
    public String nodeType;
    public int id;
    public int index = 1;
    public CriterionsTreeHolder(Context context, Window window){
        super(context);
        this.currentContext = context;
        this.currentWindow = window;
    }
    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(currentContext);
        final View view = inflater.inflate(R.layout.tree_item, null, false);
        final EditText criterion_add_text = (EditText)view.findViewById(R.id.criterion_add_text);
        final TextView add = (TextView)view.findViewById(R.id.add);
        final TextView delete = (TextView)view.findViewById(R.id.delete);

        criterion_add_text.setText(R.string.criterion);

        Random r = new Random();

        id = (r.nextInt(80 - 65) + 65);
        view.setId(id);

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
                args.putInt("id", id);
                Activity activity = (Activity) currentContext;
                FragmentManager manager = activity.getFragmentManager();
                AddAlternativeDialog dialog = new AddAlternativeDialog();
                dialog.setArguments(args);
                dialog.show(manager, "dialog");
            }
        });


        return view;
    }

    public static class IconTreeItem {
        public int icon;
        public String text;
    }
}