/*
 * (c) Copyright 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * (c) Copyright 2010 Epimorphics Ltd.
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.util;

import java.util.HashMap ;
import java.util.Iterator ;
import java.util.Map ;

/** Maps string to string for use with convenience names.
 */

public class TranslationTable<X extends Symbol>
{
    Map<String, X> map = new HashMap<String, X>() ;
    boolean ignoreCase = false ;
    
    /** Create a translation table which respects case */
    
    public TranslationTable() { this(false) ; }
    
    /** Create a translation table - say whether to ignore case or not */ 
    public TranslationTable(boolean ignoreCase) { this.ignoreCase = ignoreCase ; } 
    
    public X lookup(String name)
    {
        if ( name == null )
            return null ;

        for ( Iterator<Map.Entry<String, X>> iter = map.entrySet().iterator() ; iter.hasNext() ; )
        {
            Map.Entry<String, X> entry = iter.next() ;
            String k = entry.getKey() ;
            if ( ignoreCase )
            {                
                if ( k.equalsIgnoreCase(name) )
                    return entry.getValue() ;
            }
            else
            {
                if ( k.equals(name) )
                    return entry.getValue() ;
            }
        }
        return null ;
    }
    
    public void put(String k, X v)
    {
        map.put(k, v) ;
    }
    
    public Iterator<String> keys() { return map.keySet().iterator() ; }
    public Iterator<X> values() { return map.values().iterator() ; }
}

/*
 * (c) Copyright 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * (c) Copyright 2010 Epimorphics Ltd.
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