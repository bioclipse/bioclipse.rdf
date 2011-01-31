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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * @author jonalv
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
    net.bioclipse.rdf.tests.AllRDFManagerTests.class,
    net.bioclipse.pellet.tests.AllPelletManagerTests.class
})
public class AllTests {

}
