/*******************************************************************************
 * Copyright (c) 2009  Egon Willighagen <egonw@users.sf.net>
 *               2008  Jonathan Alverson
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: Bioclipse Project <http://www.bioclipse.net>
 ******************************************************************************/
package net.bioclipse.structuredb.rdf.business.tests;

import java.util.List;

import net.bioclipse.cdk.business.CDKManager;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.MockIFile;
import net.bioclipse.core.tests.AbstractManagerTest;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.rdf.business.IRDFStore;
import net.bioclipse.rdf.business.RDFManager;
import net.bioclipse.structuredb.domain.DBMolecule;
import net.bioclipse.structuredb.rdf.business.IStructureRDFManager;
import net.bioclipse.structuredb.rdf.business.StructureRDFManager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class StructureRDFManagerTest extends AbstractManagerTest {

    private static IStructureRDFManager manager;
    private final static String database1 = "database1";
    private final static String database2 = "database2";

    private CDKManager cdk = new CDKManager();
    private RDFManager rdf = new RDFManager();

    @BeforeClass
    public static void setup() throws Exception {
        manager = new StructureRDFManager();
        Assert.assertNotNull(manager);
        manager.createDatabase(database1);
        manager.createDatabase(database2);
    }
    
    public IBioclipseManager getManager() {
        return new StructureRDFManager();
    }

    @Test public void testCreateAndDeleteDatabase() {
        manager.createDatabase("testCreateDatabase");
        manager.deleteDatabase("testCreateDatabase");
        // test if delete worked properly
        manager.createDatabase("testCreateDatabase");
        manager.deleteDatabase("testCreateDatabase");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateDatabaseDuplicate() {
        manager.createDatabase("testCreateDatabaseDuplicate");
        manager.createDatabase("testCreateDatabaseDuplicate");        
    }

    @Test public void testEditDBMolecule() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testEditTextAnnotation() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testAddMoleculesFromSDF() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testCreatingAndRetrievingAnnotations() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testAllLabels() {
        throw new RuntimeException("Not implemented yet.");
    }
    
    public void testCreatingAndRetrievingStructures() throws Exception {
        CDKManager cdk = new CDKManager();

        ICDKMolecule mol1 = cdk.loadMolecule( 
            new MockIFile( StructureRDFManagerTest.class
                                   .getClassLoader()
                                   .getResourceAsStream(
                                       "testData/0037.cml") ),
            null
        );
        Assert.assertNotNull(mol1);

        DBMolecule structure1 = manager
                                .createMolecule( database1,
                                                 "0037",
                                                 mol1 );
        Assert.assertNotNull(structure1);

        DBMolecule structure2 
            = manager.createMolecule(
                database1,
                "0106",
                cdk.loadMolecule( 
                    new MockIFile( StructureRDFManagerTest.class
                                   .getClassLoader()
                                   .getResourceAsStream(
                                       "testData/0106.cml") ),
                                       null 
                )
            );

        Assert.assertNotNull(structure2);

        Assert.assertTrue( manager
                    .allMoleculesByName( database1,
                                         structure1.getName() )
                    .contains(structure1) );
        Assert.assertTrue( manager
                    .allMoleculesByName( database1,
                                         structure2.getName() )
                    .contains(structure2) );

        List<DBMolecule> dBMolecules = manager.allMolecules(database1);

        Assert.assertTrue( dBMolecules.contains(structure1) );
        Assert.assertTrue( dBMolecules.contains(structure2) );
    }

    @Test public void testCreatingChoiceAnnotation() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testCreatingRealNumberAnnotation() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testCreatingTextAnnotation() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testDeleteAnnotation() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testRemovingDatabaseInstance() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testCreateMolecule() throws Exception {
        final String database = "testCreateMolecule";
        manager.createDatabase(database);
        DBMolecule dbMolecule = manager.createMolecule(
             database,
             "test", 
             cdk.fromSMILES("CC")
        );
        Assert.assertNotNull(dbMolecule);
        Assert.assertTrue(
            manager.allMolecules(database).contains(dbMolecule)
        );
        IRDFStore store = manager.getStore(database);
        Assert.assertNotSame(0, rdf.size(store));
    }
    
    @Test public void testDeleteStructure() throws Exception {
        DBMolecule dbMolecule = manager.createMolecule(
            database1, 
            "test", 
            cdk.fromSMILES("CC")
        );
        manager.deleteStructure(database1, dbMolecule);
        Assert.assertFalse(
            manager.allMolecules(database1).contains(dbMolecule)
        );
    }

    @Test public void testDeletingAnnotationWithMolecules() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testListSMARTSQueryResults() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testSmartsQueryIterator() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testListSubstructureSearchResults() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testSubstructureSearch() {
        throw new RuntimeException("Not implemented yet.");
    }

    @Test public void testCreatingAndRetrievingUsers() {
        throw new RuntimeException("Not implemented yet.");
    }

}
