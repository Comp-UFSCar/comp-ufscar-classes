// Definition of all the constants of the game:

// Keys:
var KEY_LEFT = 37;
var KEY_UP = 38;
var KEY_RIGHT = 39;
var KEY_DOWN = 40;
var KEY_SHIFT = 16;
var KEY_ALT = 18;
var KEY_SPACE = 32;
var KEY_ENTER = 13;
var KEY_ESC = 27;
var KEY_SPELLS = [68,69,70,71,72,73,74,75,76];  // letters from from 'd' to 'l'
var KEY_ITEMS =  [49,50,51,52,53,54,55,56];     // numbers from 1 to 8
var KEY_EQUIPPED_ITEMS = [65,66,67]             // a,b,c
var KEY_TALK = 84 // t
var KEY_DROP_GOLD = 82; // r
var KEY_CHANGE_CHARACTER = 78;  // n
var KEY_ZOOM = 90;  // z

// Direction:
var DIRECTION_NONE = -1;
var DIRECTION_UP = 0;
var DIRECTION_RIGHT = 1;
var DIRECTION_DOWN = 2;
var DIRECTION_LEFT = 3;
var direction_offx = [0,1,0,-1];
var direction_offy = [-1,0,1,0];

// Character actions and states:
var ACTION_NONE = 0;
var ACTION_MOVE = 1;
var ACTION_TAKE = 2;
var ACTION_DROP = 3;
var ACTION_DROP_GOLD = 4;
var ACTION_USE = 5;
var ACTION_UNEQUIP = 6;
var ACTION_ATTACK = 7;
var ACTION_INTERACT = 8;  // chop trees, open doors, etc. 
var ACTION_TALK = 9; 
var ACTION_TALK_ANGRY = 10; 
var ACTION_SPELL = 11; 

var STATE_READY = 0;
var STATE_MOVING = 1;
var STATE_ATTACKING = 2;
var STATE_INTERACTING = 3;
var STATE_CASTING = 4;
var STATE_VEHICLE_READY = 5;
var STATE_VEHICLE_MOVING = 6;
var STATE_TALKING = 1;  // only for talk_state

// Animations:
// these are suffixes to add to the animations below:
var ANIM_DIRECTIONS = ["-up","-right","-down","-left"]; // indexed by DIRECTON_UP, DIRECTION_RIGHT, ...
var ANIM_IDLE = "idle";
var ANIM_MOVING = "moving";
var ANIM_ATTACKING = "attacking";
var ANIM_INTERACTING = "interacting";
var ANIM_CASTING = "casting";
var ANIM_TALKING = "talking";

var COINPURSE_TILE = 10;

// Spells:
var SPELL_MAGIC_MISSILE = "magic missile";
var SPELL_HEAL = "heal";
var SPELL_SHIELD = "shield";
var SPELL_INCREASE = "increase";
var SPELL_DECREASE = "decrease";
var SPELL_FIREBALL = "fireball";
var SPELL_MAGIC_EYE = "magic eye";
var SPELL_REGENERATE = "regenerate";
var SPELL_INCINERATE = "incinerate";
var spell_cost = {};
spell_cost[SPELL_MAGIC_MISSILE] = 1;
spell_cost[SPELL_HEAL] = 1;
spell_cost[SPELL_SHIELD] = 2;
spell_cost[SPELL_INCREASE] = 2;
spell_cost[SPELL_DECREASE] = 2;
spell_cost[SPELL_FIREBALL] = 4;
spell_cost[SPELL_MAGIC_EYE] = 4;
spell_cost[SPELL_REGENERATE] = 4;
spell_cost[SPELL_INCINERATE] = 8;

// Inventory:
var ITEM_WEAPON = 0;
var ITEM_OFF_HAND = 1;
var ITEM_RING = 2;
var INVENTORY_SIZE = 8;

// Leveling up:
var LEVEL_EXPERIENCE = [    10,  15,   22,   34,   50,   75, 114, 170,  256, 
            385, 575, 865, 1300, 1950, 2920, 4380,6570,9850,14780];
var MAX_LEVEL = 20;

// Conversation constants:
var TALK_HI = "hi";
var TALK_BYE = "bye";
//var TALK_YES = "yes";
//var TALK_NO = "no";
var TALK_TRADE = "trade";
var TALK_ASK = "ask";

// Screen configuration:
var HUD_WIDTH = 176;    // size of the side bar
var HUD_HEIGHT = 64;   // size of the message window
var TILE_WIDTH_ORIGINAL = 64;
var TILE_HEIGHT_ORIGINAL = 64;
var TILE_WIDTH = 64;
var TILE_HEIGHT = 64;
var TARGET_TILE_WIDTH = 64;
var TARGET_TILE_HEIGHT = 64;


// Game states:
var GAMESTATE_TITLE = 0;
var GAMESTATE_GAME = 1;
var GAMESTATE_INSTRUCTIONS = 2;
var GAMESTATE_ENDING = 4;
var GAMESTATE_GAME_OVER = 5;

var AI_DEBUGGER_FOCUS = null;

// Create the game:
var Game = {
  cycle : 0, // cycles of actual game play (Reset each time a game restarts)
  state : 0,
  state_cycle : 0,
  paused : false,

};


Game.loadCharacters = function(xmlFilePath) {
  var xmlhttp=new XMLHttpRequest();
  xmlhttp.overrideMimeType("text/xml");
  xmlhttp.open("GET",xmlFilePath,false); 
  xmlhttp.send();
  var xmlDoc=xmlhttp.responseXML;
  var topElement = xmlDoc.documentElement;

  var characterTypeElements = topElement.getElementsByTagName("CharacterClass");
  for(var i = 0;i<characterTypeElements.length;i++) {
    var characterTypeElement = characterTypeElements[i];
    var className = characterTypeElement.attributes.getNamedItem("class").nodeValue;
    var name = characterTypeElement.attributes.getNamedItem("name").nodeValue;
    var superName = characterTypeElement.attributes.getNamedItem("super").nodeValue;

    console.log("Loading character type '" + characterTypeElement.attributes.getNamedItem("class").nodeValue + "'...");

//    this.characterTypeTable[className] = characterTypeElement;

    eval("window[className] = function() { this.getClassName = function() {return \"" + className + "\";}; this.decodeCharacterFromXML(); };");
    window[className].prototype = eval("new " + superName + "()");
    window[className].prototype.xml = characterTypeElement;
  }
}



