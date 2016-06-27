package ozone.mai_2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ozone on 04.05.2016.
 */
public class ProjectsActivity extends AppCompatActivity {
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
        if(control.getProjectList() != null) {
            for (String name : control.getProjectList()) {
                projectsList.add(control.getProjectByName(name));
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
}
