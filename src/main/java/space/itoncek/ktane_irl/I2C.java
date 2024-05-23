package space.itoncek.ktane_irl;

import com.diozero.api.I2CDevice;
import com.diozero.devices.PCF8574;

public class I2C {

	public void writeToDevice(int addr,byte... bytes) {
		I2CDevice device = I2CDevice.builder(addr).build();
		device.writeBytes(bytes);
		device.close();
	}

	public void writeToPCF() {
//		new PCF8574();
	}
}
