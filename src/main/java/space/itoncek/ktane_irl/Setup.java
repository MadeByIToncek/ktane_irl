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

import de.codeshelf.consoleui.prompt.CheckboxResult;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import de.codeshelf.consoleui.prompt.builder.CheckboxPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import org.fusesource.jansi.AnsiConsole;
import space.itoncek.ktane_irl.modules.IModule;
import space.itoncek.ktane_irl.modules.ModuleHelper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static space.itoncek.ktane_irl.KtaneIRL.log;

public class Setup {
	public Random r;

	public Setup(long seed) {
		prepareSetup(seed);
	}

	public Setup() {
		prepareSetup();
	}

	private void prepareSetup() {
		prepareSetup(new Random().nextLong());
	}

	private void prepareSetup(long seed) {
		r = new Random(seed);
	}

	public Config runSetup() {
		Config cfg = new Config();
		try {
			AnsiConsole.systemInstall();
			ConsolePrompt prompt = new ConsolePrompt();
			PromptBuilder promptBuilder = prompt.getPromptBuilder();

			promptBuilder.createListPrompt()
					.name("modules")
					.message("How many modules are you planning on adding?")
					.newItem().text("3 modules").add()
					.newItem().text("4 modules").add()
					.newItem().text("5 modules").add()
					.newItem().text("6 modules").add()
					.addPrompt();

			promptBuilder.createListPrompt()
					.name("minutes")
					.message("How many minutes should they have to solve the bomb?")
					.newItem().text("1 min").add()
					.newItem().text("2 min").add()
					.newItem().text("3 min").add()
					.newItem().text("4 min").add()
					.newItem().text("5 min").add()
					.newItem().text("6 min").add()
					.newItem().text("7 min").add()
					.newItem().text("8 min").add()
					.newItem().text("9 min").add()
					.addPrompt();

			promptBuilder.createListPrompt()
					.name("batteries")
					.message("How many batteries are you planning on adding?")
					.newItem().text("0 batteries").add()
					.newItem().text("1 battery").add()
					.newItem().text("2 batteries").add()
					.newItem().text("3 batteries").add()
					.newItem().text("4 batteries").add()
					.newItem().text("5 batteries").add()
					.addPrompt();

			//For now, the bomb is compliant with the english manual (code #251)
			cfg.custom = false;

			HashMap<String, ? extends PromtResultItemIF> result = prompt.prompt(promptBuilder.build());

			cfg.moduleAmount = Integer.parseInt(((ListResult) result.get("modules")).getSelectedId().substring(0, 1));
			cfg.time = Integer.parseInt(((ListResult) result.get("minutes")).getSelectedId().substring(0, 1)) * 60;
			cfg.batteryAmount = Integer.parseInt(((ListResult) result.get("batteries")).getSelectedId().substring(0, 1));

			runSetupStep2(cfg);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				AnsiConsole.systemUninstall();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		KtaneIRL.init();
		return cfg;
	}

	private void runSetupStep2(Config cfg) throws IOException {
		for (Config.Indicator value : Config.Indicator.values()) {
			if (r.nextInt(10) == 3) {
				cfg.indicators.add(value);
			}
		}
		//-----------------------------
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		if (!cfg.indicators.isEmpty()) {
			CheckboxPromptBuilder indicators = promptBuilder.createCheckboxPrompt()
					.name("indicators")
					.message("Please stick the following indicators on the bomb (check them off as you stick them on):");

			for (Config.Indicator indicator : cfg.indicators) {
				indicators.newItem(indicator.name()).text(indicator.name()).add();
			}

			indicators.addPrompt();

			HashMap<String, ? extends PromtResultItemIF> result = prompt.prompt(promptBuilder.build());
			//-----------------------------
			List<Config.Indicator> temp = new ArrayList<>();
			for (String selectedId : ((CheckboxResult) result.get("indicators")).getSelectedIds()) {
				temp.add(Config.Indicator.valueOf(selectedId));
			}

			if (!cfg.indicators.equals(temp)) {
				log.box("Not all indicators are set, retrying");
				runSetupStep2(cfg);
			} else {
				runSetupStep3(cfg);
			}
		} else {
			runSetupStep3(cfg);
		}
	}

	private void runSetupStep3(Config cfg) throws IOException {
		for (int i = 0; i < cfg.moduleAmount; i++) {
			IModule module = ModuleHelper.getRandomModule(cfg);
			cfg.modules.add(module);
		}
		//-----------------------------
		ConsolePrompt prompt = new ConsolePrompt();
		PromptBuilder promptBuilder = prompt.getPromptBuilder();

		CheckboxPromptBuilder modules = promptBuilder.createCheckboxPrompt()
				.name("modules")
				.message("Please prepare the following modules (check them off as you are fetching them):");

		HashMap<String, Integer> moduleManager = new HashMap<>();
		for (IModule module : cfg.modules) {
			if (!moduleManager.containsKey(module.ident())) moduleManager.put(module.ident(), 0);
			String ident = module.ident() + moduleManager.put(module.ident(), moduleManager.get(module.ident()) + 1);
			modules.newItem(ident).text(module.describe()).add();
		}

		modules.addPrompt();

		int i = 0;
		for (IModule module : cfg.modules) {
			module.setup(promptBuilder, i++);
		}

		HashMap<String, ? extends PromtResultItemIF> result = prompt.prompt(promptBuilder.build());
		//-----------------------------
	}

	public static class Config implements Serializable {
		/**
		 * Amount of modules that will be used for this game
		 * @apiNote Set up in setup #2
		 */
		public int moduleAmount;
		/**
		 * How many seconds they have to defuse the bomb
		 * @apiNote Set up in setup #3
		 */
		public int time;
		/**
		 * Is the bomb custom?
		 *
		 * @implSpec false = compatible with default ktane manual
		 * @apiNote Set up in setup #4
		 */
		public boolean custom;
		/**
		 * How many batteries should the bomb have
		 *
		 * @apiNote Set up in setup #5
		 */
		public int batteryAmount;
		/**
		 * List of active modules
		 *
		 * @apiNote Set up in setup #6
		 */
		public List<IModule> modules = new ArrayList<>();
		/**
		 * List of active indicators
		 *
		 * @apiNote Set up in setup #6
		 */
		public List<Indicator> indicators = new ArrayList<>();

		public boolean hasIndicator(Indicator indicator) {
			return indicators.contains(indicator);
		}

		public enum Indicator {
			SND,
			CLR,
			CAR,
			IND,
			FRQ,
			SIG,
			NSA,
			MSA,
			TRN,
			BOB,
			FRK,
		}
	}
}
