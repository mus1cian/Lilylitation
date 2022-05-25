class MouseP {
  PShape test;
  float hit_box;    // hitbox
  float x, y, z; // location
  MouseP() {
    hit_box = 5;
  }

  void setLocation(float tempX, float tempY, float tempZ) {
    x = tempX;
    y = tempY;
    z = tempZ;
    test = createShape(SPHERE, 5);
    test.setStroke(false);
  }

  void display() {
    pushMatrix();
    translate(x, y, z);
    shape(test);
    popMatrix();
  }

  boolean intersect(Enemy e) {
    // Calculate distance
    float distance = dist(x, y, (float)e.hb_x, (float)e.hb_y); 

    // Compare distance to sum of radii
    if (distance < hit_box + e.hit_box) {
      return true;
    } else {
      return false;
    }
  }
}
