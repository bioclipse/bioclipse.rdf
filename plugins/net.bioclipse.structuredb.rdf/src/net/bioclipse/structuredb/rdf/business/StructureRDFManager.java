/*******************************************************************************
 * Copyright (c) 2009  Egon Willighagen <egonw@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.structuredb.rdf.business;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.core.domain.RecordableList;
import net.bioclipse.rdf.business.IRDFStore;
import net.bioclipse.rdf.business.RDFManager;
import net.bioclipse.structuredb.business.IStructureDBChangeListener;
import net.bioclipse.structuredb.domain.Annotation;
import net.bioclipse.structuredb.domain.ChoiceAnnotation;
import net.bioclipse.structuredb.domain.DBMolecule;
import net.bioclipse.structuredb.domain.RealNumberAnnotation;
import net.bioclipse.structuredb.domain.TextAnnotation;
import net.bioclipse.structuredb.domain.User;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;

public class StructureRDFManager implements IStructureRDFManager {

    Map<String,IRDFStore> stores = new HashMap<String,IRDFStore>();
    
    RDFManager rdf = new RDFManager();
    
    public final static String STRUCTEN_NS =
        "http://pele.farmbio.uu.se/structurerdf/#";
    
    public final static String MOLECULE_ID = STRUCTEN_NS + "MoleculeID";
    public final static String MOLECULE_TYPE = STRUCTEN_NS + "MoleculeType";
    
    public String getManagerName() {
        return "structrdf";
    }

    public void addMoleculesFromSDF(String databaseName, String filePath)
            throws BioclipseException {
        // TODO Auto-generated method stub

    }

    public void addMoleculesFromSDF(String databaseName, IFile file,
            IProgressMonitor monitor) throws BioclipseException {
        // TODO Auto-generated method stub

    }

    public void addMoleculesFromSDF(String databaseName, IFile file)
            throws BioclipseException {
        // TODO Auto-generated method stub

    }

    public List<Annotation> allAnnotations(String databaseName) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<String> allDatabaseNames() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<TextAnnotation> allLabels(String databaseName) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<DBMolecule> allMolecules(String databaseName) {
        List<DBMolecule> allMolecules = new RecordableList<DBMolecule>();
        IRDFStore store = stores.get(databaseName);
        if (store != null) {
            try {
                String sparql =
                    "PREFIX structrdf: <http://pele.farmbio.uu.se/structurerdf/#> " +
                    "PREFIX rdf: <" + RDF.getURI() + "> " +
                    "PREFIX dc: <" + DC.getURI() + "> " +
                    "SELECT ?s ?t ?i WHERE {" +
                    "    ?s rdf:type structrdf:MoleculeType ." +
                    "    ?s dc:title ?t ." +
                    "    ?s structrdf:MoleculeID ?i . " +
                    "}";
                List<List<String>> results = rdf.sparql(store, sparql);
                for (List<String> molecule : results) {
                    DBMolecule mol = new DBMolecule();
                    System.out.println("Mol: " + molecule.get(1));
                    System.out.println("Mol: " + molecule.get(2));
                    mol.setName(molecule.get(1)); // ?t
                    mol.setId(molecule.get(2));   // ?i
                    allMolecules.add(mol);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (BioclipseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (CoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return allMolecules;
    }

    public List<DBMolecule> allMoleculesByName(String databaseName,
            String structureName) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<User> allUsers(String databaseName) {
        // TODO Auto-generated method stub
        return null;
    }

    public ChoiceAnnotation createChoiceAnnotation(String databaseName,
            String propertyName, String value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    public void createDatabase(String databaseName)
            throws IllegalArgumentException {
        IRDFStore store = stores.get(databaseName);
        if (store != null)
            throw new IllegalArgumentException(
                "Database " + databaseName + " already exists."
            );

        store = rdf.createStore();
        stores.put(databaseName, store);
    }

    public DBMolecule createMolecule(String databaseName, String moleculeName,
            ICDKMolecule cdkMolecule) throws BioclipseException {
        DBMolecule m = new DBMolecule(moleculeName, cdkMolecule);
        IRDFStore store = stores.get(databaseName);
        System.out.println("Size before: " + rdf.size(store));
        String resource = "http://pele.farmbio.uu.se/structurerdf/" +
            databaseName + "/" + moleculeName;
        rdf.addObjectProperty(store,
            resource, RDF.type.toString(), MOLECULE_TYPE
        );
        rdf.addDataProperty(store,
            resource, DC.title.toString(), moleculeName
        );
        rdf.addDataProperty(store, resource, MOLECULE_ID, m.getId());
        System.out.println("Size after: " + rdf.size(store));
        return m;
    }

    public RealNumberAnnotation createRealNumberAnnotation(String databaseName,
            String propertyName, double value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    public TextAnnotation createTextAnnotation(String databaseName,
            String propertyName, String value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    public User createUser(String databaseName, String username,
            String password, boolean sudoer) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    public void deleteAnnotation(String database, Annotation annotation) {
        // TODO Auto-generated method stub

    }

    public void deleteDatabase(String databaseName) {
        if (stores.containsKey(databaseName)) {
            stores.remove(databaseName);
        }
    }

    public void deleteStructure(String database, DBMolecule molecule) {
        // TODO Auto-generated method stub

    }

    public void deleteWithMolecules(String databaseName, Annotation annotation) {
        // TODO Auto-generated method stub

    }

    public void deleteWithMolecules(String name, Annotation annotation,
            IProgressMonitor monitor) {
        // TODO Auto-generated method stub

    }

    public DBMolecule moleculeAtIndexInLabel(String databaseName, int index,
            TextAnnotation annotation) {
        // TODO Auto-generated method stub
        return null;
    }

    public int numberOfMoleculesInLabel(String databaseName,
            TextAnnotation annotation) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void save(String database, DBMolecule molecule) {
        // TODO Auto-generated method stub

    }

    public void save(String database, Annotation annotation) {
        // TODO Auto-generated method stub

    }

    public List<DBMolecule> smartsQuery(String database, String smarts) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<DBMolecule> smartsQuery(String database, String smarts,
            IProgressMonitor monitor) {
        // TODO Auto-generated method stub
        return null;
    }

    public Iterator<DBMolecule> smartsQueryIterator(String database,
            String smarts) {
        // TODO Auto-generated method stub
        return null;
    }

    public Iterator<DBMolecule> smartsQueryIterator(String database,
            String smarts, IProgressMonitor monitor) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<DBMolecule> subStructureSearch(String databaseName,
            IMolecule molecule) throws BioclipseException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<DBMolecule> subStructureSearch(String databaseName,
            IMolecule molecule, IProgressMonitor monitor)
            throws BioclipseException {
        // TODO Auto-generated method stub
        return null;
    }

    public Iterator<DBMolecule> subStructureSearchIterator(String databaseName,
            IMolecule molecule) throws BioclipseException {
        // TODO Auto-generated method stub
        return null;
    }

    public User userByName(String databaseName, String username) {
        // TODO Auto-generated method stub
        return null;
    }

    public IRDFStore getStore(String databaseName) {
        return stores.get(databaseName);
    }

    public void addListener(IStructureDBChangeListener listener) {
        // TODO Auto-generated method stub
        
    }

    public int numberOfMoleculesInDatabaseInstance(String databaseName) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void removeListener(IStructureDBChangeListener listener) {
        // TODO Auto-generated method stub
        
    }


}
