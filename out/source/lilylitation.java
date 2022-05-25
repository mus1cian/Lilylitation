import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 
import processing.sound.*; 
import P5ireBase.library.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class lilylitation extends PApplet {

//import processing.vr.*;

 //Connection between Arduino and Processing
  //Sound Effects
 //Firebase connection

ArrayList <PImage> virusimg;
SoundFile file, pop, complete;
Serial myPort;
Player myplayer;
MouseP mouse;
PShape grid;
PShape firstSphere, secondSphere;
PImage levelOneButton, levelTwoButton, levelThreeButton;
PImage levelOneBS, levelTwoBS, levelThreeBS;
PImage firstSphereImage, secondSphereImage;
PImage completeimg;
PFont font;
PFont fontGames;
P5ireBase fire;
Circles_hand giro;
Cerrar_virus viru;
Salute sal;
final int menu = 0;
final int level1 = 1;
final int level2 = 2;
final int level3 = 3;
int oneX, twoX, threeX;
int oneY = 210;
int twoY = 480;
int threeY = 750;
int state = menu;
String val;
boolean rectOverOne, rectOverTwo, rectOverThree, flag, firstContact;

public void setup() {
  //size(1000,900,P3D);
  
  
  oneX = twoX = threeX = 770;
  oneY = 210;
  twoY = 480;
  threeY = 750;
  
  //Initializing variables
  firstSphereImage = loadImage("texture.jpg");
  secondSphereImage = loadImage("texture2.jpg");
  levelOneButton = loadImage("level1.jpg");
  levelTwoButton = loadImage("level2.jpg");
  levelThreeButton = loadImage("level3.jpg");
  levelOneBS = loadImage("level1Select.jpg");
  levelTwoBS = loadImage("level2Select.jpg");
  levelThreeBS = loadImage("level3Select.jpg");
  completeimg = loadImage("complete.png");
  font = createFont("friz.ttf", 128);
  firstSphere = createShape(SPHERE, 150);
  secondSphere = createShape(SPHERE, 150);
  firstSphere.setStroke(false);
  secondSphere.setStroke(false);
  firstSphere.setTexture(firstSphereImage);
  secondSphere.setTexture(secondSphereImage);

  file = new SoundFile(this, "bgMusic.mp3");
  pop = new SoundFile(this, "bubble.mp3");
  complete = new SoundFile(this, "complete.mp3");
  file.amp(0.2f);
  file.loop();

  fire = new P5ireBase(this, "https://lilylitation-default-rtdb.firebaseio.com");
  
  //Grid
  grid = createShape();
  grid.beginShape(LINES);
  grid.stroke(255);
  for (int x = -10000; x < +10000; x += 250) {
    grid.vertex(x, +1000, +10000);
    grid.vertex(x, +1000, -10000);
  }
  for (int z = -10000; z < +10000; z += 250) {
    grid.vertex(+10000, +1000, z);
    grid.vertex(-10000, +1000, z);      
  }  
  grid.endShape(); 

  //Acelerometer
  myPort = new Serial(this, Serial.list()[0], 9600);
  myPort.bufferUntil('\n');

  virusimg = new ArrayList<PImage>();
  virusimg.add(loadImage("virus1.jpg"));
  virusimg.add(loadImage("virus2.jpg"));
  virusimg.add(loadImage("virus3.jpg"));
  virusimg.add(loadImage("virus4.jpg"));
  virusimg.add(loadImage("virus5.jpg"));

  firstContact = false;

  noStroke();
}


/**
* This function takes state as an input, which determines which screen to show.
*/
public void draw() {
  switch (state) {
    case menu:
      showMenu();
      break;
     case level1:
      showLevelOne();
      break;
     case level2:
      showLevelTwo();
      break;
     case level3:
      showLevelThree();
      break;
  }
}

/**
* Shows menu. User selects one of the levels
*/
public void showMenu() { 
  background(0);

  update(mouseX, mouseY);
  flag = true;
  
  pushMatrix();
  translate(width/2, height/2);
  shape(grid);
  popMatrix();
  fill(255);
  textFont(font);
  text("LILYLITATION", width/2-420, 80, -120);

  image(levelOneButton, oneX, oneY, width/5, height/5);
  image(levelTwoButton, twoX, twoY, width/5, height/5);
  image(levelThreeButton, threeX, threeY, width/5, height/5);
  
  textSize(45);
  text("PRESS X TO EXIT", 785, 1050);
  
  pushMatrix();
  translate(width/5, height/2, 0);
  lights();
  rotateX(PApplet.parseFloat(frameCount)/width*2*PI);
  rotateX(PApplet.parseFloat(frameCount)/200);
  shape(firstSphere, 0, 0);
  shape(secondSphere, 1150, 0);
  popMatrix();

  if (rectOverOne) {
    image(levelOneBS, oneX, oneY, width/5, height/5);
    } else if (rectOverTwo) {
    image(levelTwoBS, twoX, twoY, width/5, height/5);
  } else if (rectOverThree) {
    image(levelThreeBS, threeX, threeY, width/5, height/5);
  }
  
}

public void showLevelOne(){
  if(flag) {
    giro = new Circles_hand();
    flag = false;
  }
  giro.draws();
}

public void showLevelTwo(){
  if(flag) {
    viru = new Cerrar_virus();
    flag = false;
  }
  viru.draws();
}

public void showLevelThree(){
  if(flag) {
    sal = new Salute();
    flag = false;
  }
  sal.draws();
}

/**
* If 'X' key is pressed in the menu screen, the app closes
*/
public void keyPressedForMenu() {
  switch(key) {
  case 'x':
    exit();
    break;
  case 'X':
    exit();
    break;
  }
} 

/**
* If 'X' key is pressed inside a game, menu screen is shown
*/
public void keyPressedGame() {
  switch(key) {
    case 'x':
     key=0; // kill Esc
    state = menu;
    break;
  } 
} 


public void keyPressed() {
  switch (state) {
  case menu:
    keyPressedForMenu();
    break;
  default:
    keyPressedGame();
    break;
  }
} 

/**
* Level buttons change color when you hover over
*/
public void mousePressed() {
  if (rectOverOne) {
    state = level1;
    showLevelOne();
  } else if (rectOverTwo) {
    state = level2;
    showLevelTwo();
  } else if (rectOverThree) {
    state = level3;
    showLevelThree();
  }
}

public void update(int x, int y) {
  if (overRect(oneX, oneY, 453, 244) ) {
    rectOverOne = true;
    rectOverTwo = false;
    rectOverThree = false;
  } else if ( overRect(twoX, twoY, 459, 249) ) {
    rectOverOne = false;
    rectOverTwo = true;
    rectOverThree = false;
  } else if (overRect(threeX, threeY, 459, 259)) {
    rectOverOne = false;
    rectOverTwo = false;
    rectOverThree = true;
  } else {
    rectOverOne = false;
    rectOverTwo = false;
    rectOverThree = false;
  }
}

public boolean overRect(int x, int y, int width, int height)  {
  if (mouseX >= x && mouseX <= x+width && 
      mouseY >= y && mouseY <= y+height) {
    return true;
  } else {
    return false;
  }
}

/**
* Connection with arduino. Receives the coordinates of the glove
*/
public void serialEvent(Serial myPort){
    val = myPort.readStringUntil('\n');
    if(val != null){
      val = trim(val);
      //println(val);
      if(firstContact == false){
        if(val.equals("A")){
          myPort.clear();
          firstContact = true;
          myPort.write("A");file.amp(0.5f);
          //println("contact");
        }
      }
      else{
        println(val);
        if(mousePressed == true){
          myPort.write('1');
          //Bprintln("1");
        }
        myPort.write("A");
      }
    }
  }
/**
* Cerrar_virus is the code for level 2. The player must move the glow in a downwards motion in order to remove all of the images
*/
class Cerrar_virus{
  ArrayList <Virus> viruses;
  boolean flag, flag2;
  int time2;
  PFont fontVirus;
  int ellapsedMillis = 1000;
  int previousTime = 0;
  Cerrar_virus() {
    viruses = new ArrayList<Virus>();    
    flag = false;
    flag2 = true;
    myplayer = new Player(1);
    //mouse = new MouseP();
    Create_exercise();
    time2 = 0;
    fontGames = createFont("friz.ttf", 80);
  }

  public void draws() {
    background(0);
    textFont(fontGames);
    fill(255);
    text("Time: " + time2, 0, 80, -120);
    text("High Score: " + fire.getValue("score2"), width-500, 80, -120); //firebase connection
    
    //timer
    if (millis()-previousTime >= ellapsedMillis) {
      previousTime = millis();
      time2 += 1;
    }
  
    myplayer.functional(); //getting coordinates from the glove
    // mouse.setLocation(mouseX, mouseY, ((height/2) / tan(PI/6))-200);
    // mouse.display();
    if(viruses.size() == 0) {
      if(flag2) {
        complete.play(); //plays sound whenever user slides down a virus
        flag2 = false;
      }
      
      if (PApplet.parseInt(fire.getValue("score2"))>time2){
         fire.setValue("score2", str(time2));  //if all viruses are removed and current score is lower than high score, the score is updated in firebase database
      }
      pushMatrix();
      translate(width/2, height/2, ((height/2) / tan(PI/6))-1000);
      image(completeimg, -657 , -364); //complete image is shown when user completes level
      popMatrix();
    } else {
      //Grid
      pushMatrix();
      translate(width/2, height/2);
      shape(grid);
      popMatrix();
      if(myplayer.y >= 0.90f || flag) {
        float multi = ((700-viruses.get(0).y)/1.8f);
        viruses.get(0).y = (1-myplayer.y)*multi;  //updating coordinates of the virus 
        flag = true;
      }
      for(int i=0; i<viruses.size(); i++){
        if(viruses.get(i).live){
          viruses.get(i).draws(); //if virus is active it is added to the screen
        } else {
          viruses.remove(i);
          if(viruses.size() > 0) {
            for(int j=0; j<viruses.size(); j++) viruses.get(j).z -= 10;
            viruses.get(0).fall = true; //sliding down the virus
            i--;
            flag = false;
          }
        }
      }
    }
  }
  public void Create_exercise(){
    for(int i=0; i<10; i++){
      viruses.add(new Virus(((height/2) / tan(PI/6))-1500-i*10)); //initializing the viruses
    }
    viruses.get(0).fall = true;
  }
}
/**
* Circles_hand represents Level 1. Player is prompted to do circular motions with their hand
*/
class Circles_hand {
  int NUM_ENEMY = 8;
  float RADIAN = ((2*PI)/NUM_ENEMY);
  ArrayList <Enemy> enemys = new ArrayList<Enemy>(); //Enemy represents each of the spheres in the grid
  int counter;
  boolean flag;
  int time;
  int ellapsedMillis = 1000;
  int previousTime = 0;

  Circles_hand() {
    myplayer = new Player(80);
    //mouse = new MouseP();
    Create_Exercise();
    counter = 0;
    flag = true;
    fontGames = createFont("friz.ttf", 80);
    time = 0;
  }

  public void draws() {
    background(0);
    textFont(fontGames);
    fill(255);
    text("Time: " + time, 0, 80, -120);
    text("High Score: " + fire.getValue("score1"), width-500, 80, -120); //firebase connection
    
    //timer
    if (millis()-previousTime >= ellapsedMillis) {
      previousTime = millis();
      time += 1;
    }

    
    if(enemys.size() == 0) {
      counter++;
      if(counter >= 3) {
        if(flag) {
          complete.play();
          flag = false;
        }
        if (PApplet.parseInt(fire.getValue("score1"))>time) {
           fire.setValue("score1", str(time)); //if time is less than current high score, then the value is updated in the firebase database
        }
        
        pushMatrix();
        translate(width/2, height/2, ((height/2) / tan(PI/6))-1000);
        image(completeimg, -657 , -364); //whenever the person completes three rounds, the level finishes
        popMatrix();
      } else {
        Create_Exercise();
      }
    } else {
      //Grid
      pushMatrix();
      translate(width/2, height/2);
      shape(grid);
      popMatrix();

      // mouse.setLocation(mouseX, mouseY, ((height/2) / tan(PI/6))-200);
      // mouse.display();
      myplayer.draws(((height/2) / tan(PI/6))-200); //sphere that represents the movement of the person

      if(myplayer.intersect(enemys.get(0))) { //if both the glove coordinates and the enemies intersect, then the sphere is destroyed
        enemys.get(0).destroy();
        enemys.remove(0);
        if(enemys.size() > 0) {
          enemys.get(0).activate();
        }
      }
      for(int i=0; i<enemys.size(); i++) {
        enemys.get(i).draws();
      }
    }
  }

//initializing enemies array
  public void Create_Exercise() { 
    float ini_pos = RADIAN * PApplet.parseInt(random(0, NUM_ENEMY-0.01f));
    int direction = PApplet.parseInt(random(0,1.99f));
    if(direction == 0) {
      for(int i=0; i<NUM_ENEMY; i++) {
        enemys.add(new Enemy(width/2, height/2, ((height/2) / tan(PI/6))-200, ini_pos));
        ini_pos += RADIAN;
      }
    } else {
      for(int i=0; i<NUM_ENEMY; i++) {
        enemys.add(new Enemy(width/2, height/2, ((height/2) / tan(PI/6))-200, ini_pos));
        ini_pos -= RADIAN;
      }
    }
    enemys.get(0).active = true;
  }
}
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
  
  public void draws() {
    pushMatrix();
    translate(x, y, z);
    sphere(0.0001f);
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
  
  
  public void activate() {
    mob.setStroke(true);
    active = true; //activating updates the sphere's image and position
    mob.setStroke(false);
  }
  public void destroy(){
    active = false;
    live = false;
    pop.play(); //if a sphere is destroyed, a sound is played
  }
}
class MouseP {
  PShape test;
  float hit_box;    // hitbox
  float x, y, z; // location
  MouseP() {
    hit_box = 5;
  }

  public void setLocation(float tempX, float tempY, float tempZ) {
    x = tempX;
    y = tempY;
    z = tempZ;
    test = createShape(SPHERE, 5);
    test.setStroke(false);
  }

  public void display() {
    pushMatrix();
    translate(x, y, z);
    shape(test);
    popMatrix();
  }

  public boolean intersect(Enemy e) {
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
  public void draws(float az) {
    z = az;
    functional();
    if(x != 0.0f && y != 0){
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
  public void functional() {
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
  public boolean intersect(Enemy e) {
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
/**
* Salute represents Level 3. Player is prompted to move their hand left to right
*/
class Salute{
  int NUM_ENEMY = 8;
  float RADIAN = ((PI)/NUM_ENEMY);
  ArrayList <Enemy> enemys = new ArrayList<Enemy>(); //Enemy represents the sphere player has to destroy
  boolean direction, flag;
  int counter;
  int time3;
  int ellapsedMillis = 1000;
  int previousTime = 0;

  Salute() {
    direction = true;
    myplayer = new Player(60);
    //mouse = new MouseP();
    counter = 0;
    flag = true;
    fontGames = createFont("friz.ttf", 80);
    time3 = 0;
  }

  public void draws() {
    background(0);
    textFont(fontGames);
    fill(255);
    text("Time: " + time3, 0, 80, -120);
    text("High Score: " + fire.getValue("score3"), width-500, 80, -120); //firebase connection
 
    if (millis()-previousTime >= ellapsedMillis) {
      previousTime = millis();
      time3 += 1;
    }
    
    if(enemys.size() == 0) {
      counter++;
      if(counter >= 4) {
        if(flag) {
          complete.play();
          flag = false;
          if (PApplet.parseInt(fire.getValue("score3"))>time3) {  //if time is less than current high score, then the value is updated in the firebase database
             fire.setValue("score3", str(time3)); 
          }
        }
        pushMatrix();
        translate(width/2, height/2, ((height/2) / tan(PI/6))-1000);
        image(completeimg, -657 , -364); //whenever the person completes three rounds, the level finishes
        popMatrix();
      } else {
        Create_Exercise();
      }
    } else {
      //Grid
      pushMatrix();
      translate(width/2, height/2);
      shape(grid);
      popMatrix();

      myplayer.draws(((height/2) / tan(PI/6))-200); //sphere that represents the movement of the person

      // mouse.setLocation(mouseX, mouseY, ((height/2) / tan(PI/6))-200);
      // mouse.display();
      if(myplayer.intersect(enemys.get(0))) { //if both the glove coordinates and the enemies intersect, then the sphere is destroyed
        enemys.get(0).destroy();
        enemys.remove(0);
        if(enemys.size() > 0) {
          enemys.get(0).activate();
        }
      }
      for(int i=0; i<enemys.size(); i++) {
        enemys.get(i).draws();
      }
    }
  }

//initializing enemies array
  public void Create_Exercise() {
    float ini_pos = 0;
    if(direction) {
      for(int i=0; i<NUM_ENEMY+1; i++) {
        enemys.add(new Enemy(width/2, height/2, ((height/2) / tan(PI/6))-200, ini_pos));
        ini_pos -= RADIAN;
        direction = false;
      }
    } else {
      ini_pos = PI;
      for(int i=0; i<NUM_ENEMY+1; i++) {
        enemys.add(new Enemy(width/2, height/2, ((height/2) / tan(PI/6))-200, ini_pos));
        ini_pos += RADIAN;
        direction = true;
      }
    }
    enemys.get(0).active = true;
  }
}
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
    ran = PApplet.parseInt(random(0,4.9f));
    rx = PApplet.parseInt(random(-1200,800));
    y = ((height/2)-300)+PApplet.parseInt(random(-500,0));
    fall = false;
    live = true;
  }
  /*
  Draws function
  Insert the image in the coordinates and detects if the image 
  needs to be destroy.
  */
  public void draws(){
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
  public void destroy(){
    live = false;
  }
}
  public void settings() {  fullScreen(P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "lilylitation" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
