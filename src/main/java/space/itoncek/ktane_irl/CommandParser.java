/*
 *
 * ██╗  ██╗████████╗ █████╗ ███╗   ██╗███████╗    ██╗██████╗ ██╗
 * ██║ ██╔╝╚══██╔══╝██╔══██╗████╗  ██║██╔════╝    ██║██╔══██╗██║
 * █████╔╝    ██║   ███████║██╔██╗ ██║█████╗      ██║██████╔╝██║
 * ██╔═██╗    ██║   ██╔══██║██║╚██╗██║██╔══╝      ██║██╔══██╗██║
 * ██║  ██╗   ██║   ██║  ██║██║ ╚████║███████╗    ██║██║  ██║███████╗
 * ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═══╝╚══════╝    ╚═╝╚═╝  ╚═╝╚══════╝
 *
 *            Licensed under terms in LICENSE.md
 *                 Made by IToncek & JellyCZ
 */

package space.itoncek.ktane_irl;

import org.jetbrains.annotations.Nullable;

import static space.itoncek.ktane_irl.KtaneIRL.prefix;

public class CommandParser {

	public static @Nullable Commands parse(String line) {
		if (line.startsWith(prefix)) {
			line = line.substring(1);
			for (Commands value : Commands.values()) {
				for (String c : value.commands) {
					if (line.startsWith(c)) {
						return value;
					}
				}
			}
		}
		return null;
	}

	public enum Commands {
		HELP("Prints this message", "help", "h"),
		STATUS("Shows the current system status", "status", "stat"),
		FORCEGC("Forces GC to run, DEVELOPER USE ONLY!", "forcegc", "fgc"),
		SAFE_EXIT("Terminates program execution, saves progress (virtual game pause)", "safe_exit", "sx"),
		EXIT("Terminates program execution, saves nothing!", "exit", "quit", "stop", "shutdown", "q");

		public final String description;
		public final String[] commands;

		Commands(String description, String... commands) {
			this.description = description;
			this.commands = commands;
		}
	}
}
