package jace.app.Assembly;

import jace.app.Intermediate.Signature;

/**
 * Created by jaceliu on 15/06/2017.
 */
public class Variable {
    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Variable(Signature signature){
        setName(signature.getName());
        setType(signature.getType() == Signature.Type.INTEGER ? "dd" : "dd");
    }
}

