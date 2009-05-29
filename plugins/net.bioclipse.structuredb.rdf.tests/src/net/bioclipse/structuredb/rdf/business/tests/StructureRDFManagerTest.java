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
import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.MockIFile;
import net.bioclipse.core.business.IBioclipseManager;
import net.bioclipse.core.tests.AbstractManagerTest;
import net.bioclipse.structuredb.business.IStructuredbManager;
import net.bioclipse.structuredb.domain.DBMolecule;
import net.bioclipse.structuredb.rdf.business.StructureRDFManager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class StructureRDFManagerTest extends AbstractManagerTest {

    private static IStructuredbManager manager;
    private final static String database1 = "database1";
    private final static String database2 = "database2";

    private ICDKManager cdk = new CDKManager();

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
        Assert.fail("Not implemented yet.");
    }

    @Test public void testEditTextAnnotation() {
        Assert.fail("Not implemented yet.");
    }

    @Test public void testAddMoleculesFromSDF() {
        Assert.fail("Not implemented yet.");
    }

    @Test public void testCreatingAndRetrievingAnnotations() {
        Assert.fail("Not implemented yet.");
    }

    @Test public void testAllLabels() {
        Assert.fail("Not implemented yet.");
    }
    
    public void testCreatingAndRetrievingStructures() throws Exception {
        ICDKManager cdk = new CDKManager();

        ICDKMolecule mol1 = cdk.loadMolecule( 
            new MockIFile( StructureRDFManagerTest.class
                                   .getClassLoader()
                                   .getResourceAsStream(
                                       "testData/0037.cml") ) );
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
                                       "testData/0106.cml") ) ) );

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
        Assert.fail("Not implemented yet.");
    }

    @Test public void testCreatingRealNumberAnnotation() {
        Assert.fail("Not implemented yet.");
    }

    @Test public void testCreatingTextAnnotation() {
        Assert.fail("Not implemented yet.");
    }

    @Test public void testDeleteAnnotation() {
        Assert.fail("Not implemented yet.");
    }

    @Test public void testRemovingDatabaseInstance() {
        Assert.fail("Not implemented yet.");
    }

    @Test public void testCreateMolecule() throws Exception {
        DBMolecule dbMolecule = manager.createMolecule(
             database1, 
             "test", 
             cdk.fromSMILES("CC")
        );
        Assert.assertNotNull(dbMolecule);
        Assert.assertTrue(
            manager.allMolecules(database1).contains(dbMolecule)
        );
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
        Assert.fail("Not implemented yet.");
    }

    @Test public void testListSMARTSQueryResults() {
        Assert.fail("Not implemented yet.");
    }

    @Test public void testSmartsQueryIterator() {
        Assert.fail("Not implemented yet.");
    }

    @Test public void testListSubstructureSearchResults() {
        Assert.fail("Not implemented yet.");
    }

    @Test public void testSubstructureSearch() {
        Assert.fail("Not implemented yet.");
    }

    @Test public void testCreatingAndRetrievingUsers() {
        Assert.fail("Not implemented yet.");
    }

}
