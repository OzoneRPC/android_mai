package ozone.mai_2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.unnamed.b.atv.model.TreeNode;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ozone on 30.04.2016.
 */
public class Project{
    public String name;
    public String objective;
    public TreeNode tree;
    public List<ArrayList<Double>> criterionsMatrix;
    public HashMap<String, List<ArrayList<Double>>> alternativesMaxtrix;
    public List<Integer> criterionsPositions;
    public List<Integer> alternativesPostions;
    public String projectTreeJson;

    public JsonObject getProjectTree(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(projectTreeJson, JsonObject.class);
    }
}
