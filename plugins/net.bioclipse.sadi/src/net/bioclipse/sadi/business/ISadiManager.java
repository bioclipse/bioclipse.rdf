/*******************************************************************************
 * Copyright (c) 2012  Egon Willighagen <egon.willighagen@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.sadi.business;

import java.util.List;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(
    value="TODO: Describe the manager here."
)
public interface ISadiManager extends IBioclipseManager {
	
	@PublishedMethod(
		methodSummary="List all registered SADI services"
	)
	public List<String> listServices() throws BioclipseException;

}
