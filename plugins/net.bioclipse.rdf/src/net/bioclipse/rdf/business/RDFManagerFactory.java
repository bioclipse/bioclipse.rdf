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
package net.bioclipse.rdf.business;

import net.bioclipse.rdf.Activator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExecutableExtensionFactory;

public class RDFManagerFactory implements IExecutableExtension, 
                                          IExecutableExtensionFactory {

    private Object rdfManager;
    
    public void setInitializationData( IConfigurationElement config,
                                       String propertyName, 
                                       Object data) throws CoreException {
        
        rdfManager = Activator.getDefault().getJavaScriptManager();
        
        if (rdfManager == null ) {
            rdfManager = new Object();
        }
    }

    public Object create() throws CoreException {
        return rdfManager;
    }
}
