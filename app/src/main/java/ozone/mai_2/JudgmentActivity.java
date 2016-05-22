package ozone.mai_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.infteh.comboseekbar.ComboSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ozone on 09.05.2016.
 */
public class JudgmentActivity extends AppCompatActivity {
    private SharedPreferences projects;
    private HashMap<Integer, Integer> values = new HashMap<>();
    private Project currentProject;
    private int id = 0;
    private int notCompleted = 0;
    private LinearLayout seekbarContainer;
    private Gson gson = new GsonBuilder().create();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.judgment);
        seekbarContainer = (LinearLayout)findViewById(R.id.comboseekbar_l_laylout);

        projects = getSharedPreferences("projects", MODE_PRIVATE);

        currentProject = gson.fromJson(projects.getString(this.getIntent().getExtras().getString("project_name"), null), new TypeToken<Project>() {
        }.getType());

        Intent intent = getIntent();
        int type = intent.getIntExtra("type", -1);
        switch (type){
            case 0:
                setTitle(R.string.judgment_criterions_title);
                criterions();
                break;
            case 1:
                setTitle(R.string.judgment_alternatives_title);
                break;

        }

    }
    private void addComboSeekBar(final int row, final int column){
        final View view = getLayoutInflater().inflate(R.layout.custom_seekbar, null);
        ComboSeekBar comboSeekBar = (ComboSeekBar)view.findViewById(R.id.comboseekbar);
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
        id++;
        comboSeekBar.setAdapter(points);
        comboSeekBar.setSelection(4);

        comboSeekBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long position_id) {
                double value = getComboSeebarValueByPosition(position);
                double invertedValue;
                if(position < 4){
                    invertedValue = value;
                    value = 1.0/value;
                }else{
                    invertedValue = 1.0/value;
                }
                currentProject.criterionsMatrix.get(row).set(column, value);
                currentProject.criterionsMatrix.get(column).set(row, invertedValue);
                notCompleted--;
                if(notCompleted == 0){
                    Button save = (Button)findViewById(R.id.save);
                    save.setVisibility(View.VISIBLE);
                    save.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            save();
                            finish();
                        }
                    });
                }
            }
        });
        seekbarContainer.addView(view);
    }
    private void criterions(){
        int size = currentProject.criterionsMatrix.size();
        for(int i = 0; i < size-1; i++){
            for(int j = 1; j <= currentProject.criterionsMatrix.get(i).size() - 1 - i; j++ ){
                addComboSeekBar(i , j+i);
                notCompleted++;
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
        save();
        finish();
    }
    private void save(){
        SharedPreferences.Editor editor = projects.edit();
        editor.putString(currentProject.name, gson.toJson(currentProject, new TypeToken<Project>() {
        }.getType()));
        editor.apply();

        Intent intent = new Intent();
        intent.putExtra("project_name", currentProject.name);
        setResult(RESULT_OK, intent);
    }
}
