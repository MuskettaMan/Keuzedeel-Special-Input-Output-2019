import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 
import processing.io.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MainPI extends PApplet {



MainMenu mainMenu;

Game currentGame;
Game games[] = new Game[3];
PFont font;

int counter = 0;
boolean switchColor = false;

public void setup() {
  
  strokeWeight(1);
  games[0] = new Asteroids();
  games[1] = new Snake();
  
  Input.Setup();
  
  font = loadFont("8-Bit.vlw");
  textFont(font);
  
  mainMenu = new MainMenu();
  
  currentGame = mainMenu;
  
  currentGame.Setup();
  
}

public void draw() {
  background(254, 138, 113);
  pushMatrix();
  boolean switchColor = false;
    counter++;
  for(int i = 0; i < 20; i++) {
    noStroke();
    fill(0xfff6cd61);
    if(switchColor) {
      rect(i * 100, 0, 100, height);
      switchColor = false;
      continue;
    }
    switchColor = true;
    
  }
  fill(100, 100, 100, 0);
  rect(0, 0, width, height);
  popMatrix();
  strokeWeight(1);
  
  currentGame.KeyReleased();
  currentGame.KeyPressed();
  currentGame.Update();
  currentGame.Draw();
}

public void keyReleased() {
}

public void keyPressed() {
  
}


public void SelectNewGame(Game game) {
  if(game == null) return;
  currentGame = game;
  game.Setup();
  game.Reset();
}
class Asteroid {
  PVector pos;
  float r;
  float speedModifier = 1;
  PVector vel = PVector.random2D().mult(speedModifier);
  int total = floor(random(5, 15));
  Float[] offset;
  int currentColor = 0xffffffff;

  Asteroid() {
    this(new PVector(random(-width), random(-height)), random(60, 80));
  }

  Asteroid(PVector pos_, float r_) {
    pos = pos_.copy();
    r = r_ * 0.5f;
    offset = new Float[total];
    for (int i = 0; i < total; i++) {
      offset[i] = random(-r * 0.5f, r * 0.5f);
    }
  }

  public void update() {
    pos.add(vel);
  }

  public void render() {
    pushMatrix();
    stroke(currentColor);
    noFill();
    translate(pos.x, pos.y);
    //ellipse(0, 0, this.r * 2);
    beginShape();
    for (int i = 0; i < total; i++) {
      float angle = map(i, 0, total, 0, TWO_PI);
      float r1 = r + offset[i];
      float x = r1 * cos(angle);
      float y = r1 * sin(angle);
      vertex(x, y);
    }
    endShape(CLOSE);
    popMatrix();
  }

  public List<Asteroid> breakup() {
    List<Asteroid> newA = new ArrayList<Asteroid>();
    newA.add(new Asteroid(pos, r));
    newA.add(new Asteroid(pos, r));
    newA.get(0).currentColor = currentColor;
    newA.get(1).currentColor = currentColor;
    return newA;
  }

  public void edges() {
    if (pos.x > width + r) {
      pos.x = -r;
    } else if (pos.x < -r) {
      pos.x = width + r;
    }

    if (pos.y > height + r) {
      pos.y = -r;
    } else if (pos.y < -r) {
      pos.y = height + r;
    }
  }
  
  public void SetSpeedModifier(float speed) {
    vel = PVector.random2D().mult(speed);
  }
};
public class Asteroids extends Game {
  
  Ship ship;
  List<Asteroid> asteroids = new ArrayList<Asteroid>();
  List<Laser> lasers = new ArrayList<Laser>();
  
  int[] colors = { 0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffff00, 0xff00ffff, 0xffff00ff };
  
  boolean inputEnabled = true;
  

  int score = 0;
  
  int asteroidSpawnCount = 20;
  
  public void Setup() {
  }

  public void Draw() {
    pushMatrix();
    fill(0);
    rect(0, 0, width, height);
    fill(255);
    textFont(font, 60);
    textAlign(LEFT);
    text("SCORE: " + score, 30, 60);
    textFont(font, 100);
    if(!inputEnabled) {
      textAlign(CENTER);
      text("GAME OVER", width/2, height/2);
      textFont(font, 80);
      text("SCORE: " + score, width/2, height/2 + 50);
      ReturnToHubButton();
      ship.shipColor = 0xffff0000;
    }
    popMatrix();

    for (int i = 0; i < asteroids.size(); i++) {
      Asteroid asteroid = asteroids.get(i);
      if (ship.hits(asteroid)) {
        inputEnabled = false;
      }
      asteroid.render();
      asteroid.update();
      asteroid.edges();
    }

    for (int i = lasers.size() - 1; i >= 0; i--) {
      Laser laser = lasers.get(i);
      laser.render();
      laser.update();
      if (laser.offscreen()) {
        lasers.remove(i);
      } else {
        for (int j = asteroids.size() - 1; j >= 0; j--) {
          Asteroid asteroid = asteroids.get(j);
          if (laser.hits(asteroid)) {
            if (asteroid.r > 20) {
              List<Asteroid> newAsteroids = asteroid.breakup();
              asteroids.addAll(newAsteroids);
            }
            score += asteroid.r * 10;
            asteroids.remove(j);
            lasers.remove(i);
            break;
          }
        }
      }
    }

    ship.render();
    ship.turn();
    ship.update();
    ship.edges();
  }
  
  public void Update() {
    if(asteroids.size() <= 0) {
      asteroidSpawnCount += 10;
      int randomColor = (int)random(colors.length);    
      for (int i = 0; i < asteroidSpawnCount; i++) {
        asteroids.add(new Asteroid());
        asteroids.get(i).SetSpeedModifier(3);
        asteroids.get(i).currentColor = colors[randomColor];
      }
    }
  }
  
  public void Reset() {
    counter = 0;
    score = 0;
    asteroidSpawnCount = 20;
    asteroids = new ArrayList<Asteroid>();
    lasers = new ArrayList<Laser>();
    inputEnabled = true;
    ship = new Ship();
    int randomColor = (int)random(colors.length);
    for (int i = 0; i < asteroidSpawnCount; i++) {
      asteroids.add(new Asteroid());
      asteroids.get(i).currentColor = colors[randomColor];
    }
  }
  
  public void KeyPressed() {
      if(inputEnabled) {
        if (Input.Red()) {
          lasers.add(new Laser(ship.pos, ship.heading));
        } else if (Input.Right()) {
          ship.setRotation(0.1f);
        } else if (Input.Left()) {
          ship.setRotation(-0.1f);
        } else if (Input.Blue()) {
          ship.boosting(true);
        } 
      } else {
        if(Input.Blue()) {
          SelectNewGame(mainMenu);
        }
      }
      
      if(Input.Menu()) {
        SelectNewGame(mainMenu);
      }
  }
  
  public void KeyReleased() {
    if (!Input.Right()) {
      ship.setRotation(0);
    } else if (!Input.Left()) {
      ship.setRotation(0);
    } else if (!Input.Blue()) {
      ship.boosting(false);
    }
  }
  
  private void ReturnToHubButton() {
    stroke(255);
    fill(0);
    rect(width/2 - 250, height/2 + 80, 500, 65);
    if(counter % 30 == 0) switchColor = !switchColor;
    
    if(switchColor) fill(255);
    
    if(!switchColor) fill(150);
    textAlign(CENTER);
    text("RETURN TO HUB", width/2, height/2 + 130);
  }
  
}
public abstract class Game {
  
  public abstract void Setup();
  public abstract void Draw();
  public abstract void Update();
  public abstract void Reset();
  
  public abstract void KeyPressed();
  public abstract void KeyReleased();
}


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
class Laser {
  PVector pos;
  PVector vel;

  Laser(PVector spos_, float angle_) {
    pos = new PVector(spos_.x, spos_.y);
    vel = PVector.fromAngle(angle_);
    vel.mult(10);
  }

  public void update() {
    pos.add(vel);
  }
  
  public void render() {
    pushMatrix();
    stroke(255);
    strokeWeight(4);
    point(pos.x, pos.y);
    popMatrix();
  }

  public boolean hits(Asteroid asteroid) {
    float d = dist(pos.x, pos.y, asteroid.pos.x, asteroid.pos.y);
    if (d < asteroid.r) {
      return true;
    } else {
      return false;
    }
  }

  public boolean offscreen() {
    if (pos.x > width || pos.x < 0) {
      return true;
    }
    
    if (pos.y > height || pos.y < 0) {
      return true;
    }
    return false;
  }
};
public class MainMenu extends Game {

  Slot slots[];
  
  private PVector padding;
  private PVector size;
  
  int selectedSlot = 0;
  
  PImage[] thumbnails = new PImage[3];
  
  Map<Slot, Game> dictionary = new HashMap<Slot, Game>();
  
  public MainMenu() {
    size = new PVector(300, 300);
    padding = new PVector(50, 40);
    
    thumbnails[0] = loadImage("tn_asteroids.png");
    thumbnails[1] = loadImage("Screenshot_4.png");
  }
  
  public void Setup() {
    slots = new Slot[games.length];
    for(int i = 0; i < slots.length; i++) {
      slots[i] = new Slot("Sample Text", new PVector((width / 2) - (games.length * (padding.x + size.x) / 2 - padding.x / 2) + i * (padding.x + size.x), height / 2 - size.y / 2), new PVector(size.x, size.y));
      dictionary.put(slots[i], games[i]);
    }
    
    slots[0].label = "Asteroids";
    slots[1].label = "Snake";
    slots[2].label = "Something";
    slots[selectedSlot].SelectSlot();
  }
  
  public void Draw() {
    for(int i = 0; i < slots.length; i++) { 
      if(thumbnails[i] != null) {
        slots[i].Draw(thumbnails[i]);
      } else {
        slots[i].Draw();
      }
    }
    
  }
  
  public void Update() {
    if(selectedSlot < 0) {
      selectedSlot = slots.length - 1;
    }
    
    if(selectedSlot >= slots.length) {
      selectedSlot = 0;
    }
    
    for(Slot slot : slots) { 
      if(slot.selected) {
        slot.UnselectSlot();
      }
    }
    
    slots[selectedSlot].SelectSlot();
  }
  
  public void Reset() {
  
  }
  
  public void KeyPressed() {
    if(Input.Right()) {
      selectedSlot++;
    } else if(Input.Left()) {
      selectedSlot--;
    } else if(Input.Blue()) {
      SelectNewGame(dictionary.get(slots[selectedSlot]));
    }
  }
  
  public void KeyReleased() {
  
  }
  

}
class Ship {
  PVector pos;
  float r;
  float heading;
  float rotation;
  PVector vel;
  boolean isBoosting;
  int shipColor = 0xffffffff;

  Ship() {
    pos = new PVector(width / 2, height / 2);
    r = 20;
    heading = 0;
    rotation = 0;
    vel = new PVector(0, 0);
    isBoosting = false;
  }

  public void boosting(boolean b) {
    isBoosting = b;
  }

  public void update() {
    if (isBoosting) {
      boost();
    }
    pos.add(vel);
    vel.mult(0.99f);
  }

  public void boost() {
    PVector force = PVector.fromAngle(heading);
    force.mult(0.1f);
    vel.add(force);
  }

  public boolean hits(Asteroid asteroid) {
    float d = dist(pos.x, pos.y, asteroid.pos.x, asteroid.pos.y);
    if (d < r + asteroid.r) {
      return true;
    } else {
      return false;
    }
  }

  public void render() {
    pushMatrix();
    strokeWeight(1);
    translate(pos.x, pos.y);
    rotate(heading + PI / 2);
    fill(0);
    stroke(shipColor);
    triangle(-r, r, r, r, 0, -r);
    popMatrix();
  }

  public void edges() {
    if (this.pos.x > width + this.r) {
      this.pos.x = -this.r;
    } else if (this.pos.x < -this.r) {
      this.pos.x = width + this.r;
    }
    if (this.pos.y > height + this.r) {
      this.pos.y = -this.r;
    } else if (this.pos.y < -this.r) {
      this.pos.y = height + this.r;
    }
  }

  public void setRotation(float a) {
    rotation = a;
  }

  public void turn() {
    heading += rotation;
  }
}
public class Slot {

  PVector position;
  PVector size;
  int currentColor = color(0, 0, 0);
  private boolean selected = false;
  
  int defaultColor = 0xff3da4ab;
  int selectedColor = 0xff2c757a;
  
  String label;
  
  public Slot(String label, PVector position, PVector size) {
    this.label = label;
    this.position = position;
    this.size = size;
    currentColor = defaultColor;
  }
  
  public void Draw() {
    pushMatrix();
    fill(currentColor);
    strokeWeight(5);
    stroke(14, 154, 167);
    rect(position.x, position.y, size.x, size.y);
    fill(0xff4a4e4d);
    textSize(32);
    textAlign(CENTER);
    text(label, position.x + size.x / 2, position.y + size.y + 30);
    popMatrix();
  }
  
  public void Draw(PImage image) {
    pushMatrix();
    fill(currentColor);
    strokeWeight(5);
    stroke(14, 154, 167);
    rect(position.x, position.y, size.x, size.y);
    stroke(255);
    //tint(currentColor);
    image(image, position.x + 25, position.y + 25, 250, 250);
    textSize(32);
    fill(0xff4a4e4d);
    textAlign(CENTER);
    text(label, position.x + size.x / 2, position.y + size.y + 30);
    popMatrix();
  }
  
  public void SelectSlot() {
    selected = true;
    currentColor = selectedColor;
  }
  
  public void UnselectSlot() {
    selected = false;
    currentColor = defaultColor;
  }
  
}
public class Snake extends Game {
  
  ArrayList<Integer> x, y;
  
  int w, h, bs, dir, ax, ay;
  int[] dx = {0,0,1,-1}, dy = {1,-1,0,0};
  boolean gameover;
  
  int score;
  
  public Snake() {
  }
  
  public void Setup() {
    x = new ArrayList<Integer>();
    y = new ArrayList<Integer>();
    
    w = 60;
    h = 60;
    bs = 40;
    dir = 2;
    ax = 12;
    ay = 10;
    dx[0] = 0; dx[1] = 0; dx[2] = 1; dx[3] = -1;
    dy[0] = 1; dy[1] = -1; dy[2] = 0; dy[3] = 0;
    
    score = 0;
    
    gameover = false;
    
    w = width / bs;
    h = height / bs;
    x.add(5);
    y.add(5); 
  }
  
  public void Update() {
    background(50, 51, 65);
    stroke(0xff646e82);
    for (int i = 0; i < w; i++) line(i * bs, 0, i * bs, height);
    for(int i = 0; i < h; i++) line(0, i * bs, width, i * bs);
    fill(0xffbe5a5a);
    for (int i = 0 ; i < x.size(); i++) rect(x.get(i) * bs, y.get(i) * bs, bs, bs);
    if (!gameover) 
    {  
      fill(0xff822d2d);
      rect(ax * bs, ay * bs, bs, bs);
      if (frameCount % 8 == 0) 
      {
        x.add(0, x.get(0) + dx[dir]); y.add(0, y.get(0) + dy[dir]);
        if(x.get(0) < 0 || y.get(0) < 0 || x.get(0) >= w || y.get(0) >= h)
          gameover = true;
        for(int i = 1; i < x.size(); i++)
        if(x.get(0) == x.get(i) && y.get(0) == y.get(i))
          gameover=true;
        if (x.get(0) == ax && y.get(0)==ay)
        { 
          ax = (int)random(0, w); ay = (int)random(0, h);
          int temp = 0;
          temp += 1;
          temp += x.size() - 1;
          temp *= 5;
          score += temp;
        }
        else 
        { 
          x.remove(x.size() - 1); y.remove(y.size() - 1); 
        }
      }
      pushMatrix();
      textSize(80);
      textAlign(LEFT, TOP);
      fill(0xffAABCDD);
      text("SCORE: " + score, 10, 30);
      popMatrix();
    }
    else
    {
      fill(0xffAABCDD);
      textSize(90);
      textAlign(CENTER);
      text("GAME OVER!",width/2,height/2);
      ReturnToHubButton();
      pushMatrix();
      textSize(80);
      textAlign(CENTER);
      fill(0xffAABCDD);
      text("SCORE: " + score, width / 2, height / 2 + 230);
      popMatrix();
    }
  }
  
  public void Draw() {
  
  }
  
  public void Reset() {
  
  }
  
  public void KeyPressed() {
    int nd = Input.Down() ? 0 : (Input.Up() ? 1 : (Input.Right() ? 2 : (Input.Left() ? 3 : -1)));
    if (nd != -1 && (x.size() <= 1 || !(x.get(1) == x.get(0) + dx[nd] && y.get(1) == y.get(0) + dy[nd]))) dir = nd;
    
    if(gameover) {
      if(keyCode == ENTER) {
        SelectNewGame(mainMenu);
      }
    }
    
    if(Input.Menu()) {
      SelectNewGame(mainMenu);
    }
  }
  
  public void KeyReleased() {
  
  }
  
  private void ReturnToHubButton() {
    stroke(0xff758199);
    fill(0xff363847);
    rect(width/2 - 230, height/2 + 68, 460, 65);
    if(counter % 30 == 0) switchColor = !switchColor;
    
    if(switchColor) fill(0xffAABCDD);
    
    if(!switchColor) fill(0xff758199);
    textFont(font, 60);
    text("RETURN TO HUB", width/2, height/2 + 120);
  }
  
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MainPI" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
