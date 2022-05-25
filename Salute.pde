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

  void draws() {
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
          if (int(fire.getValue("score3"))>time3) {  //if time is less than current high score, then the value is updated in the firebase database
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
  void Create_Exercise() {
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
