#include <avr/io.h>
#include <util/delay.h>

//TODO: Modifiable LED port
//#ifndef STATUS_LED
//#error "Specify status led in STATUS_LED"
//#endif

enum BLINKSTATUS {
    NORMAL,
    ERROR
};

void
status_blink(enum BLINKSTATUS status) {
    switch (status) {
        case NORMAL:
            PORTB |= (1 << PORTB0) | (1 << PORTB1);
            _delay_ms(1000);
            PORTB &= ~((1 << PORTB0) | (1 << PORTB1));
            _delay_ms(1000);
            break;
        case ERROR:
            int x;
            for (x=0;x<10;x++) {
                PORTB |= (1 << PORTB0) | (1 << PORTB1);    
                _delay_ms(100);
                PORTB &= ~((1 << PORTB0) | (1 << PORTB1));
                _delay_ms(100);
            }
            _delay_ms(1000);
            break;
    }
}
