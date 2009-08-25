/*******************************************************************************
 * Copyright (c) 2009  Egon Willighagen <egonw@users.sf.net>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 * 
 * Contact: http://www.bioclipse.net/    
 ******************************************************************************/
package net.bioclipse.myexperiment.tests.business;

import net.bioclipse.myexperiment.Activator;

import org.junit.BeforeClass;

public class JavaMyExperimentManagerPluginTest
    extends AbstractMyExperimentManagerPluginTest {

    @BeforeClass public static void setup() {
        myexperiment = Activator.getDefault().getJavaMyExperimentManager();
    }

}
