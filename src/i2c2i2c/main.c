#include <avr/io.h>
#include <util/delay.h>
// NOT FINAL!
int main(void) {
    // Set pin 0 (PB0) and pin 1 (PB1) as output
    DDRB |= (1 << DDB0) | (1 << DDB1);

    while (1) {
        // Turn the LEDs on
        PORTB |= (1 << PORTB0) | (1 << PORTB1);
        _delay_ms(250);

        // Turn the LEDs off
        PORTB &= ~((1 << PORTB0) | (1 << PORTB1));
        _delay_ms(250);
    }
    
    return 0; // This line will never be reached
}

