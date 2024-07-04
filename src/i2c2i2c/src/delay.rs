use core::arch::asm;

#[inline(always)]
pub fn delay(count: u64) {
    // Our asm busy-wait takes a 16 bit word as an argument,
    // so the max number of loops is 2^16
    let outer_count = count / 65536;
    let last_count = ((count % 65536) + 1) as u16;
    for _ in 0..outer_count {
        // Each loop through should be 4 cycles.
        let zero = 0u16;
        unsafe {
            asm!("1: sbiw {i}, 1",
                 "brne 1b",
                 i = inout(reg_iw) zero => _,
            )
        }
    }
    unsafe {
        asm!("1: sbiw {i}, 1",
             "brne 1b",
             i = inout(reg_iw) last_count => _,
        )
    }
}

///delay for N milliseconds
/// # Arguments
/// * 'ms' - an u64, number of milliseconds to busy-wait
#[inline(always)]
pub fn delay_ms(ms: u64) {
    // microseconds
    let us = ms * 1000;
    delay_us(us);
}

///delay for N microseconds
/// # Arguments
/// * 'us' - an u64, number of microseconds to busy-wait
#[inline(always)]
pub fn delay_us(us: u64) {

    let cpu_frequency_hz: u32 = 16000000;

    let us_in_loop = (cpu_frequency_hz / 1000000 / 4) as u64;
    let loops = us * us_in_loop;
    delay(loops);
}