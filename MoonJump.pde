import queasycam.*;
import com.jogamp.newt.opengl.GLWindow;
String[] level;
Player player;
World maze;

boolean cursorLocked = true;
GLWindow w;
float viewDistance = 60;

void setup() {
  fullScreen(P3D);
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

void draw() {
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

World addMazeFromFile(String[] file) {
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

void keyReleased() {
  if (key == 'q') {
    cursorLocked = !cursorLocked;
  }
}

void GUILayer() {
  //player.undoRotation();
  fill(255);
  stroke(255);
  textSize(48);
  push();
  //translate(player.position.x, player.position.y, player.position.z);


  text("Moon Jump", 0, 0);
  pop();
  push();
  textSize(.1);
  textAlign(CENTER);
  fill(255);
  stroke(255);
  translate((player.position.x-player.velocity.x)+player.getForward().x, (player.position.y-player.velocity.y)+player.getForward().y, (player.position.z-player.velocity.z)+player.getForward().z);
  rotateY(-(player.pan-80));
  //rotateX(-(player.tilt-90));
  text(".", 0, 0);
  pop();
}
