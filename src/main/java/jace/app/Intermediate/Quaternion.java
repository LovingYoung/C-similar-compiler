package jace.app.Intermediate;

/**
 * Created by jaceliu on 13/06/2017.
 */
public class Quaternion {
    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private String op, arg1, arg2, result;

    public Quaternion(String op, String arg1, String arg2, String result) {
        setOp(op);
        setArg1(arg1);
        setArg2(arg2);
        setResult(result);
    }

    public Quaternion(){}

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Quaternion))
            return false;
        Quaternion q = (Quaternion) obj;
        return (q.op.equals(this.op) &&
                q.arg1.equals(this.arg1) &&
                q.arg2.equals(this.arg2) &&
                q.result.equals(this.result)
        );
    }
}

