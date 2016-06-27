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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public static String nodeType_alternative = "alternative";
    public static String nodeType_criterion = "criterion";

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

        Type type_project = new TypeToken<Project>(){}.getType();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(TreeNode.class, new treeSerializer());
        Gson gson = builder.create();

        Project project = new Project();
        project.name = name;
        project.objective = objective;
        project.tree = tree;

        project.criterionsMatrix = mai.generateCriterionsMatrix(tree.getChildren());
        project.alternativesMatrix = mai.generateAlternativesMatrix(tree.getChildren());


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
    public String serializeJsonObject(JsonObject projectTree){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(projectTree);
    }
    public JsonObject userializeJsonObject(String json){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(json, JsonObject.class);
    }

    public void updateProject(Project project){
        SharedPreferences.Editor editor = projects.edit();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(TreeNode.class, new treeSerializer());
        Gson gson = builder.create();

        editor.remove(project.name);
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
    public List<Integer> initializePositionsList(int count){
        List<Integer> list = new ArrayList<>();
        for(int i=0; i < count; i++){
            list.add(0);
        }
        return list;
    }

}
class treeSerializer implements JsonSerializer<TreeNode>
{
    @Override
    public JsonElement serialize(TreeNode src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray root = new JsonArray();

        for(TreeNode criterionNode : src.getChildren()){
            String crName = ((CriterionsTreeHolder)criterionNode.getViewHolder()).getValues().name;
            Integer crId = ((CriterionsTreeHolder)criterionNode.getViewHolder()).getValues().id;
            JsonObject criterion = new JsonObject();
            criterion.add("id", new JsonPrimitive(crId));
            criterion.add("name", new JsonPrimitive(crName));

            JsonArray alternatives = new JsonArray();

            for (TreeNode altNode : criterionNode.getChildren()){
                JsonObject alternative = new JsonObject();
                String altName = ((CriterionsTreeHolder)altNode.getViewHolder()).getValues().name;
                Integer altId = ((CriterionsTreeHolder)altNode.getViewHolder()).getValues().id;
                alternative.add("id", new JsonPrimitive(altId));
                alternative.add("name", new JsonPrimitive(altName));
                alternatives.add(alternative);
            }
            criterion.add("children", alternatives);
            root.add(criterion);
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

        HashMap<Integer, TreeNode> nodes = new HashMap<>();
        for(int i = 0; i < list.size(); i++){
            JsonObject criterion = list.get(i).getAsJsonObject();

            CriterionsTreeHolder.IconTreeItem crItemIcon = new CriterionsTreeHolder.IconTreeItem();
            TreeNode criterionNode = new TreeNode(crItemIcon).setViewHolder( new CriterionsTreeHolder(currentActivity.activity));

            CriterionsTreeHolder holder = (CriterionsTreeHolder)criterionNode.getViewHolder();
            holder.setValues(criterion.get("id").getAsInt(), criterion.get("name").getAsString());

            JsonArray alternatives = criterion.get("children").getAsJsonArray();
            for(int j = 0; j < alternatives.size(); j++){
                JsonObject alternative = alternatives.get(j).getAsJsonObject();
                CriterionsTreeHolder altHolder = new CriterionsTreeHolder(currentActivity.activity);
                TreeNode alternativeNode = new TreeNode(crItemIcon).setViewHolder(altHolder);

                int altId = alternative.get("id").getAsInt();
                if(nodes.containsKey(altId)){
                    altHolder.setValues(((CriterionsTreeHolder)nodes.get(altId).getViewHolder()).getValues());
                }else{
                    String altName = alternative.get("name").getAsString();
                    ((CriterionsTreeHolder)alternativeNode.getViewHolder()).setValues(altId, altName);
                }
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






