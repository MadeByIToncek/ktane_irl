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

import java.io.Serializable;

public interface IModule extends Serializable {
	/**
	 * Setup all hardware requirements
	 */
	void setup(PromptBuilder promptBuilder, int order);

	/**
	 * Fetch short description of said module
	 *
	 * @return Name of this module
	 */
	String describe();

	/**
	 * Fetch module identificator
	 * @return Identificator for this module
	 */
	String ident();
}
