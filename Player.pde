class Player{
  float x, y, z, hit_box, w, h;
  String [] splitted;
  int range;
  /*
  Constructor of Player
  Inicialize the hitbox value, range of interaction of the player 
  and the window's values.
  Parameters:
  int r - This value is assigned to the range value of the class.
  */
  Player(int r) {
    hit_box = 8;
    range = r;
    w = width/2;
    h = height/2;
  }
  /*
  Draws function
  Use the coordinates to insert a sphere which exemplifies the 
  coordinates of the arduino.
  Parameters:
  float az - This value is assigned to the z value of the class.
  */
  void draws(float az) {
    z = az;
    functional();
    if(x != 0.0 && y != 0){
      pushMatrix();
      translate(w-x*range, h-y*range, z);
      sphere(3);
      popMatrix();
    }
  }
  /*
  Functional function
  Assigns the coordinates X and Y of the class with the coordinates 
  registered by the arduino.
  */
  void functional() {
    String aux = val;
    if(aux != null && !aux.equals("A")){
      splitted = aux.split(" ");
      x = Float.parseFloat(splitted[0]);
      y = Float.parseFloat(splitted[1]);
    }
  }
  /*
  Intersect function
  This event returns a boolean in case of a collision between hitboxes of the 
  player and an enemy.

  Parameters:
  Enemy e - The enemy we want to check
  */
  boolean intersect(Enemy e) {
    // Calculate distance
    float distance = dist((w-x*range), (h-y*range), (float)e.hb_x, (float)e.hb_y); 

    // If the hitboxs colide return true
    if (distance < hit_box + e.hit_box) {
      return true;
    } else {
      return false;
    }
  }
}
