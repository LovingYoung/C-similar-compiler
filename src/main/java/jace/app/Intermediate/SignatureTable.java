package jace.app.Intermediate;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jaceliu on 12/06/2017.
 */
public class SignatureTable {

    private Set<Signature> signatureSet = new HashSet<Signature>();

    /**
     * Find an signature with specific name
     * @param name the name of signature
     * @return if found, return the signature, or return null
     */
    public Signature findEntrace(String name){
        for(Signature signature : signatureSet){
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
        signatureSet.add(signature);
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
        signatureSet.remove(signature);
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
        signatureSet.remove(signature);
    }

    @Override
    public String toString() {
        String result = "Name\tType\tPara\n";
        for(Signature signature: signatureSet){
            result += (signature.toString() + "\n");
        }
        return result;
    }
}
