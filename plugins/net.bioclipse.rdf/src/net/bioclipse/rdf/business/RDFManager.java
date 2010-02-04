/*******************************************************************************
 * Copyright (c) 2009  Egon Willighagen <egonw@users.sf.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.rdf.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.bioclipse.core.ResourcePathTransformer;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

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
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.PrefixMapping;

public class RDFManager implements IBioclipseManager {

    public String getManagerName() {
        return "rdf";
    }
    public IRDFStore importFile(IRDFStore store, IFile target, String format,
            IProgressMonitor monitor)
    throws IOException, BioclipseException, CoreException {
        return importFromStream(store, target.getContents(), format, monitor);
    }

    public IRDFStore importFromStream(IRDFStore store, InputStream stream,
            String format, IProgressMonitor monitor)
    throws IOException, BioclipseException, CoreException {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (format == null) format = "RDF/XML";

        if (!(store instanceof IJenaStore))
            throw new RuntimeException(
                "Can only handle IJenaStore's for now."
            );
        
        Model model = ((IJenaStore)store).getModel();
        model.read(stream, "", format);
        return store;
    }

    public IRDFStore importURL(IRDFStore store, String url, IProgressMonitor monitor)
            throws IOException, BioclipseException, CoreException {
        URL realURL = new URL(url);
        URLConnection connection = realURL.openConnection();
        connection.setRequestProperty(
            "Accept",
            "application/xml, application/rdf+xml"
        );
        importFromStream(store, connection.getInputStream(), null, monitor);
        return store;
    }

    public String dump(IRDFStore store) {
        if (!(store instanceof IJenaStore))
            throw new RuntimeException(
                "Can only handle IJenaStore's for now."
            );

        Model model = ((IJenaStore)store).getModel();
        StringBuffer dump = new StringBuffer();

        StmtIterator statements = model.listStatements();
        while (statements.hasNext()) {
            Statement statement = statements.nextStatement();
            RDFNode object = statement.getObject();
            dump.append(statement.getSubject().getLocalName())
                .append(' ')
                .append(statement.getPredicate().getLocalName())
                .append(' ')
                .append((object instanceof Resource ?
                     object.toString() : '"' + object.toString() + "\""))
                .append('\n');
        }
        return dump.toString();
    }

    public List<List<String>> sparql(IRDFStore store, String queryString) throws IOException, BioclipseException,
    CoreException {
        if (!(store instanceof IJenaStore))
            throw new RuntimeException(
                "Can only handle IJenaStore's for now."
            );

        List<List<String>> table = null;
        Model model = ((IJenaStore)store).getModel();
        Query query = QueryFactory.create(queryString);
        PrefixMapping prefixMap = query.getPrefixMapping();
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            table = convertIntoTable(prefixMap, results);
        } finally {
            qexec.close();
        }
        return table;
    }

    private List<List<String>> convertIntoTable(
            PrefixMapping prefixMap, ResultSet results) {
        List<List<String>> table = new ArrayList<List<String>>();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            List<String> row = new ArrayList<String>();
            Iterator<String> varNames = soln.varNames();
            while (varNames.hasNext()) {
                RDFNode node = soln.get(varNames.next());
                if (node != null) {
                    String nodeStr = node.toString();
                    if (node.isResource()) {
                        Resource resource = (Resource)node;
                        // the resource.getLocalName() is not accurate, so I
                        // use some custom code
                        String[] uriLocalSplit = split(prefixMap, resource);
                        if (uriLocalSplit[0] == null) {
                            row.add(resource.getURI());
                        } else {
                            row.add(
                                uriLocalSplit[0] + ":" + uriLocalSplit[1]
                            );
                        }
                    } else {
                        row.add(nodeStr);
                    }
                }
            }
            table.add(row);
        }
        return table;
    }

    /**
     * Helper method that splits up a URI into a namespace and a local part.
     * It uses the prefixMap to recognize namespaces, and replaces the
     * namespace part by a prefix.
     *
     * @param prefixMap
     * @param resource
     */
    public static String[] split(PrefixMapping prefixMap, Resource resource) {
        String uri = resource.getURI();
        if (uri == null) {
            return new String[] {null, null};
        }
        Map<String,String> prefixMapMap = prefixMap.getNsPrefixMap();
        Set<String> prefixes = prefixMapMap.keySet();
        String[] split = { null, null };
        for (String key : prefixes){
            String ns = prefixMapMap.get(key);
            if (uri.startsWith(ns)) {
                split[0] = key;
                split[1] = uri.substring(ns.length());
                return split;
            }
        }
        split[1] = uri;
        return split;
    }

    public IRDFStore createInMemoryStore() {
        return new JenaModel();
    }

    public IRDFStore createStore(String tripleStoreDirectoryPath) {
    	return new TDBModel(tripleStoreDirectoryPath);
    }

    public void addObjectProperty(IRDFStore store,
        String subject, String property, String object)
        throws BioclipseException {
        if (!(store instanceof IJenaStore))
            throw new RuntimeException(
                "Can only handle IJenaStore's for now."
            );
        Model model = ((IJenaStore)store).getModel();
        Resource subjectRes = model.createResource(subject);
        Property propertyRes = model.createProperty(property);
        Resource objectRes = model.createResource(object);
        model.add(subjectRes, propertyRes, objectRes);
    }

    public void addDataProperty(IRDFStore store, String subject,
            String property, String value) throws BioclipseException {
        if (!(store instanceof IJenaStore))
            throw new RuntimeException(
                "Can only handle IJenaStore's for now."
            );
        Model model = ((IJenaStore)store).getModel();
        Resource subjectRes = model.createResource(subject);
        Property propertyRes = model.createProperty(property);
        model.add(subjectRes, propertyRes, value);
    }

    public long size(IRDFStore store) throws BioclipseException {
        if (!(store instanceof IJenaStore))
            throw new RuntimeException(
                "Can only handle IJenaStore's for now."
            );
        Model model = ((IJenaStore)store).getModel();
        return model.size();
    }

    public void saveRDFXML(IRDFStore store, String fileName)
        throws BioclipseException {
        IFile file = ResourcePathTransformer.getInstance().transform(fileName);
        saveRDFXML(store, file, null);
    };

    public IFile saveRDFXML(IRDFStore store, IFile file,
                            IProgressMonitor monitor)
        throws BioclipseException {
        return saveRDF(store, file, "RDF/XML-ABBREV", monitor);
    }

    public void saveRDFN3(IRDFStore store, String fileName)
    throws BioclipseException {
        IFile file = ResourcePathTransformer.getInstance().transform(fileName);
        saveRDFN3(store, file, null);
    };

    public IFile saveRDFN3(IRDFStore store, IFile file,
            IProgressMonitor monitor)
    throws BioclipseException {
        return saveRDF(store, file, "N3", monitor);
    }

    public void saveRDFNTriple(IRDFStore store, String fileName)
        throws BioclipseException {
        IFile file = ResourcePathTransformer.getInstance().transform(fileName);
        saveRDFNTriple(store, file, null);
    };

    public IFile saveRDFNTriple(IRDFStore store, IFile file,
            IProgressMonitor monitor)
        throws BioclipseException {
        return saveRDF(store, file, "N-TRIPLE", monitor);
    }

    public IFile saveRDF(IRDFStore store, IFile file,
                         String type,
                         IProgressMonitor monitor)
        throws BioclipseException {

        if (type == null && !"RDF/XML-ABBREV".equals(type) &&
                            !"N-TRIPLE".equals(type) &&
                            !"N3".equals(type))
            throw new BioclipseException("Can only save RDF/XML-ABBREV, N3, " +
            		"and N-TRIPLE.");

        if (file.exists()) {
            throw new BioclipseException("File already exists!");
        }
        if (monitor == null)
            monitor = new NullProgressMonitor();
        monitor.beginTask("Writing file", 100);

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            if (store instanceof IJenaStore) {
                Model model = ((IJenaStore)store).getModel();
                model.write(output, type);
                output.close();
                file.create(
                        new ByteArrayInputStream(output.toByteArray()),
                        false,
                        monitor
                );
            } else {
                monitor.worked(100);
                monitor.done();
                throw new BioclipseException("Only supporting IJenaStore!");
            }
        } catch (CoreException e) {
            monitor.worked(100);
            monitor.done();
            throw new BioclipseException("Error while writing RDF.", e);
        } catch (IOException e) {
            monitor.worked(100);
            monitor.done();
            throw new BioclipseException("Error while writing RDF.", e);
        }

        monitor.worked(100);
        monitor.done();
        return file;
    };

    public void copy(IRDFStore targetStore, IRDFStore sourceStore)
        throws BioclipseException {
        if (!(targetStore instanceof IJenaStore &&
              sourceStore instanceof IJenaStore)) {
            throw new BioclipseException("Only supporting IJenaStore.");
        }
        ((IJenaStore)targetStore).getModel().add(
            ((IJenaStore)sourceStore).getModel()
        );
    }

    public void addPrefix(IRDFStore store, String prefix, String namespace)
        throws BioclipseException {
        if (!(store instanceof IJenaStore)) {
            throw new BioclipseException("Only supporting IJenaStore.");
        }
        ((IJenaStore)store).getModel().setNsPrefix(prefix, namespace);
    }

    public List<List<String>> sparqlRemote(
           String serviceURL,
           String sparqlQueryString, IProgressMonitor monitor) {
        if (monitor == null)
            monitor = new NullProgressMonitor();

        monitor.beginTask("Sparqling the remote service..", 100);
        Query query = QueryFactory.create(sparqlQueryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURL, query);
        PrefixMapping prefixMap = query.getPrefixMapping();
        monitor.worked(80);

        List<List<String>> table = null;
        try {
            ResultSet results = qexec.execSelect();
            table = convertIntoTable(prefixMap, results);
        } finally {
            qexec.close();
        }
        monitor.worked(20);
        return table;
    }
    
    public IRDFStore importRDFa(IRDFStore store, String url,
            IProgressMonitor monitor)
    throws IOException, BioclipseException, CoreException {
        new URL(url);
        // if no exception was thrown,
        // than the given url seems OK
        
        String pyrdfaURL = "http://www.w3.org/2007/08/pyRdfa/extract?uri="
            + url;
        return importURL(store, pyrdfaURL, monitor);
    }
    
}
