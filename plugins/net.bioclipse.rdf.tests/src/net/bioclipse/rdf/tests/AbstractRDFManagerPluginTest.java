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

import net.bioclipse.rdf.business.IRDFManager;
import net.bioclipse.rdf.business.IRDFStore;

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

}
