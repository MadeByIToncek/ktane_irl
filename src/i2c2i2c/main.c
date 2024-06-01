#include <avr/io.h>
#include <util/delay.h>
#include "status.h"

int main(void) {
    // Set pin 0 (PB0) and pin 1 (PB1) as output
    DDRB |= (1 << DDB0) | (1 << DDB1);

    enum BLINKSTATUS status = NORMAL;

    while (1) {
        status_blink(status);
    }
    
    return 0; // This line will never be reached
}

