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
package net.bioclipse.pellet.business;

import java.io.IOException;
import java.util.List;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.TestClasses;
import net.bioclipse.core.TestMethods;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.rdf.business.IRDFStore;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

@PublishedClass(
    value="Contains Pellet related methods",
    doi={"10.1016/j.websem.2007.03.004"}
)
@TestClasses(
     "net.bioclipse.pellet.tests.APITest," +
     "net.bioclipse.pellet.tests.CoverageTest," +
     "net.bioclipse.pellet.tests.AbstractPelletManagerPluginTest"
)
public interface IPelletManager extends IBioclipseManager {

    @Recorded
    @PublishedMethod(
        methodSummary = "Creates a new Pellet-targeted store"
    )
    @TestMethods("testCreateInMemoryStore")
    public IRDFStore createInMemoryStore();

    @Recorded
    @PublishedMethod(
        params = "String tripleStoreDirectoryPath",
        methodSummary = "Creates a new scalable Pellet-targeted store, " +
                "(using the Jena TDB package, which stores on disk as a " +
                "complement to memory, for scalability). " +
                "tripleStoreDirectoryPath is the path (relative to the" +
                "Bioclipse workspace) to a folder to use for the " +
                "triple store"
    )
    public IRDFStore createStore(String tripleStoreDirectoryPath); 

    @Recorded
    public IRDFStore createStore(IFile tripleStoreDirectoryPath); 

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String query",
        methodSummary = "Returns the results matching the SPARQL query using" +
            " the Pellet reasoner"
    )
    public List<List<String>> reason(IRDFStore store, String query)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store",
        methodSummary = "Validates the consistency of the RDF model in the " +
            "store"
    )
    @TestMethods("testValidate")
    public void validate(IRDFStore store)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String type",
        methodSummary = "Returns the Objects that are of the given rdf:type."
    )
    public List<String> isRDFType(IRDFStore store, String type)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String identifier",
        methodSummary = "Returns all triples about the Object with the given " +
        		"identifier."
    )
    public List<List<String>> allAbout(IRDFStore store, String identifier)
        throws BioclipseException, IOException, CoreException;
}