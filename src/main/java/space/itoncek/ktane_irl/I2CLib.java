package space.itoncek.ktane_irl;

import com.pi4j.Pi4J;
import com.pi4j.io.i2c.I2C;
import org.jetbrains.annotations.NotNull;

public class I2CLib implements I2CInterface {
	/**
	 * Writes data to the active #1 I2C bus
	 *
	 * @param target ID of the bus target
	 * @param data   byte array of the data to be sent
	 *
	 * @return Number of bytes written
	 */
	public int writeDataToBus(int target, byte @NotNull ... data) {
		try (I2C i2c = Pi4J.newAutoContext().i2c().create(1, target)) {
			return i2c.write(data);
		}
	}

	/**
	 * Writes data to the active #1 I2C bus
	 *
	 * @param target ID of the bus target
	 * @param data   CharSequence of the data to be sent
	 *
	 * @return Number of bytes written
	 */
	public int writeDataToBus(int target, @NotNull CharSequence data) {
		try (I2C i2c = Pi4J.newAutoContext().i2c().create(1, target)) {
			return i2c.write(data);
		}
	}
}