Game.loadObjects = function(xmlFilePath) {
  var xmlhttp=new XMLHttpRequest();
  xmlhttp.overrideMimeType("text/xml");
  xmlhttp.open("GET",xmlFilePath,false); 
  xmlhttp.send();
  var xmlDoc=xmlhttp.responseXML;
  var topElement = xmlDoc.documentElement;

  var objectTypeElements = topElement.getElementsByTagName("ObjectClass");
  for(var i = 0;i<objectTypeElements.length;i++) {
    var objectTypeElement = objectTypeElements[i];
    var className = objectTypeElement.attributes.getNamedItem("class").nodeValue;
    var name = objectTypeElement.attributes.getNamedItem("name").nodeValue;
    var superName = objectTypeElement.attributes.getNamedItem("super").nodeValue;

    console.log("Loading object type '" + objectTypeElement.attributes.getNamedItem("class").nodeValue + "'...");

    eval("window[className] = function() { this.getClassName = function() {return \"" + className + "\";}; this.decodeObjectFromXML(); };");
    window[className].prototype = eval("new " + superName + "()");
    window[className].prototype.xml = objectTypeElement;
  }
}


Game.initialize = function(gamepath, gamefile) {
  // read the game file:
  var gamefilePath = gamepath + gamefile;
  var xmlhttp=new XMLHttpRequest();
  xmlhttp.overrideMimeType("text/xml");
  xmlhttp.open("GET",gamefilePath,false); 
  xmlhttp.send();
  var xmlDoc=xmlhttp.responseXML;
  var gameElement = xmlDoc.documentElement;

  // load game name:
  this.gameName = gameElement.attributes.getNamedItem("name").nodeValue;

  // load game title image:
  this.titleIMG_bg = null;
  if (gameElement.getElementsByTagName("titleImage").length>0) {
    this.titleIMG_bg = new Image();
    this.titleIMG_bg.src = gamepath + gameElement.getElementsByTagName("titleImage")[0].textContent;
  }

  // load game story:
  this.story = [];
  var storyElements = gameElement.getElementsByTagName("story")[0].getElementsByTagName("line");
  for(var i = 0;i<storyElements.length;i++) {
    this.story.push(storyElements[i].textContent);
  }
  this.ending = [];
  var endingElements = gameElement.getElementsByTagName("ending")[0].getElementsByTagName("line");
  for(var i = 0;i<endingElements.length;i++) {
    this.ending.push(endingElements[i].textContent);
  }
  this.instructions = ["Quick keys:",
                    "- Move with the arrow keys.",
                    "- Walk into enemies to attack, and into NPCs to talk.",
                    "- SHIFT + arrow keys to force attack",
                    "- ALT + arrow keys to interact (talk, open, etc.)",
                    "- 'T' to talk to the closest character.",
                    "- SPACE to take/use/embark/disembark",
                    "- Switch characters with 'N'.",
                    "- Use/equip/unquip items by pressing their number.",
                    "- SHIFT + number to drop an item.",
                    "- 'R' to drop gold.",
                    "- letters to cast spells (+ arrow keys for direction",
                    "  or 'enter' to cast on self)",
                    "- 'Z' to zoom in and out.",
                    "- Hold 'TAB' to increase the game speed x4.",
                    "- 'ESC' to pause/load/save/quit.",
                    "",
                    "Hints:",
                    "- Levers open/close doors or trigger other secrets.", 
                    "- You might lose hit points by fighting.",
                    "- Find potions to recover hit/magic points.",
                    "- Remember to equip the most powerful items.",
                    "- Explore methodically, you might find some clues...",
                    "- To fight while on a vehicle, first disembark."];  

  // Load tile types:
  this.defaultTileEntry = null;
  this.tiles = {};
  var tilesElement = gameElement.getElementsByTagName("tiles")[0];
  TILE_WIDTH_ORIGINAL = parseInt(tilesElement.attributes.getNamedItem("sourcewidth").nodeValue);
  TILE_HEIGHT_ORIGINAL = parseInt(tilesElement.attributes.getNamedItem("sourceheight").nodeValue);
  TARGET_TILE_WIDTH = TILE_WIDTH = parseInt(tilesElement.attributes.getNamedItem("targetwidth").nodeValue);
  TARGET_TILE_HEIGHT = TILE_WIDTH = parseInt(tilesElement.attributes.getNamedItem("targetheight").nodeValue);

  var coinPurseElement = tilesElement.getElementsByTagName("coinPurse")[0];
  COINPURSE_TILE = parseInt(coinPurseElement.attributes.getNamedItem("tile").nodeValue);
  var tileTypesElements = tilesElement.getElementsByTagName("types");
  for(var i = 0;i<tileTypesElements.length;i++) {
    var tmp = tileTypesElements[i];
    var name = tmp.attributes.getNamedItem("file").nodeValue;
    var types = eval("[" + tmp.textContent + "]");
    if (!(name in this.tiles)) {
      var te = {image:new Image()};
      te.image.onload = function() {generateRedTintedImage(te);};
      te.image.src = gamepath + name;
      this.tiles[name] = te;
      if (this.defaultTileEntry==null) this.defaultTileEntry = te;
    }
    var tilesEntry = this.tiles[name];  
    tilesEntry.tileTypes = types;  
  }
  var tileTypesElements = tilesElement.getElementsByTagName("seeThrough");
  for(var i = 0;i<tileTypesElements.length;i++) {
    var tmp = tileTypesElements[i];
    var name = tmp.attributes.getNamedItem("file").nodeValue;
    var seeThrough = eval("[" + tmp.textContent + "]");
    if (!(name in this.tiles)) {
      var te = {image:new Image()};
      te.image.onload = function() {generateRedTintedImage(te);};
      te.image.src = gamepath + name;
      this.tiles[name] = te;
      if (this.defaultTileEntry==null) this.defaultTileEntry = te;
    }
    var tilesEntry = this.tiles[name];  
    tilesEntry.seeThrough = seeThrough;  
  }
  var tileTypesElements = tilesElement.getElementsByTagName("canDig");
  for(var i = 0;i<tileTypesElements.length;i++) {
    var tmp = tileTypesElements[i];
    var name = tmp.attributes.getNamedItem("file").nodeValue;
    var canDig = eval("[" + tmp.textContent + "]");
    if (!(name in this.tiles)) {
      var te = {image:new Image()};
      te.image.onload = function() {generateRedTintedImage(te);};
      te.image.src = gamepath + name;
      this.tiles[name] = te;
      if (this.defaultTileEntry==null) this.defaultTileEntry = te;
    }
    var tilesEntry = this.tiles[name];  
    tilesEntry.canDig = canDig;  
  }

  // load conversation topics:
  this.defaultConversationTopics = [];

  // load npc/enemy/player definitions:
  console.log("Loading character types...");
//  this.characterTypeTable = {};
  var characterDefinitionElements = gameElement.getElementsByTagName("characterDefinition");
  for(var i = 0;i<characterDefinitionElements.length;i++) {
    var filePath = gamepath + characterDefinitionElements[i].attributes.getNamedItem("file").nodeValue;
    console.log("Loading characters from '" + filePath + "'...");
    this.loadCharacters(filePath, this);
  }

  console.log("Loading object types...");
//  this.characterTypeTable = {};
  var objectDefinitionElements = gameElement.getElementsByTagName("objectDefinition");
  for(var i = 0;i<objectDefinitionElements.length;i++) {
    var filePath = gamepath + objectDefinitionElements[i].attributes.getNamedItem("file").nodeValue;
    console.log("Loading objects from '" + filePath + "'...");
    this.loadObjects(filePath, this);
  }


  // load maps:
  this.mapCreationData = [];
  var mapElements = gameElement.getElementsByTagName("map");
  for(var i = 0;i<mapElements.length;i++) {
    if (mapElements[i].attributes.getNamedItem("file")!=null) {
      this.mapCreationData.push({type:"file", path:gamepath + mapElements[i].attributes.getNamedItem("file").nodeValue});
    } else {
      if (mapElements[i].attributes.getNamedItem("method")!=null) {
        this.mapCreationData.push({type:"method", method:mapElements[i].attributes.getNamedItem("method").nodeValue});
      } else {
        console.log("ERROR! map specified without a file or a creation method in game file!");
      }
    }
  }

  // load player starting positions:
  this.playerStartingPositions = [];
  var playerElements = gameElement.getElementsByTagName("player");
  for(var i = 0;i<playerElements.length;i++) {
    var playerElement = playerElements[i];
    this.playerStartingPositions.push({type : playerElement.attributes.getNamedItem("class").nodeValue,
                                       x : parseInt(playerElement.attributes.getNamedItem("x").nodeValue),
                                       y : parseInt(playerElement.attributes.getNamedItem("y").nodeValue),
                                       map : parseInt(playerElement.attributes.getNamedItem("map").nodeValue)});
  }

  parseAIRules(this,gameElement);
}

