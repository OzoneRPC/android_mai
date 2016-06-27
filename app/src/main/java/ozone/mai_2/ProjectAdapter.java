package ozone.mai_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ozone on 27.06.2016.
 */
public class ProjectAdapter extends ArrayAdapter<Project> {
    private final Context context;
    private final List<Project> projects;

    public ProjectAdapter(Context context, List<Project> projects){
        super(context, R.layout.row_layout, projects);
        this.context = context;
        this.projects = projects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        TextView projectNameView = (TextView)rowView.findViewById(R.id.project_name);
        TextView projectChangeView = (TextView)rowView.findViewById(R.id.project_change);
        TextView projectDeleteView = (TextView)rowView.findViewById(R.id.project_delete);

        projectNameView.setText(projects.get(position).name);

        return rowView;
    }
}
