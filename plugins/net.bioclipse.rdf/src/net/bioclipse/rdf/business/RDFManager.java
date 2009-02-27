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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.bioclipse.core.ResourcePathTransformer;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.scripting.ui.business.IJsConsoleManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.PrefixMapping;

public class RDFManager implements IRDFManager {

    public String getNamespace() {
        return "rdf";
    }

    public IRDFStore importFile(IRDFStore store, String target) throws IOException,
            BioclipseException, CoreException {
        return importFile(
            store,
            ResourcePathTransformer.getInstance().transform(target),
            null
        );        
    }

    public IRDFStore importFile(IRDFStore store, IFile target, IProgressMonitor monitor)
            throws IOException, BioclipseException, CoreException {
        return importFromStream(store, target.getContents(), monitor);
    }

    public IRDFStore importFromStream(IRDFStore store, InputStream stream, IProgressMonitor monitor)
            throws IOException, BioclipseException, CoreException {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }

        Model model = ((JenaModel)store).getModel();
        model.read(stream, "");
        return store;
    }

    public IRDFStore importURL(IRDFStore store, String url) throws IOException, BioclipseException,
            CoreException {
        return importURL(store, url, null);
    }

    public IRDFStore importURL(IRDFStore store, String url, IProgressMonitor monitor)
            throws IOException, BioclipseException, CoreException {
        URL realURL = new URL(url);
        importFromStream(store, realURL.openStream(), monitor);
        return store;
    }

    public void dump(IRDFStore store) throws IOException, BioclipseException, CoreException {
        IJsConsoleManager js = net.bioclipse.scripting.ui.Activator
            .getDefault().getJsConsoleManager();
        
        Model model = ((JenaModel)store).getModel();
        
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

    public List<List<String>> sparql(IRDFStore store, String queryString) throws IOException, BioclipseException,
            CoreException {
        List<List<String>> table = new ArrayList<List<String>>();

        Model model = ((JenaModel)store).getModel();

        Query query = QueryFactory.create(queryString);
        PrefixMapping prefixMap = query.getPrefixMapping();
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                List<String> row = new ArrayList<String>();
                Iterator<String> varNames = soln.varNames();
                while (varNames.hasNext()) {
                    RDFNode node = soln.get(varNames.next());
                    String nodeStr = node.toString();
                    if (node.isResource()) {
                        Resource resource = (Resource)node;
                        // the resource.getLocalName() is not accurate, so I
                        // use some custom code
                        String[] uriLocalSplit = split(prefixMap, resource);
                        if (uriLocalSplit[0] == null) {
                            row.add(resource.getURI());
                        } else {
                            row.add(uriLocalSplit[0] + ":" + uriLocalSplit[1]);
                        }
                    } else {
                        row.add(nodeStr);
                    }
                }
                table.add(row);
            }
        } finally {
            qexec.close();
        }
        return table;
    }

    private String[] split(PrefixMapping prefixMap, Resource resource) {
        String uri = resource.getURI();
        Set<String> prefixes = prefixMap.getNsPrefixMap().keySet();
        String[] split = { null, null };
        for (String key : prefixes){
            if (uri.startsWith(key)) {
                split[0] = key;
                split[1] = uri.substring(key.length());
                return split;
            }
        }
        split[1] = uri;
        return split;
    }

    public IRDFStore createStore() {
        return new JenaModel();
    }

}
