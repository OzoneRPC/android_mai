package ozone.mai_2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.infteh.comboseekbar.ComboSeekBar;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private HashMap <String, ArrayList> alternatives = new HashMap<>();

    private HashMap<Integer, Integer> values = new HashMap<>();

    private int id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        printComboSeekbar();
        printTree();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.projects) {
            Intent intent = new Intent("android.intent.action.PROJECTS");
            startActivityForResult(intent, 1);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void printComboSeekbar(){
        ArrayList vector = new ArrayList();
        vector.add(0);
        vector.add(0);
        vector.add(0);
        vector.add(0);

        alternatives.put("First alternative", vector);
        alternatives.put("FiSerst alternative", vector);


        RelativeLayout contentLayout = (RelativeLayout)findViewById(R.id.content_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final LinearLayout linear = (LinearLayout) findViewById(R.id.linear);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final View view = getLayoutInflater().inflate(R.layout.custom_seekbar, null);
                ComboSeekBar comboSeekBar = (ComboSeekBar)view.findViewById(R.id.comboseekbar);
                List<String> points = new ArrayList<>();
                points.add("-9");
                points.add("-7");
                points.add("-5");
                points.add("-3");
                points.add("1");
                points.add("3");
                points.add("5");
                points.add("7");
                points.add("9");
                comboSeekBar.setId(id);
                id++;
                comboSeekBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View v, int position, long position_id) {
                        int view_id = v.getId();
                        values.put(v.getId(), position + 1);
                    }
                });
                comboSeekBar.setAdapter(points);
                comboSeekBar.setSelection(4);
                linear.addView(view);

            }
        });

        int n = 5;

        int[] list = new int[n];
        Arrays.fill(list, 0);

    }
    private void printTree(){
        TreeNode crRoot = TreeNode.root();

        DefaultTreeHolder defItemCriterions = new DefaultTreeHolder(this, getWindow());
        DefaultTreeHolder.IconTreeItem item = new DefaultTreeHolder.IconTreeItem();
        item.text = "Критерии";

        //CriterionsTreeHolder.IconTreeItem crItemIcon = new CriterionsTreeHolder.IconTreeItem();


        TreeNode crParent = new TreeNode(item).setViewHolder(defItemCriterions);
        //TreeNode crChild = new TreeNode(crItemIcon).setViewHolder(new CriterionsTreeHolder(this, getWindow()));

        //crParent.addChild(crChild);
        crRoot.addChild(crParent);
        final AndroidTreeView tView = new AndroidTreeView(this, crRoot);
        //tView.expandAll();
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.main_relative_layout);
        layout.addView(tView.getView());
        /*layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText)findViewById(R.id.criterion_add_text);
                if(text != null && text.isFocused()){
                    text.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });*/

        /*TreeNode parent = new TreeNode("ParentNode");
        CriterionsTreeHolder.IconTreeItem item = new CriterionsTreeHolder.IconTreeItem();
        TreeNode child1 = new TreeNode(item).setViewHolder(new CriterionsTreeHolder(this));
        TreeNode childChild1 = new TreeNode("childChild1");
        TreeNode childChild2 = new TreeNode("childChild2");
        TreeNode childChild3 = new TreeNode("childChild3");

        child1.addChildren(childChild1, childChild2, childChild3);
        parent.addChildren(child1);
        root.addChild(parent);
        final AndroidTreeView tView = new AndroidTreeView(this, root);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.main_relative_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(v instanceof EditText)){

                }
            }
        });*/
        //layout.addView(tView.getView());
    }
}


