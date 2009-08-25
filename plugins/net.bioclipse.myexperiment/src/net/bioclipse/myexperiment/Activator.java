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
package net.bioclipse.myexperiment;

import net.bioclipse.myexperiment.business.IMyExperimentManager;
import net.bioclipse.myexperiment.business.IJavaMyExperimentManager;
import net.bioclipse.myexperiment.business.IJavaScriptMyExperimentManager;

import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The Activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    private static final Logger logger = Logger.getLogger(Activator.class);
    
    // The shared instance
    private static Activator plugin;

    // Trackers for getting the managers
    private ServiceTracker javaFinderTracker;
    private ServiceTracker jsFinderTracker;

    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        javaFinderTracker 
            = new ServiceTracker( context, 
                                  IJavaMyExperimentManager.class.getName(), 
                                  null );
        
        javaFinderTracker.open();
        jsFinderTracker 
            = new ServiceTracker( context, 
                                  IJavaScriptMyExperimentManager.class.getName(), 
                                  null );
                                    
        jsFinderTracker.open();
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    public IMyExperimentManager getJavaMyExperimentManager() {
        IMyExperimentManager manager = null;
        try {
            manager = (IMyExperimentManager) 
                      javaFinderTracker.waitForService(1000*10);
        } 
        catch (InterruptedException e) {
            throw new IllegalStateException(
                          "Could not get the Java MyExperimentManager",
                          e );
        }
        if (manager == null) {
            throw new IllegalStateException(
                          "Could not get the Java MyExperimentManager");
        }
        return manager;
    }
    
    public IJavaScriptMyExperimentManager getJavaScriptMyExperimentManager() {
        IJavaScriptMyExperimentManager manager = null;
        try {
            manager = (IJavaScriptMyExperimentManager) 
                      jsFinderTracker.waitForService(1000*10);
        } 
        catch (InterruptedException e) {
            throw new IllegalStateException(
                          "Could not get the JavaScript MyExperimentManager",
                          e );
        }
        if (manager == null) {
            throw new IllegalStateException(
                          "Could not get the JavaScript MyExperimentManager");
        }
        return manager;
    }
}
