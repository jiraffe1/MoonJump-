class Block {
  PVector position;
  PVector dimensions;
  color fillColor;
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
  
  void update(){
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
  
  void display(){
    if(PVector.dist(this.position, player.position) < viewDistance+25) {
      pushMatrix();
      translate(position.x, position.y, position.z);
      stroke(0, 0,0 , 500-(PVector.dist(this.position, player.position)*20));
      fill(red(fillColor), green(fillColor), blue(fillColor), 600-(PVector.dist(this.position, player.position)*10));
      box(dimensions.x, dimensions.y, dimensions.z);
      popMatrix();
    }
  }
  
  void moveDown(){
    position.y += 500;
  }
}
