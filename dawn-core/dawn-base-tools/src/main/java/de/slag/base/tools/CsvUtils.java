package de.slag.base.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CsvUtils {

	private static final Log LOG = LogFactory.getLog(CsvUtils.class);

	private static final char DEFAULT_DELIMITER = ';';

	public static Collection<Collection<String>> toLines(Collection<String> header, Collection<CSVRecord> records) {
		return records.stream().map(r -> header.stream().map(h -> r.get(h)).collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

	public static void write(final File file, Collection<String> header, Collection<Collection<String>> lines)
			throws IOException {
		write(header, lines, Paths.get(file.getAbsolutePath()));
	}

	public static void write(final String filename, Collection<String> header, Collection<Collection<String>> lines)
			throws IOException {
		write(header, lines, Paths.get(filename));
	}

	public static void write(Collection<String> header, Collection<Collection<String>> lines, final Path path)
			throws IOException {
		
		final BufferedWriter writer = Files.newBufferedWriter(path);
		final CSVFormat format = CSVFormat.newFormat(DEFAULT_DELIMITER).withHeader(header.toArray(new String[0]))
				.withRecordSeparator("\r\n");
		final CSVPrinter csvPrinter = new CSVPrinter(writer, format);
		for (Collection<String> collection : lines) {
			csvPrinter.printRecord(collection);
		}
		csvPrinter.flush();
		csvPrinter.close();
	}

	public static Collection<CSVRecord> getRecords(final String filename, Collection<String> header)
			throws IOException {
		return getRecords(filename, header.toArray(new String[0]));
	}

	/**
	 * ...first record as header
	 */

	public static Collection<CSVRecord> getRecords(final String filename) throws IOException {
		return getRecords(filename, new String[0]);
	}

	public static Collection<CSVRecord> getRecords(final String filename, String... header) throws IOException {
		final Path path = Paths.get(filename);
		if (!Files.exists(path)) {
			LOG.warn(path + " not exists, return default value.");
			return Collections.emptyList();
		}

		final BufferedReader in = Files.newBufferedReader(path);
		final CSVFormat format;
		if (header != null && header.length > 0) {
			format = CSVFormat.newFormat(DEFAULT_DELIMITER).withHeader(header);
		} else {
			format = CSVFormat.newFormat(DEFAULT_DELIMITER).withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim();
		}

		final CSVParser parse = format.parse(in);
		final List<CSVRecord> records = parse.getRecords();
		validate(records, header);
		return records;
	}

	public static void validate(final List<CSVRecord> records, String... header) {
		final Collection<String> s = new ArrayList<>();
		for (int record = 0; record < records.size(); record++) {
			for (int col = 0; col < header.length; col++) {
				final String columnName = header[col];
				final CSVRecord csvRecord = records.get(record);
				try {
					csvRecord.get(columnName);
				} catch (IllegalArgumentException e) {
					LOG.debug(e);
					s.add("record: " + record + ", col: " + col + ": " + e.getMessage());
				}
			}
		}
		if (s.isEmpty()) {
			return;
		}
		throw new RuntimeException(String.join("\n", s));
	}
}
