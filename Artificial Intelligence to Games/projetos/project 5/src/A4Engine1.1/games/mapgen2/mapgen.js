// Example calls:
// generateDungeonMaze(19,17, 'a maze', ['Kruskal',4], 57,12, 'graphics2x.png', [[1,7,0,null],[18,7,147,'MAP1EXIT']])
// generateDungeonMaze(19,17, 'another maze', ['RecursiveDivision',3], 57,12, 'graphics2x.png', [[1,7,146,'MAP1EXIT'],[18,7,147,'gameComplete']])

function generateDungeonMaze(width, height, name, method, groundtile, walltiles, sourceImage, exits) {

  var map = new Map();
  map.width = width;
  map.height = height;
  map.visibility = new Array(this.width*this.height);
  map.tileEntry = Game.tiles[sourceImage];
  map.name = name;

  // Create a simple maze room with an exit:
  var maze = null
  if (method[0]=='Kruskal') maze = generateMazeKruskal(width,height,method[1]);
  if (method[0]=='RecursiveDivision') maze = generateMazeRecursiveDivision(width,height,method[1]);

  // make sure exists is open:
  for(var i = 0;i<exits.length;i++) {
    var exit = exits[i];
    for(var j = 0;j<exit[0][2];j++) {
      for(var k = 0;k<exit[0][3];k++) {
        maze[exit[0][0]+j][exit[0][1]+k] = 1;        
      }
    }
    for(var j = 0;j<exit[1][2];j++) {
      for(var k = 0;k<exit[1][3];k++) {
        maze[exit[1][0]+j][exit[1][1]+k] = 1;        
      }
    }
  }

  map.newLayer();
  map.newLayer();
  for(var i = 0;i<height;i++) {
    for(var j = 0;j<width;j++) {
      map.setTile(j,i,0,groundtile);
//      if (maze[j][i]==0) map.setTile(j,i,1,walltiles[0]);
      if (maze[j][i]==0) map.setTile(j,i,1,properWallTile(j,i,maze,walltiles));
    }
  }

  // Create the exits:
  for(var i = 0;i<exits.length;i++) {
    var exit = exits[i];
    for(var j = 0;j<exit[0][2];j++) {
      for(var k = 0;k<exit[0][3];k++) {
        map.setTile(exit[0][0]+j, exit[0][1]+k, 1, exit[2]);
      }
    }
    if (exit[3]==null) {
      // do nothing
    } else if (exit[3]=='gameComplete') {
      var script = [{action:"gameComplete"}];
      map.triggers.push({x:exit[0][0], y:exit[0][1],
                         width:exit[0][2], height:exit[0][3], 
                         repeat:false,
                         script:script});
    } else {
      // create a bridge:
      map.bridges.push({x:exit[0][0], y:exit[0][1], width:exit[0][2], height:exit[0][3], id:exit[3]});
      map.bridgeDestinations.push({x:exit[1][0], y:exit[1][1], width:exit[1][2], height:exit[1][3], id:exit[3]});
    }
  }


  return map;
}


function properWallTile(x, y, maze, walltiles) {
  var left = 0;
  var right = 0;
  var up = 0;
  var down = 0;

  if (x>0 && maze[x-1][y]==0) left = 1;
  if (x<maze.length-1 && maze[x+1][y]==0) right = 1;
  if (y>0 && maze[x][y-1]==0) up = 1;
  if (y<maze[x].length-1 && maze[x][y+1]==0) down = 1;

  var tile = down + up*2 + right*4 + left*8;
  return walltiles[tile];
}