Game.draw = function() { 
  // clear background:
  var tmp = ctx.globalAlpha;
  ctx.globalAlpha = 1;
  ctx.fillStyle = '#000000';  
  ctx.beginPath();  
  ctx.rect(0, 0, canvas.width, canvas.height);  
  ctx.closePath();  
  ctx.fill();  
  ctx.globalAlpha = tmp;

  switch(this.state) {
    case GAMESTATE_TITLE: this.drawTitle();
            break;
    case GAMESTATE_GAME: this.drawGame();
            break;
    case GAMESTATE_GAME_OVER: this.drawGameOver();
            break;
    case GAMESTATE_ENDING: this.drawEnding();
            break;
    case GAMESTATE_INSTRUCTIONS: this.drawInstructions();
            break;
  }

/*
    var tmp = ctx.globalAlpha;
    ctx.globalAlpha = 1;
    ctx.fillStyle = "#ffffff";
    ctx.font = "11px Emulogic";  
    ctx.fillText("Mouse: " + mouseState.x + ", " + mouseState.y , 8, 16);
    ctx.fillText("Cycle: " + this.cycle + " (fps " + FPS + ")", 8, 32)
    ctx.fillText("State: " + this.state + "  State Cycle: " + this.state_cycle, 8, 48);
    ctx.globalAlpha = tmp;
*/
};

Game.update = function() { 
  var oldState = this.state;
  switch(this.state) {
    case GAMESTATE_TITLE: this.state = this.updateTitle();
            break;
    case GAMESTATE_GAME: this.state = this.updateGame();
            break;
    case GAMESTATE_GAME_OVER: this.state = this.updateGameOver();
            break;
    case GAMESTATE_ENDING: this.state = this.updateEnding();
            break;
    case GAMESTATE_INSTRUCTIONS: this.state = this.updateInstructions();
            break;
  }
  if (oldState==this.state) {
    this.state_cycle++;
  } else {
    this.state_cycle=0;
  }

  if (TILE_HEIGHT<TARGET_TILE_HEIGHT) {
    TILE_HEIGHT++;
    TILE_WIDTH = TILE_HEIGHT;
  }
  if (TILE_HEIGHT>TARGET_TILE_HEIGHT) {
    TILE_HEIGHT--;
    TILE_WIDTH = TILE_HEIGHT;
  }
};

Game.pushMessage = function(msg) {
  if (this.messages.length>0 && this.messages[this.messages.length-1][1] == msg) {
    this.messages[this.messages.length-1][0] = (new Date).getTime();
  } else {
    this.messages.push([(new Date).getTime(),msg]);  
    if (this.messages.length>4) {
      this.messages.splice(0,1);
    }
  }
}

