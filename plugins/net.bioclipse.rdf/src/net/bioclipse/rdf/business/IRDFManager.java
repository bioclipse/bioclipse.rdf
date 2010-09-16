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
package net.bioclipse.rdf.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.TestClasses;
import net.bioclipse.core.TestMethods;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IStringMatrix;
import net.bioclipse.managers.business.IBioclipseManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.hp.hpl.jena.rdf.model.Model;

@PublishedClass("Contains RDF related methods")
@TestClasses(
    "net.bioclipse.rdf.tests.APITest," +
    "net.bioclipse.rdf.tests.AbstractRDFManagerPluginTest"
)
public interface IRDFManager extends IBioclipseManager {

    @Recorded
    @PublishedMethod(
        methodSummary = "Creates a new in-memory store."
    )
    @TestMethods("testCreateInMemoryStore")
    public IRDFStore createInMemoryStore();

    @Recorded
    @PublishedMethod(
        params = "String tripleStoreDirectoryPath",
        methodSummary = "Creates a new on-disk store, " +
            "(using the Jena TDB package, which stores on disk as a " +
            "complement to memory, for scalability). " +
            "tripleStoreDirectoryPath is the path (relative to the" +
            "Bioclipse workspace) to a folder to use for the " +
            "triple store"
    )
    @TestMethods("testCreateStore")
    public IRDFStore createStore(String tripleStoreDirectoryPath);

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String target, String format",
        methodSummary = "Loads an RDF file in the given content format " +
        		"(\"RDF/XML\", \"N-TRIPLE\", \"TURTLE\" and \"N3\") into " +
        		"the given store"
    )
    @TestMethods("testImportFile_RDFXML,testImportFile_NTriple")
    public IRDFStore importFile(IRDFStore store, String target, String format)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, InputStream stream, String format",
        methodSummary = "Loads an RDF file in the given content format " +
                "(\"RDF/XML\", \"N-TRIPLE\", \"TURTLE\" and \"N3\") into " +
                "the given store"
    )
    public IRDFStore importFromStream(
            IRDFStore store, InputStream stream, String format
        ) throws IOException, BioclipseException, CoreException;

    @Recorded
    public IRDFStore importFile(IRDFStore store, IFile target, String format,
            IProgressMonitor monitor)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String url",
        methodSummary = "Loads a RDF/XML file from the URL into the given store"
    )
    @TestMethods("testImportURL")
    public IRDFStore importURL(IRDFStore store, String url)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    public IRDFStore importURL(IRDFStore store, String url, IProgressMonitor monitor)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String url",
        methodSummary = "Loads a XHTML+RDFa file from the URL into the given " +
        		"store"
    )
    @TestMethods("testImportRDFa")
    public IRDFStore importRDFa(IRDFStore store, String url)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store",
        methodSummary = "Dumps the full model to the returned String"
    )
    @TestMethods("testDump")
    public String dump(IRDFStore store);

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String query",
        methodSummary = "Returns the results matching the SPARQL query"
    )
    @TestMethods("testAddDataProperty,testAddObjectProperty," +
        "testImportFile_NTriple,testImportURL")
    public IStringMatrix sparql(IRDFStore store, String query)
        throws IOException, BioclipseException, CoreException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String subject, String predicate, " +
            "String object",
        methodSummary = "Adds a triple to the given store"
    )
    @TestMethods("testAddObjectProperty")
    public void addObjectProperty(IRDFStore store,
        String subject, String predicate, String object)
        throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String subject, String predicate, " +
                "String value",
        methodSummary = "Adds a triple to the given store"
    )
    @TestMethods("testAddDataProperty")
    public void addDataProperty(IRDFStore store,
        String subject, String predicate, String value)
        throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store",
        methodSummary = "Returns the number of triples in the store"
    )
    @TestMethods("testSize")
    public long size(IRDFStore store) throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String fileName",
        methodSummary = "Saves the store to RDF/XML."
    )
    @TestMethods("testSaveRDFXML")
    public void saveRDFXML(IRDFStore store, String fileName)
        throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String fileName",
        methodSummary = "Saves the store to a N3 file."
    )
    @TestMethods("testSaveRDFN3")
    public void saveRDFN3(IRDFStore store, String fileName)
        throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store",
        methodSummary = "Returns a String with the N3 serialization."
    )
    public String asRDFN3(IRDFStore store)
        throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String fileName",
        methodSummary = "Saves the store to RDF in N-TRIPLE format."
    )
    @TestMethods("testSaveRDFNTriple")
    public void saveRDFNTriple(IRDFStore store, String fileName)
        throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "String url, String SPARQL",
        methodSummary = "Queries a remote SPARQL end point."
    )
    @TestMethods("testSparqlRemote")
    public IStringMatrix sparqlRemote(String url, String SPARQL)
        throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "String url, String SPARQL",
        methodSummary = "Queries a remote SPARQL endpoint and returns RDF/XML. " +
        		        "Assumes that the query is creating an RDF graph with the " +
        		        "CONSTRUCT keyword"
    )
    public Model sparqlConstructRemote(String url, String SPARQL)
        throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore targetStore, IRDFStore sourceStore",
        methodSummary = "Copies the triples from the sourceStore into the " +
                "targetStore."
    )
    @TestMethods("testCopy")
    public void copy(IRDFStore targetStore, IRDFStore sourceStore)
        throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String prefix, String namespace",
        methodSummary = "Registers the prefix for the given namespace."
    )
    @TestMethods("testAddPrefix_WithPrefix")
    public void addPrefix(IRDFStore store, String prefix, String namespace)
        throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store",
        methodSummary = "Lists all existing classes."
    )
    @TestMethods("testAllClasses")
    public List<String> allClasses(IRDFStore store) throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store",
        methodSummary = "Lists all existing predicates."
    )
    @TestMethods("testAllPredicates")
    public List<String> allPredicates(IRDFStore store) throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "IRDFStore store, String resourceURI",
        methodSummary = "Lists all resources that are owl:sameAs as the " +
        		"given resource."
    )
    public List<String> allOwlSameAs(IRDFStore store, String resourceURI)
    throws IOException, BioclipseException, CoreException;
}