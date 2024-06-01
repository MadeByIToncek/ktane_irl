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

import space.itoncek.ktane_irl.Setup;

import java.util.Random;

public class ModuleHelper {
	static Random rnd = new Random();

	public static IModule getRandomModule(Setup.Config config) {
		return switch (rnd.nextInt(0, 2)) {
			case 0 -> new ButtonModule(config);
			case 1 -> new WiresModule(config);
			default -> throw new IllegalStateException("Unexpected value: " + rnd.nextInt());
		};
	}
}
