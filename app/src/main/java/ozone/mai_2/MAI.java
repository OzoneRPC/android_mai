package ozone.mai_2;

import com.unnamed.b.atv.model.TreeNode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ozone on 23.05.2016.
 */
public class MAI {
    public List<ArrayList<Double>>  generateCriterionsMatrix(int size){
        return generateMatrix(size);
    }
    public List<List<ArrayList<Double>>> generateAlternativesMatrix(TreeNode tree){




        List <List<ArrayList<Double>>> alternativesMatrixList = new ArrayList<>();

        for(TreeNode criterionNode : tree.getChildren()){

            List<ArrayList<Double>> alternativeMatrix = generateMatrix(criterionNode.getChildren().size());

            alternativesMatrixList.add(alternativeMatrix);
        }

        return alternativesMatrixList;
    }
    public List<ArrayList<Double>> generateMatrix(int size){
        List<ArrayList<Double>> matrix = new ArrayList<>();
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
        List<Double> W = new ArrayList<>(A.size());
        List<Double> Wnew =  new ArrayList<>(A.size());
        List<ArrayList<Double>> tempA = A;
        W = calculateWmax(tempA);
        while(true){
            tempA = multiplyMaxtix(tempA, tempA);
            Wnew = calculateWmax(tempA);
            if(compareVectors(W, Wnew)){
                return (ArrayList)Wnew;
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
                vectorSum+= A.get(arrayIndex).get(vectorsCount);
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
        if (nA != mB) throw new RuntimeException("Illegal matrix dimensions.");
        List<ArrayList<Double>> C =  A;

        for (int i = 0; i < mA; i++) {
            for (int j = 0; j < nB; j++) {
                for (int k = 0; k < nA; k++) {
                    double mult = A.get(i).get(k) * B.get(k).get(j);
                    C.get(i).set(j, mult);
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
    public double getVectorsSum(ArrayList<Double> vector){
        double sum = 0;
        for (int i = 0; i < vector.size(); i++){
            sum += vector.get(i);
        }
        return sum;
    }

}
