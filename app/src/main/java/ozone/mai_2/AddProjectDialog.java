package ozone.mai_2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ozone on 04.05.2016.
 */
public class AddProjectDialog extends DialogFragment{
    private final static int MIN_CRITERIONS_COUNT = 1;
    private final static int MIN_ALTERNATIVES_COUNT = 1;
    private int criterionsCount = 0;
    private int alternativesCount = 0;
    private List<View> criterionsListView;
    private List<View> alternativesListView;
    private SharedPreferences projects;

    private static String KEY_PROJECTS = "projects";
    private String existProjectsKey = "exitst_projects";
    private String projectKey = "project";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        criterionsListView = new ArrayList<View>();
        alternativesListView = new ArrayList<View>();
        projects = getActivity().getSharedPreferences(KEY_PROJECTS, 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.add_project, null);
        builder.setView(view);


        final EditText projectNameView = (EditText)view.findViewById(R.id.projectName);
        final EditText projectObjectiveView = (EditText)view.findViewById(R.id.projectObjective);
        final Button add_alternatives = (Button)view.findViewById(R.id.add_alternatives);
        final LinearLayout criterionLayout = (LinearLayout)view.findViewById(R.id.add_criterion_linear);
        final LinearLayout alternativesLayout = (LinearLayout)view.findViewById(R.id.add_alternative_linear);
        Button add_criterions = (Button)view.findViewById(R.id.add_criterions);
        add_criterions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = inflater.inflate(R.layout.custom_edittext_layout, null);
                EditText textfield = (EditText)view.findViewById(R.id.editText);
                textfield.setText("Критерий " + (criterionsCount+1));
                Button textfieldDelete = (Button)view.findViewById(R.id.editTextDelete);

                textfieldDelete.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        ((LinearLayout) view.getParent()).removeView(view);
                    }
                });

                criterionsListView.add(view);
                criterionLayout.addView(view);
                criterionsCount++;
                if(criterionsCount == 2){
                    add_alternatives.setVisibility(View.VISIBLE);
                }
            }
        });
        add_alternatives.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                final View view = inflater.inflate(R.layout.custom_edittext_layout, null);
                EditText textfield = (EditText)view.findViewById(R.id.editText);
                textfield.setText("Альтернатива " + (alternativesCount+1));
                Button textfieldDelete = (Button)view.findViewById(R.id.editTextDelete);

                textfieldDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) view.getParent()).removeView(view);
                    }
                });
                alternativesListView.add(view);
                alternativesLayout.addView(view);
                alternativesCount++;

            }
        });
        builder.setPositiveButton("Далее", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String projectName = projectNameView.getText().toString();
                String projectObjective = projectObjectiveView.getText().toString();
                saveProject(projectName, projectObjective);
                updateExistProjects(projectName);
                Bundle b = new Bundle();
                b.putString("project_name", projectName);

                Intent intent = new Intent("android.intent.action.CHOOSE_JUDGMENT");
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        builder.setNeutralButton("Сохранить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    protected void saveProject(String name, String objective){
        int criterionsCount = criterionsListView.size();
        int alternativesCount = alternativesListView.size();
        String[] criterions = new String[criterionsCount];
        String[] alternatives = new String[alternativesCount];
        for(int i=0; i < criterionsCount; i++) {
            criterions[i] = (((EditText) criterionsListView.get(i).findViewById(R.id.editText)).getText().toString());
        }
        for(int i=0; i < alternativesCount; i++) {
            alternatives[i] = (((EditText) alternativesListView.get(i).findViewById(R.id.editText)).getText().toString());
        }
        Project project = new Project();

        project.name = name;
        project.objective = objective;
        project.criterions = criterions;
        project.alternatives = alternatives;
        project.criterionsMatrix = generateCriterionsMatrix(criterionsCount);
        project.alternativeMatrix = generateAlternativesMatrix(criterionsCount, alternativesCount);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        SharedPreferences.Editor editor = projects.edit();
        editor.putString(name, gson.toJson(project, new TypeToken<Project>() {
        }.getType()));
        editor.apply();
    }
    protected void updateExistProjects(String name){
        Set<String> existProjects = projects.getStringSet(existProjectsKey, null);
        if(existProjects == null){
            existProjects = new HashSet<>();
        }
        existProjects.add(name);
        SharedPreferences.Editor editor = projects.edit();
        editor.putStringSet(existProjectsKey, existProjects);
        editor.apply();
    }

    private  List<ArrayList<Double>> generateCriterionsMatrix(int size){
        return generateMatrix(size);
    }
    private List<List<ArrayList<Double>>> generateAlternativesMatrix(int arrayCount, int vectorsCount){
        List<List<ArrayList<Double>>> matrix = new ArrayList<>();
        for(int i=0; i < arrayCount; i++){
            matrix.add(generateMatrix(vectorsCount));
        }
        return matrix;
    }
    private List<ArrayList<Double>> generateMatrix(int size){
        List<ArrayList<Double>> matrix = new ArrayList<>();
        for(int i=0; i < size;i++){
            ArrayList<Double> vector = new ArrayList<>();
            for(int j=0; j < size; j++){
                if(i == j){
                    vector.add(1.0);
                }else{
                    vector.add(0.0);
                }
            }
            matrix.add(vector);
        }
        return matrix;
    }
}
