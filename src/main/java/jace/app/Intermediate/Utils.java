package jace.app.Intermediate;

/**
 * Created by jaceliu on 14/06/2017.
 */
public class Utils {
    private static final Utils INSTANCE = new Utils();

    private Utils() {}

    public static Utils getInstance() {
        return INSTANCE;
    }

    public Signature.Type string2Type(String type){
        if(type.equals("int")) return Signature.Type.INTEGER;
        else return Signature.Type.STRING;
    }

    public void tempNameClear(){
        tempId = 0;
    }

    private int tempId = 0;

    public String getTempName(){
        tempId += 1;
        return "T" + Integer.toString(tempId);
    }
}
