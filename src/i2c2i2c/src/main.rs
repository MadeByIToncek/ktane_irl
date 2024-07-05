#![no_std]
#![no_main]

#![feature(asm_experimental_arch)]

#![allow(unused_variables)]
#![allow(dead_code)]
#![allow(unused_mut)]

use core::panic::PanicInfo;

use attiny_hal::Peripherals;
use delay::delay_ms;
use i2c::i2c_send;
use i2c::I2cConnection;

mod delay;
mod i2c;

use bit_struct::{u7, u1};

#[panic_handler]
fn panic(_info: &PanicInfo) -> ! {
    loop {}
}

#[no_mangle]
fn main() -> ! {
    let peripherals = Peripherals::take().unwrap();
    let pins = attiny_hal::pins!(peripherals);

    let mut led = pins.pb1.into_output();
    let mut i2cin: i2c::I2cInput = i2c::I2cInput{sda: pins.pb0.into_floating_input(), scl: pins.pb2.into_floating_input()};
    let mut i2cout: i2c::I2cOutput = i2c::I2cOutput{sda: pins.pb3.into_output_high(), scl: pins.pb4.into_output_high()};

    i2c_send(&mut i2cout, &I2cConnection::new(u7!(0b0001001), u1!(1)));

    loop {
        delay_ms(10)
    }
}
