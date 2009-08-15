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

import java.net.URI;
import java.net.URL;
import java.util.List;

import net.bioclipse.rdf.business.IRDFManager;
import net.bioclipse.rdf.business.IRDFStore;

import org.eclipse.core.runtime.FileLocator;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractRDFManagerPluginTest {

    protected static IRDFManager rdf;

    @Test public void testCreateStore() {
        IRDFStore store = rdf.createStore();
        Assert.assertNotNull(store);
    }

    @Test public void testDump() {
        IRDFStore store = rdf.createStore();
        Assert.assertNotNull(store);
        String dump = rdf.dump(store);
        Assert.assertNotNull(dump);
        Assert.assertNotSame(0, dump.length());
    }

    @Test public void testSize() throws Exception {
        IRDFStore store = rdf.createStore();
        Assert.assertNotNull(store);
        long size = rdf.size(store);
        Assert.assertNotSame(0, size);
    }

    @Test public void testAddObjectProperty() throws Exception {
        IRDFStore store = rdf.createStore();
        Assert.assertNotNull(store);
        long originalSize = rdf.size(store);
        rdf.addObjectProperty(store,
            "http://example.com/#subject",
            "http://example.com/#predicate",
            "http://example.com/#object"
        );
        Assert.assertEquals(1, rdf.size(store)-originalSize);
        List<List<String>> results = askAllTriplesAbout(
            store, "http://example.com/#subject"
        );
        Assert.assertEquals(1, results.size());
        List<String> triple = results.get(0);
        Assert.assertEquals("http://example.com/#predicate", triple.get(0));
        Assert.assertEquals("http://example.com/#object", triple.get(1));
    }

    @Test public void testAddDataProperty() throws Exception {
        IRDFStore store = rdf.createStore();
        Assert.assertNotNull(store);
        long originalSize = rdf.size(store);
        rdf.addDataProperty(store,
            "http://example.com/#subject",
            "http://example.com/#predicate",
            "someDataObject"
        );
        Assert.assertEquals(1, rdf.size(store)-originalSize);
        List<List<String>> results = askAllTriplesAbout(
            store, "http://example.com/#subject"
        );
        Assert.assertEquals(1, results.size());
        List<String> triple = results.get(0);
        Assert.assertEquals("http://example.com/#predicate", triple.get(0));
        Assert.assertEquals("someDataObject", triple.get(1));
    }

    @Test public void testImportFile_NTriple() throws Exception {
        URI uri = getClass().getResource("/testFiles/example.nt").toURI();
        URL url=FileLocator.toFileURL(uri.toURL());
        String path=url.getFile();
        IRDFStore store = rdf.createStore();
        long originalTripleCount = rdf.size(store);
        rdf.importFile(store, path, "N-TRIPLE");
        Assert.assertEquals(
            originalTripleCount+1,
            rdf.size(store)
        );
        List<List<String>> results = askAllTriplesAbout(
            store, "http://example.com/#subject"
        );
        Assert.assertEquals(1, results.size());
        List<String> triple = results.get(0);
        Assert.assertEquals("http://example.com/#predicate", triple.get(0));
        Assert.assertEquals("http://example.com/#object", triple.get(1));
    }

    @Test public void testImportFile_RDFXML() throws Exception {
        URI uri = getClass().getResource("/testFiles/example.rdfxml").toURI();
        URL url=FileLocator.toFileURL(uri.toURL());
        String path=url.getFile();
        IRDFStore store = rdf.createStore();
        long originalTripleCount = rdf.size(store);
        rdf.importFile(store, path, "RDF/XML");
        Assert.assertEquals(
            originalTripleCount+1,
            rdf.size(store)
        );
        List<List<String>> results = askAllTriplesAbout(
            store, "http://example.com/#subject"
        );
        Assert.assertEquals(1, results.size());
        List<String> triple = results.get(0);
        Assert.assertEquals("http://example.com/#predicate", triple.get(0));
        Assert.assertEquals("http://example.com/#object", triple.get(1));
    }

    private List<List<String>> askAllTriplesAbout(
            IRDFStore store, String string) throws Exception {
        String query =
            "SELECT ?pred ?obj " +
            "WHERE {<" + string + "> ?pred ?obj . }";
        return rdf.sparql(store, query);
    }

}
