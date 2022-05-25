class Virus {
  int ran, ri;
  float y, z, rx;
  boolean live, fall;
  /*
  Constructor of Virus
  Inicialize the data of the virus.
  Parameters:
  float z - This value is assigned to the z value of the class.
  */
  Virus(float z) {
    this.z = z;
    ran = int(random(0,4.9));
    rx = int(random(-1200,800));
    y = ((height/2)-300)+int(random(-500,0));
    fall = false;
    live = true;
  }
  /*
  Draws function
  Insert the image in the coordinates and detects if the image 
  needs to be destroy.
  */
  void draws(){
    pushMatrix();
    translate((width/2)-300, y, z);
    image(virusimg.get(ran), rx, 0);
    popMatrix();
    if(y > 700){
      destroy();
    }
  }
  /*
  Destroy function
  Assings the virus life to false.
  */
  void destroy(){
    live = false;
  }
}
