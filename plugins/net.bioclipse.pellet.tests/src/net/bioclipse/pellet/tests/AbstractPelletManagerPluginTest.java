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
package net.bioclipse.pellet.tests;

import java.util.List;

import net.bioclipse.pellet.business.IPelletManager;
import net.bioclipse.rdf.business.IRDFManager;
import net.bioclipse.rdf.business.IRDFStore;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.hp.hpl.jena.vocabulary.RDF;

public abstract class AbstractPelletManagerPluginTest {

    protected static IPelletManager pellet;
    protected static IRDFManager rdf;

    @Test
    public void testCreateInMemoryStore() {
        IRDFStore store = pellet.createInMemoryStore();
        Assert.assertNotNull(store);
    }

    @Ignore("Test needs to be rewritten as /Virtual will be removed")
    public void testCreateStore() {
        IRDFStore store = pellet.createStore("/Virtual/test.store");
        Assert.assertNotNull(store);
    }

    @Test public void testIsRDFType() throws Exception {
        IRDFStore store = pellet.createInMemoryStore();
        rdf.addObjectProperty(store,
            "http://www.example.com/foo",
            RDF.type.getURI(),
            "http://www.example.com/bar"
        );
        List<String> results =
            pellet.isRDFType(store, "http://www.example.com/bar");
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(
            "http://www.example.com/foo", results.get(0)
        );
    }

    @Test public void testAllAbout() throws Exception {
        IRDFStore store = pellet.createInMemoryStore();
        rdf.addObjectProperty(store,
            "http://www.example.com/foo",
            "http://www.example.com/x",
            "http://www.example.com/bar"
        );
        List<List<String>> results =
            pellet.allAbout(store, "http://www.example.com/foo");
        boolean includesAboveTriple = false;
        for (List<String> triple : results) {
            if (triple.get(0).equals("http://www.example.com/x")) {
                includesAboveTriple = true;
                Assert.assertEquals(
                    "http://www.example.com/bar", triple.get(1)
                );
            }
        }
        Assert.assertTrue(includesAboveTriple);
    }

    @Test public void testReason() throws Exception {
        IRDFStore store = pellet.createInMemoryStore();
        String query =
            "SELECT ?pred ?obj " +
            "WHERE {<http://www.example.com/foo> ?pred ?obj . }";
        pellet.reason(store, query);
    }

    @Test public void testValidate() throws Exception {
        IRDFStore store = pellet.createInMemoryStore();
        pellet.validate(store);
        Assert.assertNotNull(store);
    }

}
