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

import net.bioclipse.pellet.Activator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExecutableExtensionFactory;

public class PelletManagerFactory implements IExecutableExtension, 
                                          IExecutableExtensionFactory {

    private Object manager;
    
    public void setInitializationData( IConfigurationElement config,
                                       String propertyName, 
                                       Object data) throws CoreException {
        
        manager = Activator.getDefault().getJavaScriptManager();
        
        if (manager == null ) {
            manager = new Object();
        }
    }

    public Object create() throws CoreException {
        return manager;
    }
}
