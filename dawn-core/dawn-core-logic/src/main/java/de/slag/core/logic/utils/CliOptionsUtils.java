package de.slag.core.logic.utils;


import java.util.function.Consumer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

public class CliOptionsUtils {

	public static String HELP = "h";

	/**
	 * Creates cli-options with '-h' as default help parameter.
	 */
	public static Options createOptions() {
		final Options o = new Options();
		o.addOption(HELP, false, "prints this help page");
		return o;
	}

	/**
	 * 
	 * @param options , for the application defined
	 * @param args , given to main-method on runtime
	 * @return {@link CommandLine} to use in application
	 * @throws ParseException
	 */
	public static CommandLine parse(Options options, String[] args) throws ParseException {
		return new DefaultParser().parse(options, args);
	}
	
	/**
	 * Prints cli-help in a user friendly way.
	 * @param options , for the application defined
	 */
	public static void printHelpAndExit(Options options) {
		final Consumer<Option> oc = new Consumer<Option>() {

			@Override
			public void accept(Option option) {
				final StringBuilder sb = new StringBuilder();
				final String opt = option.getOpt();
				sb.append(opt);
				sb.append(StringUtils.repeat(" ", 8 - opt.length()));
				sb.append(option.getDescription());
				System.out.println(sb);
			}
		};
		options.getOptions().forEach(oc);
		System.exit(0);
	}
}
