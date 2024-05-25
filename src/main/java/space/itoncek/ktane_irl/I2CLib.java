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

import com.pi4j.Pi4J;
import com.pi4j.io.i2c.I2C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class I2CLib implements Closeable {
	Queue<BinaryData> queue = new PriorityQueue<>();
	TimerTask queueDispatcher = new TimerTask() {
		@Override
		public void run() {
			BinaryData poll = queue.poll();
			if (poll != null) {
				switch (poll.type()) {
					case 1 -> {
						try (I2C i2c = Pi4J.newAutoContext().i2c().create(1, poll.target())) {
							i2c.write(poll.string);
						}
					}
					case 2 -> {
						try (I2C i2c = Pi4J.newAutoContext().i2c().create(1, poll.target())) {
							i2c.write(poll.byteArray);
						}
					}
				}
			}
		}
	};

	Timer timer = new Timer();

	public I2CLib() {
		timer.scheduleAtFixedRate(queueDispatcher, 10, 10);
	}

	public void writeDataToBus(int target, byte @NotNull ... data) {
		queue.add(new BinaryData(target, null, data));
	}

	public void writeDataToBus(int target, @NotNull CharSequence data) {
		queue.add(new BinaryData(target, data));
	}

	@Override
	public void close() {
		timer.cancel();
		timer.purge();
		timer = null;
		KtaneIRL.runGC();
	}

	public record BinaryData(int target, @Nullable CharSequence string, byte @Nullable ... byteArray) {
		short type() {
			short ret = 0;
			if (string != null) ret += 1;
			if (byteArray != null) ret += 2;
			return ret;
		}
	}
}
