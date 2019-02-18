package de.slag.dawn.core.apps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;

import de.slag.core.logic.utils.BaseException;



public class ModelGenerator {

	private static final String JAVA = ".java";

	private static final String BEAN = "ApplicationBean";

	private static final String NEWLINE = "\n";

	private static final String SRV = "Service";

	private static final String SRVI = "ServiceImpl";

	private static final String ABS = "ApplicationBeanService";

	private static final String AABS = "AbstractApplicationBeanService";

	public static void main(String[] args) {
		String entity = "TestEntity";
		String outputDir = "/tmp";
		generate(entity, outputDir);
	}

	private static void out(String outPath, String s) {
		final Path path = Paths.get(outPath);
		try {
			Files.write(path, s.getBytes());
		} catch (IOException e) {
			throw new BaseException(e);
		}
		System.out.println(s);
	}

	private static void generate(String e, String outDir) {
		out(outDir + "/" + e + JAVA, generateClass(e, Type.CLASS, BEAN, Collections.emptyList()));
		out(outDir + "/" + e + SRV + JAVA,
				generateClass(e + SRV, Type.INTERFACE, typed(ABS, e), Collections.emptyList()));
		out(outDir + "/" + e + SRVI + JAVA, generateClass(e + SRVI, Type.CLASS, typed(AABS, e), e + SRV));
	}

	private static String typed(String typedClass, String type) {
		return typedClass + "<" + type + ">";
	}

	private static String generateClass(String name, Type type, String extend, String implement) {
		return generateClass(name, type, extend, Collections.singleton(implement));
	}

	private static String generateClass(String name, Type type, String extend, Collection<String> implement) {
		final StringBuilder sb = new StringBuilder();

		sb.append("public ");
		switch (type) {
		case CLASS:
			sb.append("class ");
			break;
		case INTERFACE:
			sb.append("interface ");
			break;
		}
		sb.append(name);
		if (StringUtils.isNotBlank(extend)) {
			sb.append(" extends " + extend);
		}
		final String allImplement = String.join(",", implement);
		if (StringUtils.isNotBlank(allImplement)) {
			sb.append(" implements ");
			sb.append(allImplement);
		}
		sb.append(" {}");
		return sb.toString();
	}

	private enum Type {
		CLASS, INTERFACE
	}

}
