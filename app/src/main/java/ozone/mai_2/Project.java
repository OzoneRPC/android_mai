package ozone.mai_2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.unnamed.b.atv.model.TreeNode;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Ozone on 30.04.2016.
 */
public class Project{
    public String name;
    public String objective;
    public TreeNode tree;
    public LinkedHashMap<Integer,LinkedHashMap<Integer, Double>> criterionsMatrix;
    public LinkedHashMap<Integer,LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>>  alternativesMatrix;
    public List<Integer> crPositions;
    public List<Integer> altPositions;
    public LinkedHashMap<Integer, Double> resultVector = null;
    public String currentStage;
    public boolean criterionJudgmentMaked = false;
    public boolean alternativeJudgmentMaked = false;

}
