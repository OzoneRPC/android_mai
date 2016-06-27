package ozone.mai_2;

import android.widget.ArrayAdapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.unnamed.b.atv.model.TreeNode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ozone on 23.05.2016.
 */
public class MAI {
    public LinkedHashMap<Integer,LinkedHashMap<Integer, Double>> generateCriterionsMatrix(List<TreeNode> criterions){



        LinkedHashMap<Integer,LinkedHashMap<Integer, Double>> matrix = new LinkedHashMap<>();
        for(int i=0; i < criterions.size();i++){
            int columnId = ((CriterionsTreeHolder)criterions.get(i).getViewHolder()).getValues().id;

            LinkedHashMap<Integer, Double> vector = new LinkedHashMap<>();
            for(int j=0; j < criterions.size(); j++){
                int rowId = ((CriterionsTreeHolder)criterions.get(j).getViewHolder()).getValues().id;
                if(i == j){
                    vector.put(rowId, 1.0);
                }else{
                    vector.put(rowId, 0.0);
                }
            }
            matrix.put(columnId, vector);
        }
        return matrix;
    }
    public LinkedHashMap<Integer,LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> generateAlternativesMatrix(List<TreeNode> criterions){

        LinkedHashMap<Integer,LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> matrixList = new LinkedHashMap<>();

        for(int i = 0; i < criterions.size(); i++){
            int crId = ((CriterionsTreeHolder)criterions.get(i).getViewHolder()).getValues().id;
            LinkedHashMap<Integer,LinkedHashMap<Integer, Double>> matrix = new LinkedHashMap<>();

            for(int j = 0; j < criterions.get(i).getChildren().size(); j++){
                int columnId = ((CriterionsTreeHolder)criterions.get(i).getChildren().get(j).getViewHolder()).getValues().id;

                LinkedHashMap<Integer, Double> row = new LinkedHashMap<>();
                for(int k = 0; k < criterions.get(i).getChildren().size(); k++){
                    int rowId = ((CriterionsTreeHolder)criterions.get(i).getChildren().get(k).getViewHolder()).getValues().id;
                    if(j == k){
                        row.put(rowId, 1.0);
                    }else{
                        row.put(rowId, 0.0);
                    }
                }
                matrix.put(columnId, row);
            }
            matrixList.put(crId, matrix);
        }
        return matrixList;
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
    public LinkedHashMap<Integer, Double> getWmax(LinkedHashMap<Integer,LinkedHashMap<Integer, Double>> A){
        List<Double> W = new ArrayList<>();
        List<Double> Wnew =  new ArrayList<>();
        List<ArrayList<Double>> tempA = new ArrayList<>();

        List<Integer> keySet = new ArrayList<>(A.keySet());

        for (int i = 0; i < keySet.size(); i++){
            ArrayList<Double> row = new ArrayList<>();
            for (int j = 0; j  < keySet.size(); j++){
                row.add(A.get(keySet.get(i)).get(keySet.get(j)));
            }
            tempA.add(row);
        }

        W = calculateWmax(tempA);
        while(true){
            tempA = multiplyMatrix(tempA, tempA);
            Wnew = calculateWmax(tempA);
            if(compareVectors(W, Wnew)){
                LinkedHashMap<Integer, Double> result = new LinkedHashMap<>();

                for (int i = 0; i < Wnew.size(); i++){
                    result.put(keySet.get(i), Wnew.get(i));
                }
                return result;
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

    public List<ArrayList<Double>> multiplyMatrix(List<ArrayList<Double>> A, List<ArrayList<Double>> B){
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
    public LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> makeVectorsMatrix(LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> vectors){
/*
        int criterionsCount = vectors.size();
*/

        /*for(int i = 0; i < criterionsCount; i++) {

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
        }*/
        List <Integer> altIds = new ArrayList<>();
        for (Integer crKey : vectors.keySet()){

            for (Integer altKey : vectors.get(crKey).keySet()){
                if(!altIds.contains(altKey)){
                    altIds.add(altKey);
                }
            }
        }
        LinkedHashMap <Integer, LinkedHashMap<Integer, Double>> matrix = new LinkedHashMap<>();
        for (int i = 0; i < altIds.size(); i++){

            LinkedHashMap<Integer, Double> row = new LinkedHashMap<>();

            for (Integer crKey : vectors.keySet()){
                if(vectors.get(crKey).containsKey(altIds.get(i))){
                    row.put(crKey, vectors.get(crKey).get(altIds.get(i)));
                }else{
                    row.put(crKey, 0.0);
                }
                matrix.put(altIds.get(i), row);
            }
        }

        return matrix;
    }
    public LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> makeLmatrix(LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> vectors){
        int criterionsCount = vectors.size();
        List <Integer> altIds = new ArrayList<>();
        for (Integer crKey : vectors.keySet()){

            for (Integer altKey : vectors.get(crKey).keySet()){
                if(!altIds.contains(altKey)){
                    altIds.add(altKey);
                }
            }
        }
        int alternativesCount = altIds.size();

        LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> matrix = new LinkedHashMap<>();

        List<Integer> crKeys = new ArrayList<>(vectors.keySet());
        for(int i = 0; i < criterionsCount; i++) {

            LinkedHashMap<Integer, Double> row = new LinkedHashMap<>();
            for(int j=0; j < criterionsCount; j++){
                if(i == j){
                    row.put(crKeys.get(j), (((double) vectors.get(crKeys.get(i)).size()) / alternativesCount));
                }else{
                    row.put(crKeys.get(j), 0.0);
                }
            }
            matrix.put(crKeys.get(i),row);
        }
        return matrix;
    }

    public LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> makeBmatrix(LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> vectors){
        double Wsum = 0.0;
        int alternativesCount = 0;

        List<Integer> altIds = new ArrayList<>();
        for (Integer crKey : vectors.keySet()){
            for (Integer altKey : vectors.get(crKey).keySet()){
                Wsum += vectors.get(crKey).get(altKey);
                if(!altIds.contains(altKey)){
                    altIds.add(altKey);
                }
            }
        }
        alternativesCount = altIds.size();
        LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> matrix = new LinkedHashMap<>();
        for(int i = 0; i < alternativesCount; i++) {

            LinkedHashMap<Integer, Double> row = new LinkedHashMap<>();
            for(int j=0; j < alternativesCount; j++){
                if(i == j){
                    row.put(altIds.get(j), Wsum);
                }else{
                    row.put(altIds.get(j), 0.0);
                }
            }
            matrix.put(altIds.get(i), row);
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
    public double getCR(LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> matrix, LinkedHashMap<Integer, Double> wmax){
        double[] RI = {0.0,0.0, 0.58, 0.9, 1.12, 1.24, 1.32, 1.41,1.45,1.49}; // Ранг матрицы не должен быть меньше 3, ибо получится деление на ноль
        int matrixRang = matrix.size();
        double lambdaMax = getLambdaMax(matrix, wmax);
        double CI = (lambdaMax - (double) matrixRang)/ ((double)matrixRang - 1.0);
        if(matrixRang > RI.length){
            matrixRang = RI.length;
        }
        return (double) (CI/RI[matrixRang-1]);
    }
    public double getLambdaMax(LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> matrix, LinkedHashMap<Integer, Double> wmax){
        double vectorSum = 0.0;
        ArrayList<Double> tempA = new ArrayList<>();
        for (int i=0; i < matrix.size(); i++){
            tempA.add(0.0);
        }
        List<Integer> rowKeySet = new ArrayList<>(matrix.keySet());

        ArrayList<Double> tempWmax = new ArrayList<>(wmax.values());

        for(int i = 0; i < matrix.size(); i++){
            vectorSum = 0;
            for(int j = 0; j < matrix.get(rowKeySet.get(i)).size(); j++){
                vectorSum+=matrix.get(rowKeySet.get(j)).get(rowKeySet.get(i));
            }
            tempA.set(i, vectorSum);
        }
        return multipyVectors(tempA, tempWmax);
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
