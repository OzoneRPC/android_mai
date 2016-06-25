package ozone.mai_2;

import android.widget.ArrayAdapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.unnamed.b.atv.model.TreeNode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ozone on 23.05.2016.
 */
public class MAI {
    public List<ArrayList<Double>>  generateCriterionsMatrix(int size){
        return generateMatrix(size);
    }
    public HashMap<String , List<ArrayList<Double>>> generateAlternativesMatrix(JsonObject projectTree){


        HashMap<String ,List<ArrayList<Double>>> map = new HashMap<>();

        for(Map.Entry<String, JsonElement> entry : projectTree.entrySet()){
            if(entry.getKey() != "crCount"){
               JsonObject group = entry.getValue().getAsJsonObject();
                int altCount = group.get("alternatives").getAsJsonObject().get("altCount").getAsInt();


                for(Map.Entry<String, JsonElement> groupEntry : group.entrySet()){
                    String key = groupEntry.getKey();
                    JsonElement value = groupEntry.getValue();
                    if(key != "alternatives"){
                        List<ArrayList<Double>> alternativeMatrix = generateMatrix(altCount);
                        map.put(key, alternativeMatrix);
                    }
                }
            }
        }
        return map;
    }
    public List<ArrayList<Double>> generateMatrix(int size){
        List<ArrayList<Double>> matrix = new ArrayList<>
                  ();
        for(int i=0; i < size;i++){
            ArrayList<Double> vector = new ArrayList<>();
            for(int j=0; j < size; j++){
                if(i == j){
                    vector.add(1.0);
                }else{
                    vector.add(0.0);
                }
            }
            matrix.add(vector);
        }
        return matrix;
    }
    public ArrayList<Double> getWmax(List<ArrayList<Double>> A){
        List<Double> W = new ArrayList<>();
        List<Double> Wnew =  new ArrayList<>();
        List<ArrayList<Double>> tempA = new ArrayList<>();
        for (int i = 0; i < A.size(); i++){
            ArrayList<Double> row = new ArrayList<>();
            for (int j = 0; j < A.get(i).size(); j++){
                row.add(A.get(i).get(j));
            }
            tempA.add(row);
        }

        W = calculateWmax(tempA);
        while(true){
            tempA = multiplyMaxtix(tempA, tempA);
            Wnew = calculateWmax(tempA);
            if(compareVectors(W, Wnew)){
                return (ArrayList<Double>)Wnew;
            }else{
                W = Wnew;
            }
        }

    }
    public ArrayList<Double> calculateWmax(List<ArrayList<Double>> A){
        int length = A.size();
        ArrayList<Double> Wmax = new ArrayList<>();
        ArrayList<Double> Ae = new ArrayList<>();
        double vectorSum = 0;


        for(int arrayIndex = 0; arrayIndex < length; arrayIndex++){
            vectorSum = 0;
            for(int vectorsCount = 0; vectorsCount < A.get(arrayIndex).size(); vectorsCount++){
                vectorSum += A.get(arrayIndex).get(vectorsCount);
            }
            Ae.add(arrayIndex, vectorSum);
        }
        double eTAe = 0;
        for(int arrayIndex = 0; arrayIndex < Ae.size(); arrayIndex++){
            eTAe += Ae.get(arrayIndex);
        }
        for(int arrayIndex = 0; arrayIndex < Ae.size(); arrayIndex++){
            Wmax.add(arrayIndex, Ae.get(arrayIndex) / eTAe);
        }
        return Wmax;
    }

