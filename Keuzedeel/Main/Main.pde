import java.util.*;

MainMenu mainMenu;

Game currentGame;
Game games[] = new Game[3];
PFont font;

void setup() {
  fullScreen();
  strokeWeight(1);
  games[0] = new Asteroids();
  
  font = loadFont("8-Bit.vlw");
  textFont(font);
  
  mainMenu = new MainMenu();
  
  currentGame = mainMenu;
  
  currentGame.Setup();
}

void draw() {
  background(254, 138, 113);
  pushMatrix();
  boolean switchColor = false;
  for(int i = 0; i < 20; i++) {
    noStroke();
    fill(#f6cd61);
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
  
  currentGame.Update();
  currentGame.Draw();
}

void keyReleased() {
  currentGame.KeyReleased();
}

void keyPressed() {
  currentGame.KeyPressed();
  
}


public void SelectNewGame(Game game) {
  if(game == null) return;
  currentGame = game;
  game.Setup();
  game.Reset();
}
