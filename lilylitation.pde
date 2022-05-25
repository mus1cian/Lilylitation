//import processing.vr.*;

import processing.serial.*; //Connection between Arduino and Processing
import processing.sound.*;  //Sound Effects
import P5ireBase.library.*; //Firebase connection

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

void setup() {
  //size(1000,900,P3D);
  fullScreen(P3D);
  
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
  file.amp(0.2);
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
void draw() {
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
void showMenu() { 
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
  rotateX(float(frameCount)/width*2*PI);
  rotateX(float(frameCount)/200);
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

void showLevelOne(){
  if(flag) {
    giro = new Circles_hand();
    flag = false;
  }
  giro.draws();
}

void showLevelTwo(){
  if(flag) {
    viru = new Cerrar_virus();
    flag = false;
  }
  viru.draws();
}

void showLevelThree(){
  if(flag) {
    sal = new Salute();
    flag = false;
  }
  sal.draws();
}

/**
* If 'X' key is pressed in the menu screen, the app closes
*/
void keyPressedForMenu() {
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
void keyPressedGame() {
  switch(key) {
    case 'x':
     key=0; // kill Esc
    state = menu;
    break;
  } 
} 


void keyPressed() {
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
void mousePressed() {
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

void update(int x, int y) {
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

boolean overRect(int x, int y, int width, int height)  {
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
void serialEvent(Serial myPort){
    val = myPort.readStringUntil('\n');
    if(val != null){
      val = trim(val);
      //println(val);
      if(firstContact == false){
        if(val.equals("A")){
          myPort.clear();
          firstContact = true;
          myPort.write("A");file.amp(0.5);
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
