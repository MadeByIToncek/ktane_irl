build:
	RUSTC_BOOTSTRAP=1 cargo build --release

.PHONY: build

upload:
	avr-objcopy --output-target=ihex target/avr-attiny85/release/i2c2i2c.elf target/avr-attiny85/release/i2c2i2c.hex
	micronucleus --timeout 60 --run --no-ansi target/avr-attiny85/release/i2c2i2c.hex

.PHONY: upload
