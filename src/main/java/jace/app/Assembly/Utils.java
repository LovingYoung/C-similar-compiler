package jace.app.Assembly;


/**
 * Created by jaceliu on 14/06/2017.
 */
public class Utils {
    private static final Utils INSTANCE = new Utils();

    private Utils() {}

    public static Utils getInstance() {
        return INSTANCE;
    }

    public void tempNameClear(){
        tempId = 0;
    }

    private int tempId = 0;

    public String getTempName(){
        tempId += 1;
        return "PROCEDURE_" + Integer.toString(tempId);
    }
}
