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
    if(key == 'd' || keyCode == RIGHT) {
      selectedSlot++;
    } else if(key == 'a' || keyCode == LEFT) {
      selectedSlot--;
    } else if(keyCode == ENTER) {
      SelectNewGame(dictionary.get(slots[selectedSlot]));
    }
  }
  
  public void KeyReleased() {
  
  }
  

}
