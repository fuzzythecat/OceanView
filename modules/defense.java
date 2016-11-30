package oceanview;

import lejos.hardware.Battery;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import lejos.hardware.Sound;

public class Defence {

  /**
   * Main method
   * @param args
   * @throws InterruptedException 
   */
  public static void main(String[] args) throws InterruptedException {
    
    /*
    Port sonarSensorPort = null;
    @SuppressWarnings({ "resource", "unused" })
    EV3UltrasonicSensor sonarSensor = new EV3UltrasonicSensor(sonarSensorPort);
    */
    
    
  //  @SuppressWarnings({ "resource"})
    //EV3TouchSensor touchSensor = new EV3TouchSensor(SensorProt,S3);
    //touchSensor
    
  
    EV3UltrasonicSensor dist_sensor = new EV3UltrasonicSensor(SensorPort.S2);
    SampleProvider distanceMode = dist_sensor.getDistanceMode();
    SampleProvider is_touch = new EV3TouchSensor(SensorPort.S3);
    SampleProvider is_light = new NXTLightSensor(SensorPort.S1);
    float[] sonarsample = new float[distanceMode.sampleSize()];
    float[] touchsample = new float[is_touch.sampleSize()];
    float[] lightsample = new float[is_light.sampleSize()];
    
    Stopwatch timer = new Stopwatch();
    
    @SuppressWarnings("unused")
    RegulatedMotor forntLeft = Motor.A;
    @SuppressWarnings("unused")
    RegulatedMotor forntRight = Motor.B;
    
  //  String message = args.length > 0 ? args[0] : "Hello world!";

  //  System.out.println(message);

  //  Delay.msDelay(500);
  //  Sound.beep();

  //  System.out.println();
  //  System.out.println("Battery voltage:");
  //  System.out.println(Battery.getVoltage());

     Motor.A.setSpeed(1020);
     Motor.B.setSpeed(1020);
     
     int cnt = 0;
     LCD.clear();
     
     timer.reset();
    
     Stopwatch timer_out = new Stopwatch();
      
     while(true)
     {
      Motor.A.forward();
      Motor.B.forward();
      
      is_touch.fetchSample(touchsample, 0);
      distanceMode.fetchSample(sonarsample, 0);
      is_light.fetchSample(lightsample, 0);
      
      if( sonarsample[0] < 0.30 )
      {
        if(sonarsample[0] < 0.15)
        {
          Motor.A.backward();
          Motor.B.backward();
          Thread.sleep(150);
        }
        Motor.A.rotate(-60);
      }

      
      if( lightsample[0] > 0.32)// No Black Area
      {
        Motor.A.rotate(-60);
        
        is_light.fetchSample(lightsample, 0);
        
        if( timer_out.elapsed() > 3000 && lightsample[0] > 0.32)
        {
          //Sound.twoBeeps();
          Motor.A.setSpeed(1020);
          Motor.B.setSpeed(1020);
          cnt = 500;
          while(true){
            
            Motor.A.rotate(10);
            Motor.A.forward();
            Motor.B.forward();
            Thread.sleep((long) cnt);
            is_light.fetchSample(lightsample, 0);
            if(timer.elapsed() == 180000)break;
            if( lightsample[0] <= 0.32 )
            {
              timer_out.reset();
              break;
            }
            cnt = cnt + 100;
          }
        }
        
        Motor.A.setSpeed(1020);
        Motor.B.setSpeed(1020); 
      }
      else
      {
        timer_out.reset();
      }
      
      if ( touchsample[0] == 1)
      {
        Motor.A.rotate(60);
      }
      //System.out.println(timer.elapsed());
      
      if(timer.elapsed() >= 180000)break;
      
     }
     Delay.msDelay(3000);
  }

  
}
