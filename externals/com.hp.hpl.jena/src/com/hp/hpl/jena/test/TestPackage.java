/*
  (c) Copyright 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: TestPackage.java,v 1.48 2009/03/30 13:15:27 andy_seaborne Exp $
*/

package com.hp.hpl.jena.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.hp.hpl.jena.assembler.test.TestAssemblerPackage;

/**
 * All developers should edit this file to add their tests.
 * Please try to name your tests and test suites appropriately.
 * Note, it is better to name your test suites on creation
 * rather than in this file.
 * @author  jjc
 */
public class TestPackage extends TestCase{

    static public TestSuite suite() {
        TestSuite ts = new TestSuite() ;
        ts.setName("Jena") ;
        addTest(ts,  "Enhanced", com.hp.hpl.jena.enhanced.test.TestPackage.suite());
        addTest(ts,  "Graph", com.hp.hpl.jena.graph.test.TestPackage.suite());
        addTest(ts,  null, com.hp.hpl.jena.mem.test.TestMemPackage.suite() );
        addTest(ts,  "Model", com.hp.hpl.jena.rdf.model.test.TestPackage.suite());
        addTest(ts,  "N3", com.hp.hpl.jena.n3.test.N3TestSuite.suite());
        addTest(ts,  "Turtle", com.hp.hpl.jena.n3.turtle.test.TurtleTestSuite.suite()) ;

        // Avoid a compile time dependency on ARQ.
        {
            TestSuite arqSuite = TestPackageARQ.suite() ;
            if ( arqSuite != null )
                addTest(ts, "ARQ", arqSuite) ;
            else
                System.err.println("ARQ test suite not run") ;
        }
        addTest(ts,  "XML Output", com.hp.hpl.jena.xmloutput.test.TestPackage.suite());
        addTest(ts,  "Util", com.hp.hpl.jena.util.test.TestPackage.suite());
        addTest(ts,  "Jena iterator", com.hp.hpl.jena.util.iterator.test.TestPackage.suite() );
        addTest(ts,  "Mega", com.hp.hpl.jena.regression.MegaTestSuite.suite());
        addTest(ts,  "Assembler", TestAssemblerPackage.suite() );
        addTest(ts,  null, com.hp.hpl.jena.rdf.arp.test.TestPackage.suite());
        addTest(ts,  "Vocabularies", com.hp.hpl.jena.vocabulary.test.TestVocabularies.suite() );
        addTest(ts,  null, com.hp.hpl.jena.shared.test.TestSharedPackage.suite() );
        addTest(ts,  "Reasoners", com.hp.hpl.jena.reasoner.test.TestPackage.suite());
        addTest(ts,  "Composed graphs", com.hp.hpl.jena.graph.compose.test.TestPackage.suite() );
        addTest(ts,  "Ontology", com.hp.hpl.jena.ontology.impl.test.TestPackage.suite() );
        addTest(ts,  "cmd line utils", jena.test.TestPackage.suite() );
        return ts ;
    }

    private static void addTest(TestSuite ts, String name, TestSuite tc) {
        if ( name != null )
            tc.setName(name);
        ts.addTest(tc);
    }

}

/*
    (c) Copyright 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:

    1. Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.

    2. Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.

    3. The name of the author may not be used to endorse or promote products
       derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/