package ozone.mai_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infteh.comboseekbar.ComboSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private int judgType;
    private boolean changed;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.judgment);

        control = new ProjectControl(this);
        seekbarContainer = (LinearLayout)findViewById(R.id.comboseekbar_l_laylout);

        String nameFromExtras = this.getIntent().getExtras().getString("project_name");

        currentProject = control.getProjectByName(nameFromExtras);

        Intent intent = getIntent();
        judgType  = intent.getIntExtra("type", -1);
        changed = intent.getBooleanExtra("changed", false);
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
            EditText a = (EditText) currentProject.tree.getChildren().get(row).getViewHolder().getView().findViewById(R.id.criterion_add_text);
            EditText b = (EditText) currentProject.tree.getChildren().get(column).getViewHolder().getView().findViewById(R.id.criterion_add_text);
            criterionA.setText(a.getText().toString());
            criterionB.setText(b.getText().toString());
        }else{
            EditText nameEdit = (EditText) currentProject.tree.getChildren().get(criterionNumber).getViewHolder().getView().findViewById(R.id.criterion_add_text);
            EditText a = (EditText) currentProject.tree.getChildren().get(criterionNumber).getChildren().get(row).getViewHolder().getView().findViewById(R.id.criterion_add_text);
            EditText b = (EditText) currentProject.tree.getChildren().get(criterionNumber).getChildren().get(column).getViewHolder().getView().findViewById(R.id.criterion_add_text);
            criterionName.setText(nameEdit.getText());
            criterionA.setText(a.getText());
            criterionB.setText(b.getText());
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
}
