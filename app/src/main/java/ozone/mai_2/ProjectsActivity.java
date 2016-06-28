package ozone.mai_2;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ozone on 04.05.2016.
 */
public class ProjectsActivity extends AppCompatActivity {
    private static ArrayList<Activity> activities = new ArrayList<Activity>();

    ProjectControl control;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects);
        control = new ProjectControl(this);

        /*ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.show();
*/
        List<Project> projectsList = new ArrayList<>();
        if(ProjectControl.getProjectList() != null && ProjectControl.getProjectList().size() != 0) {
            for (String name : ProjectControl.getProjectList()) {
                projectsList.add(ProjectControl.getProjectByName(name));
            }

            ProjectAdapter adapter = new ProjectAdapter(this, projectsList);
            ListView listView = (ListView) findViewById(R.id.project_list_view);
            listView.setAdapter(adapter);
        }else{
            setTitle("Проекты - пусто");
            TextView createIfEmpty = (TextView)findViewById(R.id.projects_empty_create_new);
            createIfEmpty.setVisibility(View.VISIBLE);

            createIfEmpty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.ADD_PROJECT");
                    intent.putExtra("type", "new");
                    startActivity(intent);
                    finish();
                }
            });

        }
        //progress.dismiss();
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.ADD_PROJECT");
                startActivity(intent);
            }
        });*/
        /*Intent intent = new Intent("android.intent.action.ADD_PROJECT");
        startActivity(intent);*/
        /*AddProjectDialog dialog = new AddProjectDialog();
        dialog.show(getFragmentManager(), "dlg1");*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_new:
                Intent intent = new Intent("android.intent.action.ADD_PROJECT");
                intent.putExtra("type", "new");
                startActivity(intent);
                return true;

            /*case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;*/

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.setGroupVisible(R.id.action_add_new,true);
        /*menu.add(0, 1, 0, R.string.menu_item1)
                .setIcon(android.R.drawable.ic_menu_add)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS
                                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);*/

        /*if (chbAddDel.isChecked()) {
            menu.add(0, MENU_ID, 0, R.string.menu_item1)
                    .setIcon(android.R.drawable.ic_delete)
                    .setShowAsAction(
                            MenuItem.SHOW_AS_ACTION_ALWAYS
                                    | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        } else {
            menu.removeItem(MENU_ID);
        }*/
        return true;
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        activities.remove(this);
    }



}
