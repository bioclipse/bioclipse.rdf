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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.bioclipse.core.ResourcePathTransformer;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.rdf.Activator;
import net.bioclipse.scripting.ui.business.IJsConsoleManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RDFManager implements IRDFManager {

    public String getNamespace() {
        return "rdf";
    }

    public void importFile(String target) throws IOException,
            BioclipseException, CoreException {
        importFile(
            ResourcePathTransformer.getInstance().transform(target),
            null
        );        
    }

    public void importFile(IFile target, IProgressMonitor monitor)
            throws IOException, BioclipseException, CoreException {
        importFromStream(target.getContents(), monitor);
    }

    public void importFromStream(InputStream stream, IProgressMonitor monitor)
            throws IOException, BioclipseException, CoreException {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }

        Model model = Activator.getModel();
        model.read(stream, "");
    }

    public void importURL(String url) throws IOException, BioclipseException,
            CoreException {
        importURL(url, null);
    }

    public void importURL(String url, IProgressMonitor monitor)
            throws IOException, BioclipseException, CoreException {
        URL realURL = new URL(url);
        importFromStream(realURL.openStream(), monitor);
    }

    public void dump() throws IOException, BioclipseException, CoreException {
        IJsConsoleManager js = net.bioclipse.scripting.ui.Activator
            .getDefault().getJsConsoleManager();
        
        Model model = Activator.getModel();
        
        StmtIterator statements = model.listStatements();
        while (statements.hasNext()) {
            Statement statement = statements.nextStatement();
            RDFNode object = statement.getObject();
            js.print(
                 statement.getSubject().getLocalName() + " " +
                 statement.getPredicate().getLocalName() + " " +
                 (object instanceof Resource ?
                      object.toString() :
                      "\"" + object.toString() + "\"") +
                 "\n"
            );
        }
        
    }
    
}