Game.warpObject = function(object, x, y, map, layer) {
  old_map = this.maps[object.map];
  new_map = this.maps[map];

  // record the WARP for the perception module of the character AI:
  old_map.warps.push({x:object.x, y:object.y, object: object, timer: 200});

  old_map.removeObject(object);
  new_map.addObject(object,layer);

  object.x = x;
  object.y = y;
  object.map = map;
}

// 'topic' should be a list of two elements, something like: ["monks","There is a massive amount of monks in the Black rock!"]
Game.addConversationTopic = function(topic) {
  for(var i=0;i<this.conversationTopics.length;i++) {
    if (this.conversationTopics[i][0]==topic[0]) return;
  }
  this.conversationTopics.push(topic);
}


Game.resetGame = function() {
  this.gameComplete = 0;  // 0: not complete, 1: complete, but not shown to the player, 2: complete after shown to the player
  this.endingMenu = null;
  this.conversationTopics = [];
  for(var i = 0;i<this.defaultConversationTopics.length;i++) {
    this.conversationTopics.push(this.defaultConversationTopics[i]);
  }
  this.cycle = 0;
  this.paused = false;

  this.storyState = {}; // the story state is where all the variables used by the scripts are stored
  this.storyStateRulesLastCycleChecked = -1;
  this.storyStateLastCycleUpdated = -1;
  this.storyStateRuleActionQueue = [];  // when rules get triggered, they push their actions into this queue

  this.maps = createMaps(this.mapCreationData);
  this.characters = createCharacters(this.playerStartingPositions, this);
  this.currentCharacter = 0;
  this.messages = [];
  this.menuStack = [];
  this.pushMessage("You have entered " + this.maps[this.characters[this.currentCharacter].map].name);
  this.maps[this.characters[this.currentCharacter].map].updateVisibility(this.characters[this.currentCharacter].x,this.characters[this.currentCharacter].y);
}


// Load/Save game:
Game.saveGame = function(slot) {
  var saveID = this.gameName + "slot" + slot;
  var summaryString = this.generateSummarySaveString();
  var saveString = this.generateSaveString();
  if (saveString!=null) {
    localStorage.setItem(saveID+"summary",summaryString);
    localStorage.setItem(saveID,saveString);
    return true;
  }
  console.log("Error saving the game");
  return false;
}


Game.loadGame = function(slot) {
  var saveID = this.gameName + "slot" + slot;
  var saveString = localStorage.getItem(saveID);
  if (saveString==null) return;

  // decode string:
  this.decodeSaveString(saveString);
}


Game.getSaveGameSummary = function(slot) {
  var saveID = this.gameName + "slot" + slot;
  var data = localStorage.getItem(saveID + "summary");
  if (data==null) return "-";
  return data;
}


Game.generateSummarySaveString = function() {
  return "LVL" + this.characters[0].level + ", LVL" + this.characters[1].level;
}


Game.generateSaveString = function() {
  // encode game state:
  // encode the global Game variables:
  var data = {};
  data.cycle = this.cycle;
  data.endingMenu = this.endingMenu;
  data.conversationTopics = this.conversationTopics;
  data.storyState = this.StoryState;
  data.currentCharacter = this.currentCharacter;
  data.messages = this.messages;

  // encode the objects:
  var objects = [];
  objects = this.characters[0].generateObjectListForSaveData(objects);
  objects = this.characters[1].generateObjectListForSaveData(objects);
  for(var i = 0;i<this.maps.length;i++) {
    objects = this.maps[i].generateObjectListForSaveData(objects);
  }  
//  console.log("Objects: " + objects.length);

  data.characters = [objects.indexOf(this.characters[0]),objects.indexOf(this.characters[1])];

  // encode each of the maps:
  var mapsdata = [];
  for(var i = 0;i<this.maps.length;i++) {
    var mapdata = this.maps[i].generateSaveData(objects);
    mapsdata.push(mapdata);
  }
  data.maps = mapsdata;

  // encode each of the objects:
  var objectsdata = [];
  for(var i = 0;i<objects.length;i++) {
    var objectdata = objects[i].generateSaveData(objects,{});
    objectsdata.push(objectdata);
  }
  data.objects = objectsdata;

  try {
    var globalString = JSON.stringify(data);
    return globalString;
  } catch (err) {
    console.log(err);
    console.log("Error generating the save string!");
    /*
    findCircularityFeature(data);
    for(var i = 0;i<data.objects.length;i++) {
      if (data.objects[i].AI!=undefined)
        findCircularityFeature(data.objects[i].AI);
    }
    */
    return null;
  }
}

/*
function findCircularityFeature(p) {
  for(var propt in p) {
    try {
      JSON.stringify(p[propt]);
    } catch(err) {
      console.log("Circularity in " + propt);
    }
  }  
}
*/

