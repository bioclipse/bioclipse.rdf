/*******************************************************************************
 * Copyright (c) 2008-2009  Egon Willighagen <egonw@users.sf.net>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org�epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 * 
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.pellet;

import net.bioclipse.core.util.LogUtils;
import net.bioclipse.pellet.business.IJavaPelletManager;
import net.bioclipse.pellet.business.IJavaScriptPelletManager;

import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "net.bioclipse.pellet";

    private static final Logger logger = Logger.getLogger(Activator.class);

    private static Activator plugin;
    private ServiceTracker finderTracker;
    private ServiceTracker jsFinderTracker;
    
    public Activator() {}

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        finderTracker = new ServiceTracker(
            context, 
            IJavaPelletManager.class.getName(), 
            null
        );
        finderTracker.open();
        jsFinderTracker = new ServiceTracker(
            context, 
            IJavaScriptPelletManager.class.getName(), 
            null
        );
        jsFinderTracker.open();
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static Activator getDefault() {
        return plugin;
    }

    public IJavaPelletManager getJavaManager() {
        IJavaPelletManager manager = null;
        try {
            manager = (IJavaPelletManager)finderTracker.waitForService(1000*10);
        } catch (InterruptedException exception) {
            LogUtils.debugTrace(logger, exception);
            throw new IllegalStateException("Could not get the Pellet manager: " +
                exception.getMessage(), exception);
        }
        if (manager == null) {
            throw new IllegalStateException("Could not get the Pellet manager.");
        }
        return manager;
    }

    public IJavaScriptPelletManager getJavaScriptManager() {
        IJavaScriptPelletManager manager = null;
        try {
            manager = (IJavaScriptPelletManager)jsFinderTracker.waitForService(1000*10);
        } catch (InterruptedException exception) {
            LogUtils.debugTrace(logger, exception);
            throw new IllegalStateException("Could not get the Pellet manager: " +
                exception.getMessage(), exception);
        }
        if (manager == null) {
            throw new IllegalStateException("Could not get the Pellet manager.");
        }
        return manager;
    }
}
