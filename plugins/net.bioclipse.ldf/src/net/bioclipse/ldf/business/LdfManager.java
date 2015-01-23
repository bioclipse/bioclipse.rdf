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

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.rdf.business.IRDFStore;
import net.bioclipse.rdf.business.JenaModel;

import org.apache.log4j.Logger;
import org.linkeddatafragments.model.LinkedDataFragmentGraph;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class LdfManager implements IBioclipseManager {

    private static final Logger logger = Logger.getLogger(LdfManager.class);

    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "ldf";
    }

    public IRDFStore createStore(String provider) throws BioclipseException {
    	logger.debug("Creating a Jena model with LDF for: " + provider);
    	LinkedDataFragmentGraph ldfg = new LinkedDataFragmentGraph(provider);
    	Model model = ModelFactory.createModelForGraph(ldfg);
    	return new JenaModel(model);
    }
    
    
}
