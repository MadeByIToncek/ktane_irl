using System;
using System.Device.I2c;

namespace ktane_irl
{
    internal class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello I2C!");
            I2cDevice i2c = I2cDevice.Create(new I2cConnectionSettings(1, 0x12));
            i2c.WriteByte(0x42);
            var read = i2c.ReadByte();
            Console.WriteLine(read);
        }
    }
}
