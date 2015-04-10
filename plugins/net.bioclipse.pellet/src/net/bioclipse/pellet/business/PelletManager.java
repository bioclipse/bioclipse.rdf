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
package net.bioclipse.pellet.business;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.StringMatrix;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.rdf.StringMatrixHelper;
import net.bioclipse.rdf.business.IJenaStore;
import net.bioclipse.rdf.business.IRDFStore;
import net.bioclipse.scripting.ui.business.IJsConsoleManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.mindswap.pellet.exceptions.InternalReasonerException;
import org.mindswap.pellet.exceptions.UnsupportedFeatureException;
import org.mindswap.pellet.jena.PelletQueryExecution;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.shared.PrefixMapping;

public class PelletManager implements IBioclipseManager {

    public String getManagerName() {
        return "pellet";
    }

    public StringMatrix reason(IRDFStore store, String queryString)
        throws IOException, BioclipseException, CoreException {
        StringMatrix matrix = new StringMatrix();

        if (!(store instanceof IJenaStore))
            throw new RuntimeException(
                "Can only handle IJenaStore's for now."
            );
        Model model = ((IJenaStore)store).getModel();

        Query query = null;
        try {
            query = QueryFactory.create(queryString);
        } catch (QueryParseException exception) {
            throw new BioclipseException(
                "SPARQL syntax error: " + exception.getMessage()
            );
        }
        if (!query.isSelectType()) {
            throw new UnsupportedFeatureException(
                "Only SELECT queries are supported."
            );
        }

        PrefixMapping prefixMap = query.getPrefixMapping();
        OntModel ontModel = null;
        if (model instanceof OntModel) {
            ontModel = (OntModel)model;
        } else {
            ontModel = ModelFactory.createOntologyModel(
                PelletReasonerFactory.THE_SPEC,
                model
            );
        }
        ontModel.setStrictMode( false );
        if( query.getGraphURIs().size() != 0 ) {
            Iterator<String> queryURIs = query.getGraphURIs().iterator();
            while (queryURIs.hasNext()) {
                ontModel.read( queryURIs.next() );
            }
        }

        QueryExecution qexec = new PelletQueryExecution(query, ontModel);
        try {
            ResultSet results = qexec.execSelect();
            matrix = StringMatrixHelper.convertIntoTable(
            	prefixMap, results
            );
        } finally {
            qexec.close();
        }
        return matrix;
    }

    public IRDFStore createInMemoryStore() {
        return new PelletInMemoryModel();
    }

    public IRDFStore createStore(IFile tripleStoreDirectoryPath) {
        String tripleStoreDirectoryPathFull = tripleStoreDirectoryPath.getRawLocation().toString();
        return new PelletModel(tripleStoreDirectoryPathFull);
    }

    public void validate(IRDFStore store) throws IOException,
            BioclipseException, CoreException {
        IJsConsoleManager js = net.bioclipse.scripting.ui.Activator
            .getDefault().getJavaJsConsoleManager();

        Reasoner reasoner = PelletReasonerFactory.theInstance().create();

        if (!(store instanceof IJenaStore))
            throw new RuntimeException(
                "Can only handle IJenaStore's for now."
            );
        
        Model model = ((IJenaStore)store).getModel();

        // create an inferencing model using Pellet reasoner
        InfModel infModel = ModelFactory.createInfModel(
            reasoner, model
        );
        try {
            ValidityReport overallReport = infModel.validate();
            if (overallReport.isValid()) {
                js.print("Model is valid.");
            } else {
                js.print("Validation Results");
                Iterator<ValidityReport.Report> reports =
                    overallReport.getReports();
                while (reports.hasNext()) {
                    ValidityReport.Report report = reports.next();
                    js.print(report.getType() + ": " + report.getDescription());
                }
            }
        } catch (InternalReasonerException exception) {
            js.print("Model is invalid: " + exception.getMessage());
        }
    }

    public StringMatrix isRDFType(IRDFStore store, String type)
        throws IOException, BioclipseException, CoreException {
    	return reason(
    			store,
    			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
    			"SELECT ?o WHERE { "+
    			"  ?o rdf:type <" + type + ">." +
    			"}"
    	);
    }

    public List<String> getRDFType(IRDFStore store, String type)
        throws IOException, BioclipseException, CoreException {
    	return reason(
        	store,
        	"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
        	"SELECT ?o WHERE { "+
        	"  ?o rdf:type <" + type + ">." +
        	"}"
        ).getColumn("o");
    }

    public StringMatrix allAbout(IRDFStore store, String identifier)
        throws BioclipseException, IOException, CoreException {
        return reason(
            store,
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
            "SELECT ?p ?s WHERE { "+
            "<" + identifier + "> ?p ?s." +
            "}"
        );
    }
}
