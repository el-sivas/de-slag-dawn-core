package de.slag.dawn.core.utils;


import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.odftoolkit.simple.Document;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

public class Ods implements Closeable {

	private static final Log LOG = LogFactory.getLog(Ods.class);

	private File file;

	private SpreadsheetDocument ods;

	private Ods(String filename) {
		file = new File(filename);
		final Document odf;
		try {
			odf = Document.loadDocument(file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (!(odf instanceof SpreadsheetDocument)) {
			throw new RuntimeException("no ods");
		}
		ods = (SpreadsheetDocument) odf;
	}

	public static Ods opening(String filename) {
		return new Ods(filename);
	}

	public DataSheet getDataSheet(String sheetName) {
		final Table sheetByName = ods.getSheetByName(sheetName);
		return new DataSheet(sheetByName);

	}

	@Override
	public void close() throws IOException {
		try {
			ods.save(file);
		} catch (Exception e) {
			throw new IOException(e);
		}
		ods.close();

	}

	public class DataSheet {

		private Table table;

		public DataSheet(final Table table) {
			this.table = table;
		}

		@Deprecated
		public Table getTable() {
			return table;
		}

		public Cell getDataCell(int row, String columnName) {
			if (row == 0) {
				throw new RuntimeException("row index 0 is no data cell");
			}

			final int columnIndex = getColumnIndex(columnName);
			return table.getRowByIndex(row).getCellByIndex(columnIndex);
		}

		private int getColumnIndex(String columnName) {
			final Row headRow = table.getRowByIndex(0);
//			final int cellCount = headRow.getCellCount();
			for (int i = 0; i < 1000; i++) {
				final Cell cellByIndex = headRow.getCellByIndex(i);
				final String displayText = cellByIndex.getDisplayText();
				if (columnName.equals(displayText)) {
					return i;
				}
			}
			throw new RuntimeException("no col found with name: " + columnName);
		}

	}

}
