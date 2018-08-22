/* Copyright (c) 2010 Aschauer EDV GmbH.  All rights reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This software was developed by Aschauer EDV GmbH and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 * 
 * Please contact Aschauer EDV GmbH www.aschauer-edv.at if you need additional
 * information or have any questions.
 * 
 * @author  Robert Wintner robert.wintner@aschauer-edv.at
 * @since   PDI 4.0
 */

package org.pentaho.di.trans.steps.sapinput.sap.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Properties;
import java.util.Vector;

import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.steps.sapinput.SapInputMeta;
import org.pentaho.di.trans.steps.sapinput.sap.SAPConnection;
import org.pentaho.di.trans.steps.sapinput.sap.SAPConnectionParams;
import org.pentaho.di.trans.steps.sapinput.sap.SAPConnectionParamsHelper;
import org.pentaho.di.trans.steps.sapinput.sap.SAPException;
import org.pentaho.di.trans.steps.sapinput.sap.SAPField;
import org.pentaho.di.trans.steps.sapinput.sap.SAPFunction;
import org.pentaho.di.trans.steps.sapinput.sap.SAPFunctionSignature;
import org.pentaho.di.trans.steps.sapinput.sap.SAPLibraryTester;
import org.pentaho.di.trans.steps.sapinput.sap.SAPResultSet;
import org.pentaho.di.trans.steps.sapinput.sap.SAPRow;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRecord;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;

// import com.sap.conn.jco.ext.Environment;

public class SAPConnectionImpl implements SAPConnection {

	// private static SAPDestinationDataProvider sddp = new
	// SAPDestinationDataProvider();
	private JCoDestination destination;

	/*
	 * static { Environment.registerDestinationDataProvider(sddp); }
	 */

	public void open(DatabaseMeta sapConnection) throws SAPException {
		open(SAPConnectionParamsHelper.getFromDatabaseMeta(sapConnection));
	}

