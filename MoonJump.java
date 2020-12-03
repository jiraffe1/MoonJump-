import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import queasycam.*; 
import com.jogamp.newt.opengl.GLWindow; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MoonJump extends PApplet {



String[] level;
Player player;
World maze;

boolean cursorLocked = true;
GLWindow w;
float viewDistance = 60;

public void setup() {
  
  strokeWeight(4);
  //noCursor();
  w = (GLWindow)surface.getNative();
  level = loadStrings("new level.txt");
  player = new Player(this);
  maze = addMazeFromFile(level);

  maze.setPlayerAtStart(player);
  maze.doSomeRandomStuff();
  GUILayer();
}

public void draw() {
  if (cursorLocked) {
    w.confinePointer(true);
    w.setPointerVisible(false);
  } else {
    w.confinePointer(false);
    w.setPointerVisible(true);
  }
  background(20);//blue
  maze.update();
  maze.display();
  player.update();

  if (frameCount%60 == 0) {
    println(frameRate);
  }

  GUILayer();
 // stars();
  lights();
}

public World addMazeFromFile(String[] file) {
  World newMaze = new World(50);
  String[] i = file;
  for (int a = 0; a < 50; a++) {
    String v = i[a];
    println(v);

    for (int b = 0; b < v.length(); b++) {
      char val = v.charAt(b);
      float x = a*5;
      float y = b*5;
      switch(val) {
      case '0':
        break;
      case '1':
        newMaze.blocks[a][b] = new Block(x, 0, y, 5, 5, 5, 1);
        break;
      case '2':

        newMaze.blocks[a][b] = new Block(x, 0, y, 5, 5, 5, 2);
        break;
      case '3':
        break;
      case '4':
        break;
      }
      //if(a!=0){
      //}
    }
  }

  return newMaze;
}

public void keyReleased() {
  if (key == 'q') {
    cursorLocked = !cursorLocked;
  }
}

public void GUILayer() {
  //player.undoRotation();
  fill(255);
  stroke(255);
  textSize(48);
  push();
  //translate(player.position.x, player.position.y, player.position.z);


  text("Moon Jump", 0, 0);
  pop();
  push();
  textSize(.1f);
  textAlign(CENTER);
  fill(255);
  stroke(255);
  translate((player.position.x-player.velocity.x)+player.getForward().x, (player.position.y-player.velocity.y)+player.getForward().y, (player.position.z-player.velocity.z)+player.getForward().z);
  rotateY(-(player.pan-80));
  //rotateX(-(player.tilt-90));
  text(".", 0, 0);
  pop();
}
class Block {
  PVector position;
  PVector dimensions;
  int fillColor;
  boolean visited;
  boolean isDowned = false;
  int isnum = 3;
  Block(float x, float y, float z, float w, float h, float d, int num){
    position = new PVector(x, y, z);
    dimensions = new PVector(w, h, d);
    if(num == 1) {
    fillColor = color(random(100, 100));
    fillColor = color(noise(x/50, z/50)*155);
    position.y-=5;
    }
    else if(num == 2){
      fillColor = color(50);//red
      fillColor = color(noise(x/50, z/50)*155);
      position.y-=5;
    }
    else if(isnum == 3) {
      fillColor = color(random(50, 150));///greeen
      fillColor = color(noise(x/50, z/50)*155);
      isDowned = true;
    }
    visited = false;
  }
  
  public void update(){
    if(PVector.dist(player.position, this.position) < 60) {
      float playerLeft = player.position.x - player.dimensions.x/2;
      float playerRight = player.position.x + player.dimensions.x/2;
      float playerTop = player.position.y - player.dimensions.y/2;
      float playerBottom = player.position.y + player.dimensions.y/2;
      float playerFront = player.position.z - player.dimensions.z/2;
      float playerBack = player.position.z + player.dimensions.z/2;
      
      float boxLeft = position.x - dimensions.x/2;
      float boxRight = position.x + dimensions.x/2;
      float boxTop = position.y - dimensions.y/2;
        float boxBottom = position.y + dimensions.y/2;
      float boxFront = position.z - dimensions.z/2;
      float boxBack = position.z + dimensions.z/2;
      
      float boxLeftOverlap = playerRight - boxLeft;
      float boxRightOverlap = boxRight - playerLeft;
      float boxTopOverlap = playerBottom - boxTop;
      float boxBottomOverlap = boxBottom - playerTop;
      float boxFrontOverlap = playerBack - boxFront;
      float boxBackOverlap = boxBack - playerFront;
    
      if  (((playerLeft > boxLeft && playerLeft < boxRight || (playerRight > boxLeft && playerRight < boxRight)) && ((playerTop > boxTop && playerTop < boxBottom) || (playerBottom > boxTop && playerBottom < boxBottom)) && ((playerFront > boxFront && playerFront < boxBack) || (playerBack > boxFront && playerBack < boxBack)))){
        float xOverlap = max(min(boxLeftOverlap, boxRightOverlap), 0);
        float yOverlap = max(min(boxTopOverlap, boxBottomOverlap), 0);
        float zOverlap = max(min(boxFrontOverlap, boxBackOverlap), 0);
      
        if (xOverlap < yOverlap && xOverlap < zOverlap){
          if (boxLeftOverlap < boxRightOverlap){
            player.position.x = boxLeft - player.dimensions.x/2;
          } else {
            player.position.x = boxRight + player.dimensions.x/2;
          }
        }
      
        else if (yOverlap < xOverlap && yOverlap < zOverlap){
          if (boxTopOverlap < boxBottomOverlap){
            player.position.y = boxTop - player.dimensions.y/2;
            player.velocity.y = 0;
            player.grounded = true;
            } else {
            player.position.y = boxBottom + player.dimensions.y/2;
          }
        }
        
        else if (zOverlap < xOverlap && zOverlap < yOverlap){
          if  (boxFrontOverlap < boxBackOverlap){
            player.position.z = boxFront - player.dimensions.x/2;
          } else {
            player.position.z = boxBack + player.dimensions.x/2;
          }
        }
      }
    }
  }
  
  public void display(){
    if(PVector.dist(this.position, player.position) < viewDistance+25) {
      pushMatrix();
      translate(position.x, position.y, position.z);
      stroke(0, 0,0 , 500-(PVector.dist(this.position, player.position)*20));
      fill(red(fillColor), green(fillColor), blue(fillColor), 600-(PVector.dist(this.position, player.position)*10));
      box(dimensions.x, dimensions.y, dimensions.z);
      popMatrix();
    }
  }
  
  public void moveDown(){
    position.y += 500;
  }
}
class Player extends QueasyCam {
  PVector dimensions;
  PVector velocity;
  PVector gravity;
  boolean grounded;
  
  float p;
  float t;
  
  Player(PApplet applet){

    super(applet);
    speed = 0.08f;//0.075
    sensitivity = 0.5f;
    dimensions = new PVector(1, 3, 1);
    velocity = new PVector(0, 0, 0);
    gravity = new PVector(0, 0.01f, 0);//0, 0.01, 0
    grounded = false;
    
    p = 0;
    t = 0;
  }
  
  public void update(){
    velocity.add(gravity);
    position.add(velocity);
    
    if (grounded && keyPressed && key == ' '){
      grounded = false;
      velocity.y = -0.3f;
      position.y -= 0.1f;
    }
    
    p = pan;
    t = tilt;
  }
}
class World {
  Block[][] blocks;
  Block start;
  Block end;
  
  World(int size){
    blocks = new Block[size][size];
    
    for (int i=0; i<size; i++){
      for (int j=0; j<size; j++){
        float x = i * 5;
        float y = 0;
        float z = j * 5;
        blocks[i][j] = new Block(x, y, z, 5, 5, 5, 3);
      }
    }
    
    int row = PApplet.parseInt(random(1, size-1));
    int col = PApplet.parseInt(random(1, size-1));
    start = blocks[row][col];
 
    //for (int i=0; i<size*size*size/10; i++){
    //  if (!blocks[row][col].visited) blocks[row][col].moveDown();
    //  blocks[row][col].visited = true;
      
     /// if (random(0, 1) < 0.5){
     //  if (random(0, 1) < 0.5 && row > 1) row -= 1;
     //   else if (row < size-2) row += 1;
     // } else {
     //   if (random(0, 1) < 0.5 && col > 1) col -= 1;
     //   else if (col < size-2) col += 1;
     // }
    //}
  }
  
  public void update(){
    for (int i=0; i<blocks.length; i++){
      for (int j=0; j<blocks[i].length; j++){
        blocks[i][j].update();
      }
    }
  }
  
  public void display(){
    for (int i=0; i<blocks.length; i++){
      for (int j=0; j<blocks[i].length; j++){
        blocks[i][j].display();
      }
    }
  }
  
  public void setPlayerAtStart(Player player){
    player.position = PVector.add(start.position, new PVector(0, -15, 0));
  }
  
  public void doSomeRandomStuff() {
    for(int i = 0; i > 50; i++) {
      for(int j = 0; j < 50; j++) {
        if(blocks[i][j].fillColor == color(0, 100, 0) && blocks[i][j].isDowned)blocks[i][j].moveDown();
      }
    } 
  }
}
  public void settings() {  fullScreen(P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MoonJump" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