Game.decodeSaveString = function(saveString) {
  var data = JSON.parse(saveString);
  if (data!=null) {
    // recover the state:
    this.resetGame();
    this.cycle = data.cycle;
    this.endingMenu = data.endingMenu;
    this.conversationTopics = data.conversationTopics;
    this.storyState = data.storyState;
    this.currentCharacter = data.currentCharacter;
    this.messages = data.messages;

    // decode the objects:
    // first pass (create dummy versions of each object, of the right class):
    var objects = [];
    for(var i = 0;i<data.objects.length;i++) {
      var otmp = data.objects[i];
      var tmp = "new " + otmp.className + "()";
      var o = eval(tmp);
      objects.push(o);
    }
    // second pass (set their properties):
    for(var i = 0;i<data.objects.length;i++) {
      var otmp = data.objects[i];
      var o = objects[i];
      for(var propt in otmp) {
        o[propt] = otmp[propt];
      }
      if (otmp.AI!=undefined) {
        o.AI = new HighLevelAI();
        o.AI.reconstructFromSaveData(objects, o, otmp.AI);
      }
    }
    // third pass (resolve references):
    for(var i = 0;i<objects.length;i++) {
      var o = objects[i];
      if (o.itemInside!=undefined) {
        if (o.itemInside==-1) {
          o.itemInside = null;
        } else {
          o.itemInside = objects[o.itemInside];
        }
      }
      if (o.owner!=undefined) {
        if (o.owner==-1) {
          o.owner = null;
        } else {
          o.owner = objects[o.owner];
        }
      }
      if (o.target!=undefined) {
        if (o.target==-1) {
          o.target = null;
        } else {
          o.target = objects[o.target];
        }
      }
      if (o.just_attacked_by!=undefined) {
        if (o.just_attacked_by==-1) {
          o.just_attacked_by = null;
        } else {
          o.just_attacked_by = objects[o.just_attacked_by];
        }
      }
      if (o.vehicle!=undefined) {
        if (o.vehicle==-1) {
          o.vehicle = null;
        } else {
          o.vehicle = objects[o.vehicle];
        }
      }
      if (o.inventory!=undefined) {
        for(var j = 0;j<o.inventory.length;j++) {
          if (o.inventory[j]==-1) {
            o.inventory[j] = null;
          } else {
            o.inventory[j] = objects[o.inventory[j]];
          }         
        }
      }
      if (o.equippedItems!=undefined) {
        for(var j = 0;j<o.equippedItems.length;j++) {
          if (o.equippedItems[j]==-1) {
            o.equippedItems[j] = null;
          } else {
            o.equippedItems[j] = objects[o.equippedItems[j]];
          }         
        }
      }
      if (o.posessions!=undefined) {
        for(var j = 0;j<o.posessions.length;j++) {
          if (o.posessions[j]==-1) {
            o.posessions[j] = null;
          } else {
            o.posessions[j] = objects[o.posessions[j]];
          }         
        }
      }      
      if (o.following!=undefined) {
        for(var j = 0;j<o.following.length;j++) {
          if (o.following[j]==-1) {
            o.following[j] = null;
          } else {
            o.following[j] = objects[o.following[j]];
          }         
        }
      }      
    }
    this.characters[0] = objects[data.characters[0]];
    this.characters[1] = objects[data.characters[1]];

    // decode the maps:
    for(var i = 0;i<data.maps.length;i++) {
      var mapdata = data.maps[i];
      var map = this.maps[i];
      map.visibility = mapdata.visibility;
      map.messages = mapdata.messages;
      map.respawnData = mapdata.respawnData;
      for(var j = 0;j<map.layers.length;j++) {
        var layerdata = mapdata.layers[j];
        var layer = map.layers[j];
        layer.tiles = layerdata.tiles;
        layer.types = layerdata.types;
        layer.seeThrough = layerdata.seeThrough;
        var tmp = [];
        for(var k = 0;k<layerdata.objects.length;k++) {
          tmp.push(objects[layerdata.objects[k]]);
        }
        layer.objects = tmp;
      }
    }
  }
}



