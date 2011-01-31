/* 
 * Copyright (c) 2011  Jonathan Alvarsson <jonalv@users.sourceforge.net>
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package net.bioclipse.rdf.completerepotest;

import net.bioclipse.pellet.tests.AllPelletManagerPluginTests;
import net.bioclipse.rdf.tests.AllRDFManagerPluginTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author jonalv
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
    AllRDFManagerPluginTests.class,
    AllPelletManagerPluginTests.class
})
public class AllPluginTests {


}
