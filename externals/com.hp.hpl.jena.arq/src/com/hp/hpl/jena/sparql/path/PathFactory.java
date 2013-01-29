/*
 * (c) Copyright 2008, 2009 Hewlett-Packard Development Company, LP
 * (c) Copyright 2010 Epimorphics Ltd.
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.path;

import com.hp.hpl.jena.graph.Node ;

public class PathFactory
{
    public static final long UNSET = P_Mod.UNSET ;
    
    public static Path pathLink(Node property)          { return new P_Link(property) ; }

    public static Path pathInverse(Path path)           { return new P_Inverse(path) ; }
    public static Path pathMod(Path path, long min, long max)   { return new P_Mod(path, min, max) ; }
    public static Path pathFixedLength(Path path, long count)   { return new P_FixedLength(path, count) ; }
    

    public static Path pathAlt(Path path1, Path path2)  { return new P_Alt(path1, path2) ; }
    public static Path pathSeq(Path path1, Path path2)  { return new P_Seq(path1, path2) ; }
    
    public static Path pathZeroOrMore(Path path)        { return new P_ZeroOrMore(path) ; }
    public static Path pathZeroOrOne(Path path)         { return new P_ZeroOrOne(path) ; }
    public static Path pathOneOrMore(Path path)         { return new P_OneOrMore(path) ; }
}

/*
 * (c) Copyright 2008, 2009 Hewlett-Packard Development Company, LP
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