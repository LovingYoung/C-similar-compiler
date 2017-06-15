package jace.app.Intermediate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaceliu on 12/06/2017.
 */
public class SignatureTable {

    private List<Signature> signatureList = new ArrayList<Signature>();

    /**
     * Find an signature with specific name
     * @param name the name of signature
     * @return if found, return the signature, or return null
     */
    public Signature findEntrace(String name){
        for(Signature signature : signatureList){
            if(signature.getName().equals(name)){
                return signature;
            }
        }
        return null;
    }

    /**
     * Add a new signature into this signature table
     * @param name the name of the variable
     * @param type the type of the variable
     * @param parameter if the variable is a parameter
     * @throws IntermediateException throw if there is an existing signature with the same name
     */
    public void addSignature(String name, Signature.Type type, boolean parameter) throws IntermediateException {
        Signature signature = new Signature(name, type, parameter);
        addSignature(signature);
    }

    /**
     * Add a new signature into this signature table
     * @param signature the signature to be added
     * @throws IntermediateException throw if there is an existing signature with the same name
     */
    public void addSignature(Signature signature) throws IntermediateException {
        Signature en = findEntrace(signature.getName());
        if(en != null){
            throw new IntermediateException("Existing signature: " + signature.getName());
        }
        signatureList.add(signature);
    }

    /**
     * Remove an existing signature from this signature table
     * @param signature the signature to be deleted
     * @throws IntermediateException throw if there is no signature with the name
     */
    public void removeSignature(Signature signature) throws IntermediateException {
        Signature en = findEntrace(signature.getName());
        if(en == null)
            throw new IntermediateException("Removing non-existing signature: " + signature.getName());
        signatureList.remove(signature);
    }

    /**
     * Remove an existing signature from this signature table
     * @param name the name of the signature
     * @throws IntermediateException throw if there is no signature with the name
     */
    public void removeSignature(String name) throws IntermediateException {
        Signature signature = findEntrace(name);
        if(signature == null)
            throw new IntermediateException("Removing non-existing signature: " + name);
        signatureList.remove(signature);
    }

    @Override
    public String toString() {
        String result = "Name\tType\tPara\n";
        for(Signature signature: signatureList){
            result += (signature.toString() + "\n");
        }
        return result;
    }
}
