package ozone.mai_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private boolean criterionsMatrixChanged = false;
    private boolean alternativeMatrixChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_judgment);

        control = new ProjectControl(this);


        final String projectName = this.getIntent().getExtras().getString("project_name");
        final Intent intent = new Intent("android.intent.action.MAKE_JUDGMENT");
        intent.putExtra("project_name",projectName);



        final Button criterionsJudgment = (Button)findViewById(R.id.button_judgment_criterions);
        Button alternativesJudgment = (Button)findViewById(R.id.button_judgment_alternatives);

        criterionsJudgment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                intent.putExtra("type", CRITERIONS);
                if(criterionsMatrixChanged){
                    intent.putExtra("changed", true);
                }else{
                    intent.putExtra("changed", false);
                }
                startActivityForResult(intent, 1);
            }
        });

        alternativesJudgment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(alternativeMatrixChanged){
                    intent.putExtra("changed", true);
                }else{
                    intent.putExtra("changed", false);
                }
                intent.putExtra("type",ALTERNATIVES);
                startActivityForResult(intent,1);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        String nameFromExtras = data.getExtras().getString("project_name");
        int judgType = data.getExtras().getInt("completed_type");

        switch (judgType){
            case 0:
                criterionsMatrixChanged = true;
                break;
            case 1:
                alternativeMatrixChanged = true;
                break;
        }
        SharedPreferences projects = getSharedPreferences("projects", MODE_PRIVATE);

        MAI mai = new MAI();

        Project project = control.getProjectByName(nameFromExtras);

        boolean criterionsCrIsNormal = false;
        boolean alternativesCrIsNormal = true;
        ArrayList<Double> criterionsWmax = new ArrayList<Double>();
        if(criterionsMatrixChanged) {
            criterionsWmax = mai.getWmax(project.criterionsMatrix);

            double cr = mai.getCR(project.criterionsMatrix, criterionsWmax);

            if(cr < 0.1){
                criterionsCrIsNormal = true;
            }
            TextView criterionResultCr = (TextView) findViewById(R.id.result_cr);

            criterionResultCr.setText("Индекс согласованности: " + cr);
        }
        List<ArrayList<Double>> vectors = new ArrayList<>();

        if(alternativeMatrixChanged){
            TextView alternativeCr = (TextView)findViewById(R.id.alternatives_cr);
            alternativeCr.setText("");
            for (int i = 0; i < project.alternativesMaxtrix.size(); i++) {
                ArrayList<Double> alternativeWmax = mai.getWmax(project.alternativesMaxtrix.get(i));
                double cr = mai.getCR(project.alternativesMaxtrix.get(i), alternativeWmax);
                if(cr > 0.1){
                    EditText textfield = (EditText) project.tree.getChildren().get(i).getViewHolder().getView().findViewById(R.id.criterion_add_text);
                    String matrixName = textfield.getText().toString().replace("\"", "").replace("\\","");
                    alternativeCr.append("Индекс согласованности у критерия " + matrixName + " - " + cr + "\n");
                    alternativesCrIsNormal = false;
                }
                vectors.add(alternativeWmax);
            }
        }else{
            alternativesCrIsNormal = false;
        }
        if(criterionsCrIsNormal && alternativesCrIsNormal){
            List<ArrayList<Double>> matrix = mai.makeVectorsMatrix(vectors);
            List<ArrayList<Double>> Lmatrix = mai.makeLmatrix(vectors);
            List<ArrayList<Double>> Bmatrix = mai.makeBmatrix(vectors);

            List<ArrayList<Double>> matrixMultiplyFirst = mai.multiplyMaxtix(matrix, Lmatrix);
            List<ArrayList<Double>> matrixMultiplySecond = mai.multiplyMaxtix(Bmatrix, matrixMultiplyFirst);

            ArrayList<Double> result = mai.multiplyVector(matrixMultiplySecond, criterionsWmax);

            ArrayList<Integer> creterionsIdMap = new ArrayList<>();
            for(int i = 0; i < project.tree.getChildren().size(); i++){
                for (int j = 0; j < project.tree.getChildren().get(i).getChildren().size(); j++){
                    creterionsIdMap.add(i);
                }
            }
            TextView resultText = (TextView)findViewById(R.id.result_text);

            int lastPos = 0;
            int alternativeLastPos = 0;
            double maxValue = 0.0;
            for(int i=0 ; i< result.size(); i++){
                if(result.get(i) > maxValue){
                    maxValue = result.get(i);
                    alternativeLastPos++;
                    if(lastPos != i){
                        alternativeLastPos = 0;
                    }
                    lastPos = i;
                }
            }
            EditText alternativeName = (EditText)project.tree.getChildren().get(creterionsIdMap.get(lastPos)).getChildren().get(alternativeLastPos).getViewHolder().getView().findViewById(R.id.criterion_add_text);
            EditText criterion = (EditText)project.tree.getChildren().get(creterionsIdMap.get(lastPos)).getViewHolder().getView().findViewById(R.id.criterion_add_text);
            String name = alternativeName.getText().toString();
            String criterionName = criterion.getText().toString();
            //resultText.setText("Наиболее оптимальной является альтернатива "+ name + "");

        }
            /*for(int i =0; i < vectors.size(); i ++){
                double cr = mai.getCR(project.alternativesMaxtrix.get(i), vectors.get(i));
            }*/




    }
}
