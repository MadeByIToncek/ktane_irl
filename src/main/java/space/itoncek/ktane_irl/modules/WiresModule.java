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

import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import space.itoncek.ktane_irl.Setup;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class WiresModule implements IModule {
	public WiresModule(Setup.Config config) {

	}

	@Override
	public void setup(PromptBuilder promptBuilder, int order) {

	}

	@Override
	public String describe() {
		return "Basic wires";
	}

	@Override
	public String ident() {
		return "basic_wires";
	}

	public static class WireConfig implements Serializable {
		public final int cableAmt;
		public final WireColor[] wires;

		public WireConfig(long seed, Setup.Config config) {
			Random r = new Random(seed);
			cableAmt = r.nextInt(3, 7);
			wires = new WireColor[6];
			List<Integer> emptySlots = List.of(0, 1, 2, 3, 4, 5);
			for (int i = 0; i < cableAmt; i++) {
				int target = r.nextInt(emptySlots.size());
				wires[emptySlots.get(target)] = getRandomColor();
			}
		}

		public static ButtonModule.ButtonConfig setupRandomly(Setup.Config config) {
			return new ButtonModule.ButtonConfig(new Random().nextLong(), config);
		}

		private WireColor getRandomColor() {
			return WireColor.values()[new Random().nextInt(WireColor.values().length)];
		}

		public enum WireColor {
			Yellow,
			Red,
			Blue,
			Black,
			White;
		}
	}
}
