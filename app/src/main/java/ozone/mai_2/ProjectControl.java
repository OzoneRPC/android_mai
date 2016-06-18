package ozone.mai_2;

import android.app.Activity;

import android.content.SharedPreferences;

import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.unnamed.b.atv.model.TreeNode;




import java.lang.reflect.Type;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ozone on 23.05.2016.
 */

public class ProjectControl {
    protected Gson gson = new GsonBuilder().create();
    protected SharedPreferences projects;
    protected static String KEY_PROJECTS = "projects";
    protected String existProjectsKey = "exitst_projects";
    protected String projectKey = "project";
    private Type type_project;
    private Type type_treeNode;

    public MAI mai = new MAI();

    public ProjectControl(Activity activity){
        currentActivity.activity = activity;

        projects = activity.getSharedPreferences(KEY_PROJECTS, 0);
        type_project = new TypeToken<Project>(){}.getType();
        type_treeNode = new TypeToken<TreeNode>(){}.getType();
    }
    public Map getAllProjects(){
        return projects.getAll();
    }
    public void updateExistProjectsList(String name){
        Set<String> existProjects = projects.getStringSet(existProjectsKey, null);
        if(existProjects == null){
            existProjects = new HashSet<>();
        }
        existProjects.add(name);
        SharedPreferences.Editor editor = projects.edit();
        editor.putStringSet(existProjectsKey, existProjects);
        editor.apply();
    }


    public void saveProject(String name, String objective, TreeNode tree){
        SharedPreferences.Editor editor = projects.edit();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(TreeNode.class, new treeSerializer());
        Gson gson = builder.create();

        Type type_project = new TypeToken<Project>(){}.getType();

        Project project = new Project();
        project.name = name;
        project.objective = objective;
        project.tree = tree;

        project.criterionsMatrix = mai.generateCriterionsMatrix(tree.getChildren().size());
        project.alternativesMaxtrix = mai.generateAlternativesMatrix(tree);

        String projectJson = gson.toJson(project, type_project);

        editor.putString(name, projectJson);

        editor.apply();

        updateExistProjectsList(name);


    }
    public Project getProjectByName(String name){
        Project project = null;
        String projectJson = projects.getString(name, null);
        if(projectJson != null){
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(TreeNode.class, new treeUnserializer());
            Gson gson = builder.create();
            project = gson.fromJson(projectJson, type_project);
        }
        return project;
    }

    public void updateProject(Project project){
        SharedPreferences.Editor editor = projects.edit();
        editor.putString(project.name, gson.toJson(project, new TypeToken<Project>() {
        }.getType()));
        editor.apply();
    }

    public void deleteProject(String name){
        Set<String> existProjects = projects.getStringSet(existProjectsKey, null);
        if(existProjects != null){
            existProjects.remove(name);

            SharedPreferences.Editor editor = projects.edit();
            editor.putStringSet(existProjectsKey, existProjects);

            editor.remove(name);
            editor.apply();
        }
    }
    public String serializeTree(TreeNode tree){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(TreeNode.class, new treeSerializer());
        Gson gson = builder.create();

        Type type_treeNode = new TypeToken<TreeNode>(){}.getType();
        return gson.toJson(tree, type_treeNode);
    }
    public TreeNode unserializeTree(String json){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(TreeNode.class, new treeUnserializer());
        Gson gson = builder.create();

        Type type_treeNode = new TypeToken<TreeNode>(){}.getType();

        return gson.fromJson(json, type_treeNode);
    }

}
class treeSerializer implements JsonSerializer<TreeNode>
{
    @Override
    public JsonElement serialize(TreeNode src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray root = new JsonArray();

        int nodeNameEditText = R.id.criterion_add_text;
        for(TreeNode criterionNode : src.getChildren()){
            JsonObject criterionJson = new JsonObject();
            EditText criterionEditText = (EditText)criterionNode.getViewHolder().getView().findViewById(nodeNameEditText);

            criterionJson.add("name", new JsonPrimitive(criterionEditText.getText().toString()));

            JsonArray alternativesList = new JsonArray();

            for(TreeNode alternativeNode : criterionNode.getChildren()){
                JsonObject alternativeJson = new JsonObject();

                EditText alternativeEditText = (EditText)alternativeNode.getViewHolder().getView().findViewById(nodeNameEditText);
                alternativeJson.add("name", new JsonPrimitive(alternativeEditText.getText().toString()));
                alternativesList.add(alternativeJson);
            }
            criterionJson.add("alternatives", alternativesList);
            root.add(criterionJson);
        }

        return root;
    }
}
class treeUnserializer implements JsonDeserializer<TreeNode>{
    @Override
    public TreeNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context){
        JsonArray list = json.getAsJsonArray();

        DefaultTreeHolder defItemCriterions = new DefaultTreeHolder(currentActivity.activity, currentActivity.activity.getWindow());
        DefaultTreeHolder.IconTreeItem item = new DefaultTreeHolder.IconTreeItem();
        item.text = "Критерии";
        TreeNode rootNode = new TreeNode(defItemCriterions);

        for(int i = 0; i < list.size(); i++){
            JsonObject criterion = list.get(i).getAsJsonObject();
            CriterionsTreeHolder.IconTreeItem crItemIcon = new CriterionsTreeHolder.IconTreeItem();
            CriterionsTreeHolder holder = new CriterionsTreeHolder(currentActivity.activity,  currentActivity.activity.getWindow());
            TreeNode criterionNode = new TreeNode(crItemIcon).setViewHolder(holder);
            EditText criterionName = (EditText)criterionNode.getViewHolder().getView().findViewById(R.id.criterion_add_text);
            criterionName.setText(criterion.get("name").toString());

            JsonArray alternatives = criterion.get("alternatives").getAsJsonArray();
            for(int j = 0; j < alternatives.size(); j++){
                JsonObject alternative = alternatives.get(j).getAsJsonObject();
                CriterionsTreeHolder altHolder = new CriterionsTreeHolder(currentActivity.activity,  currentActivity.activity.getWindow());
                TreeNode alternativeNode = new TreeNode(crItemIcon).setViewHolder(altHolder);
                EditText alternativeName = (EditText)alternativeNode.getViewHolder().getView().findViewById(R.id.criterion_add_text);
                alternativeName.setText(alternative.get("name").toString());
                criterionNode.addChildren(alternativeNode);
            }
            rootNode.addChildren(criterionNode);
        }
        return rootNode;
    }
}

class currentActivity{
    public static Activity activity;
}






