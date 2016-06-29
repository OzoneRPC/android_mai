package ozone.mai_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by Ozone on 03.05.2016.
 */
public class ChooseJudgmentActivity extends AppCompatActivity {
    private int CRITERIONS = 0;
    private int ALTERNATIVES = 1;
    private boolean criterionsMatrixChanged = false;
    private boolean alternativeMatrixChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_judgment);

        final String projectName = this.getIntent().getExtras().getString("project_name");

        CurrentProject.setCurrentProject(ProjectControl.getProjectByName(projectName));

        final Intent intent = new Intent("android.intent.action.MAKE_JUDGMENT");


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

        final MAI mai = new MAI();

        final Project project = CurrentProject.getCurrentProject();

        boolean criterionsCrIsNormal = false;
        boolean alternativesCrIsNormal = true;

        LinkedHashMap<Integer, Double> criterionsWmax = new LinkedHashMap<>();

        if(criterionsMatrixChanged) {
            criterionsWmax = mai.getWmax(project.criterionsMatrix);


            double cr = mai.getCR(project.criterionsMatrix, criterionsWmax);

            if(cr < 0.1 || Double.isNaN(cr)){
                criterionsCrIsNormal = true;
            }
            TextView criterionResultCr = (TextView) findViewById(R.id.result_cr);

            String crStr = cr+"";
            if(Double.isNaN(cr)){
                crStr = "идеально";
            }

            criterionResultCr.setText("Индекс согласованности критериев: " + crStr);
        }
        final LinkedHashMap<Integer, LinkedHashMap<Integer,Double>> vectors = new LinkedHashMap<>();

        if(alternativeMatrixChanged){
            TextView alternativeCr = (TextView)findViewById(R.id.alternatives_cr);
            alternativeCr.setText("");
            List<Integer> crKeySet = new ArrayList<>(project.alternativesMatrix.keySet());
            for (int i = 0; i < project.alternativesMatrix.size(); i++) {
                LinkedHashMap<Integer,Double> alternativeWmax = mai.getWmax(project.alternativesMatrix.get(crKeySet.get(i)));
                double cr = mai.getCR(project.alternativesMatrix.get(crKeySet.get(i)), alternativeWmax);
                if(cr > 0.1){
                    EditText textfield = (EditText) project.tree.getChildren().get(i).getViewHolder().getView().findViewById(R.id.criterion_add_text);
                    String crName = ((CriterionsTreeHolder)project.tree.getChildren().get(i).getViewHolder()).getValues().name;
                    alternativeCr.append("Индекс согласованности у критерия " + crName + " - " + cr + "\n");
                    alternativesCrIsNormal = false;
                }
                vectors.put(crKeySet.get(i), alternativeWmax);
            }
        }else{
            alternativesCrIsNormal = false;
        }
        if(criterionsCrIsNormal && alternativesCrIsNormal){

            Button buttonRun = (Button)findViewById(R.id.run);

            buttonRun.setVisibility(View.VISIBLE);

            final LinkedHashMap<Integer, Double> criterionsWmaxFinal = criterionsWmax;
            buttonRun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> matrix = mai.makeVectorsMatrix(vectors);
                    LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> Lmatrix = mai.makeLmatrix(vectors);
                    LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> Bmatrix = mai.makeBmatrix(vectors);

                    List<ArrayList<Double>> matrixList = new ArrayList<>();
                    for (Integer altKey : matrix.keySet()){

                        ArrayList<Double> row =  new ArrayList<>();
                        for(Integer crKey : matrix.get(altKey).keySet()){
                            row.add(matrix.get(altKey).get(crKey));
                        }
                        matrixList.add(row);
                    }
                    List<ArrayList<Double>> LmatrixList = new ArrayList<>();

                    for(Integer rowId: Lmatrix.keySet()){
                        ArrayList<Double> row = new ArrayList<>();
                        for (Integer colId : Lmatrix.get(rowId).keySet()){
                            row.add(Lmatrix.get(rowId).get(colId));
                        }
                        LmatrixList.add(row);
                    }
                    List<ArrayList<Double>> BmatrixList = new ArrayList<>();

                    for(Integer rowId: Bmatrix.keySet()){
                        ArrayList<Double> row = new ArrayList<>();
                        for (Integer colId : Bmatrix.get(rowId).keySet()){
                            row.add(Bmatrix.get(rowId).get(colId));
                        }
                        BmatrixList.add(row);
                    }
                    ArrayList<Double> criterionsWmaxList = new ArrayList<>();
                    for(Integer rowId: criterionsWmaxFinal.keySet()){
                        criterionsWmaxList.add(criterionsWmaxFinal.get(rowId));
                    }


                    List<ArrayList<Double>> matrixMultiplyFirst = mai.multiplyMatrix(matrixList, LmatrixList);
                    List<ArrayList<Double>> matrixMultiplySecond = mai.multiplyMatrix(BmatrixList, matrixMultiplyFirst);

                    ArrayList<Double> result = mai.multiplyVector(matrixMultiplySecond, criterionsWmaxList);

                    LinkedHashMap<Integer, Double> resultVector = new LinkedHashMap<>();
                    List<Integer> altKeys = new ArrayList<>(matrix.keySet());

                    for (int i = 0; i < result.size(); i++){
                        resultVector.put(altKeys.get(i), result.get(i));
                    }
                    project.resultVector = resultVector;

                    project.currentStage = "completed";
                    ProjectControl.updateProject(project);

                    Integer altId = -1;
                    for(int key : resultVector.keySet()){

                        if(altId == -1){
                            altId = key;
                        }
                        if(resultVector.get(key) > resultVector.get(altId)){
                            altId = key;
                        }
                    }
                    String altName = "";
                    for(int i=0; i < project.tree.getChildren().size(); i++){
                        for(int j = 0; j < project.tree.getChildren().get(i).size(); j++){
                            CriterionsTreeHolder holder = (CriterionsTreeHolder)project.tree.getChildren().get(i).getChildren().get(j).getViewHolder();
                            int curAltId = holder.getValues().id;
                            if(curAltId == altId){
                                altName = holder.getValues().name;
                            }
                        }
                    }

                    TextView resultText = (TextView)findViewById(R.id.result_text);
                    if(!(altName.equals(""))){
                        String text = altName + " - наилучшая альтернатива";
                        resultText.setText(text);
                    }

                    TextView toProjects = (TextView)findViewById(R.id.to_projects_list);
                    toProjects.setVisibility(View.VISIBLE);
                    toProjects.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent("android.intent.action.MAIN");
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            });



        }
    }
}
