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

import com.pi4j.util.Console;
import io.javalin.Javalin;

import java.io.File;
import java.io.IOException;

public class KtaneIRL {
	public static final String prefix = ";";
	private static final I2CLib lib = new I2CLib();
	private static final Console log = new Console();
	private static Javalin app;

	public static void main(String[] args) {
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");
		init();

		Runtime.getRuntime().addShutdownHook(new Thread(KtaneIRL::shutdown));

		setupNetworkHandler();
	}

	private static void setupNetworkHandler() {
		app = Javalin.create(config -> {
			config.http.gzipOnlyCompression(9);
			config.jetty.defaultPort = 7777;
			File tempfolder;
			try {
				tempfolder = File.createTempFile("tempDir", "ktaneIRL");
				tempfolder.mkdirs();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			config.jetty.multipartConfig.cacheDirectory(tempfolder.getAbsolutePath());

			config.requestLogger.http((ctx, ms) -> {
				log.print(ctx.path() + " in " + ms);
			});

			config.requestLogger.ws(ws -> ws.onMessage(ctx -> System.out.println("Received: " + ctx.message())));

			config.router.ignoreTrailingSlashes = true; // treat '/path' and '/path/' as the same path
			config.router.treatMultipleSlashesAsSingleSlash = true; // treat '/path//subpath' and '/path/subpath' as the same path
			config.router.caseInsensitiveRoutes = true; // treat '/PATH' and '/path' as the same path
		});


		app.start();
	}

	public static void shutdown() {
		lib.close();
		app.stop();
	}

	private static void init() {
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

	public static void runGC() {
		System.gc();
		System.runFinalization();
	}
}
