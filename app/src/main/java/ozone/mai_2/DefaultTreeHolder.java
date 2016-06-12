package ozone.mai_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

/**
 * Created by Ozone on 28.05.2016.
 */

public class DefaultTreeHolder extends TreeNode.BaseNodeViewHolder<DefaultTreeHolder.IconTreeItem> {
    private String text;
    private Context currentContext;
    private Window currentWindow;
    public DefaultTreeHolder(Context context, Window window) {
        super(context);
        currentContext = context;
        currentWindow = window;
    }
    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.default_tree_item, null, false);
        final TextView text = (TextView)view.findViewById(R.id.tree_text);
        final TextView add = (TextView)view.findViewById(R.id.add);
        text.setText(value.text);

        final AndroidTreeView tree = this.getTreeView();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CriterionsTreeHolder.IconTreeItem crItemIcon = new CriterionsTreeHolder.IconTreeItem();
                node.addChildren(new TreeNode(crItemIcon).setViewHolder(new CriterionsTreeHolder(currentContext, currentWindow)));
                tree.expandLevel(node.getLevel());
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tree.toggleNode(node);
            }
        });
        return view;
    }
    public static class IconTreeItem {
        public int icon;
        public String text;
    }
}

