public class Slot {

  PVector position;
  PVector size;
  color currentColor = color(0, 0, 0);
  private boolean selected = false;
  
  color defaultColor = #3da4ab;
  color selectedColor = #2c757a;
  
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
    fill(#4a4e4d);
    textSize(32);
    text(label, position.x, position.y + size.y + 30);
    popMatrix();
  }
  
  public void Draw(PImage image) {
    pushMatrix();
    fill(currentColor);
    strokeWeight(5);
    stroke(14, 154, 167);
    rect(position.x, position.y, size.x, size.y);
    stroke(255);
    tint(currentColor);
    image(image, position.x + 25, position.y + 25, 250, 250);
    textSize(32);
    fill(#4a4e4d);
    text(label, position.x, position.y + size.y + 30);
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
