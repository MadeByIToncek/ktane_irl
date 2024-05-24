package space.itoncek.ktane_irl;

import ch.qos.logback.classic.Logger;
import com.pi4j.Pi4J;
import com.pi4j.boardinfo.util.BoardInfoHelper;
import org.slf4j.LoggerFactory;

public class KtaneIRL {
	private static final Logger factory = (Logger) LoggerFactory.getLogger("Main");

	public static void main(String[] args) throws Exception {
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");
		init();
	}

	private static void init() {
		var pi4j = Pi4J.newAutoContext();
		// ------------------------------------------------------------
		// Output Pi4J Board information
		// ------------------------------------------------------------
		// When the Pi4J Context is initialized, a board detection is
		// performed. You can use this info in case you need board-specific
		// functionality.
		// OPTIONAL
		factory.info("Board model: {}", pi4j.boardInfo().getBoardModel().getLabel());
		factory.info("Operating system: {}", pi4j.boardInfo().getOperatingSystem());
		factory.info("Java versions: {}", pi4j.boardInfo().getJavaInfo());
		// This info is also available directly from the BoardInfoHelper,
		// and with some additional realtime data.
		factory.info("Board model: {}", BoardInfoHelper.current().getBoardModel().getLabel());
		factory.info("Raspberry Pi model with RP1 chip (Raspberry Pi 5): {}", BoardInfoHelper.usesRP1());
		factory.info("OS is 64-bit: {}", BoardInfoHelper.is64bit());
		factory.info("JVM memory used (MB): {}", BoardInfoHelper.getJvmMemory().getUsedInMb());
		factory.info("Board temperature (°C): {}", BoardInfoHelper.getBoardReading().getTemperatureInCelsius());
	}
}
