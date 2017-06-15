package jace.app.Intermediate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jaceliu on 12/06/2017.
 */
public class QuaternionTable {
    private List<Quaternion> quaternionList = new ArrayList<Quaternion>();

    /**
     * Get the quaternion according to the index
     * @param index the index of the quaternion
     * @return the quaternion with the index
     */
    public Quaternion fetch(int index){
        return quaternionList.get(index);
    }

    /**
     * Add a new quaternion to the list
     * @param quaternion
     */
    public void addQuaternion(Quaternion quaternion){
        quaternionList.add(quaternion);
    }

    public void addQuaternion(String op, String arg1, String arg2, String result){
        addQuaternion(new Quaternion(op, arg1, arg2, result));
    }

    public int size(){
        return quaternionList.size();
    }

    public Quaternion getLastQuaternion(){
        return quaternionList.get(size() - 1);
    }

    public void merge(QuaternionTable other){
        int size = size();
        for(Quaternion quaternion : other.quaternionList){
            boolean isInt = true;
            int lineNumber = 0;
            try{
                lineNumber = Integer.parseInt(quaternion.getResult());
            } catch (NumberFormatException e){
                isInt = false;
            }
            if(isInt){
                quaternion.setResult(String.valueOf(lineNumber + size));
            }
            quaternionList.add(quaternion);
        }
    }


    @Override
    public String toString() {
        String result = "op\targ1\targ2\tresult\n";
        for(Quaternion quaternion: quaternionList){
            result += (quaternion.toString() + "\n");
        }
        return result;
    }

    public int getCountTempVars() {
        Set<String> strings = new HashSet<String>();
        for(Quaternion quaternion: quaternionList){
            String arg1, arg2, result;
            arg1 = quaternion.getArg1();
            arg2 = quaternion.getArg2();
            result = quaternion.getResult();
            String[] s = new String[]{arg1, arg2, result};
            for(String a: s){
                if (a.startsWith("T")){
                    try{
                        Integer.parseInt(a.substring(1));
                        strings.add(a);
                    } catch (NumberFormatException e){
                        continue;
                    }
                }
            }
        }
        return strings.size();
    }
}
