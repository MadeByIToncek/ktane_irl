#![no_std]
#![no_main]
#![feature(asm_experimental_arch)]

use core::panic::PanicInfo;

use attiny_hal::Peripherals;
use avr_hal_generic::hal::digital::v2::InputPin;
use delay::{delay_ms, delay_us};

mod delay;

#[panic_handler]
fn panic(_info: &PanicInfo) -> ! {
    loop {}
}

#[no_mangle]
fn main() -> ! {
    let peripherals = Peripherals::take().unwrap();
    let pins = attiny_hal::pins!(peripherals);

    let mut led = pins.pb1.into_output();
    let mut sda = pins.pb0.into_floating_input();
    let mut scl = pins.pb2.into_floating_input();

    loop {
        if (sda.is_low()) {
            led.set_high();
        } else {
                led.toggle();
                delay_ms(1000);
            }
        }
    }
