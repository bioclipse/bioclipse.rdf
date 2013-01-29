/*
 * (c) Copyright 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.mgt;

import com.hp.hpl.jena.graph.Node ;
import com.hp.hpl.jena.n3.IRIResolver ;


public class SystemInfo implements SystemInfoMBean
{
    private final String name ;
    private final Node   iri ;
    private final String version ;
    private final String buildDate ;

    public SystemInfo(String name, String version, String buildDate)
    {
        this.name = name ;
        this.iri = createIRI(name) ;
        this.version = version ;
        this.buildDate = buildDate ;
    }
    
    private static Node createIRI(String iriStr)
    {
        try {
            return Node.createURI(IRIResolver.resolveGlobal(iriStr)) ;
        } catch (RuntimeException ex) { return null ; }
    }
        
    public String getBuildDate()
    {
        return buildDate ;
    }

    public String getVersion()
    {
        return version ;
    }

    public String getName()
    { 
        return name ;
    }
    
    public Node getIRI()
    { 
        return Node.createURI(name) ;
    }
}

/*
 * (c) Copyright 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */