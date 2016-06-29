package ozone.mai_2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ozone on 27.06.2016.
 */
public class ProjectAdapter extends ArrayAdapter<Project> {
    private final Context context;
    private final List<Project> projects;
    private final Activity activity;
    private ProjectControl control;
    public ProjectAdapter(Activity activity, List<Project> projects){
        super(activity, R.layout.row_layout, projects);
        this.context = activity;
        this.projects = projects;
        this.activity = activity;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        TextView projectNameView = (TextView)rowView.findViewById(R.id.project_name);
        TextView projectObjective = (TextView)rowView.findViewById(R.id.project_objective);
        TextView projectChangeView = (TextView)rowView.findViewById(R.id.project_change);
        TextView projectDeleteView = (TextView)rowView.findViewById(R.id.project_delete);
        TextView projectResult = (TextView)rowView.findViewById(R.id.project_completed);



        projectObjective.setText(projects.get(position).objective);
        projectDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectControl.deleteProject(projects.get(position).name);
                projects.remove(position);
                notifyDataSetChanged();
            }
        });


        if(projects.get(position).currentStage.equals("new")){
            projectChangeView.setText("Оценка");
        }
        projectChangeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CurrentProject.setCurrentProject(projects.get(position));
                Intent intent = new Intent("android.intent.action.CHOOSE_JUDGMENT");
                activity.startActivity(intent);
                activity.finish();
            }
        });

        if(projects.get(position).resultVector != null){
            Integer altId = -1;
            for(int key : projects.get(position).resultVector.keySet()){

                if(altId == -1){
                    altId = key;
                }
                if(projects.get(position).resultVector.get(key) > projects.get(position).resultVector.get(altId)){
                    altId = key;
                }
            }
            String altName = "";
            for(int i=0; i < projects.get(position).tree.getChildren().size(); i++){
                for(int j = 0; j < projects.get(position).tree.getChildren().get(i).size(); j++){
                    CriterionsTreeHolder holder = (CriterionsTreeHolder)projects.get(position).tree.getChildren().get(i).getChildren().get(j).getViewHolder();
                    int curAltId = holder.getValues().id;
                    if(curAltId == altId){
                        altName = holder.getValues().name;
                    }
                }
            }
            if(!(altName.equals(""))){
                String text = "наилучшая альтернатива - " + altName;
                projectResult.setText(text);
            }
        }else{
            projectResult.setText("нет");
        }
        projectNameView.setText(projects.get(position).name);

        return rowView;
    }
}
