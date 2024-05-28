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

import com.pi4j.Pi4J;
import com.pi4j.boardinfo.util.BoardInfoHelper;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;
import com.pi4j.io.i2c.I2C;
import com.pi4j.util.Console;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringJoiner;

import static space.itoncek.ktane_irl.Setup.runSetup;

public class KtaneIRL {
	public static final String prefix = ";";
	private static final I2CLib lib = new I2CLib();
	public static final Console log = new Console();

	public static void main(String[] args) {
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");
		init();

		Runtime.getRuntime().addShutdownHook(new Thread(KtaneIRL::shutdown));

//		setupMainConsole();
		Context pi4j = Pi4J.newAutoContext();

		DigitalInputConfig config = DigitalInput.newConfigBuilder(pi4j)
				.pull(PullResistance.PULL_DOWN)
				.address(4)
				.build();

		DigitalInputProvider provider = pi4j.provider("gpiod-digital-input");
		DigitalInput input = provider.create(config);

		input.addListener(e -> {
			if (e.state() == DigitalState.LOW) {
				try (I2C i2c = Pi4J.newAutoContext().i2c().create(1, 0x20, "PCF8574")) {
					log.println(Integer.toBinaryString(i2c.read()));
					i2c.write(new Random().nextInt(0, 0b11111111));
				}
			} else {
				log.print("reset");
			}
		});

		System.out.println("input.isHigh() = " + input.isHigh());
	}

	public static void setupMainConsole() {
		Scanner sc = new Scanner(System.in);
		InterruptReason reason = InterruptReason.NATURAL;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			CommandParser.Commands parse = CommandParser.parse(line);
			if (parse != null) {
				switch (parse) {
					case HELP -> printHelp();
					case STATUS -> printStatus();
					case FORCEGC -> runGC();
					case TEST -> runTest();
					case SETUP -> {
						reason = InterruptReason.SETUP;
					}
					case SAFE_EXIT, EXIT -> System.exit(0);
				}

				if (reason != InterruptReason.NATURAL) {
					break;
				}
			}
		}
		sc.close();
		switch (reason) {
			case NATURAL -> System.exit(0);
			case SETUP -> runSetup();
		}
	}

	private static void runTest() {
		try (I2C i2c = Pi4J.newAutoContext().i2c().create(1, 8)) {
			int i = i2c.read();
			log.print(i + " bits shall be read.");
			char[] bytes = new char[i];
			int read = i2c.read(bytes, 0, i);
			//String s = new String(bytes);
			log.print(read + " bytes were read.");
			log.print(new String(bytes));
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
