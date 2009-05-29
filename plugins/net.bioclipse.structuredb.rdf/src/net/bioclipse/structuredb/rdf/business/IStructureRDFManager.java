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
package net.bioclipse.structuredb.rdf.business;

import net.bioclipse.core.TestClasses;
import net.bioclipse.core.business.IBioclipseManager;
import net.bioclipse.rdf.business.IRDFStore;
import net.bioclipse.structuredb.business.IStructuredbManager;

@TestClasses(value="net.bioclipse.structuredb.rdf.business.tests.StructureRDFManagerTest")
public interface IStructureRDFManager extends IStructuredbManager, IBioclipseManager {

    public IRDFStore getStore(String databaseName);

}
