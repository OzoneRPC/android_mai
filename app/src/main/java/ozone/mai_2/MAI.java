package ozone.mai_2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ozone on 23.05.2016.
 */
public class MAI {
    public List<ArrayList<Double>> generateCriterionsMatrix(int size){
        return generateMatrix(size);
    }
    public List<List<ArrayList<Double>>> generateAlternativesMatrix(int arrayCount, int vectorsCount){
        List<List<ArrayList<Double>>> matrix = new ArrayList<>();
        for(int i=0; i < arrayCount; i++){
            matrix.add(generateMatrix(vectorsCount));
        }
        return matrix;
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
}
