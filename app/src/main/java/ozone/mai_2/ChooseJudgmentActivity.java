package ozone.mai_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ozone on 03.05.2016.
 */
public class ChooseJudgmentActivity extends AppCompatActivity {
    private int CRITERIONS = 0;
    private int ALTERNATIVES = 1;
    private ProjectControl control;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_judgment);

        control = new ProjectControl(this);


        final String projectName = this.getIntent().getExtras().getString("project_name");
        final Intent intent = new Intent("android.intent.action.MAKE_JUDGMENT");
        intent.putExtra("project_name",projectName);



        Button criterionsJudgment = (Button)findViewById(R.id.button_judgment_criterions);
        Button alternativesJudgment = (Button)findViewById(R.id.button_judgment_alternatives);

        criterionsJudgment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                intent.putExtra("type", CRITERIONS);
                startActivityForResult(intent, 1);
            }
        });

        alternativesJudgment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                intent.putExtra("type",ALTERNATIVES);
                startActivityForResult(intent,1);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences projects = getSharedPreferences("projects", MODE_PRIVATE);

        MAI mai = new MAI();

        String nameFromExtras = data.getExtras().getString("project_name");
        Project project = control.getProjectByName(nameFromExtras);

        ArrayList<Double> firstWmax = mai.getWmax(project.alternativesMaxtrix.get(0));

        List<ArrayList<Double>> vectors = new ArrayList<>();
        for(int i = 0; i < project.alternativesMaxtrix.size(); i++){
            vectors.add(mai.getWmax(project.alternativesMaxtrix.get(i)));
        }

        List<ArrayList<Double>> matrix = mai.makeVectorsMatrix(vectors);
        List<ArrayList<Double>> Lmatrix = mai.makeLmatrix(vectors);
        List<ArrayList<Double>> Smatrix = mai.makeSmatrix(vectors);



    }
}
