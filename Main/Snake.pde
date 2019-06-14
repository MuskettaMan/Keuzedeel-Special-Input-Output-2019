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
    stroke(#646e82);
    for (int i = 0; i < w; i++) line(i * bs, 0, i * bs, height);
    for(int i = 0; i < h; i++) line(0, i * bs, width, i * bs);
    fill(#be5a5a);
    for (int i = 0 ; i < x.size(); i++) rect(x.get(i) * bs, y.get(i) * bs, bs, bs);
    if (!gameover) 
    {  
      fill(#822d2d);
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
      fill(#AABCDD);
      text("SCORE: " + score, 10, 30);
      popMatrix();
    }
    else
    {
      fill(#AABCDD);
      textSize(90);
      textAlign(CENTER);
      text("GAME OVER!",width/2,height/2);
      ReturnToHubButton();
      pushMatrix();
      textSize(80);
      textAlign(CENTER);
      fill(#AABCDD);
      text("SCORE: " + score, width / 2, height / 2 + 230);
      popMatrix();
    }
  }
  
  public void Draw() {
  
  }
  
  public void Reset() {
  
  }
  
  public void KeyPressed() {
    int nd = key == 's' ? 0 : (key == 'w' ? 1 : (key == 'd' ? 2 : (key == 'a' ? 3 : -1)));
    if (nd != -1 && (x.size() <= 1 || !(x.get(1) == x.get(0) + dx[nd] && y.get(1) == y.get(0) + dy[nd]))) dir = nd;
    
    if(gameover) {
      if(keyCode == ENTER) {
        SelectNewGame(mainMenu);
      }
    }
  }
  
  public void KeyReleased() {
  
  }
  
  private void ReturnToHubButton() {
    stroke(#758199);
    fill(#363847);
    rect(width/2 - 230, height/2 + 68, 460, 65);
    if(counter % 30 == 0) switchColor = !switchColor;
    
    if(switchColor) fill(#AABCDD);
    
    if(!switchColor) fill(#758199);
    textFont(font, 60);
    text("RETURN TO HUB", width/2, height/2 + 120);
  }
  
}
