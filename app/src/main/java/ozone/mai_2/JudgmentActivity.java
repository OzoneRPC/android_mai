package ozone.mai_2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infteh.comboseekbar.ComboSeekBar;
import com.unnamed.b.atv.model.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ozone on 09.05.2016.
 */
public class JudgmentActivity extends AppCompatActivity {
    private HashMap<Integer, Integer> values = new HashMap<>();
    private Project currentProject;
    private int id = 0;
    private int notCompleted = 0;
    private LinearLayout seekbarContainer;
    private static ArrayList<Activity> activities = new ArrayList<Activity>();
    private ArrayList<String> crList;

    private int judgType;
    private boolean changed;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.judgment);
        activities.add(this);

        seekbarContainer = (LinearLayout)findViewById(R.id.comboseekbar_l_laylout);


        currentProject = CurrentProject.getCurrentProject();

        //projectTree = currentProject.getProjectTree();
        Intent intent = getIntent();
        judgType  = intent.getIntExtra("type", -1);
        changed = intent.getBooleanExtra("changed", false);


        switch (judgType){
            case 0:
                if(!changed) {
                    currentProject.criterionsPositions = ProjectControl.initializePositionsList(currentProject.criterionsMatrix.size());
                }
                setTitle(R.string.judgment_criterions_title);
                criterions();
                break;
            case 1:
                int alternativesCount = 0;
                /*if(!changed) {
                    for (int i = 0; i < currentProject.alternativesMatrix.size(); i++) {

                        for (int j = 0; j < currentProject.alternativesMatrix.get(i).size(); j++) {
                            alternativesCount++;
                        }
                    }
                    currentProject.alternativesPostions = ProjectControl.initializePositionsList(alternativesCount);
                }*/
                setTitle(R.string.judgment_alternatives_title);
                alternatives();
                break;
        }

    }
    private void addComboSeekBar(final int rowKey, final int colKey, final int crId){
        final View view = getLayoutInflater().inflate(R.layout.custom_seekbar, null);
        ComboSeekBar comboSeekBar = (ComboSeekBar)view.findViewById(R.id.comboseekbar);
        TextView criterionName = (TextView)view.findViewById(R.id.criterion_name);
        TextView criterionA = (TextView)view.findViewById(R.id.item_a);
        TextView criterionB = (TextView)view.findViewById(R.id.item_b);

        if(crId == -1) {

            TreeNode aNode = getNodeById(rowKey);
            TreeNode bNode = getNodeById(colKey);
            if(aNode != null && bNode != null){
                String aText = ((CriterionsTreeHolder)aNode.getViewHolder()).getValues().name;
                String bText = ((CriterionsTreeHolder)bNode.getViewHolder()).getValues().name;

                criterionA.append(aText.replace("\"", "").replace("\\", ""));
                criterionB.append(bText.replace("\"", "").replace("\\", ""));
            }

        }else{

            TreeNode crNode = getNodeById(crId);
            TreeNode aNode = getNodeById(rowKey);
            TreeNode bNode = getNodeById(colKey);
            if(aNode != null && bNode != null && crNode != null){
                String crText = ((CriterionsTreeHolder)crNode.getViewHolder()).getValues().name;
                String aText = ((CriterionsTreeHolder)aNode.getViewHolder()).getValues().name;
                String bText = ((CriterionsTreeHolder)bNode.getViewHolder()).getValues().name;

                criterionName.setText(crText.replace("\"", "").replace("\\", ""));
                criterionA.append(aText.replace("\"", "").replace("\\", ""));
                criterionB.append(bText.replace("\"", "").replace("\\", ""));

            }
        }


        List<String> points = new ArrayList<>();
        points.add("9");
        points.add("7");
        points.add("5");
        points.add("3");
        points.add("1");
        points.add("3");
        points.add("5");
        points.add("7");
        points.add("9");
        comboSeekBar.setId(id);
        comboSeekBar.setAdapter(points);
        int selection;

        /*if(changed){
            if(judgType == 0){
                selection = currentProject.criterionsPositions.get(id);
            }else{
                selection = currentProject.alternativesPostions.get(id);
            }
        }else{
        }*/
        selection = 4;
        comboSeekBar.setSelection(selection);

        comboSeekBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long position_id) {

                /*if(judgType == 0){
                    currentProject.criterionsPositions.set(v.getId(), position);
                }else{
                    currentProject.alternativesPostions.set(v.getId(), position);
                }*/
                double value = getComboSeebarValueByPosition(position);
                double invertedValue;
                if(position < 4){
                    invertedValue = value;
                    value = 1.0/value;
                }else{
                    invertedValue = 1.0/value;
                }
                if(crId == -1) {
                    currentProject.criterionsMatrix.get(rowKey).put(colKey, value);
                    currentProject.criterionsMatrix.get(colKey).put(rowKey, invertedValue);
                }else{
                    currentProject.alternativesMatrix.get(crId).get(rowKey).put(colKey, value);
                    currentProject.alternativesMatrix.get(crId).get(colKey).put(rowKey, invertedValue);
                }
                notCompleted--;
                if(notCompleted == 0 || changed){
                    Button save = (Button)findViewById(R.id.save);
                    save.setVisibility(View.VISIBLE);
                    save.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            saveAndExit();
                        }
                    });
                }
            }
        });
        id++;
        seekbarContainer.addView(view);
    }
    private void criterions(){

        List<Integer> columnKeys = new ArrayList<>(currentProject.criterionsMatrix.keySet());

        for (int i = 0; i < columnKeys.size()-1; i++){
            HashMap<Integer, Double> rowMap = (HashMap<Integer, Double>)currentProject.criterionsMatrix.get(columnKeys.get(i));
            List<Integer> rowKeys = new ArrayList<Integer>(rowMap.keySet());

            for(int j = 1; j <= rowKeys.size() - 1 - i; j++ ){
                int rowKey = columnKeys.get(i);
                int colKey = columnKeys.get(j+i);
                addComboSeekBar(rowKey, colKey, -1);
                notCompleted++;
            }
        }




    }
    private void alternatives(){
        /*for(int i = 0; i < currentProject.alternativesMaxtrix.size(); i++){

            for(int j = 0; j < currentProject.alternativesMaxtrix.get(i).size() - 1; j++){
                for(int k = 1; k <= currentProject.alternativesMaxtrix.get(i).get(j).size() - 1 - j; k++){
                    addComboSeekBar(j , k+j, i);
                    notCompleted++;
                }
            }
        }*/

        List<Integer> crIds = new ArrayList<>(currentProject.alternativesMatrix.keySet());
        for (int i=0; i < crIds.size(); i++) {
            int crKey = crIds.get(i);

            List<Integer> altColIds = new ArrayList<>(((LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>) currentProject.alternativesMatrix.get(crKey)).keySet());

            for (int j = 0; j < altColIds.size() - 1; j++) {
                int altRowKey = altColIds.get(j);

                List<Integer> altRowIds = new ArrayList<>(((LinkedHashMap<Integer, Double>) currentProject.alternativesMatrix.get(crKey).get(altRowKey)).keySet());

                for (int k = 1; k <= altRowIds.size() - 1 - j; k++) {
                    int alColKey = altRowIds.get(k + j);

                    addComboSeekBar(altRowKey, alColKey, crKey);
                    notCompleted++;
                }

            }
        }


    }
    private int getComboSeebarValueByPosition(int position){
        int result = -1;
        switch (position){
            case 0:
            case 8:
                result = 9;
                break;
            case 1:
            case 7:
                result = 7;
                break;
            case 2:
            case 6:
                result = 5;
                break;
            case 3:
            case 5:
                result = 3;
                break;
            case 4:
                result = 1;
                break;
        }
        return result;
    }
    public int getComboSeebarValueByPositionFull(int position){
        int result = -1;
        switch (position){
            case 0:
            case 16:
                result = 9;
                break;
            case 1:
            case 15:
                result = 8;
                break;
            case 2:
            case 14:
                result = 7;
                break;
            case 3:
            case 13:
                result = 6;
                break;
            case 4:
            case 12:
                result = 5;
                break;
            case 5:
            case 11:
                result = 4;
                break;
            case 6:
            case 10:
                result = 3;
                break;
            case 7:
            case 9:
                result = 2;
                break;
        }
        return result;
    }
        @Override
    public void onBackPressed() {
        judgType = -1;
        saveAndExit();
        finish();
    }
    private void saveAndExit(){
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.show();

        ProjectControl.updateProject(currentProject);

        progress.dismiss();

        Intent intent = new Intent();
        intent.putExtra("completed_type",judgType);
        setResult(RESULT_OK, intent);
        finish();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        activities.remove(this);
    }
    public TreeNode getNodeById(int id){
        for(int i=0; i < currentProject.tree.getChildren().size(); i++){

            int crId = ((CriterionsTreeHolder)currentProject.tree.getChildren().get(i).getViewHolder()).getValues().id;

            if(crId == id){
                return currentProject.tree.getChildren().get(i);
            }
            for(int j=0; j < currentProject.tree.getChildren().get(i).size(); j++){
                if(id == ((CriterionsTreeHolder)currentProject.tree.getChildren().get(i).getChildren().get(j).getViewHolder()).getValues().id){
                    return currentProject.tree.getChildren().get(i).getChildren().get(j);
                }
            }
        }
        return null;
    }
}
