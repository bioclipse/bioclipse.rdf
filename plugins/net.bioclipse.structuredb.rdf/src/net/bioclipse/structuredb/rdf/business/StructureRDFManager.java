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

import java.util.Iterator;
import java.util.List;

import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.structuredb.business.IDatabaseListener;
import net.bioclipse.structuredb.domain.Annotation;
import net.bioclipse.structuredb.domain.ChoiceAnnotation;
import net.bioclipse.structuredb.domain.DBMolecule;
import net.bioclipse.structuredb.domain.RealNumberAnnotation;
import net.bioclipse.structuredb.domain.TextAnnotation;
import net.bioclipse.structuredb.domain.User;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

public class StructureRDFManager implements IStructureRDFManager {

    public String getNamespace() {
        return "structrdf";
    }

    public void addListener(IDatabaseListener listener) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub

    }

    public DBMolecule createMolecule(String databaseName, String moleculeName,
            ICDKMolecule cdkMolecule) throws BioclipseException {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub

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

    public void removeListener(IDatabaseListener listener) {
        // TODO Auto-generated method stub

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


}
