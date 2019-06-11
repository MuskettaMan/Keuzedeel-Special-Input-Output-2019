class Asteroid {
  PVector pos;
  float r;
  float speedModifier = 1;
  PVector vel = PVector.random2D().mult(speedModifier);
  int total = floor(random(5, 15));
  Float[] offset;
  color currentColor = #ffffff;

  Asteroid() {
    this(new PVector(random(-width), random(-height)), random(60, 80));
  }

  Asteroid(PVector pos_, float r_) {
    pos = pos_.copy();
    r = r_ * 0.5;
    offset = new Float[total];
    for (int i = 0; i < total; i++) {
      offset[i] = random(-r * 0.5, r * 0.5);
    }
  }

  void update() {
    pos.add(vel);
  }

  void render() {
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

  List<Asteroid> breakup() {
    List<Asteroid> newA = new ArrayList<Asteroid>();
    newA.add(new Asteroid(pos, r));
    newA.add(new Asteroid(pos, r));
    newA.get(0).currentColor = currentColor;
    newA.get(1).currentColor = currentColor;
    return newA;
  }

  void edges() {
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
