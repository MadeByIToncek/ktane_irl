use attiny_hal::port::mode::{Floating, Input, Output};
use attiny_hal::port::{PB0, PB2, PB3, PB4, Pin};

use bit_struct::bit_struct;
use bit_struct::u1;
use bit_struct::u7;

use delay::delay_ms;

use crate::delay;

pub struct I2cOutput {
    pub sda: Pin<Output, PB3>,
    pub scl: Pin<Output, PB4>,
}

pub struct I2cInput {
    pub sda: Pin<Input<Floating>, PB0>,
    pub scl: Pin<Input<Floating>, PB2>,
}


bit_struct! {
    // u8 is the base storage type. This can be any multiple of 8
    pub struct I2cConnection(u8) {
        address: u7,
        iobit: u1,
    }
}

fn i2c_delay() {
    delay::delay_us(10);
}

pub fn i2c_send(i2c: &mut I2cOutput, con: &I2cConnection) {
    let mut config: I2cConnection = I2cConnection::try_from(114_u8).unwrap();
    let address: u7 = config.address().get();
    let iobit: u1 = config.iobit().get();

    loop {
        if i2c.sda.is_set_low() {
            continue;
        } else {
            i2c.sda.set_low();
            i2c_delay();
            i2c.scl.is_set_low();
        }
    }
}