package jace.app.Intermediate;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jaceliu on 12/06/2017.
 */
public class EntranceTable {

    /**
     * Store the list of entrances
     */
    private Set<Entrance> entranceSet = new HashSet<Entrance>();

    public SignatureTable getSignatureTable() {
        return signatureTable;
    }

    public void setSignatureTable(SignatureTable signatureTable) {
        this.signatureTable = signatureTable;
    }

    private SignatureTable signatureTable = new SignatureTable();

    /**
     * Find an entrance with specific name
     * @param name the name of entrance
     * @return if found, return the entrance, or return null
     */
    public Entrance findEntrance(String name){
        for(Entrance entrance : entranceSet){
            if(entrance.getName().equals(name)){
                return entrance;
            }
        }
        return null;
    }

    /**
     * Add a new entrance into this entrance table
     * @param name the name of the entrance
     * @param signatureTable the signatureTable of the entrance
     * @param quaternionTable the quaternionTable of the entrance
     * @throws IntermediateException throw if there is an existing entrance with the same name
     */
    public void addEntrance(String name, Signature.Type type, SignatureTable signatureTable, QuaternionTable quaternionTable) throws IntermediateException {
        Entrance entrance = new Entrance(name, type, signatureTable, quaternionTable);
        addEntrance(entrance);
    }

    /**
     * Add a new entrance into this entrance table
     * @param entrance the entrance to be added
     * @throws IntermediateException throw if there is an existing entrance with the same name
     */
    public void addEntrance(Entrance entrance) throws IntermediateException {
        Entrance en = findEntrance(entrance.getName());
        if(en != null){
            throw new IntermediateException("Existing entrance: " + entrance.getName());
        }
        entranceSet.add(entrance);
    }

    /**
     * Remove an existing entrance from this entrance table
     * @param entrance the entrance to be deleted
     * @throws IntermediateException throw if there is no entrance with the name
     */
    public void removeEntrance(Entrance entrance) throws IntermediateException {
        Entrance en = findEntrance(entrance.getName());
        if(en == null)
            throw new IntermediateException("Removing non-existing entrance: " + entrance.getName());
        entranceSet.remove(entrance);
    }

    /**
     * Remove an existing entrance from this entrance table
     * @param name the name of the entrance
     * @throws IntermediateException throw if there is no entrance with the name
     */
    public void removeEntrance(String name) throws IntermediateException {
        Entrance entrance = findEntrance(name);
        if(entrance == null)
            throw new IntermediateException("Removing non-existing entrance: " + name);
        entranceSet.remove(entrance);
    }

    /**
     * Create a new entrance in the table and return it back
     * @return the created entrance
     * @throws IntermediateException throw if there is already an entrance with undefined name
     */
    public Entrance newEntrance() throws IntermediateException {
        Entrance entrance = new Entrance();
        addEntrance(entrance);
        return entrance;
    }

    /**
     * Show the entrance table
     * @param out the PrintStream to show
     */
    public void show(PrintStream out){
        for(Entrance entrance: entranceSet){
            out.println(entrance.toString());
        }
    }
}
