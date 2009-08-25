/*******************************************************************************
 * Copyright (c) 2009 Egon Willighagen <egonw@users.sf.net> 
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.myexperiment.business;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(
    value="MyExperiment manager."
)
public interface IMyExperimentManager extends IBioclipseManager {

    @Recorded
    @PublishedMethod(
        params="Integer workflowNumber",
        methodSummary="Download a single workflow from MyExperiment."
    )
    public String downloadWorkflow(Integer workflowNumber)
    throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params="Integer workflowNumber, String filename",
        methodSummary="Download a single workflow from MyExperiment into the " +
        		"file."
    )
    public String downloadWorkflow(Integer workflowNumber, String filename)
    throws BioclipseException;

}
