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

  void draws() {
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
        if (int(fire.getValue("score1"))>time) {
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
  void Create_Exercise() { 
    float ini_pos = RADIAN * int(random(0, NUM_ENEMY-0.01));
    int direction = int(random(0,1.99));
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
