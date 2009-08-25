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
package net.bioclipse.myexperiment.tests.business;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.List;

import net.bioclipse.core.ResourcePathTransformer;
import net.bioclipse.myexperiment.business.IMyExperimentManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractMyExperimentManagerPluginTest {

    protected static IMyExperimentManager myexperiment;
    
    @Test public void testDoSomething() {
        // FIXME: myexperiment.doSomething();
    }

}