function decodeRuleEffectFromXML(actionElement) {
  if (actionElement.tagName=="gameComplete") {
    return {action : actionElement.tagName};
  }
  if (actionElement.tagName=="addBehavior") {
    var priority = parseInt(actionElement.attributes.getNamedItem("priority").nodeValue);
    var behaviorName = actionElement.textContent;
    var id = actionElement.attributes.getNamedItem("id");
    if (id!=null) id = id.nodeValue;
    return {action : actionElement.tagName, 
            behaviorName : behaviorName,
            priority : priority,
            id : id};
  } 
  if (actionElement.tagName=="removeBehavior") {
    var id = actionElement.attributes.getNamedItem("id").nodeValue;
    return {action : actionElement.tagName,
            id: id};
  }
  if (actionElement.tagName=="teleport") {
    var xstr = eval(actionElement.attributes.getNamedItem("x").nodeValue);
    var ystr = eval(actionElement.attributes.getNamedItem("y").nodeValue);
    var mapstr = actionElement.attributes.getNamedItem("map");    
    if (mapstr!=null) mapstr = eval(mapstr.nodeValue);
    return {action : actionElement.tagName, 
        x : xstr,
        y : ystr,
        map : mapstr};
  } 
  if (actionElement.tagName=="goTo") {
    var xstr = eval(actionElement.attributes.getNamedItem("x").nodeValue);
    var ystr = eval(actionElement.attributes.getNamedItem("y").nodeValue);
    var mapstr = actionElement.attributes.getNamedItem("map");    
    if (mapstr!=null) mapstr = eval(mapstr.nodeValue);
    return {action : actionElement.tagName, 
        x : xstr,
        y : ystr,
        map : mapstr};
  } 
  if (actionElement.tagName=="die") {
    return {action : actionElement.tagName};
  }
  if (actionElement.tagName=="activateLever") {
    var xstr = eval(actionElement.attributes.getNamedItem("x").nodeValue);
    var ystr = eval(actionElement.attributes.getNamedItem("y").nodeValue);
    var mapstr = actionElement.attributes.getNamedItem("map");    
    if (mapstr!=null) mapstr = eval(mapstr.nodeValue);
    return {action : actionElement.tagName, 
        x : xstr,
        y : ystr,
        map : mapstr};
  } 
  if (actionElement.tagName=="message") {
    return {action : actionElement.tagName,
            text : actionElement.attributes.getNamedItem("text").nodeValue};
  }
  if (actionElement.tagName=="talk") {
    var subActions = [];
    for(var j = 0;j<actionElement.childNodes.length;j++) {
      var subActionElement = actionElement.childNodes[j];
      var subAction = null;
      if (subActionElement.tagName!=undefined) subAction = this.decodeRuleEffectFromXML(subActionElement);
      if (subAction!=null) subActions.push(subAction);
    }
    return {action : actionElement.tagName, 
        text : actionElement.attributes.getNamedItem("text").nodeValue,
        IP: 0,
        subactions: subActions};
  } 
  if (actionElement.tagName=="pendingTalk") {
    var subActions = [];
    for(var j = 0;j<actionElement.childNodes.length;j++) {
      var subActionElement = actionElement.childNodes[j];
      var subAction = null;
      if (subActionElement.tagName!=undefined) subAction = this.decodeRuleEffectFromXML(subActionElement);
      if (subAction!=null) subActions.push(subAction);
    }
    return {action : actionElement.tagName, 
        character : actionElement.attributes.getNamedItem("character").nodeValue,
        text : actionElement.attributes.getNamedItem("text").nodeValue,
        IP: 0,
        subactions: subActions};
  }
  if (actionElement.tagName=="addTopic") {
    return {action : actionElement.tagName, 
        topic : actionElement.attributes.getNamedItem("topic").nodeValue,
        text : actionElement.attributes.getNamedItem("text").nodeValue};
  }
  if (actionElement.tagName=="updateConversationRule") {
    var subActions = [];
    for(var j = 0;j<actionElement.childNodes.length;j++) {
      var subActionElement = actionElement.childNodes[j];
      var subAction = null;
      if (subActionElement.tagName!=undefined) subAction = this.decodeRuleEffectFromXML(subActionElement);
      if (subAction!=null) subActions.push(subAction);
    }
    return {action : actionElement.tagName, 
        topic : actionElement.attributes.getNamedItem("topic").nodeValue,
        effects : subActions};
  }
  if (actionElement.tagName=="storyState") {
    return {action : actionElement.tagName,
        scope : actionElement.attributes.getNamedItem("scope").nodeValue,
        variable : actionElement.attributes.getNamedItem("variable").nodeValue,
        value : actionElement.attributes.getNamedItem("value").nodeValue};
  }
  if (actionElement.tagName=="addLongTermWME") {
    return {action : actionElement.tagName,
        type : actionElement.attributes.getNamedItem("type").nodeValue,
        params : actionElement.attributes.getNamedItem("params").nodeValue};
  }
  if (actionElement.tagName=="addLongTermWMEToOthers") {
    return {action : actionElement.tagName,
        characterClass : actionElement.attributes.getNamedItem("class").nodeValue,
        select : actionElement.attributes.getNamedItem("select").nodeValue,
        type : actionElement.attributes.getNamedItem("type").nodeValue,
        params : actionElement.attributes.getNamedItem("params").nodeValue};
  }
  if (actionElement.tagName=="steal") {
    return {action : actionElement.tagName,
        itemName : actionElement.attributes.getNamedItem("name").nodeValue};
  }
  if (actionElement.tagName=="give") {
    var inventory = actionElement.attributes.getNamedItem("inventory");
    var newItem = actionElement.attributes.getNamedItem("new");
    if (inventory!=null) inventory = inventory.nodeValue;
    if (newItem!=null) newItem = newItem.nodeValue;
    return {action : actionElement.tagName,
        inventory : inventory,
        newItem : newItem};
  }
  if (actionElement.tagName=="drop") {
    var inventory = actionElement.attributes.getNamedItem("inventory");
    var newItem = actionElement.attributes.getNamedItem("new");
    if (inventory!=null) inventory = inventory.nodeValue;
    if (newItem!=null) newItem = newItem.nodeValue;
    return {action : actionElement.tagName,
        inventory : inventory,
        newItem : newItem};
  }
  if (actionElement.tagName=="loseItem") {
    var inventory = actionElement.attributes.getNamedItem("inventory");
    if (inventory!=null) inventory = inventory.nodeValue;
    return {action : actionElement.tagName,
        inventory : inventory};
  }
  if (actionElement.tagName=="gainItem") {
    var newItem = actionElement.attributes.getNamedItem("new");
    if (newItem!=null) newItem = newItem.nodeValue;
    return {action : actionElement.tagName,
        newItem : newItem};
  }
  if (actionElement.tagName=="experienceGain") {
    var xp = actionElement.attributes.getNamedItem("xp");
    if (xp!=null) xp = eval(xp.nodeValue);
    return {action : actionElement.tagName, xp : xp};
  }
  if (actionElement.tagName=="if") {
    var conditionElement = actionElement.getElementsByTagName("condition");
    var thenElement = actionElement.getElementsByTagName("then");
    var elseElement = actionElement.getElementsByTagName("else");
    var subActionsCondition = [];
    var subActionsThen = [];
    var subActionsElse = [];
    if (conditionElement.length>0) {
      conditionElement = conditionElement[0];
      for(var j = 0;j<conditionElement.childNodes.length;j++) {
        var subActionElement = conditionElement.childNodes[j];
        var subAction = null;
        if (subActionElement.tagName!=undefined) subAction = this.decodeRuleEffectFromXML(subActionElement);
        if (subAction!=null) subActionsCondition.push(subAction);
      }
    }
    if (thenElement.length>0) {
      thenElement = thenElement[0];
      for(var j = 0;j<thenElement.childNodes.length;j++) {
        var subActionElement = thenElement.childNodes[j];
        var subAction = null;
        if (subActionElement.tagName!=undefined) subAction = this.decodeRuleEffectFromXML(subActionElement);
        if (subAction!=null) subActionsThen.push(subAction);
      }
    }
    if (elseElement.length>0) {
      elseElement = elseElement[0];
      for(var j = 0;j<elseElement.childNodes.length;j++) {
        var subActionElement = elseElement.childNodes[j];
        var subAction = null;
        if (subActionElement.tagName!=undefined) subAction = this.decodeRuleEffectFromXML(subActionElement);
        if (subAction!=null) subActionsElse.push(subAction);
      }
    }
    return {action: actionElement.tagName,
        conditionResult: undefined,
        conditionIP: 0, // these are "instruction pointers", to keep track of execution state
        thenIP: 0,
        elseIP: 0,
        conditionActions: subActionsCondition,
        thenActions: subActionsThen,
        elseActions: subActionsElse}
  }
}


