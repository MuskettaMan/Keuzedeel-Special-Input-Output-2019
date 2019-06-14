public abstract class Game {
  
  abstract void Setup();
  abstract void Draw();
  abstract void Update();
  abstract void Reset();
  
  abstract void KeyPressed();
  abstract void KeyReleased();
}
