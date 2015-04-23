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

import javax.resource.cci.ConnectionFactory;

import org.teiid.language.Call;
import org.teiid.metadata.BaseColumn.NullType;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.Procedure;
import org.teiid.metadata.ProcedureParameter;
import org.teiid.metadata.ProcedureParameter.Type;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.ProcedureExecution;
import org.teiid.translator.Translator;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.TypeFacility;

@Translator(name="array", description="Array iterator translator")
public class ArrayExecutionFactory extends ExecutionFactory<ConnectionFactory, Object> {

	private static final String ARRAYITERATE = "ARRAYITERATE";

	@Override
    public void start() throws TranslatorException {
    	super.start();
    }

    @Override
	public ProcedureExecution createProcedureExecution(Call command,
			ExecutionContext executionContext, RuntimeMetadata metadata,
			Object connection) throws TranslatorException {
    	if (command.getProcedureName().equals(ARRAYITERATE)){
    		return new ArrayProcedureExecution(command);
    	}
    	return super.createProcedureExecution(command, executionContext, metadata, connection);
	}	

    @Override
    public boolean isSourceRequired() {
    	return false;
    }

    @Override
	public boolean isSourceRequiredForMetadata() {
		return false;
	} 
    
	@Override
	public void getMetadata(MetadataFactory metadataFactory, Object connection) throws TranslatorException {
		Procedure p = metadataFactory.addProcedure(ARRAYITERATE); 
		p.setAnnotation("Invokes a procedure that unnests the array type"); //$NON-NLS-1$
		
		metadataFactory.addProcedureResultSetColumn("result", TypeFacility.RUNTIME_NAMES.OBJECT, p);

		ProcedureParameter param = metadataFactory.addProcedureParameter("input", TypeFacility.RUNTIME_NAMES.OBJECT, Type.In, p); //$NON-NLS-1$
		param.setVarArg(true);
		param.setAnnotation("Array to be unnested"); //$NON-NLS-1$
		param.setNullType(NullType.No_Nulls);
	}

}
