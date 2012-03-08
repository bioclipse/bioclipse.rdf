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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;
import ca.wilkinsonlab.sadi.SADIException;
import ca.wilkinsonlab.sadi.client.Config;
import ca.wilkinsonlab.sadi.client.Registry;
import ca.wilkinsonlab.sadi.client.Service;

public class SadiManager implements IBioclipseManager {

    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "sadi";
    }

    public List<String> listServices() throws BioclipseException {
    	Registry registry = Config.getConfiguration().getMasterRegistry();
    	try {
    		List<String> serviceURIs = new ArrayList<String>();
			Collection<? extends Service> services = registry.getAllServices();
			for (Service service : services) {
				serviceURIs.add(service.getURI());
			}
			return serviceURIs;
		} catch (SADIException sadiException) {
			throw new BioclipseException(
				"Error contacting the SADI registry...", sadiException
			);
		}
    }
    
}
