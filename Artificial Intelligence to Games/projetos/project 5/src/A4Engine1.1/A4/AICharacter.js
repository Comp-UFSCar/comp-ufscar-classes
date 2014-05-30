AICharacter.prototype = new Character();
function AICharacter() {

	this.respawn = 0.0;
	this.picky = true;

	// This list is used to store all the messages received from other characters. They are stored in this list
	// as soon as the original character starts saying the message, and stay here until the text bubble disappears,
	// then they are processed.
	this.delayedMessages = null;

	// This variable holds a list of the "conversation engines", one per conversation the character is having. 
	// If the constructor of the character leaves this set fo null, the AI will assume this character cannot talk
	this.conversationEngines = null;

	// list of actions that will be executed as soon as the character is created and when the character dies:
	this.onStart = [];
	this.onEnd = [];

	// list of things this character wants to say to the other characters in the game:
	this.pendingTalk = [];

	this.storyState = {};	// the story state is where all the variables used by the scripts are stored
	this.storyStateLastCycleUpdated = -1;
	this.storyStateRules = [];
	this.storyStateRulesLastCycleChecked = -1;
	this.storyStateRuleActionQueue = [];	// when rules get triggered, they push their actions into this queue


	this.generateSaveData = function(objects, data) {
		data = AICharacter.prototype.generateSaveData.call(this,objects,data);
		
		data.respawn = this.respawn;
		// data.delayedMessages = ???
		data.picky = this.picky;
		data.onStart = this.onStart;
		data.onEnd = this.onEnd;
		data.pendingTalk = this.pendingTalk;
		data.storyState = this.storyState;
		data.storyStateLastCycleUpdated = this.storyStateLastCycleUpdated;
		data.storyStateRules = this.storyStateRules;
		data.storyStateRulesLastCycleChecked = this.storyStateRulesLastCycleChecked;
		data.storyStateRuleActionQueue = this.storyStateRuleActionQueue;

		// NPC/Enemy data:
		if (this.home!=undefined) data.home = this.home;
		if (this.respawnID!=undefined) data.respawnID = this.respawnID;
		if (this.respawnMap!=undefined) data.respawnMap = this.respawnMap;

		data.AI = this.AI.generateSaveData(objects, {});

		return data;
	}


	this.decodeCharacterFromXML = function() {
		AICharacter.prototype.decodeCharacterFromXML.call(this);

		this.AI = new HighLevelAI();
		this.conversationEngines = [];
		this.delayedMessages = [];
		this.conversationKnowledge = {};
		this.pendingTalk = [];

		// conversation rules:
		var conversationElements = this.xml.getElementsByTagName("conversationRule");
		for(var i = 0;i<conversationElements.length;i++) {
			var conversationElement = conversationElements[i];
			var actions = [];
			for(var j = 0;j<conversationElement.childNodes.length;j++) {
				var actionElement = conversationElement.childNodes[j];
				var action = null;
				if (actionElement.tagName!=undefined) action = decodeRuleEffectFromXML(actionElement);
				if (action!=null) actions.push(action);
			}
			this.conversationKnowledge[conversationElement.attributes.getNamedItem("topic").nodeValue] = actions;
		}

		parseAIRules(this,this.xml);

		var behaviorElements = this.xml.getElementsByTagName("behavior");
		for(var i = 0;i<behaviorElements.length;i++) {
			var behaviorElement = behaviorElements[i];
			var priority = parseInt(behaviorElement.attributes.getNamedItem("priority").nodeValue);
			var behaviorName = behaviorElement.textContent;
		    var id = behaviorElement.attributes.getNamedItem("id");
		    if (id!=null) id = id.nodeValue;
			var tmp = {priority: priority, id: id, behavior: eval("new " + behaviorName)};
			this.AI.rules.push(tmp);
		}		
	}	


	this.walkable = function () {
		return false;
	};

	this.draw = function(alpha,offsetx,offsety,game) {
		AICharacter.prototype.draw.call(this,alpha,offsetx,offsety,game);

		if (this.AI==null) return;

		var tile = null;
		var bestActivation = 0;
		var curious = this.AI.getWME("curious");
		var scared = this.AI.getWME("scared");
		var angry = this.AI.getWME("angry");
		var tired = this.AI.getWME("tired");
		var happy = this.AI.getWME("happy");

		if (curious!=null) {
			tile = 74;
			bestActivation = curious.activation;
		}
		if (scared!=null) {
			if (tile==null || scared.activation>bestActivation) {
				tile = 84;
				bestActivation = scared.activation;
			}
		}
		if (angry!=null) {
			if (tile==null || angry.activation>bestActivation) {
				tile = 94;
				bestActivation = angry.activation;
			}
		}
		if (tired!=null) {
			if (tile==null || tired.activation>bestActivation) {
				tile = 104;
				bestActivation = tired.activation;
			}
		}
		if (happy!=null) {
			if (tile==null || happy.activation>bestActivation) {
				tile = 75;
				bestActivation = happy.activation;
			}
		}

		if (tile!=null && (Math.floor(this.time/8)%2)==0) {
			var offs = 0;
			if (this.state==STATE_MOVING) offs = Math.floor(TILE_HEIGHT*(this.walk_speed-this.state_timer)/this.walk_speed);

			switch(this.direction) {
				case DIRECTION_UP:    drawTile(tile, this.x*TILE_WIDTH + offsetx, this.y*TILE_HEIGHT + offs + offsety,alpha); break;
				case DIRECTION_RIGHT: drawTile(tile, this.x*TILE_WIDTH - offs + offsetx,this.y*TILE_HEIGHT + offsety,alpha); break;
				case DIRECTION_DOWN:  drawTile(tile, this.x*TILE_WIDTH + offsetx,this.y*TILE_HEIGHT - offs + offsety,alpha); break;
				case DIRECTION_LEFT:  drawTile(tile, this.x*TILE_WIDTH + offs + offsetx,this.y*TILE_HEIGHT + offsety,alpha); break;
			}
		}
	}	

	this.update = function(map, game) {
		if (this.onStart.length>0) {
			var retVal = executeRuleEffect(this.onStart[0], this, map, game, null);
			if (retVal!=undefined) this.onStart.splice(0,1);
		}

		// check for story state rules:
		if (game.storyStateLastCycleUpdated > this.storyStateRulesLastCycleChecked ||
			map.storyStateLastCycleUpdated > this.storyStateRulesLastCycleChecked ||
			this.storyStateLastCycleUpdated > this.storyStateRulesLastCycleChecked) {
			for(var i = 0;i<this.storyStateRules.length;i++) {
				var rule = this.storyStateRules[i];
				if (!rule.once || !rule.triggered) {
					var triggered = false;
					if (rule.scope=="game") {
						if (game.storyState[rule.variable] == rule.value) triggered = true;
					} else if (rule.scope=="map") {
						if (map.storyState[rule.variable] == rule.value) triggered = true;
					} else if (rule.scope=="character") {
						if (this.storyState[rule.variable] == rule.value) triggered = true;
					} else {
						console.log("storyStateRule has an incorrect scope (should be game, map, character): " + rule.scope);
					}
					if (triggered) {
						for(var j = 0;j<rule.actions.length;j++) {
							this.storyStateRuleActionQueue.push(rule.actions[j]);
						}
					}
				}
			}
		}
		if(this.storyStateRuleActionQueue.length>0) {
			var retVal = executeRuleEffect(this.storyStateRuleActionQueue[0], this, map, game, null);
			if (retVal!=undefined) this.storyStateRuleActionQueue.splice(0,1);
		}

		if (this.state==STATE_READY) {
//			if (this.AI==undefined) console.log("No AI for character: " + this.name);
			this.AI.perceive(this, map, this.time);
			var action = this.AI.update(this, map, this.time, game);
			if (action!=null) {
				this.conversationEngines=[]; // clear all the conversation engines
				this.sendAction(action,map,game);
			}
		}
		this.time++;

		if (this.respawn>0.0) {
			var tmp = game.maps[this.respawnMap].respawnData[this.respawnID];
//			if (tmp == null) {
//				console.log("Respawn list: " + map.respawnData.length + " ID: " + this.respawnID + " class: " + this.getClassName());
//			}
			tmp.lastCycleAlive = game.cycle;
		}

		if (this.conversationEngines!=null) {
			var toDelete = [];
			var msg = this.updateDelayedMessages();
			var otherCharacter = null;
			if (msg!=null) {
//				console.log(this.name + " received a message from " + msg.character.name);
				otherCharacter = msg.character;
			}
			for(var idx = 0;idx<this.conversationEngines.length;idx++) {
				var ce = this.conversationEngines[idx];
				if (otherCharacter == ce.otherCharacter) {
					if (!ce.update(msg,map,game)) toDelete.push(ce);
					msg = null;
				} else {
					if (!ce.update(null,map,game)) toDelete.push(ce);
				}
			}
			if (msg!=null) {
				// this means the message was not sent to any ConversationEngine:
				var ce = new ConversationEngine(this, otherCharacter);
				this.conversationEngines.push(ce);
				if (!ce.update(msg,map,game)) toDelete.push(ce);
			}
			for(var i = 0;i<toDelete.length;i++) {
				this.conversationEngines.splice(this.conversationEngines.indexOf(toDelete[i]),1);
			}
		}

		var retVal = AICharacter.prototype.update.call(this, map, game);
		if (retVal==false) {
			while(this.onEnd.length>0) {
				var retVal = executeRuleEffect(this.onEnd[0], this, map, game, null);
				if (retVal!=undefined) this.onEnd.splice(0,1);
			}
		}
		return retVal;
	}		

	this.getConversationEngineForCharacter = function(otherCharacter) {
		for(var idx = 0;idx<this.conversationEngines.length;idx++) {
			var ce = this.conversationEngines[idx];
			if (otherCharacter == ce.otherCharacter) return ce;
		}
		return null;
	}

	this.receiveDelayedMessage = function(performative, character, delay) {
//		console.log(this.name + " enqueing a delayed message from " + character.name);
		this.delayedMessages.push({performative:performative, character:character, delay:delay});
	}

	// notifies that trading is over with character 'character':
	this.endOfTrace = function(character) {
		for(var idx = 0;idx<this.conversationEngines.length;idx++) {
			var ce = this.conversationEngines[idx];
			if (ce.otherCharacter == character) {
				if (ce.state = CONVERSATION_STATE_IN_TRADE) {
					ce.state = CONVERSATION_STATE_IN_CONVERSATION;
					ce.state_timer = 0;
				}
			}
		}		
	}

	this.updateDelayedMessages = function() {
		for(var i = 0;i<this.delayedMessages.length;i++) {
			var msg = this.delayedMessages[i];
			msg.delay--;
			if (msg.delay<=0) {
				this.delayedMessages.splice(i,1);
				// receive message!
				return msg;
			}
		}
		return null;
	}
}


NPC.prototype = new AICharacter();
function NPC() {
}


Enemy.prototype = new AICharacter();
function Enemy() {
	this.respawn = 1.0;
}


