using System.Device.I2c;

namespace ktane_irl
{
    internal class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello I2C!");
            I2cDevice i2c = I2cDevice.Create(new I2cConnectionSettings(1, 8));
            for (byte i = 0; i < 0xff; i++) {
                i2c.WriteByte(i);
            }
        }
    }
}