    public List<ArrayList<Double>> multiplyMaxtix(List<ArrayList<Double>> A, List<ArrayList<Double>> B){
        int mA = A.size();
        int nA = A.get(0).size();
        int mB = B.size();
        int nB = B.get(0).size();
        if (nA != mB) {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        List<ArrayList<Double>> C =  new ArrayList<>();

        for(int i =0; i < mA; i++){
            ArrayList<Double> row = new ArrayList<>();
            for(int j = 0; j < nB; j++){
                double num = 0.0;
                row.add(num);
            }
            C.add(row);
        }
        for (int i = 0; i < mA; i++) {
            for (int j = 0; j < nB; j++) {
                for (int k = 0; k < nA; k++) {
                    double mult = A.get(i).get(k) * B.get(k).get(j);
                    double oldVal = C.get(i).get(j);
                    C.get(i).set(j, oldVal+mult);
                }
            }
        }
        return C;
    }

    public boolean compareVectors(List<Double> A, List<Double> B){
        double diff = 0.0;
        for(int i=0;i<A.size();i++){
            diff = A.get(i) - B.get(i);
            if(diff > 0.0005 || diff < -0.0005){
                return false;
            }
        }
        return true;
    }
    public List<ArrayList<Double>> makeVectorsMatrix(List<ArrayList<Double>> vectors){
        int criterionsCount = vectors.size();

        List<ArrayList<Double>> matrix = new ArrayList<>();
        for(int i = 0; i < criterionsCount; i++) {

            for (int j = 0 ; j < vectors.get(i).size(); j++){
                ArrayList<Double> row = new ArrayList<>();

                for (int k = 0; k < criterionsCount; k ++){
                    if (k == i){
                        row.add(vectors.get(i).get(j));
                    }else{
                        double num = 0;
                        row.add(num);
                    }
                }
                matrix.add(row);
            }
        }
        return matrix;
    }
    public List<ArrayList<Double>> makeLmatrix(List<ArrayList<Double>> vectors){
        int criterionsCount = vectors.size();
        int alternativesCount = 0;
        for (int i =0; i < criterionsCount; i ++){
            alternativesCount += vectors.get(i).size();
        }

        List<ArrayList<Double>> matrix = new ArrayList<>();
        for(int i = 0; i < criterionsCount; i++) {

            ArrayList<Double> row = new ArrayList<>();
            for(int j=0; j < criterionsCount; j++){
                if(i == j){
                    row.add((double) vectors.get(i).size() / alternativesCount);
                }else{
                    double num = 0;
                    row.add(num);
                }
            }
            matrix.add(row);
        }
        return matrix;
    }
    public List<ArrayList<Double>> makeSmatrix(List<ArrayList<Double>> vectors){
        int criterionsCount = vectors.size();
        int alternativesCount = 0;
        for (int i =0; i < criterionsCount; i ++){
            alternativesCount += vectors.get(i).size();
        }

        List<ArrayList<Double>> matrix = new ArrayList<>();
        for(int i = 0; i < criterionsCount; i++) {

            ArrayList<Double> row = new ArrayList<>();
            for(int j=0; j < criterionsCount; j++){
                if(i == j){
                    row.add(getVectorsSum(vectors.get(i)));
                }else{
                    double num = 0;
                    row.add(num);
                }
            }
            matrix.add(row);
        }
        return matrix;
    }
    public List<ArrayList<Double>> makeBmatrix(List<ArrayList<Double>> vectors){
        double Wsum = 0.0;
        int alternativesCount = 0;
        for (int i = 0; i < vectors.size(); i++){
            for (int j=0; j < vectors.get(i).size(); j++){
                Wsum += vectors.get(i).get(j);
                alternativesCount++;
            }
        }
        List<ArrayList<Double>> matrix = new ArrayList<>();
        for(int i = 0; i < alternativesCount; i++) {

            ArrayList<Double> row = new ArrayList<>();
            for(int j=0; j < alternativesCount; j++){
                if(i == j){
                    row.add(Wsum);
                }else{
                    double num = 0;
                    row.add(num);
                }
            }
            matrix.add(row);
        }
        return matrix;
    }
    public double getVectorsSum(ArrayList<Double> vector){
        double sum = 0;
        for (int i = 0; i < vector.size(); i++){
            sum += vector.get(i);
        }
        return sum;
    }
    public double getCR(List<ArrayList<Double>> matrix, ArrayList<Double> wmax){
        double[] RI = {0.0,0.0, 0.58, 0.9, 1.12, 1.24, 1.32, 1.41,1.45,1.49}; // Ранг матрицы не должен быть меньше 3, ибо получится деление на ноль
        int matrixRang = matrix.size();
        double lambdaMax = getLambdaMax(matrix, wmax);
        double CI = (lambdaMax - (double) matrixRang)/ ((double)matrixRang - 1.0);
        if(matrixRang > RI.length){
            matrixRang = RI.length;
        }
        return CI/RI[matrixRang-1];
    }
    public double getLambdaMax(List<ArrayList<Double>> matrix, ArrayList<Double> wmax){
        double vectorSum = 0.0;
        ArrayList<Double> tempA = new ArrayList<>();
        for (int i=0; i < matrix.size(); i++){
            double num = 0.0;
            tempA.add(num);
        }
        for(int arrayIndex = 0; arrayIndex < matrix.size(); arrayIndex++){
            vectorSum = 0;
            for(int vectorsCount = 0; vectorsCount < matrix.get(arrayIndex).size(); vectorsCount++){
                vectorSum+=matrix.get(vectorsCount).get(arrayIndex);
            }
            tempA.set(arrayIndex, vectorSum);
        }
        return multipyVectors(tempA, wmax);
    }
    public double multipyVectors(ArrayList<Double> vectorA, ArrayList<Double> vectorB){
        double sum = 0.0;
        for(int i=0; i < vectorA.size(); i++){
            sum += vectorA.get(i)*vectorB.get(i);
        }
        return sum;
    }
    public ArrayList<Double> multiplyVector(List<ArrayList<Double>> A, ArrayList<Double> x) {
        ArrayList<Double> result = new ArrayList<>();
        for(int i=0; i < A.size(); i++){
            result.add(0.0);
        }

        for(int i = 0; i < A.size(); i++){
            for(int w = 0; w < A.get(0).size(); w++){
                double val = result.get(i) + (A.get(i).get(w) * x.get(w));
                result.set(i, val);
            }
        }
        return result;
    }


}