function executeRuleEffect(action, character, map, game, otherCharacter) {
  if (action.action=="gameComplete") {
    console.log("gameComplete executed on " + game);
    if (game.gameComplete==0) game.gameComplete = 1;
    return true;
  }
  if (action.action=="addBehavior") {
    var tmp = {priority: action.priority, id:action.id, behavior: eval("new " + action.behaviorName)};
    var bname = action.behaviorName.split("(")[0];
    tmp.className = bname;
    character.AI.rules.push(tmp);
    return true;
  }
  if (action.action=="removeBehavior") {
      for(var i = 0;i<character.AI.rules.length;i++) {
        var b = character.AI.rules[i];
        if (b.id==action.id) {
          character.AI.rules.splice(i,1);
          return true;
        }
      }
    return false;     
  }
  if (action.action=="teleport") {
    console.log(character.layer);
    if (action.map==null) {
      game.warpObject(character, action.x, action.y, character.map, character.layer);
      return true;
    } else {
      game.warpObject(character, action.x, action.y, action.map, character.layer);
      return true;
    }
  }
  if (action.action=="goTo") {
    var b = null;
    if (action.map==null) {
      if (character.x==action.x && character.y==action.y) return true;
      b = new BRGoTo(action.x,action.y,character.map);
    } else {
      if (character.x==action.x && character.y==action.y && character.map==action.map) return true;
      b = new BRGoTo(action.x,action.y,action.map);
    }
    var action = b.update(character, map, character.time, game);
    if (action!=null) character.sendAction(action,map,game);
    return undefined;
  }
  if (action.action=="die") {
    character.hp = 0;
    return true;
  }
  if (action.action=="activateLever") {
    var b = null;
    if (action.map==null) {
      if (character.x==action.x && character.y==action.y) {
        character.sendAction({type:ACTION_TAKE},map,game);
        return true;
      }
      b = new BRGoTo(action.x,action.y,character.map);
    } else {
      if (character.x==action.x && character.y==action.y && character.map==action.map) {
        character.sendAction({type:ACTION_TAKE},map,game);
        return true;
      }
      b = new BRGoTo(action.x,action.y,action.map);
    }
    var action = b.update(character, map, character.time, game);
    if (action!=null) character.sendAction(action,map,game);
    return undefined;
  }
  if (action.action=="message") {
    game.pushMessage(action.text);
    return true;
  }
  if (action.action=="talk") {
    if (action.IP<action.subactions.length) {
      var subaction = action.subactions[action.IP];
      var retVal = executeRuleEffect(subaction, character, map, game, otherCharacter);
      // when "executeRuleEffect" returns "undefined", it means that the action has not completed execution, so we have to keep it:
      if (retVal!=undefined) action.IP++;
      if (action.IP>=action.subactions.length) {
        character.sendAction({type:ACTION_TALK, 
                    performative:{type:action.text, target:otherCharacter}}, 
                map, game);
        return true;
      }
      return undefined;
    } else {
      character.sendAction({type:ACTION_TALK, 
                  performative:{type:action.text, target:otherCharacter}}, 
              map, game);
      return true;
    }
  } 
  if (action.action=="pendingTalk") {
    var pt = null;
    for(var i = 0;i<character.pendingTalk.length;i++) {
      if (character.pendingTalk[i].character == action.character) {
        pt = character.pendingTalk[i];
        break;
      }
    }
    if (pt==null) {
      pt = {character:action.character, list:[]};
      character.pendingTalk.push(pt);
    }
    pt.list.push({action:"talk",
            text : action.text,
            IP: 0,
            subactions: action.subactions});
    return true;
  }
  if (action.action=="addTopic") {
    // if character==null, it means that the script is executed form a map or from the game itself:
    // otherwise, if it's from a character, we only want to add the topic if one of the characters is a player
    if (character==null ||      
        (character instanceof PlayerCharacter) ||
        (otherCharacter instanceof PlayerCharacter)) {
        game.addConversationTopic([action.topic, action.text]);
    }
    return true;
  } 
  if (action.action=="updateConversationRule") {
    character.conversationKnowledge[action.topic] = action.effects;
    return true;
  }
  if (action.action=="storyState") {
    if (action.scope=="game") {
      game.storyState[action.variable] = action.value;
      game.storyStateLastCycleUpdated = game.cycle;
    } else if (action.scope=="map") {
      map.storyState[action.variable] = action.value;
      map.storyStateLastCycleUpdated = game.cycle;
    } else if (action.scope=="character") {
      character.storyState[action.variable] = action.value;
      character.storyStateLastCycleUpdated = game.cycle;
    } else {
      console.log("scope of storyState is invalid (should be game, map or character): " + action.scope);
    }
    return true;
  }
  if (action.action=="addLongTermWME") {
    character.AI.longTermMemory.push({type:action.type, params:action.params, time:character.time});
    return true;
  }
  if (action.action=="addLongTermWMEToOthers") {
    var objectwmes = character.AI.getAllWMEs("object");
    for(var i = 0;i<objectwmes.length;i++) {
      var wme = objectwmes[i];
      if (eval("wme.params instanceof " + action.characterClass)) {
        var target = wme.params;
        target.AI.longTermMemory.push({type:action.type, params:action.params, time:character.time});
        if (action.select=="first") break;
      }
    }
    return true;
  } 
  if (action.action=="steal") {
    var found = -1;
    for(var j = 0;j<otherCharacter.inventory.length;j++) {
      var item = otherCharacter.inventory[j];
      if (item.name==action.itemName) {
        found = j;
      }
    }
    if (found>=0) {
      var item = otherCharacter.inventory[found];
      otherCharacter.inventory.splice(found,1);
      character.inventory.push(item);
      return true;
    }
    return false;
  } 
  if (action.action=="give") {
    if (action.inventory!=null) {
      for(var i = 0;i<character.inventory.length;i++) {
        if (character.inventory[i].name==action.inventory) {
          var item = character.inventory[i];
          character.inventory.splice(i,1);
          game.warpObject(item, otherCharacter.x, otherCharacter.y, otherCharacter.map, 1);
          return true;
        }
      }
      return false;
    } 
    if (action.newItem!=null) {
      game.warpObject(eval(action.newItem), 
              otherCharacter.x, otherCharacter.y, otherCharacter.map, 1);
      return true;
    }
    return false;
  }
  if (action.action=="drop") {
    if (action.inventory!=null) {
      for(var i = 0;i<character.inventory.length;i++) {
        if (character.inventory[i].name==action.inventory) {
          var item = character.inventory[i];
          character.inventory.splice(i,1);
          game.warpObject(item, character.x, character.y, character.map, 1);
          return true;
        }
      }
      return false;
    } 
    if (action.newItem!=null) {
      game.warpObject(eval(action.newItem), character.x, character.y, character.map, 1);
      return true;
    }
    return false;
  }   
  if (action.action=="loseItem") {
    for(var i = 0;i<character.inventory.length;i++) {
      if (character.inventory[i].name==action.inventory) {
        var item = character.inventory[i];
        character.inventory.splice(i,1);
        return true;
      }
    }
    return false;
  }   
  if (action.action=="gainItem") {
    if (action.newItem!=null && character.inventory.length<INVENTORY_SIZE) {
      character.inventory.push(eval(action.newItem));
      return true;
    }
    return false;
  }   
  if (action.action=="experienceGain") {
    if (character instanceof PlayerCharacter) {
      character.experienceGain(action.xp,map);
      return true;
    } else {
      return false;
    }
  }
  if (action.action=="if") {
    if (action.conditionResult == undefined) {
      var action2 = action.conditionActions[action.conditionIP];
      var retVal = executeRuleEffect(action2, character, map, game, otherCharacter);
      // when "executeRuleEffect" returns "undefined", it means that the action has not completed execution, so we have to keep it:
      if (retVal!=undefined) action.conditionIP++;
      if (action.conditionIP>=action.conditionActions.length) action.conditionResult = retVal;
      return undefined;
    } else if (action.conditionResult == true) {
      var action2 = action.thenActions[action.thenIP];
      var retVal = executeRuleEffect(action2, character, map, game, otherCharacter);
      // when "executeRuleEffect" returns "undefined", it means that the action has not completed execution, so we have to keep it:
      if (retVal!=undefined) action.thenIP++;
      if (action.thenIP>=action.thenActions.length) {
        // reset the action:
        action.conditionResult = undefined;
        action.conditionIP = 0;
        action.thenIP = 0;
        action.elseIP = 0;
        return retVal;
      }
      return undefined;
    } else {
      var action = action.elseActions[action.elseIP];
      var retVal = executeRuleEffect(action, character, map, game, otherCharacter);
      // when "executeRuleEffect" returns "undefined", it means that the action has not completed execution, so we have to keep it:
      if (retVal!=undefined) action.elseIP++;
      if (action.elseIP>=action.elseActions.length) {
        // reset the action:
        action.conditionResult = undefined;
        action.conditionIP = 0;
        action.thenIP = 0;
        action.elseIP = 0;
        return retVal;
      }
      return undefined;
    }
    return true;
  }
  return false;
} 


