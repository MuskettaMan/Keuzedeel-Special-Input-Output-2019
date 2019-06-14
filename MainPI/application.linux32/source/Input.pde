import processing.io.*;

public static class Input {

  //Telling the raspberry PI what pins I'm going to use.
  public static void Setup()
  {
    //Buttons
     GPIO.pinMode(16, GPIO.INPUT_PULLUP); 
     GPIO.pinMode(20, GPIO.INPUT_PULLUP); 
     GPIO.pinMode(21, GPIO.INPUT_PULLUP); 
     //Joystick
     GPIO.pinMode(6, GPIO.INPUT_PULLUP); 
     GPIO.pinMode(13, GPIO.INPUT_PULLUP); 
     GPIO.pinMode(19, GPIO.INPUT_PULLUP); 
     GPIO.pinMode(26, GPIO.INPUT_PULLUP); 
  }

  //Reading input from Buttons.
  //Reading pin's for input.
  //pin 16 = Red Button.
  //pin 20 = Blue Button.
  //pin 21 = Menu Button.

  public static boolean Red()
  {
    if(GPIO.digitalRead(16) == GPIO.LOW)
    {
      return true;
    } else {
      return false;
    }
  }

  public static boolean Blue()
  {
    if(GPIO.digitalRead(20) == GPIO.LOW)
    {
      return true;
    } else {
      return false;
    }
  }

  public static boolean Menu()
  {
    if(GPIO.digitalRead(21) == GPIO.LOW)
    {
      return true;
    } else {
      return false;
    }
  }  

//Reading input from Joystick.
//Reading pin's for input.
//pin 6 = Red Button.
//pin 13 = Blue Button.
//pin 19 = Menu Button.
//pin 26 = Menu Button.
  public static boolean Up()
  {
    if(GPIO.digitalRead(6) == GPIO.LOW)
    {
      return true;
    } else {
      return false;
    }
  }

  public static boolean Down()
  {
    if(GPIO.digitalRead(13) == GPIO.LOW)
    {
      return true;
    } else {
      return false;
    }
  }

  public static boolean Left()
  {
    if(GPIO.digitalRead(19) == GPIO.LOW)
    {
      return true;
    } else {
      return false;
    }
  }
  
  public static boolean Right()
  {
    if(GPIO.digitalRead(26) == GPIO.LOW)
    {
      return true;
    } else {
      return false;
    }
  }
  
}