	public void open(SAPConnectionParams params) throws SAPException {

		String connectionname = params.getName();
		Properties connectProperties = new Properties();
		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST,
				params.getHost());
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, params
				.getSysnr());
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT,
				params.getClient());
		connectProperties.setProperty(DestinationDataProvider.JCO_USER, params
				.getUser());
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,
				params.getPassword());

		save(connectionname, connectProperties);

		// sddp.setDestinationProperties(params);

		try {
			open(connectionname);
		} catch (JCoException e) {
			e.printStackTrace();
			throw new SAPException("Cannot open SAP connection", e);
		}

		test();
	}

	public void close() {
		// do nothing
	}

	private void test() throws SAPException {
		if (!SAPLibraryTester.isJCoLibAvailable()) {
			String message = BaseMessages.getString(SapInputMeta.class,
					"SapInputDialog.JCoLibNotFound");
			throw new SAPException(message);
		}
		if (!SAPLibraryTester.isJCoImplAvailable()) {
			String message = BaseMessages.getString(SapInputMeta.class,
					"SapInputDialog.JCoImplNotFound");
			throw new SAPException(message);
		}
		try {
			getFunction("ThisNameDoesnotMatter");
		} catch (UnsatisfiedLinkError e) {
			e.printStackTrace();
			String message = BaseMessages.getString(SapInputMeta.class,
					"SapInputDialog.JCoImplNotFound");
			throw new SAPException(e.getMessage() + "\r\n" + message);
		} catch (Throwable e) {
			e.printStackTrace();
			String message = BaseMessages.getString(SapInputMeta.class,
					"SapInputDialog.JCoImplNotFound");
			throw new SAPException(e.getMessage() + "\r\n" + message);
		}
	}

	private void open(String connectionName) throws JCoException, SAPException {
		try {
			destination = JCoDestinationManager.getDestination(connectionName);
			/*
			 * System.out.println("Connecting to SAP with following attributes:")
			 * ; System.out.println(destination.getAttributes());
			 * System.out.println();
			 */
		} catch (UnsatisfiedLinkError e) {
			e.printStackTrace();
			String message = BaseMessages.getString(SapInputMeta.class,
					"SapInputDialog.JCoImplNotFound");
			throw new SAPException(message);
		} catch (Throwable e) {
			e.printStackTrace();
			String message = BaseMessages.getString(SapInputMeta.class,
					"SapInputDialog.JCoImplNotFound");
			throw new SAPException(message);
		}
	}

	private void save(String connectionname, Properties properties) {

		File cfg = new File(connectionname + ".jcoDestination");

		// if (!cfg.exists()) {
		try {
			FileOutputStream fos = new FileOutputStream(cfg, false);
			properties.store(fos, "JCo Destination");
			fos.close();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create the destination file "
					+ cfg.getName(), e);
		}
		// }
	}

	public Collection<SAPFunction> getFunctions(String query)
			throws SAPException {
		Collection<SAPFunction> csf = new Vector<SAPFunction>();
		/*
		// TODO: make better
		if ("*SKAT*".equals(query)) {
			csf.add(new SAPFunction("TABLE:SKAT", "Table SKAT", "TABLES", "",
					""));
			return csf;
		}
		*/
		try {
			JCoFunction f = this.destination.getRepository().getFunction(
					"RFC_FUNCTION_SEARCH");
			JCoParameterList parameterList = f.getImportParameterList();
			parameterList.setValue("FUNCNAME", "*" + query + "*");
			f.execute(this.destination);
			JCoTable t = f.getTableParameterList().getTable("FUNCTIONS");
			for (int i = 0; i < t.getNumRows(); i++) {
				t.setRow(i);
				csf.add(new SAPFunction(t.getString("FUNCNAME"), t
						.getString("STEXT"), t.getString("GROUPNAME"), t
						.getString("APPL"), t.getString("HOST")));
			}
		} catch (JCoException e) {
			e.printStackTrace();
			throw new SAPException("Cannot get SAP functions", e);
		}
		return csf;
	}

	public SAPFunction getFunction(String name) throws SAPException {
		try {
			JCoFunction f = this.destination.getRepository().getFunction(name);
			if (f == null)
				return null;
			return new SAPFunction(f.getName());
		} catch (JCoException e) {
			e.printStackTrace();
			throw new SAPException("Cannot get SAP function", e);
		}
	}

	public SAPFunctionSignature getFunctionSignature(SAPFunction function)
			throws SAPException {
		String functionname = function.getName();
		
		// special treatment if table
		// not used
		// maybe for future release
		if (functionname.startsWith("TABLE:")) {
			String tablename = functionname.split(":")[1];
			SAPFunction sf = getFunction("RFC_READ_TABLE");
			SAPFunctionSignature sfs = getFunctionSignature(sf);
			sfs.setOutput(getTableFields(tablename));
			return sfs;
		}
		
		// normal processing
		SAPFunctionSignature sfs = new SAPFunctionSignature();
		try {
			JCoFunction f = this.destination.getRepository().getFunction(
					"RFC_GET_FUNCTION_INTERFACE");
			JCoParameterList parameterList = f.getImportParameterList();
			parameterList.setValue("FUNCNAME", functionname);
			f.execute(this.destination);
			JCoTable t = f.getTableParameterList().getTable("PARAMS");
			for (int i = 0; i < t.getNumRows(); i++) {
				t.setRow(i);
				String io = t.getString("PARAMCLASS");
				String name = t.getString("PARAMETER");
				String tab = t.getString("TABNAME");
				String field = t.getString("FIELDNAME");
				String exid = t.getString("EXID");
				String defaultvalue = t.getString("DEFAULT");
				String description = t.getString("PARAMTEXT");
				// String optional = t.getString("OPTIONAL");
				if ("I".equalsIgnoreCase(io)) {
					if (field.isEmpty()) {
						getTable("input_structure", sfs.getInput(), tab, name);
					} else {
						SAPField inputfield = new SAPField(name, "", "input_single");
						String typePentaho = getTypePentahoFromEXID(exid);
						inputfield.setTypePentaho(typePentaho);
						inputfield.setTypeSAP(exid);
						inputfield.setDefaultvalue(defaultvalue);
						inputfield.setDescription(description);
						sfs.addInput(inputfield);
					}
				} else if ("E".equalsIgnoreCase(io)) {
					if (field.isEmpty()) {
						getTable("output_structure", sfs.getOutput(), tab, name);
					} else {
						SAPField outputfield = new SAPField(name, "",
								"output_single");
						String typePentaho = getTypePentahoFromEXID(exid);
						outputfield.setTypePentaho(typePentaho);
						outputfield.setTypeSAP(exid);
						outputfield.setDefaultvalue(defaultvalue);
						outputfield.setDescription(description);
						sfs.addOutput(outputfield);
					}
				} else if ("T".equalsIgnoreCase(io)) {
					getTable("output_table", sfs.getOutput(), tab, name);
				}
			}
		} catch (JCoException e) {
			e.printStackTrace();
			throw new SAPException("Cannot get SAP function signature", e);
		}
		return sfs;
	}

	private void getTable(String type, Collection<SAPField> fields,
			String tabname, String paramname) throws JCoException {
		try {
			JCoFunction f = this.destination.getRepository().getFunction(
					"RFC_GET_NAMETAB");
			JCoParameterList parameterList = f.getImportParameterList();
			parameterList.setValue("TABNAME", tabname);
			f.execute(this.destination);
			JCoTable t = f.getTableParameterList().getTable("NAMETAB");
			for (int i = 0; i < t.getNumRows(); i++) {
				t.setRow(i);
				String name = t.getString("FIELDNAME");
				String dtyp = t.getString("DTYP");
				String typePentaho = getTypePentahoFromDTYP(dtyp);
				SAPField sapField = new SAPField(name, paramname, type);
				sapField.setTypePentaho(typePentaho);
				sapField.setTypeSAP(dtyp);
				fields.add(sapField);
			}
		} catch (JCoException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private String getTypePentahoFromDTYP(String dtyp) {
		String typePentaho = "Object";
		if ("CHAR".equalsIgnoreCase(dtyp)) {
			typePentaho = "String";
		} else if ("CUKY".equalsIgnoreCase(dtyp)) {
			typePentaho = "String";
		} else if ("DEC".equalsIgnoreCase(dtyp)) {
			typePentaho = "BigNumber";
		} else if ("NUMC".equalsIgnoreCase(dtyp)) {
			typePentaho = "Integer";
		} else if ("DATS".equalsIgnoreCase(dtyp)) {
			typePentaho = "Date";
		} else {
			typePentaho = "String";
		}
		return typePentaho;
	}

	private String getTypePentahoFromEXID(String exid) {
		String typePentaho = "Object";
		if ("C".equalsIgnoreCase(exid)) {
			typePentaho = "String";
		} else if ("F".equalsIgnoreCase(exid)) {
			typePentaho = "Number";
		} else if ("P".equalsIgnoreCase(exid)) {
			typePentaho = "BigNumber";
		} else if ("I".equalsIgnoreCase(exid)) {
			typePentaho = "Integer";
		} else if ("D".equalsIgnoreCase(exid)) {
			typePentaho = "Date";
		} else {
			typePentaho = "String";
		}
		return typePentaho;
	}

	public SAPRowIterator executeFunctionCursored(SAPFunction function,
			Collection<SAPField> inputfields, Collection<SAPField> outputfields)
			throws SAPException {
		try {

			// get function
			JCoFunction f = this.destination.getRepository().getFunction(
					function.getName());

			setInput(f, inputfields);
			f.execute(this.destination);
			return getOutputCursor(f, outputfields);

		} catch (JCoException e) {
			e.printStackTrace();
			throw new SAPException("Cannot excute SAP function", e);
		}
	}

	public SAPResultSet executeFunctionUncursored(SAPFunction function,
			Collection<SAPField> inputfields, Collection<SAPField> outputfields)
			throws SAPException {
		SAPResultSet srs = new SAPResultSet();
		SAPRowIterator sri = executeFunctionCursored(function, inputfields,
				outputfields);
		while (sri.hasNext())
			srs.addRow(sri.next());
		return srs;
	}

	@Deprecated
	public SAPResultSet executeFunctionUncursoredOld(SAPFunction function,
			Collection<SAPField> inputfields, Collection<SAPField> outputfields)
			throws SAPException {
		SAPResultSet srs = new SAPResultSet();
		try {

			// get function
			JCoFunction f = this.destination.getRepository().getFunction(
					function.getName());

			setInput(f, inputfields);
			f.execute(this.destination);
			srs = getOutputList(f, outputfields);

		} catch (JCoException e) {
			e.printStackTrace();
			throw new SAPException("Cannot excute SAP function", e);
		}
		return srs;
	}

	private void setInput(JCoFunction f, Collection<SAPField> inputfields)
			throws SAPException {
		
		// set input
		for (SAPField param : inputfields) {
			// analyze input fields
			String input_tabname = null;
			String input_structname = null;
			if (param.getTable() != null && !param.getTable().isEmpty()) {
				if ("input_structure".equalsIgnoreCase(param.getType())) {
					input_structname = param.getTable();
				} else {
					input_tabname = param.getTable();
				}
			}
			// set input
			if (input_tabname != null) {
				JCoParameterList parameterList = f.getTableParameterList();
				JCoTable t = parameterList.getTable(input_tabname);
				String valuestring = String.valueOf(param.getValue());
				String[] values = valuestring.split(";");
				int newrows = values.length - t.getNumRows();
				if (newrows > 0)
					t.appendRows(newrows);
				int i = 0;
				for (String value : values) {
					t.setRow(i);
					t.setValue(param.getName(), value);
					System.out.println("Table: " + input_tabname + ", Name: " + param.getName() + ", Row: " + i + ", Value: " + value);
					i++;
				}
			} else if (input_structname != null) {
				JCoParameterList parameterList = f.getImportParameterList();
				JCoStructure s = parameterList.getStructure(input_structname);
				s.setValue(param.getName(), param.getValue());
				System.out.println("Structure: " + input_structname + ", Name: " + param.getName() + ", Value: " + param.getValue());
			} else {
				JCoParameterList parameterList = f.getImportParameterList();
				parameterList.setValue(param.getName(), param.getValue());
				System.out.println("Singlevalue Name: " + param.getName() + ", Value: " + param.getValue());
			}
		}
	}

	private SAPRowIterator getOutputCursor(JCoFunction f,
			Collection<SAPField> outputfields) throws SAPException {

		// analyze output fields
		String output_tabname = null;
		String output_structname = null;
		for (SAPField field : outputfields) {
			if (field.getTable() != null && !field.getTable().isEmpty()) {
				if ("output_structure".equalsIgnoreCase(field.getType())) {
					output_structname = field.getTable();
				} else {
					output_tabname = field.getTable();
				}
				break;
			}
		}

		// get output
		if (output_tabname != null) {
			JCoParameterList tableParameterList = f.getTableParameterList();
			if (tableParameterList == null)
				throw new SAPException(
						"There is no table parameter list. Did you use 'Table' instead of 'Structure'?");
			JCoTable t = tableParameterList.getTable(output_tabname);
			return new SAPRowIterator(t, outputfields, output_tabname);
		} else if (output_structname != null) {
			JCoParameterList exportParameterList = f.getExportParameterList();
			if (exportParameterList == null)
				throw new SAPException(
						"There is no export parameter list. Did you use 'Structure' instead of 'Table'?");
			JCoStructure s = exportParameterList
					.getStructure(output_structname);
			return new SAPRowIterator(s, outputfields, output_structname);
		} else {
			JCoParameterList p = f.getExportParameterList();
			return new SAPRowIterator(p, outputfields, "");
		}
	}

	@Deprecated
	private SAPResultSet getOutputList(JCoFunction f,
			Collection<SAPField> outputfields) throws SAPException {

		SAPResultSet srs = new SAPResultSet();

		// analyze output fields
		String output_tabname = null;
		String output_structname = null;
		for (SAPField field : outputfields) {
			if (field.getTable() != null && !field.getTable().isEmpty()) {
				if ("output_structure".equalsIgnoreCase(field.getType())) {
					output_structname = field.getTable();
				} else {
					output_tabname = field.getTable();
				}
				break;
			}
		}

		// get output
		if (output_tabname != null) {
			JCoParameterList tableParameterList = f.getTableParameterList();
			if (tableParameterList == null)
				throw new SAPException(
						"There is no table parameter list. Did you use 'Table' instead of 'Structure'?");
			JCoTable t = tableParameterList.getTable(output_tabname);
			if (!t.isEmpty()) {
				do {
					srs.addRow(createRow(outputfields, output_tabname, t));
				} while (t.nextRow());
			}
			if (srs.getRows().size() != t.getNumRows()) {
				throw new SAPException("Problem reading rows: "
						+ srs.getRows().size() + " " + t.getNumRows());
			}
		} else if (output_structname != null) {
			JCoParameterList exportParameterList = f.getExportParameterList();
			if (exportParameterList == null)
				throw new SAPException(
						"There is no export parameter list. Did you use 'Structure' instead of 'Table'?");
			JCoStructure s = exportParameterList
					.getStructure(output_structname);
			srs.addRow(createRow(outputfields, output_structname, s));
		} else {
			JCoParameterList p = f.getExportParameterList();
			srs.addRow(createRow(outputfields, "", p));
		}

		return srs;
	}

	private SAPRow createRow(Collection<SAPField> outputfields,
			String tableorstructurename, JCoRecord record) {
		SAPRow sr = new SAPRow();
		for (SAPField field : outputfields) {
			String name = field.getName();
			Object value = record.getValue(name);
			String type = "Object";
			if (value != null) {
				if (value instanceof String) {
					type = "String";
				} else {
					type = value.getClass().getSimpleName();
				}
			}
			SAPField sapField = new SAPField(name, tableorstructurename, type,
					value);
			sapField.setTypePentaho(type);
			sr.addField(sapField);
		}
		return sr;
	}

	// in implementation for 4.1
	public void getTables(String query) throws SAPException {

	}

	// in implementation for 4.1
	public Collection<SAPField> getTableFields(String table)
			throws SAPException {
		Collection<SAPField> fields = new Vector<SAPField>();
		try {
			JCoFunction f = this.destination.getRepository().getFunction(
					"RFC_READ_TABLE");
			JCoParameterList parameterList = f.getImportParameterList();
			parameterList.setValue("QUERY_TABLE", table);
			f.execute(this.destination);
			JCoTable t = f.getTableParameterList().getTable("FIELDS");
			for (int i = 0; i < t.getNumRows(); i++) {
				t.setRow(i);
				String name = t.getString("FIELDNAME");
				String type = t.getString("TYPE");
				// String text = t.getString("FIELDTEXT");
				if ("C".equalsIgnoreCase(type)) {
					type = "String";
				} else {
					type = "String";
				}
				fields.add(new SAPField(name, "", type));
			}
		} catch (JCoException e) {
			e.printStackTrace();
			throw new SAPException("Cannot get SAP table fields", e);
		}
		return fields;
	}

	// in implementation for 4.1
	public SAPResultSet readTable(String table, Collection<SAPField> fields)
			throws SAPException {
		SAPResultSet srs = new SAPResultSet();
		try {
			JCoFunction f = this.destination.getRepository().getFunction(
					"RFC_READ_TABLE");
			JCoParameterList parameterList = f.getImportParameterList();
			parameterList.setValue("QUERY_TABLE", table);
			parameterList.setValue("DELIMITER", ";");
			parameterList.setValue("ROWCOUNT", 30);
			JCoTable tf = f.getTableParameterList().getTable("FIELDS");
			for (SAPField field : fields) {
				tf.appendRow();
				tf.setValue("FIELDNAME", field.getName());
			}
			f.execute(this.destination);
			JCoTable td = f.getTableParameterList().getTable("DATA");
			if (!td.isEmpty()) {
				do {
					SAPRow sr = new SAPRow();
					String delimiteddata = td.getString("WA");
					String[] fielddata = delimiteddata.split(";");
					int i = 0;
					for (SAPField field : fields) {
						String value = fielddata[i];
						value = value.trim();
						sr.addField(new SAPField(field.getName(), "", field
								.getType(), value));
						i++;
					}
					srs.addRow(sr);
				} while (td.nextRow());
			}
		} catch (JCoException e) {
			e.printStackTrace();
			throw new SAPException("Cannot read SAP table", e);
		}
		return srs;
	}

}
