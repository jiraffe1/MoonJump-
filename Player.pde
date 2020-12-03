class Player extends QueasyCam {
  PVector dimensions;
  PVector velocity;
  PVector gravity;
  boolean grounded;
  
  float p;
  float t;
  
  Player(PApplet applet){

    super(applet);
    speed = 0.08;//0.075
    sensitivity = 0.5;
    dimensions = new PVector(1, 3, 1);
    velocity = new PVector(0, 0, 0);
    gravity = new PVector(0, 0.01, 0);//0, 0.01, 0
    grounded = false;
    
    p = 0;
    t = 0;
  }
  
  void update(){
    velocity.add(gravity);
    position.add(velocity);
    
    if (grounded && keyPressed && key == ' '){
      grounded = false;
      velocity.y = -0.3;
      position.y -= 0.1;
    }
    
    p = pan;
    t = tilt;
  }
}
