/*
 * (c) Copyright 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package atlas.lib;

import static java.lang.String.format;

import java.util.Arrays;
import java.util.List;



/** General descriptor of a reorderring (mapping) of columns in tuples to columns in indexes, 
 * for example, from triples to triple index order. 
 * @author Andy Seaborne
 */
public class ColumnMap
{
    // Map from tuple order to index order
    // So SPO->POS is (0->2, 1->0, 2->1)
    // i.e. the location of the element after mapping.  
    private int[] insertOrder ;            
    
    // The mapping from index to tuple order
    // For POS->SPO, is (0->1, 1->2, 2->0)
    // i.e. the location to fetch the mapped element from. 
    private int[] fetchOrder ;

    private String label ;

    /** Construct a column mapping that maps the input (one col, one char) to the output */  
    public ColumnMap(String input, String output)
    {
        this(input+"->"+output, compileMapping(input, output)) ;
    }
    
    public <T> ColumnMap(String label, List<T> input, List<T> output)
    {
        this(label, compileMapping(input, output)) ;
    }
    
    public <T> ColumnMap(String label, T[] input, T[] output)
    {
        this(label, compileMapping(input, output)) ;
    }
    
    /** Construct a column map - the elements are the 
     * mappings of a tuple originally in the order 0,1,2,...
     * so SPO->POS is 2,0,1 (SPO->POS so S->2, P->0, O->1)   
     * and not 1,2,0 (which is the extraction mapping).
     * The label is just a lable and is not interpretted.
     */
    public ColumnMap(String label, int...elements)
    {
        this.label = label ;

        this.insertOrder = new int[elements.length] ;
        System.arraycopy(elements, 0, elements, 0, elements.length) ;
        Arrays.fill(insertOrder, -1) ;
        
        this.fetchOrder = new int[elements.length] ;
        Arrays.fill(fetchOrder, -1) ;
    
        for ( int i = 0 ; i < elements.length ; i++ )
        {
            int x = elements[i] ;
            if ( x < 0 || x >= elements.length)
                throw new IllegalArgumentException("Out of range: "+x) ;
            // Checking
            if ( insertOrder[i] != -1 || fetchOrder[x] != -1 )
                throw new IllegalArgumentException("Inconsistent: "+ListUtils.str(elements)) ;
            
            insertOrder[i] = x ;
            fetchOrder[x] = i ;
        }
    }
    
    /** Length of mapping */
    
    public int length() { return fetchOrder.length ; }
    
    /** Apply to an <em>unmapped</em> tuple to get the i'th slot after mapping : SPO->POS : 0'th slot is P from SPO */
    public <T> T fetchSlot(int idx, Tuple<T> tuple)
    { 
        idx = fetchOrder[idx] ;     // Apply the reverse mapping as we are doing zero is P, so it's an unmap.
        return tuple.get(idx) ;
    }

    /** Apply to an <em>unmapped</em> tuple to get the i'th slot after mapping : SPO->POS : 0'th slot is P from SPO */
    public <T> T fetchSlot(int idx, T[] tuple)
    { 
        idx = fetchOrder[idx] ;     // Apply the reverse mapping as we are doing zero is P, so it's an unmap.
        return tuple[idx] ;
    }
    
    /** Apply to a <em>mapped</em> tuple to get the i'th slot as it appears after mapping : SPO->POS : 0'th slot is S from POS */
    public <T> T mapSlot(int idx, Tuple<T> tuple)
    { 
        idx = insertOrder[idx] ;
        return tuple.get(idx) ;
    }
    
    /** Apply to a <em>mapped</em> tuple to get the i'th slot as it appears after mapping : SPO->POS : 0'th slot is S from POS */
    public <T> T mapSlot(int idx, T[] tuple)
    { 
        idx = insertOrder[idx] ;        // Yes - it's the insert location we want to access 
        return tuple[idx] ;
    }
    
    /** Get the index of the i'th slot as it appears after mapping : SPO->POS : 0'th slot is S from POS so 2->0 */
    public int mapSlotIdx(int idx)
    { 
        return insertOrder[idx] ;        // Yes - it's the insert location we want to access 
    }

    /** Get the index of the i'th slot as it appears from a mapping : for SPO->POS : 0'th slot is P so 1->0 */
    public int fetchSlotIdx(int idx)
    { 
        return fetchOrder[idx] ;        // Yes - it's the insert location we want to access 
    }

    /** Apply to an <em>unmapped</em> tuple to get a tuple with the column mapping applied */
    public <T> Tuple<T> map(Tuple<T> src)
    {
        return map(src, insertOrder) ;
    }

    /** Apply to a <em>mapped</em> tuple to get a tuple with the column mapping reverse-applied */
    public <T> Tuple<T> unmap(Tuple<T> src)
    {
        return map(src, fetchOrder) ;
    }

    private <T> Tuple<T> map(Tuple<T> src, int[] map)
    {
        @SuppressWarnings("unchecked")
        T[] elts = (T[])new Object[src.size()] ;
        
        for ( int i = 0 ; i < src.size() ; i++ )
        {
            int j = map[i] ;
            elts[j] = src.get(i) ;
        }
        return Tuple.create(elts) ;
    }
    
    /** Compile a mapping encoded as single charcaters e.g. "SPO", "POS" */
    static int[] compileMapping(String domain, String range)
    {
        List<Character> input = StrUtils.toCharList(domain) ;
        List<Character> output = StrUtils.toCharList(range) ;
        return compileMapping(input, output) ;
    }

    /** Compile a mapping, encoded two list, the domain and range of the mapping function  */
    static <T> int[] compileMapping(T[] domain, T[] range)
    {
        return compileMapping(Arrays.asList(domain), Arrays.asList(range)) ;
    }
    
    /** Compile a mapping */
    static <T> int[] compileMapping(List<T> domain, List<T>range)
    {
        if ( domain.size() != range.size() )
            throw new AtlasException("Bad mapping: lengths not the same: "+domain+" -> "+range) ; 
        
        int[] cols = new int[domain.size()] ;
        boolean[] mapped = new boolean[domain.size()] ;
        //Arrays.fill(mapped, false) ;
        
        for ( int i = 0 ; i < domain.size() ; i++ )
        {
            T input = domain.get(i) ;
            int j = range.indexOf(input) ;
            if ( j < 0 )
                throw new AtlasException("Bad mapping: missing mapping: "+domain+" -> "+range) ;
            if ( mapped[j] )
                throw new AtlasException("Bad mapping: duplicate: "+domain+" -> "+range) ;
            cols[i] = j ;
            mapped[j] = true ;
        }
        return cols ;
    }
    
    @Override
    public String toString()
    {
        //return label ; 
        return format("%s:%s%s", label, mapStr(insertOrder), mapStr(fetchOrder)) ;
    }

    private Object mapStr(int[] map)
    {
        StringBuilder buff = new StringBuilder() ;
        String sep = "{" ;
        
        for ( int i = 0 ; i < map.length ; i++ )
        {
            buff.append(sep) ;
            sep = ", " ; 
            buff.append(format("%d->%d", i, map[i])) ;
        }
        buff.append("}") ;
        
        return buff.toString() ;
    }

    public String getLabel()
    {
        return label ;
    }
    
}

/*
 * (c) Copyright 2008, 2009 Hewlett-Packard Development Company, LP
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