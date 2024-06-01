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

package space.itoncek.ktane_irl.modules;

import de.codeshelf.consoleui.elements.ConfirmChoice;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import space.itoncek.ktane_irl.Setup;

import java.io.Serializable;
import java.util.Random;

public class ButtonModule implements IModule {
	public ButtonConfig cfg;

	public ButtonModule(Setup.Config config) {
		cfg = ButtonConfig.setupRandomly(config);
	}

	@Override
	public void setup(PromptBuilder promptBuilder, int order) {
		promptBuilder.createConfirmPromp()
				.name("btn_" + order + "_btn_color")
				.message("[BUTTON #" + order + "] The button in slot #" + order + " should have '" + cfg.buttonColor + "' color and '" + cfg.label + "' label.")
				.defaultValue(ConfirmChoice.ConfirmationValue.YES)
				.addPrompt();
	}

	@Override
	public String describe() {
		return "The Button";
	}

	@Override
	public String ident() {
		return "button";
	}


	public static class ButtonConfig implements Serializable {
		public final Color buttonColor;
		public final Color lightColor;
		public final Label label;
		public final boolean tap;
		public final int numberInAnyPosition;

		public ButtonConfig(long seed, Setup.Config config) {
			Random r = new Random(seed);
			int buttonID = r.nextInt(Color.values().length);
			int lightID = r.nextInt(Color.values().length - 1);
			int labelIndex = r.nextInt(Label.values().length);
			int number = r.nextInt(10);

			this.buttonColor = Color.values()[buttonID];
			this.lightColor = Color.values()[lightID];
			this.label = Label.values()[labelIndex];

			if (buttonColor.equals(Color.Red) && label.equals(Label.Hold)) {
				tap = true;
				numberInAnyPosition = -1;
			} else if (config.batteryAmount >= 2 && label.equals(Label.Detonate)) {
				tap = true;
				numberInAnyPosition = -1;
			} else if (buttonColor.equals(Color.Blue) && label.equals(Label.Abort)) {
				tap = false;
				numberInAnyPosition = number;
			} else if (config.hasIndicator(Setup.Config.Indicator.CAR) && buttonColor.equals(Color.White)) {
				tap = false;
				numberInAnyPosition = number;
			} else if (config.batteryAmount >= 3 && config.hasIndicator(Setup.Config.Indicator.FRK)) {
				tap = true;
				numberInAnyPosition = -1;
			} else {
				tap = false;
				numberInAnyPosition = number;
			}
		}

		public static ButtonConfig setupRandomly(Setup.Config config) {
			return new ButtonConfig(new Random().nextLong(), config);
		}

		public enum Label implements Serializable {
			Abort,
			Detonate,
			Hold,
			Press
		}

		public enum Color implements Serializable {
			Red,
			Blue,
			White,
			Yellow,
			Black
		}
	}
}
