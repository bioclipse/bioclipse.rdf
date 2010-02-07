/*******************************************************************************
 * Copyright (c) 2009-2010  Egon Willighagen <egonw@users.sf.net>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.rdf.ui.editors;

import org.eclipse.jface.viewers.LabelProvider;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;

public class RDFLabelProvider extends LabelProvider {

    public String getText(Object element) {
    	if (element instanceof Statement) {
    		Property pred = ((Statement)element).getPredicate();
    		return ":" + pred.getLocalName();
    	}

    	if (element instanceof Node)
    		return ((Node)element).getLocalName();

        return element.toString();
    }

}
