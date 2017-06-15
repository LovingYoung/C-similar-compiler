package jace.app.Intermediate;

/**
 * Created by jaceliu on 13/06/2017.
 */
public class Entrance{
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Entrance))
            return false;
        Entrance entrance = (Entrance)obj;
        return entrance.getName().equals(this.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SignatureTable getSignatureTable() {
        return signatureTable;
    }

    public void setSignatureTable(SignatureTable signatureTable) {
        this.signatureTable = signatureTable;
    }

    public QuaternionTable getQuaternionTable() {
        return quaternionTable;
    }

    public void setQuaternionTable(QuaternionTable quaternionTable) {
        this.quaternionTable = quaternionTable;
    }

    public Signature.Type getRetType() {
        return retType;
    }

    public void setRetType(Signature.Type retType) {
        this.retType = retType;
    }

    String name = "";
    Signature.Type retType;
    SignatureTable signatureTable = new SignatureTable();
    QuaternionTable quaternionTable = new QuaternionTable();

    /**
     * The constructor with some values
     * @param name the name of the entrance
     * @param signatureTable the signatureTable of the entrance
     * @param quaternionTable the quaternionTable of the entrance
     */
    public Entrance(String name, Signature.Type type, SignatureTable signatureTable, QuaternionTable quaternionTable){
        setName(name);
        setSignatureTable(signatureTable);
        setQuaternionTable(quaternionTable);
        setRetType(type);
    }

    /**
     * The constructor with no value
     */
    public Entrance(){
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\n\n"
                + "Signature Table: \n" + signatureTable.toString()
                + "\n" + "QuaternionTable: \n" + quaternionTable.toString() + "\n";
    }
}
