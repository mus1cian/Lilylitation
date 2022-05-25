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

  void draws() {
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
      
      if (int(fire.getValue("score2"))>time2){
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
      if(myplayer.y >= 0.90 || flag) {
        float multi = ((700-viruses.get(0).y)/1.8);
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
  void Create_exercise(){
    for(int i=0; i<10; i++){
      viruses.add(new Virus(((height/2) / tan(PI/6))-1500-i*10)); //initializing the viruses
    }
    viruses.get(0).fall = true;
  }
}
