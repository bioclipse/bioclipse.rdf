/*******************************************************************************
 * Copyright (c) 2015  Egon Willighagen <egonw@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.ldf.business;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.rdf.business.IRDFStore;

@PublishedClass(
    value="Linked Data Fragments manager."
)
public interface ILdfManager extends IBioclipseManager {

	@Recorded
    @PublishedMethod(
    	params="String provider",
        methodSummary="Creates an RDF store for the given RDF provider."
    )
    public IRDFStore createStore(String provider) throws BioclipseException;
}
