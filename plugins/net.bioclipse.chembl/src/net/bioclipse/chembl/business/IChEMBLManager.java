/*******************************************************************************
 * Copyright (c) 2010  Egon Willighagen <egonw@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.chembl.business;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.rdf.model.IStringMatrix;

@PublishedClass(
    value="Manager to interact with ChEMBL."
)
public interface IChEMBLManager extends IBioclipseManager {

	@PublishedMethod(
		params="Integer targetID, String activity",
		methodSummary="Downloads the QSAR data for a certain target" +
			"and activity. Automatically removes entries with NaN values."
	)
	public Map<String, Double> getQSARData(Integer targetID, String activity)
	throws BioclipseException;

	@PublishedMethod(
		params="Integer targetID",
		methodSummary="Returns the available activities."
	)
	public List<String> getActivities(Integer targetID)
	throws BioclipseException;

	@PublishedMethod(
		params="Integer targetID",
		methodSummary="Returns the properties of the given target." +
			"It returns 'organism', 'title', and 'type'."
	)
	public IStringMatrix getProperties(Integer targetID)
	throws BioclipseException;
	 @PublishedMethod(
	            params="Integer targetID",
	            methodSummary="Returns the sequence for a target."
	    )
	    public List<String> getTargetSequence(Integer targetID)
	    throws BioclipseException;

	    @PublishedMethod(
	            params="String activity, String ion, String vgc",
	            methodSummary="Returns target id, pubmed id, smiles, classifications"
	    )
	    public IStringMatrix getPCM(String activity, String classL6, String classL3)
	    throws BioclipseException;

	    @PublishedMethod(
	            params="Integer targetID, String activity",
	            methodSummary="Returns smiles, activity values, confidence score. If no specific activity write \"\" "
	    )
	    public IStringMatrix getQSARData2(Integer targetID, String activity)
	    throws BioclipseException;

	    @PublishedMethod(
	            params="Integer targetID",
	            methodSummary="Returns properties of a target protein"  )
	    public IStringMatrix getProteinData(Integer targetID) throws BioclipseException;
	    
	    @PublishedMethod(
	            params="Integer molID",
	            methodSummary="Returns chebi id"
	    )
	    public String getChebiId(Integer molID)
	    throws BioclipseException;

	    @PublishedMethod(
	            params="String proteinSeq",
	            methodSummary="Returns target id"
	    )
	    public List<String> getTargetIDWithProteinSeq(String proteinSeq)
	    throws BioclipseException;
	    
	    @PublishedMethod(
	            params="String EC ID",
	            methodSummary="Returns target id"
	    )
	    public List<String> getTargetIDWithEC(String ecNumber)
	    throws BioclipseException;
	    
	    @PublishedMethod(
	            params="String key word",
	            methodSummary="Returns target id. Search description from ChEMBL."
	    )
	    public IStringMatrix getTargetIDWithKeyword(String keyword)
	    throws BioclipseException;
	    
	    @PublishedMethod(
	            params="String chebiID",
	            methodSummary="Takes chebiID or moleculeID and returns information about the compound."
	    )
	    public IStringMatrix getCompoundInfo(Integer chebiID)
	    throws BioclipseException;
	   
	    @PublishedMethod(
	            params="String molID",
	            methodSummary="Takes moleculeIDs(\"1,2,3\") and returns their chebi ids."
	    )
	    public List<String> getChebiId(String chebiID)
	    throws BioclipseException;
	    
	    @PublishedMethod(
	            params="String molID",
	            methodSummary="Takes moleculeIDs(\"1,2,3\") and returns their chebi ids."
	    )
	    public List<String> getCompoundInfo(String chebiID)
	    throws BioclipseException;
	    
	    @PublishedMethod(
	    		params ="String smiles",
	    		methodSummary = "Returns information about a compound given a SMILES,"+
	    		"OBS! its a slow run"
	    			)
	    			public IStringMatrix getCompoundInfoWithSmiles(String smiles)
	throws BioclipseException;
	    @PublishedMethod(
	    		params ="String smiles",
	    		methodSummary = "Returns information about a compound given a SMILES,"+
	    		"OBS! its a slow run"
	    			)
	    			public IStringMatrix getCompoundInfoWithKeyword(String smiles)
	throws BioclipseException;
	    
	    @PublishedMethod(
	        params = "String file, StringMatrix matrix",
	        methodSummary = ""
	    )
	    public void saveCSV(String file, IStringMatrix s)
	    throws BioclipseException, IOException ;
	    
	    @PublishedMethod(
		        params = "String family, String activityType, Integer limit",
		        methodSummary = "Collects compound for a MoSS run given protein family and" +
		        		"activity type. Limit your search by adding a limit number."
		    )
	    public IStringMatrix MossProtFamilyCompounds(String fam, String actType, int limit) 
	    throws BioclipseException;
			

	    @PublishedMethod(
		        params = "String family",
		        methodSummary = "Returns available activities for a protein family"
		    )
	    public List<String> MossAvailableActivities(String fam) throws BioclipseException;
}
