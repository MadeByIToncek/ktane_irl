all: i2c2i2c.hex

i2c2i2c.hex: i2c2i2c.elf
	avr-objcopy -R .eeprom -O ihex i2c2i2c.elf i2c2i2c.hex
i2c2i2c.elf: main.c status.h
	avr-gcc -DF_CPU=16500000UL -mmcu=attiny85 -Os main.c -o i2c2i2c.elf

flash: all
	micronucleus --timeout 60 --run --no-ansi i2c2i2c.hex

clean: all
	rm -v *hex *.elf
