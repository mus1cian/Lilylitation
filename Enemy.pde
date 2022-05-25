/**
* Enemy represents the spheres used in Level 1 and Level 3
*/

class Enemy {
  PShape mob; //sphere
  double hb_x, hb_y; //hitbox coordinates
  float x, y, z, theta, hit_box;
  boolean live, active;
  Enemy(float x, float y, float z, float theta) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.theta = theta;
    hit_box = 10;
    hb_x = x + Math.cos(theta)*50; //hitbox x coordinate
    hb_y = y + Math.sin(theta)*50; //hitbox y coordinate
    mob = createShape(SPHERE, 15); 
    mob.setStroke(false);
    live = true; 
    active = false;
  }
  
  void draws() {
    pushMatrix();
    translate(x, y, z);
    sphere(0.0001);
    rotateZ(theta);
    pushMatrix();
    translate(50, 0, 0);
    if(active) {
      mob.setTexture(secondSphereImage); // only one active sphere per round
      shape(mob); 
    } else {
      mob.setTexture(firstSphereImage); //the remaining spheres will be assigned the same texture
      shape(mob);
    }
    popMatrix();
    popMatrix();
    
  }
  
  
  void activate() {
    mob.setStroke(true);
    active = true; //activating updates the sphere's image and position
    mob.setStroke(false);
  }
  void destroy(){
    active = false;
    live = false;
    pop.play(); //if a sphere is destroyed, a sound is played
  }
}
