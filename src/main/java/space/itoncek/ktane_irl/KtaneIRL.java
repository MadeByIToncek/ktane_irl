package space.itoncek.ktane_irl;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;

import static java.lang.Thread.sleep;

public class KtaneIRL {
	public static void main(String[] args) throws Exception {
		Context pi4j = Pi4J.newAutoContext();
		I2CProvider i2CProvider = pi4j.provider("linuxfs-i2c");
		try (I2C i2c = i2CProvider.create(1,69)) {
			for (char i = 0x0; i < 0xff; i++) {
				i2c.write(i);
				sleep(500);
			}
		}
		pi4j.shutdown();
	}
}
