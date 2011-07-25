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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphContentProvider;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RDFContentProvider implements IGraphContentProvider {

	private Model model;
    
    public void setModel(Model model) {
        this.model = model;
    }
    
    public Object getDestination( Object rel ) {
        if (rel instanceof Statement) {
        	RDFNode resource = ((Statement)rel).getObject();
        	if (resource.isLiteral())
        		return (Literal)resource;
        	if (resource.isResource()) {
        		Resource res = (Resource)resource;
            	if (res.getURI() != null &&
            		!res.getURI().startsWith("http://www.w3.org/"))
            		return resource; 
        	}
        	return ((Statement)rel).getObject();
        }
        return "unknown object";
    }

    public Object[] getElements( Object input ) {
        List<Object> elements = new ArrayList<Object>();
        StmtIterator iter = model.listStatements();
        while (iter.hasNext()) {
        	Statement stmt = iter.next();
        	Resource subject = stmt.getSubject();
        	if (subject.getURI() != null &&
        		!subject.getURI().startsWith("http://www.w3.org/"))
        		elements.add(stmt);
        }
//        System.out.println(elements);
        return elements.toArray();
    }

    public Object getSource( Object rel ) {
        if (rel instanceof Statement) {
        	Resource resource = ((Statement)rel).getSubject();
        	if (!resource.getURI().startsWith("http://www.w3.org/"))
        		return resource;
        } else {
        	System.out.println("NOT A STATEMENT");
        }
        return "unknown subject";
    }

    public void dispose() {
        model = null;
    }

    public void inputChanged( Viewer viewer, Object oldInput, Object newInput ) {
    }

}