function initAIRules(object) {
  object.onStart = [];
  object.onEnd = [];
  object.onUse = [];
  object.storyState = {};
  object.storyStateRules = [];
  object.storyStateLastCycleUpdated = -1;
  object.storyStateRulesLastCycleChecked = -1;
  object.storyStateRuleActionQueue = [];  // when rules get triggered, they push their actions into this queue  
}

function parseAIRules(object, xml) {
  initAIRules(object);

  // story state rules:
  var storystateElements = xml.getElementsByTagName("storyStateRule");
  for(var i = 0;i<storystateElements.length;i++) {
    var storystateElement = storystateElements[i];
    var actions = [];
    for(var j = 0;j<storystateElement.childNodes.length;j++) {
      var actionElement = storystateElement.childNodes[j];
      var action = null;
      if (actionElement.tagName!=undefined) action = decodeRuleEffectFromXML(actionElement);
      if (action!=null) actions.push(action);
    }
    var once = false;
    var tmp = storystateElement.attributes.getNamedItem("once");
    if (tmp!=null) once = eval(tmp.nodeValue);
    object.storyStateRules.push({scope : storystateElement.attributes.getNamedItem("scope").nodeValue,
                   variable : storystateElement.attributes.getNamedItem("variable").nodeValue,
                   value : storystateElement.attributes.getNamedItem("value").nodeValue,
                   once : once,
                   triggered : false,
                   actions: actions});
  }

  // things to execute at the start/end:
  var onStartElements = xml.getElementsByTagName("onStart");
  for(var i = 0;i<onStartElements.length;i++) {
    var onStartElement = onStartElements[i];
    for(var j = 0;j<onStartElement.childNodes.length;j++) {
      var actionElement = onStartElement.childNodes[j];
      var action = null;
      if (actionElement.tagName!=undefined) action = decodeRuleEffectFromXML(actionElement);
      if (action!=null) object.onStart.push(action);
    }
  }
  var onEndElements = xml.getElementsByTagName("onEnd");
  for(var i = 0;i<onEndElements.length;i++) {
    var onEndElement = onEndElements[i];
    for(var j = 0;j<onEndElement.childNodes.length;j++) {
      var actionElement = onEndElement.childNodes[j];
      var action = null;
      if (actionElement.tagName!=undefined) action = decodeRuleEffectFromXML(actionElement);
      if (action!=null) object.onEnd.push(action);
    }
  }
  var onUseElements = xml.getElementsByTagName("onUse");
  for(var i = 0;i<onUseElements.length;i++) {
    var onUseElement = onUseElements[i];
    for(var j = 0;j<onUseElement.childNodes.length;j++) {
      var actionElement = onUseElement.childNodes[j];
      var action = null;
      if (actionElement.tagName!=undefined) action = decodeRuleEffectFromXML(actionElement);
      if (action!=null) object.onUse.push(action);
    }
  }
}
