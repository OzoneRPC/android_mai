package ozone.mai_2;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by Ozone on 04.05.2016.
 */
public class ProjectsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects);

        Button button = (Button)findViewById(R.id.add_project);

        AddProjectDialog dialog = new AddProjectDialog();
        dialog.show(getFragmentManager(), "dlg1");
    }
}
