public class Asteroids extends Game {
  
  Ship ship;
  List<Asteroid> asteroids = new ArrayList<Asteroid>();
  List<Laser> lasers = new ArrayList<Laser>();
  
  color[] colors = { #ff0000, #00ff00, #0000ff, #ffff00, #00ffff, #ff00ff };
  
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
      ship.shipColor = #ff0000;
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
          ship.setRotation(0.1);
        } else if (Input.Left()) {
          ship.setRotation(-0.1);
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
