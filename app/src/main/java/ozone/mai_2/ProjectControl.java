package ozone.mai_2;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.unnamed.b.atv.model.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ozone on 23.05.2016.
 */
public class ProjectControl{
    protected Gson gson = new GsonBuilder().create();
    protected SharedPreferences projects;
    protected static String KEY_PROJECTS = "projects";
    protected String existProjectsKey = "exitst_projects";
    protected String projectKey = "project";
   /* public List<View> criterionsListView;
    public List<View> alternativesListView;*/
    public MAI mai = new MAI();

    public ProjectControl(Activity activity){
        projects = activity.getSharedPreferences(KEY_PROJECTS, 0);
        //HashMap map = projects.mMap;
        /*criterionsListView = new ArrayList<View>();
        alternativesListView = new ArrayList<View>();*/
    }
    public Map getProjects(){
        return projects.getAll();
    }
    public void updateExistProjects(String name){
        Set<String> existProjects = projects.getStringSet(existProjectsKey, null);
        if(existProjects == null){
            existProjects = new HashSet<>();
        }
        existProjects.add(name);
        SharedPreferences.Editor editor = projects.edit();
        editor.putStringSet(existProjectsKey, existProjects);
        editor.apply();
    }
    /*public void saveProject(String name, String objective){
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
        project.criterionsMatrix = mai.generateCriterionsMatrix(criterionsCount);
        project.alternativeMatrix = mai.generateAlternativesMatrix(criterionsCount, alternativesCount);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        SharedPreferences.Editor editor = projects.edit();
        editor.putString(name, gson.toJson(project, new TypeToken<Project>() {
        }.getType()));
        editor.apply();
    }*/


    public void saveProject(String name, String objective, TreeNode tree){
        Project project = new Project();
        project.name = name;
        project.objective = objective;
        project.tree = tree;

    }
    public void updateProject(Project project){
        SharedPreferences.Editor editor = projects.edit();
        editor.putString(project.name, gson.toJson(project, new TypeToken<Project>() {
        }.getType()));
        editor.apply();
    }
    public Project getProjectByName(String name){
        return gson.fromJson(projects.getString(name, null), new TypeToken<Project>() {
        }.getType());
    }
}
