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

import space.itoncek.ktane_irl.modules.IModule;

import java.util.List;
import java.util.Scanner;

import static space.itoncek.ktane_irl.KtaneIRL.log;
import static space.itoncek.ktane_irl.KtaneIRL.setupMainConsole;

public class Setup {
	private static String ask(String question) {
		Scanner sc = new Scanner(System.in);
		log.box(question);

		String line = sc.next();
		sc.close();
		return line;
	}

	public static void runSetup() {
		Config config = new Config();
		switch (ask("Do you have the bomb in front of you? [Y/n]")) {
			case "N" -> {
				log.box("Prepare the bomb!");
				runSetup();
			}
			default -> runSetupStep2(config);
		}

		setupMainConsole();
	}

	private static void runSetupStep2(Config config) {
		String ask = ask("How many modules are you planning on adding? [int]");
		try {
			config.moduleAmount = Integer.parseInt(ask);
			runSetupStep3(config);
		} catch (NumberFormatException e) {
			log.box("This is not an integer!");
			runSetupStep2(config);
		}
	}

	private static void runSetupStep3(Config config) {
		String[] ask = ask("How long should they have to solve this game? [int:int]").split("\\r?:");
		try {
			config.time = (60 * Integer.parseInt(ask[0])) + Integer.parseInt(ask[1]);
			runSetupStep4(config);
		} catch (NumberFormatException e) {
			log.box("This is not of a int:int format!");
			runSetupStep3(config);
		}
	}

	private static void runSetupStep4(Config config) {
		switch (ask("Do you want to setup custom modules? [Y/n]")) {
			case "N":
				log.box("No you don't!");
			default:
				config.custom = false;
				runSetupStep5();
				break;
		}
	}

	private static void runSetupStep5() {
		log.box("Choosing and setting up modules");
	}

	public static class Config {
		/**
		 * Amount of modules that will be used for this game
		 */
		public int moduleAmount;
		/**
		 * How many seconds they have to defuse the bomb
		 */
		public int time;
		public boolean custom;
		public List<IModule> modules;
	}
}
