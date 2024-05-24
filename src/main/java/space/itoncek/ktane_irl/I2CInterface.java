package space.itoncek.ktane_irl;

import org.jetbrains.annotations.NotNull;

public interface I2CInterface {
	int writeDataToBus(int target, byte @NotNull ... data);

	int writeDataToBus(int target, @NotNull CharSequence data);
}