// width and height must be odd numbers:
function generateMazeKruskal(width,height,additionalPassages) {
  var walls = [];
  var maze = [];
  var remainingWalls = [];

  // Initialize the list of walls:
  for(var i = 1;i<height;i+=2) {
    for(var j = 2;j<width-1;j+=2) {
      walls.push({direction:'h', x:j, y:i});
    }
  }
  for(var i = 2;i<height-1;i+=2) {
    for(var j = 1;j<width;j+=2) {
      walls.push({direction:'v', x:j, y:i});
    }
  }

  // Initialize the map:
  var count = 1;
  for(var i = 0;i<width;i++) {
    var col = [];
    for(var j = 0;j<height;j++) {
      if ((i%2)==1 && (j%2)==1) {
        col.push(count);
        count++;
      } else {
        col.push(0);
      }
    }
    maze.push(col);
  }

  // Randomly iterate over the walls, and remove those that connect different regions:
  while(walls.length>0) {
    var idx = Math.floor(Math.random()*walls.length);
    var wall = walls[idx];
    walls.splice(idx,1);
    if (wall.direction=='h') {
      var c1 = maze[wall.x-1][wall.y];
      var c2 = maze[wall.x+1][wall.y];
    } else {
      var c1 = maze[wall.x][wall.y-1];
      var c2 = maze[wall.x][wall.y+1];
    }
    if (c1!=c2) {
      maze[wall.x][wall.y]=c2;
      for(var i = 0;i<height;i++) {
        for(var j = 0;j<width;j++) {
          if (maze[j][i]==c1) maze[j][i]=c2;
        }
      }
    } else {
      remainingWalls.push(wall);
    }
  }

  // remove some additional walls to create loops:
  while(additionalPassages>0 && remainingWalls.length>0) {
    var idx = Math.floor(Math.random()*remainingWalls.length);
    var wall = remainingWalls[idx];
    remainingWalls.splice(idx,1);
    maze[wall.x][wall.y] = 1;
    additionalPassages--;
  }
  return maze;
}



function generateMazeRecursiveDivision(width,height,minSize) {
  var maze = [];

  // Initialize the map:
  for(var i = 0;i<width;i++) {
    var col = [];
    for(var j = 0;j<height;j++) {
      if (i==0 || i==width-1 || j==0 || j==height-1) col.push(0);
                                                else col.push(1);
    }
    maze.push(col);
  }

  generateMazeRecursiveDivisionRecursive(maze, 1,1,width-2,height-2, minSize);
  return maze;
}


function generateMazeRecursiveDivisionRecursive(maze, x1,y1, x2,y2, minSize) {
  if (x2-x1<(minSize+1)) return;
  if (y2-y1<(minSize+1)) return;

  // walls can only happen at odd coordinates:
  var splitx = Math.floor(Math.random()*(x2-x1)/2)*2+x1+1;
  var splity = Math.floor(Math.random()*(y2-y1)/2)*2+y1+1;
  var noPassage = Math.floor(Math.random()*4);
  for(var i = x1;i<x2;i++) maze[i][splity]=0;
  for(var i = y1;i<y2;i++) maze[splitx][i]=0;

  // passages can only happen at even coordinates:
  if (noPassage!=0) { // left
    var px = Math.floor(Math.random()*((1+splitx-x1)/2))*2+x1;
    maze[px][splity]=1;
  }
  if (noPassage!=1) { // right
    var px = Math.floor(Math.random()*((1+x2-splitx)/2))*2+splitx+1;
    maze[px][splity]=1;
  }
  if (noPassage!=2) { // up
    var py = Math.floor(Math.random()*((1+splity-y1)/2))*2+y1;
    maze[splitx][py]=1;
  }
  if (noPassage!=3) { // down
    var py = Math.floor(Math.random()*((1+y2-splity)/2))*2+splity+1;
    maze[splitx][py]=1;
  }

  generateMazeRecursiveDivisionRecursive(maze, x1,y1, splitx-1,splity-1, minSize);
  generateMazeRecursiveDivisionRecursive(maze, splitx+1,y1, x2,splity-1, minSize);
  generateMazeRecursiveDivisionRecursive(maze, x1,splity+1, splitx-1,y2, minSize);
  generateMazeRecursiveDivisionRecursive(maze, splitx+1,splity+1, x2,y2, minSize);
}

