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
package net.bioclipse.rdf.tests;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.List;

import net.bioclipse.core.api.ResourcePathTransformer;
import net.bioclipse.rdf.business.IRDFManager;
import net.bioclipse.rdf.business.IRDFStore;
import net.bioclipse.rdf.model.IStringMatrix;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractRDFManagerPluginTest {

    protected static IRDFManager rdf;

    @Test public void testCreateStore() {
        IRDFStore store = rdf.createStore("/Virtual/test.store");
        Assert.assertNotNull(store);
    }
    
    @Test public void testCreateInMemoryStore() {
        IRDFStore store = rdf.createInMemoryStore();
        Assert.assertNotNull(store);
    }

    @Test public void testDump() {
        IRDFStore store = rdf.createInMemoryStore();
        Assert.assertNotNull(store);
        String dump = rdf.dump(store);
        Assert.assertNotNull(dump);
        Assert.assertNotSame(0, dump.length());
    }

    @Test public void testSize() throws Exception {
        IRDFStore store = rdf.createInMemoryStore();
        Assert.assertNotNull(store);
        long size = rdf.size(store);
        Assert.assertNotSame(0, size);
    }

    @Test public void testAddObjectProperty() throws Exception {
        IRDFStore store = rdf.createInMemoryStore();
        Assert.assertNotNull(store);
        long originalSize = rdf.size(store);
        rdf.addObjectProperty(store,
            "http://example.com/#subject",
            "http://example.com/#predicate",
            "http://example.com/#object"
        );
        Assert.assertEquals(1, rdf.size(store)-originalSize);
        IStringMatrix results = askAllTriplesAbout(
            store, "http://example.com/#subject"
        );
        Assert.assertEquals(1, results.getRowCount());
        Assert.assertEquals("http://example.com/#predicate", results.get(1,"pred"));
        Assert.assertEquals("http://example.com/#object", results.get(1,"obj"));
    }

    @Test public void testAddDataProperty() throws Exception {
        IRDFStore store = rdf.createInMemoryStore();
        Assert.assertNotNull(store);
        long originalSize = rdf.size(store);
        rdf.addDataProperty(store,
            "http://example.com/#subject",
            "http://example.com/#predicate",
            "someDataObject"
        );
        Assert.assertEquals(1, rdf.size(store)-originalSize);
        IStringMatrix results = askAllTriplesAbout(
            store, "http://example.com/#subject"
        );
        Assert.assertEquals(1, results.getRowCount());
        Assert.assertEquals("http://example.com/#predicate", results.get(1,"pred"));
        Assert.assertEquals("someDataObject", results.get(1,"obj"));
    }

    @Test public void testImportFile_NTriple() throws Exception {
        URI uri = getClass().getResource("/testFiles/example.nt").toURI();
        URL url=FileLocator.toFileURL(uri.toURL());
        String path=url.getFile();
        IRDFStore store = rdf.createInMemoryStore();
        long originalTripleCount = rdf.size(store);
        rdf.importFile(store, path, "N-TRIPLE");
        Assert.assertEquals(
            originalTripleCount+1,
            rdf.size(store)
        );
        IStringMatrix results = askAllTriplesAbout(
            store, "http://example.com/#subject"
        );
        Assert.assertEquals(1, results.getRowCount());
        Assert.assertEquals("http://example.com/#predicate", results.get(1,"pred"));
        Assert.assertEquals("http://example.com/#object", results.get(1,"obj"));
    }

    @Test public void testImportFromStream_NTriple() throws Exception {
        URI uri = getClass().getResource("/testFiles/example.nt").toURI();
        URL url=FileLocator.toFileURL(uri.toURL());
        IRDFStore store = rdf.createInMemoryStore();
        long originalTripleCount = rdf.size(store);
        rdf.importFromStream(store, url.openStream(), "N-TRIPLE");
        Assert.assertEquals(
            originalTripleCount+1,
            rdf.size(store)
        );
        IStringMatrix results = askAllTriplesAbout(
            store, "http://example.com/#subject"
        );
        Assert.assertEquals(1, results.getRowCount());
        Assert.assertEquals("http://example.com/#predicate", results.get(1,"pred"));
        Assert.assertEquals("http://example.com/#object", results.get(1,"obj"));
    }

    @Test public void testImportFile_RDFXML() throws Exception {
        URI uri = getClass().getResource("/testFiles/example.rdfxml").toURI();
        URL url=FileLocator.toFileURL(uri.toURL());
        String path=url.getFile();
        IRDFStore store = rdf.createInMemoryStore();
        long originalTripleCount = rdf.size(store);
        rdf.importFile(store, path, "RDF/XML");
        Assert.assertEquals(
            originalTripleCount+1,
            rdf.size(store)
        );
        IStringMatrix results = askAllTriplesAbout(
            store, "http://example.com/#subject"
        );
        Assert.assertEquals(1, results.getRowCount());
        Assert.assertEquals("http://example.com/#predicate", results.get(1,"pred"));
        Assert.assertEquals("http://example.com/#object", results.get(1,"obj"));
    }

    @Test public void testImportURL() throws Exception {
        URI uri = getClass().getResource("/testFiles/example.rdfxml").toURI();
        URL url=FileLocator.toFileURL(uri.toURL());
        IRDFStore store = rdf.createInMemoryStore();
        long originalTripleCount = rdf.size(store);
        System.out.println(url.toExternalForm());
        rdf.importURL(store, url.toExternalForm());
        Assert.assertEquals(
            originalTripleCount+1,
            rdf.size(store)
        );
        IStringMatrix results = askAllTriplesAbout(
            store, "http://example.com/#subject"
        );
        Assert.assertEquals(1, results.getRowCount());
        Assert.assertEquals("http://example.com/#predicate", results.get(1,"pred"));
        Assert.assertEquals("http://example.com/#object", results.get(1,"obj"));
    }

    @Test public void testSaveRDFXML() throws Exception {
        IRDFStore store = rdf.createInMemoryStore();
        rdf.addObjectProperty(store,
            "http://example.com/#subject",
            "http://example.com/#predicate",
            "http://example.com/#object"
        );
        String rdfPath = "/Virtual/rdf" + store.hashCode() + ".xml";
        rdf.saveRDFXML(store, rdfPath);

        IRDFStore loadedStore = rdf.createInMemoryStore();
        rdf.importFile(loadedStore, rdfPath, "RDF/XML");
        Assert.assertEquals(rdf.size(store), rdf.size(loadedStore));
    }

    @Test public void testSaveRDFNTriple() throws Exception {
        IRDFStore store = rdf.createInMemoryStore();
        rdf.addObjectProperty(store,
            "http://example.com/#subject",
            "http://example.com/#predicate",
            "http://example.com/#object"
        );
        String rdfPath = "/Virtual/rdf" + store.hashCode() + ".nt";
        rdf.saveRDFNTriple(store, rdfPath);

        IRDFStore loadedStore = rdf.createInMemoryStore();
        rdf.importFile(loadedStore, rdfPath, "N-TRIPLE");
        Assert.assertEquals(rdf.size(store), rdf.size(loadedStore));
    }

    @Test public void testCopy() throws Exception {
        IRDFStore store = rdf.createInMemoryStore();
        rdf.addObjectProperty(store,
            "http://example.com/#subject",
            "http://example.com/#predicate",
            "http://example.com/#object"
        );
        rdf.addDataProperty(store,
            "http://example.com/#subject",
            "http://example.com/#predicate",
            "someDataObject"
        );

        IRDFStore secondStore = rdf.createInMemoryStore();
        Assert.assertEquals(0,
            tripleCount(secondStore, "http://example.com/#subject")
        );
        rdf.copy(secondStore, store);
        Assert.assertEquals(2,
            tripleCount(secondStore, "http://example.com/#subject")
        );
    }

    private IStringMatrix askAllTriplesAbout(
            IRDFStore store, String string) throws Exception {
        String query =
            "SELECT ?pred ?obj " +
            "WHERE {<" + string + "> ?pred ?obj . }";
        return rdf.sparql(store, query);
    }

    @Test public void testAddPrefix_WithoutPrefix() throws Exception {
        IRDFStore store = rdf.createInMemoryStore();
        rdf.addObjectProperty(store,
            "http://example.com/#subject",
            "http://example.com/#predicate",
            "http://example.com/#object"
        );
        String rdfPath = "/Virtual/rdf" + store.hashCode() + ".xml";
        rdf.saveRDFXML(store, rdfPath);

        IFile target = ResourcePathTransformer.getInstance().transform(rdfPath);
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(target.getContents())
        );
        StringBuffer fileContents = new StringBuffer();
        String line = reader.readLine();
        while(line != null){
            fileContents.append(line).append('\n');
            line = reader.readLine();
        }

        Assert.assertFalse(fileContents.toString().contains("foobar"));
    }

    @Test public void testAddPrefix_WithPrefix() throws Exception {
        IRDFStore store = rdf.createInMemoryStore();
        rdf.addPrefix(store, "foobar", "http://example.com/#");
        rdf.addObjectProperty(store,
            "http://example.com/#subject",
            "http://example.com/#predicate",
            "http://example.com/#object"
        );
        String rdfPath = "/Virtual/rdf" + store.hashCode() + ".xml";
        rdf.saveRDFXML(store, rdfPath);

        IFile target = ResourcePathTransformer.getInstance().transform(rdfPath);
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(target.getContents())
        );
        StringBuffer fileContents = new StringBuffer();
        String line = reader.readLine();
        while(line != null){
            fileContents.append(line).append('\n');
            line = reader.readLine();
        }

        Assert.assertTrue(fileContents.toString().contains("foobar"));
    }

    @Test public void testSaveRDFN3() throws Exception {
        IRDFStore store = rdf.createInMemoryStore();
        rdf.addObjectProperty(store,
            "http://example.com/#subject",
            "http://example.com/#predicate",
            "http://example.com/#object"
        );
        String rdfPath = "/Virtual/rdf" + store.hashCode() + ".n3";
        rdf.saveRDFN3(store, rdfPath);

        IRDFStore loadedStore = rdf.createInMemoryStore();
        rdf.importFile(loadedStore, rdfPath, "N3");
        Assert.assertEquals(rdf.size(store), rdf.size(loadedStore));
    }

    @Test public void testSparqlRemote() throws Exception {
        String query =
            "PREFIX mebase: <http://rdf.myexperiment.org/ontologies/base/> " +
            "PREFIX dcterms: <http://purl.org/dc/terms/> " +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
            "SELECT ?workflow ?title WHERE { " +
            "      ?workflow mebase:has-content-type ?type . " +
            "      ?workflow dcterms:title ?title . " +
            "      ?type rdf:type mebase:ContentType . " +
            "      ?type dcterms:title ?typetitle . " +
            "      FILTER regex(?typetitle, \"Bioclipse\") . " +
            "}";
        IStringMatrix results = rdf.sparqlRemote(
            "http://rdf.myexperiment.org/sparql", query
        );
        Assert.assertNotSame(0, results.getRowCount());
        Assert.assertNotSame(0, results.getColumnCount());
    }

    @Test public void testImportRDFa() throws Exception {
        IRDFStore store = rdf.createInMemoryStore();
        rdf.importRDFa(store, "http://egonw.github.com/");
        String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
        		"SELECT ?homepage { ?s foaf:workplaceHomepage ?homepage }";
        IStringMatrix homepages = rdf.sparql(store, query);
        Assert.assertNotSame(0, homepages.getRowCount());
        for (String homepage : homepages.getColumn("homepage")) {
            Assert.assertTrue(homepage.contains("http://"));
        }
    }

    @Test public void testAllClasses() throws Exception  {
        IRDFStore store = rdf.createInMemoryStore();
        rdf.importRDFa(store, "http://egonw.github.com/");
        List<String> classes = rdf.allClasses(store);
        Assert.assertNotSame(0, classes.size());
        for (String classURI : classes) {
        	System.out.println("Class: " + classURI);
            Assert.assertNotNull(classURI);
            Assert.assertNotSame(0, classURI.length());
        }
    }
    
    @Test public void testAllPredicates() throws Exception  {
        IRDFStore store = rdf.createInMemoryStore();
        rdf.importRDFa(store, "http://egonw.github.com/");
        List<String> predicates = rdf.allPredicates(store);
        Assert.assertNotSame(0, predicates.size());
        for (String predicateURI : predicates) {
        	System.out.println("Predicate: " + predicateURI);
            Assert.assertNotNull(predicateURI);
            Assert.assertNotSame(0, predicateURI.length());
        }
    }

    private int tripleCount(IRDFStore store, String string) throws Exception {
        return askAllTriplesAbout(store, string).getRowCount();
    }

}
