/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */

package org.teiid.translator.array;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.teiid.core.types.ArrayImpl;
import org.teiid.language.Argument;
import org.teiid.language.Call;
import org.teiid.logging.LogConstants;
import org.teiid.logging.LogManager;
import org.teiid.translator.DataNotAvailableException;
import org.teiid.translator.ProcedureExecution;
import org.teiid.translator.TranslatorException;

public class ArrayProcedureExecution implements ProcedureExecution {
    private Iterator<List<?>> results;
    private Collection<List<?>> rows = new ArrayList<List<?>>();
    private Call procedure;

    public ArrayProcedureExecution(Call query) {
        this.procedure = query;
    }
    
    public void execute() throws TranslatorException {
    	Argument arg = this.procedure.getArguments().get(0);
    	Object array = arg.getArgumentValue().getValue();
    	if (array != null) {
    		Object[] vals = null;
    		if (array instanceof Object[]) {
    			vals = (Object[])array;
    		} else {
    			ArrayImpl arrayImpl = (ArrayImpl)array;
    			vals = arrayImpl.getValues();
    		}
    		for (Object o : vals) {
    			this.rows.add(Arrays.asList(o));
    		}
    		this.results = this.rows.iterator();
    	}    	
    }    

    public List<?> next() throws TranslatorException, DataNotAvailableException {
        if (results.hasNext()) {
            return results.next();
        }
        return null;
    }

    public void close() {
        LogManager.logDetail(LogConstants.CTX_CONNECTOR, ArrayPlugin.UTIL.getString("close_query")); //$NON-NLS-1$
    }

    public void cancel() throws TranslatorException {
        LogManager.logDetail(LogConstants.CTX_CONNECTOR, ArrayPlugin.UTIL.getString("cancel_query")); //$NON-NLS-1$
    }
    
    public List<?> getOutputParameterValues() throws TranslatorException{
    	return Collections.emptyList();
    }
}
