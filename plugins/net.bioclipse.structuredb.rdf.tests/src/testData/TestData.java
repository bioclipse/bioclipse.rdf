/*******************************************************************************
 * Copyright (c) 2007-2008 Bioclipse Project
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     
 *******************************************************************************/
package testData;

import java.io.File;
import java.io.InputStream;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.io.MDLReader;

/**
 * Instatiates some testdata from files in the testdata package
 * 
 * @author jonalv
 *
 */
public abstract class TestData {

    private TestData() {
    }
    
    public static AtomContainer getCycloPropane() throws CDKException {
        return readFromFile( "testData/cyclopropane.mol" );
        
    }
    
    public static AtomContainer getCycloOctan() throws CDKException {
        return readFromFile( "testData/cyclooctan.mol" );
    }
    
    public static AtomContainer readFromFile(String path) throws CDKException {
        InputStream ins = TestData.class.getClassLoader().getResourceAsStream(path);
        MDLReader reader = new MDLReader(ins);
        return (AtomContainer) reader.read( (ChemObject)new Molecule() );
    }
    
    public static String getTestSDFFilePath() {
        return TestData.class
                       .getClassLoader()
                       .getResource("testData/sdfTestFile.sdf")
                       .getPath();
    }
}
