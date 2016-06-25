package ozone.mai_2;

import android.app.Activity;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Ozone on 09.05.2016.
 */
public class JudgmentActivity extends AppCompatActivity {
    private HashMap<Integer, Integer> values = new HashMap<>();
    private Project currentProject;
    private int id = 0;
    private int notCompleted = 0;
    private LinearLayout seekbarContainer;
    private ProjectControl control;
    private static ArrayList<Activity> activities=new ArrayList<Activity>();
    private JsonObject projectTree;
    private ArrayList<String> crList;

    private int judgType;
    private boolean changed;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.judgment);
        activities.add(this);
        control = new ProjectControl(this);
        seekbarContainer = (LinearLayout)findViewById(R.id.comboseekbar_l_laylout);

        String nameFromExtras = this.getIntent().getExtras().getString("project_name");

        currentProject = control.getProjectByName(nameFromExtras);

        projectTree = currentProject.getProjectTree();
        Intent intent = getIntent();
        judgType  = intent.getIntExtra("type", -1);
        changed = intent.getBooleanExtra("changed", false);

        crList = new ArrayList<>();

        for(Map.Entry<String, JsonElement> entry : projectTree.entrySet()){
            for(Map.Entry<String, JsonElement> group : entry.getValue().getAsJsonObject().entrySet()){
                if(group.getKey() != "alternatives"){
                    crList.add(group.getKey());
                }
            }
        }

        switch (judgType){
            case 0:
                if(!changed) {
                    currentProject.criterionsPositions = control.initializePositionsList(currentProject.criterionsMatrix.size());
                }
                setTitle(R.string.judgment_criterions_title);
                criterions();
                break;
            case 1:
                int alternativesCount = 0;
                if(!changed) {
                    for (int i = 0; i < currentProject.alternativesMaxtrix.size(); i++) {

                        for (int j = 0; j < currentProject.alternativesMaxtrix.get(i).size(); j++) {
                            alternativesCount++;
                        }
                    }
                    currentProject.alternativesPostions = control.initializePositionsList(alternativesCount);
                }
                setTitle(R.string.judgment_alternatives_title);
                alternatives();
                break;
        }

    }
    private void addComboSeekBar(final int row, final int column, final int criterionNumber){
        final View view = getLayoutInflater().inflate(R.layout.custom_seekbar, null);
        ComboSeekBar comboSeekBar = (ComboSeekBar)view.findViewById(R.id.comboseekbar);
        TextView criterionName = (TextView)view.findViewById(R.id.criterion_name);
        TextView criterionA = (TextView)view.findViewById(R.id.item_a);
        TextView criterionB = (TextView)view.findViewById(R.id.item_b);

        if(criterionNumber == -1) {

            EditText a = (EditText) getTreeNodeById(Integer.parseInt(crList.get(row))).getViewHolder().getView().findViewById(R.id.criterion_add_text);
            EditText b = (EditText) getTreeNodeById(Integer.parseInt(crList.get(column))).getViewHolder().getView().findViewById(R.id.criterion_add_text);
            criterionA.append(a.getText().toString().replace("\"", "").replace("\\", ""));
            criterionB.append(b.getText().toString().replace("\"", "").replace("\\", ""));
        }else{
            EditText nameEdit = (EditText) currentProject.tree.getChildren().get(criterionNumber).getViewHolder().getView().findViewById(R.id.criterion_add_text);
            EditText a = (EditText) currentProject.tree.getChildren().get(criterionNumber).getChildren().get(row).getViewHolder().getView().findViewById(R.id.criterion_add_text);
            EditText b = (EditText) currentProject.tree.getChildren().get(criterionNumber).getChildren().get(column).getViewHolder().getView().findViewById(R.id.criterion_add_text);
            criterionName.setText(nameEdit.getText().toString().replace("\"", "").replace("\\", ""));
            criterionA.append(a.getText().toString().replace("\"", "").replace("\\", ""));
            criterionB.append(b.getText().toString().replace("\"", "").replace("\\", ""));
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

        if(changed){
            if(judgType == 0){
                selection = currentProject.criterionsPositions.get(id);
            }else{
                selection = currentProject.alternativesPostions.get(id);
            }
        }else{
            selection = 4;
        }
        comboSeekBar.setSelection(selection);

        comboSeekBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long position_id) {

                if(judgType == 0){
                    currentProject.criterionsPositions.set(v.getId(), position);
                }else{
                    currentProject.alternativesPostions.set(v.getId(), position);
                }
                double value = getComboSeebarValueByPosition(position);
                double invertedValue;
                if(position < 4){
                    invertedValue = value;
                    value = 1.0/value;
                }else{
                    invertedValue = 1.0/value;
                }
                if(criterionNumber == -1) {
                    currentProject.criterionsMatrix.get(row).set(column, value);
                    currentProject.criterionsMatrix.get(column).set(row, invertedValue);
                }else{
                    currentProject.alternativesMaxtrix.get(criterionNumber).get(row).set(column, value);
                    currentProject.alternativesMaxtrix.get(criterionNumber).get(column).set(row, invertedValue);
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
        int size = currentProject.criterionsMatrix.size();
        for(int i = 0; i < size-1; i++){
            for(int j = 1; j <= currentProject.criterionsMatrix.get(i).size() - 1 - i; j++ ){
                addComboSeekBar(i , j+i, -1);
                notCompleted++;
            }
        }
    }
    private void alternatives(){
        for(int i = 0; i < currentProject.alternativesMaxtrix.size(); i++){

            for(int j = 0; j < currentProject.alternativesMaxtrix.get(i).size() - 1; j++){
                for(int k = 1; k <= currentProject.alternativesMaxtrix.get(i).get(j).size() - 1 - j; k++){
                    addComboSeekBar(j , k+j, i);
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
        control.updateProject(currentProject);

        Intent intent = new Intent();
        intent.putExtra("project_name", currentProject.name);
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
    public TreeNode getTreeNodeById(int id){
        TreeNode node = null;
        for(int i=0; i < currentProject.tree.getChildren().size(); i++){
            for(int j=0; j < currentProject.tree.getChildren().get(i).size(); j++){
                if(id == ((CriterionsTreeHolder)currentProject.tree.getChildren().get(i).getChildren().get(j).getViewHolder()).id){
                    node =  currentProject.tree.getChildren().get(i).getChildren().get(j);
                }
            }
        }
        return node;
    }
}
