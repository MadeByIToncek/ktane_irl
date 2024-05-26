/*
 *
 * ██╗  ██╗████████╗ █████╗ ███╗   ██╗███████╗    ██╗██████╗ ██╗
 * ██║ ██╔╝╚══██╔══╝██╔══██╗████╗  ██║██╔════╝    ██║██╔══██╗██║
 * █████╔╝    ██║   ███████║██╔██╗ ██║█████╗      ██║██████╔╝██║
 * ██╔═██╗    ██║   ██╔══██║██║╚██╗██║██╔══╝      ██║██╔══██╗██║
 * ██║  ██╗   ██║   ██║  ██║██║ ╚████║███████╗    ██║██║  ██║███████╗
 * ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═══╝╚══════╝    ╚═╝╚═╝  ╚═╝╚══════╝
 *
 *            Licensed under terms in LICENSE
 *                 Made by IToncek & JellyCZ
 */

package space.itoncek.ktane_irl;

import com.pi4j.boardinfo.util.BoardInfoHelper;
import com.pi4j.util.Console;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;

public class KtaneIRL {
	public static final String prefix = ";";
	private static final I2CLib lib = new I2CLib();
	private static final Console log = new Console();

	public static void main(String[] args) {
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");
		init();

		Runtime.getRuntime().addShutdownHook(new Thread(KtaneIRL::shutdown));

		setupInputScanner();
	}

	private static void setupInputScanner() {
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			CommandParser.Commands parse = CommandParser.parse(line);
			if (parse != null) {
				switch (parse) {
					case HELP -> printHelp();
					case STATUS -> printStatus();
					case FORCEGC -> runGC();
					case SAFE_EXIT, EXIT -> System.exit(0);
				}
			}
		}
	}

	private static void printHelp() {
		ArrayList<String> list = new ArrayList<>();
		for (CommandParser.Commands value : CommandParser.Commands.values()) {
			list.add(prefix + value.commands[0] + " --> " + value.description);
			list.add("\tAlternatives: " + getAlternatives(value.commands));
			list.add("\t");
		}
		String[] array = list.toArray(new String[0]);
		log.box(array);
	}

	private static String getAlternatives(String[] commands) {
		StringJoiner js = new StringJoiner(", ");
		for (String command : commands) {
			js.add(prefix + command);
		}
		return js.toString();
	}

	public static void shutdown() {
		lib.close();
	}

	private static void init() {
		// ------------------------------------------------------------
		// Output Pi4J Board information
		// ------------------------------------------------------------
		// When the Pi4J Context is initialized, a board detection is
		// performed. You can use this info in case you need board-specific
		// functionality.
		// OPTIONAL

		printStatus();

		log.box("                                                                  ",
				"██╗  ██╗████████╗ █████╗ ███╗   ██╗███████╗    ██╗██████╗ ██╗     ",
				"██║ ██╔╝╚══██╔══╝██╔══██╗████╗  ██║██╔════╝    ██║██╔══██╗██║     ",
				"█████╔╝    ██║   ███████║██╔██╗ ██║█████╗      ██║██████╔╝██║     ",
				"██╔═██╗    ██║   ██╔══██║██║╚██╗██║██╔══╝      ██║██╔══██╗██║     ",
				"██║  ██╗   ██║   ██║  ██║██║ ╚████║███████╗    ██║██║  ██║███████╗",
				"╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═══╝╚══════╝    ╚═╝╚═╝  ╚═╝╚══════╝",
				"                                                                  ",
				"                Running your games since 2024!");
	}

	private static void printStatus() {
		log.box("Board model: " + BoardInfoHelper.current().getBoardModel().getLabel(),
				"Raspberry Pi model with RP1 chip (Raspberry Pi 5): " + BoardInfoHelper.usesRP1(),
				"OS is 64-bit: " + BoardInfoHelper.is64bit(),
				"JVM memory used (MB): " + BoardInfoHelper.getJvmMemory().getUsedInMb(),
				"Board temperature (°C): " + BoardInfoHelper.getBoardReading().getTemperatureInCelsius());
	}

	public static void runGC() {
		System.gc();
		System.runFinalization();
	}
}
