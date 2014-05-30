function Animation(xml) {
	// parse the xml:
	this.dx = parseInt(xml.attributes.getNamedItem("dx").nodeValue);
	this.dy = parseInt(xml.attributes.getNamedItem("dy").nodeValue);
	this.period = 1;
	var tmp = xml.attributes.getNamedItem("period");
	if (tmp!=null) this.period = parseInt(tmp.nodeValue);
	this.file = xml.attributes.getNamedItem("file").nodeValue;
	this.looping = eval(xml.attributes.getNamedItem("looping").nodeValue);
	this.imageEntry = undefined;
	this.sequence = [];
	tmp = xml.textContent.split(",");
	for(var i = 0;i<tmp.length;i++) {
		this.sequence.push(parseInt(tmp[i]));
	}

	// reset the animation:
	this.cycle = 0;
	this.state = 0;

	this.reset = function() {
		this.cycle = 0;
		this.state = 0;
	}

	this.update = function() {
		this.cycle++;
		if (this.cycle>=this.period) {
			this.cycle-=this.period;
			this.state++;
			if (this.state>=this.sequence.length) {
				if (this.looping) this.state = 0;
							 else this.state = this.sequence.length-1;
			}
		}
	}

	this.getWidth = function() {
		return this.dx;
	}

	this.getHeight = function() {
		return this.dy;
	}

	this.draw = function(x,y,alpha,game) {
		if (this.imageEntry==undefined) this.imageEntry = game.tiles[this.file];
		if (this.imageEntry==undefined) this.imageEntry = Game.defaultTileEntry;
		for(var i = 0;i<this.dy;i++) {
			for(var j = 0;j<this.dx;j++) {
				drawTile(this.sequence[this.state]+j+i*this.imageEntry.tilesPerRow,x+j*TILE_WIDTH,y+i*TILE_HEIGHT,alpha,this.imageEntry);			
			}
		}
	}

	this.drawRedTinted = function(x,y,alpha,game) {
		if (this.imageEntry==undefined) this.imageEntry = game.tiles[this.file]
		if (this.imageEntry==undefined) this.imageEntry = Game.defaultTileEntry;
		for(var i = 0;i<this.dy;i++) {
			for(var j = 0;j<this.dx;j++) {
				drawRedTintedTile(this.sequence[this.state]+j+i*this.imageEntry.tilesPerRow,x+j*TILE_WIDTH,y+i*TILE_HEIGHT,alpha,this.imageEntry);			
			}
		}
	}

}


function generateRedTintedImage(imageEntry) 
{
	var tmpCanvas = document.createElement('canvas');
	tmpCanvas.width = imageEntry.image.width;
	tmpCanvas.height = imageEntry.image.height;
//	console.log("Tmp canvas size: " + tmpCanvas.width + "," + tmpCanvas.height);
	var tmpContext = tmpCanvas.getContext('2d');
	tmpContext.drawImage(imageEntry.image, 0, 0 );
	var tmpData = tmpContext.getImageData(0, 0, imageEntry.image.width, imageEntry.image.height);
	for (var i=0;i<tmpData.data.length;i+=4)
	  {
	  if (tmpData.data[i+3]!=0) {
		  tmpData.data[i]=255;
		  tmpData.data[i+1]=0;
		  tmpData.data[i+2]=0;
		  tmpData.data[i+3]=255;
	  } else {
		  tmpData.data[i]=0;
		  tmpData.data[i+1]=0;
		  tmpData.data[i+2]=0;
		  tmpData.data[i+3]=0;
	  }
	}
	tmpContext.putImageData(tmpData,0,0);
	imageEntry.imageRedTinted = new Image();
	imageEntry.imageRedTinted.src = tmpCanvas.toDataURL("image/png");
	imageEntry.tilesPerRow = imageEntry.image.width/TILE_WIDTH_ORIGINAL;
}


function drawTile(tile, x, y, alpha, imageEntry) {
	if (imageEntry==undefined) imageEntry = Game.defaultTileEntry;
	var tmp = ctx.globalAlpha;
	var gridx = (tile%imageEntry.tilesPerRow)*TILE_WIDTH_ORIGINAL;
	var gridy = Math.floor(tile/imageEntry.tilesPerRow)*TILE_HEIGHT_ORIGINAL;

	ctx.globalAlpha *= alpha;
	ctx.drawImage(imageEntry.image, gridx, gridy, TILE_WIDTH_ORIGINAL, TILE_HEIGHT_ORIGINAL, x, y, TILE_WIDTH, TILE_HEIGHT);
	ctx.globalAlpha = tmp;
}

function drawRedTintedTile(tile, x, y, alpha, imageEntry) {
	if (imageEntry==undefined) imageEntry = Game.defaultTileEntry;
	var tmp = ctx.globalAlpha;
	var gridx = (tile%imageEntry.tilesPerRow)*TILE_WIDTH_ORIGINAL;
	var gridy = Math.floor(tile/imageEntry.tilesPerRow)*TILE_HEIGHT_ORIGINAL;

	ctx.globalAlpha *= alpha;
	ctx.drawImage(imageEntry.imageRedTinted, gridx, gridy, TILE_WIDTH_ORIGINAL, TILE_HEIGHT_ORIGINAL, x, y, TILE_WIDTH, TILE_HEIGHT);
	ctx.globalAlpha = tmp;	
}
