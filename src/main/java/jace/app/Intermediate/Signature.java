package jace.app.Intermediate;

/**
 * Created by jaceliu on 13/06/2017.
 */

public class Signature{
    public enum Type{
        INTEGER, STRING, DOUBLE, POINTER
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isParameter() {
        return parameter;
    }

    public void setParameter(boolean parameter) {
        this.parameter = parameter;
    }

    String name;
    Type type;
    boolean parameter;

    public Signature(String name, Type type, boolean parameter){
        setName(name);
        setType(type);
        setParameter(parameter);
    }

    public Signature(){}

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Signature))
            return false;
        Signature sig = (Signature) obj;
        return sig.getName().equals(this.getName());
    }
}
