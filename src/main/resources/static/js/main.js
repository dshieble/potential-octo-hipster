//hash table to store tiles
//bigger tiles
//zoom inside of a tile
//drag so tiles can be partially on the screen
//make array "shown tiles" that contains all rendered tiles - change this array on each map view change
//make method that returns all tiles that should be rendered given some top left corner lat long
//
INITIAL_LAT = 41.83; // Top Left Latitude
INTITIAL_LONG = -71.4032;  // Top Left Longitude

//Get rid of these
// MIN_WIDTH = 5; 
// MIN_HEIGHT = 3; 
TILE_LAT = 0.01; // Degrees
TILE_LONG = 0.01; // Degrees

DEFAULT_WAY = "#0000FF";
GRID_LINE = "#D1D2F2";
PATH_COLOR = "#FF0000"; 

MAP_WIDTH = 500; 
MAP_HEIGHT = 300; 

// TILE_X_px = 250;
// TILE_Y_px = 300;

var WORLD_WIDTH;  // Number of Tiles in Width 
var WORLD_HEIGHT;  // Number of Tiles in Height

var ANCHOR_LAT = INITIAL_LAT; // Top Left Latitude
var ANCHOR_LONG = INTITIAL_LONG;  // Top Left Longitude

var minIndex = [0, 0];
var maxIndex = [1, 1];

var width = TILE_LAT*2;
var height = TILE_LONG*2;

var lat_over_y = height/MAP_HEIGHT;
var long_over_x = width/MAP_WIDTH;

var tileMap = {};
var visibleTiles = [];

var topLeftCol; 
var topLeftRow; 
var width; 
var height;

var grid; 

var input_state = 1; 
var node1; 
var node2; 

var lastX;
var lastY;

var lastScrollTop = 0; 
var tol = 3; 

var tilesReady = 0;
var tilesTarget = 0;

var mouseHold = false;
var dragging = false;

//lock the mouse wheel
// window.onwheel = function() { 
// 	return false;
// }


$(function() {

	//starting location is supposed to be Brown's campus


	//$.get("/anchor", function(responseJSON) {
		//var extrema = JSON.parse(responseJSON);

		//No edge of the world
		//max zoom
		//min zoom

		// ANCHOR_LAT = extrema[1] + 4*TILE_LAT; 
		// WORLD_HEIGHT = Math.ceil((extrema[1] - extrema[0]) / TILE_LAT) + 8; // deg. Lat 

		// ANCHOR_LONG = extrema[2] - 4*TILE_LONG; 
		// WORLD_WIDTH = Math.ceil((extrema[3] - extrema[2]) / TILE_LONG) + 8; // deg. Long

		// grid = new Array(WORLD_HEIGHT);

		// for (var i = 0; i < WORLD_HEIGHT; i++) {
		// 	grid[i] = new Array(WORLD_WIDTH);
		// }

		// // TODO Obtain initialial points from program
		// topLeftRow = 0;
		// topLeftCol = 0;

		// width = Math.floor(WORLD_WIDTH / 4); 
		// height = Math.floor(WORLD_HEIGHT / 4); 
	//});
	
	//Initialize the Canvas
	var canvas = $("#map")[0]; 
	canvas.width = MAP_WIDTH;
	canvas.height = MAP_HEIGHT;

	//Initialize Tile Map, and paint it
	tilesTarget = 4;

	tileMap[indexToString([0, 0])] = new Tile([0, 0]);
	tileMap[indexToString([0, 1])] = new Tile([0, 1]);
	tileMap[indexToString([1, 0])] = new Tile([1, 0]);
	tileMap[indexToString([1, 1])] = new Tile([1, 1]);
	visibleTiles = [tileMap[indexToString([0, 0])], tileMap[indexToString([0, 1])], tileMap[indexToString([1, 0])], tileMap[indexToString([1, 1])]];

	$('#suggest').change(function(event) {

		var postParameters = { rawText: $('#suggest').val() };

		$.post("/suggestions", postParameters, function(responseJSON) {
			$("#list").find('option').remove().end();
			var suggestions = JSON.parse(responseJSON);
			for (i in suggestions) {
				$('#list').append(
					$('<option>', {id: "remove", value : suggestions[i]}).text(suggestions[i]));
			}
		})
	})

	$('#suggest2').change(function(event) {

		var postParameters = { rawText: $('#suggest2').val() };

		$.post("/suggestions", postParameters, function(responseJSON) {
			$("#list2").find('option').remove().end();
			var suggestions = JSON.parse(responseJSON);
			for (i in suggestions) {
				$('#list2').append(
					$('<option>', {id: "remove 2", value : suggestions[i]}).text(suggestions[i]));
			}
		})
	})

	$('#suggest3').change(function(event) {

		var postParameters = { rawText: $('#suggest3').val() };

		$.post("/suggestions", postParameters, function(responseJSON) {
			$("#list3").find('option').remove().end();
			var suggestions = JSON.parse(responseJSON);
			for (i in suggestions) {
				$('#list3').append(
					$('<option>', {id: "remove 3", value : suggestions[i]}).text(suggestions[i]));
			}
		})
	})

	$('#suggest4').change(function(event) {

		var postParameters = { rawText: $('#suggest4').val() };

		$.post("/suggestions", postParameters, function(responseJSON) {
			$("#list4").find('option').remove().end();
			var suggestions = JSON.parse(responseJSON);
			for (i in suggestions) {
				$('#list4').append(
					$('<option>', {id: "remove 4", value : suggestions[i]}).text(suggestions[i]));
			}
		})
	})

	$("#list").change(function(event) {
		$('#suggest').val($("#list option:selected").val());
	})

	$("#list2").change(function(event) {
		$('#suggest2').val($("#list2 option:selected").val());
	})

	$("#list3").change(function(event) {
		$('#suggest3').val($("#list3 option:selected").val());
	})

	$("#list4").change(function(event) {
		$('#suggest4').val($("#list4 option:selected").val());
	})

	$("#map").mousedown(function(event) {
		lastX = event.pageX - map.offsetLeft; 
		lastY = event.pageY - map.offsetTop;
		mouseHold = true; 
	})

	//DRAG 
	$("#map").mousemove(function(event) {
		if (mouseHold) {
			dX = event.pageX - map.offsetLeft - lastX; 
			dY = event.pageY - map.offsetTop - lastY;

			if (dragging || Math.pow(dX, 2) + Math.pow(dY, 2) > 300) {
				//console.log("x " + dX)
				//console.log("y " + dY)
				dragging = true;
				ANCHOR_LAT = ANCHOR_LAT + lat_over_y*dY;
				ANCHOR_LONG = ANCHOR_LONG - long_over_x*dX;
				addTilesAndDraw();
				// console.log(event)
			}
		}
	})


	//CLICK - NEAREST NEIGHBOR
	$("#map").mouseup(function(event) {
		mouseHold = false; 
		dragging = false;
		var map = $("#map")[0];

		var x = event.pageX - map.offsetLeft; 
		var y = event.pageY - map.offsetTop; 

		if (x == lastX && y == lastY) {
			console.log("Click.");
			var latlong = clickToRowCol(x, y);
			var postParameters = {
				lat : ANCHOR_LAT - latlong[0] * TILE_LAT,
				lng : ANCHOR_LONG + latlong[1] * TILE_LONG 
			}; 

			$.post("/closest", postParameters, function(responseJSON) {
				responseObject = JSON.parse(responseJSON);
				// Find Take Closest Node
				var lat = responseObject.lat;
				var lon = responseObject.lon;
				var id = responseObject.id; 

				if (input_state == 1) {
					// Highlight Node
					node1 = new Node(lat, lon, id);
					input_state = 2; 
				} else if (input_state == 2) {
					node2 = new Node(lat, lon, id);
					input_state = 3; 
				} else if (input_state == 3) {
					//console.log("Input State 3: ")
				} else {
					alert("you done fucked up");
				}

				paintMap(); 
			});
		}
	})
	
	$('html').on('mousewheel', function(event) {
		console.log("scrolled");


		var delta = event.originalEvent.wheelDelta; 

		if (delta < 0) {
			// Scroll Down
			width = Math.min(width * 2, WORLD_WIDTH);
			height = Math.min(height * 2, WORLD_HEIGHT);  
		} else {
			// Scroll Up
			//TODO Fix this so we can zoom in more (more than a tile size)
			width = Math.max(width / 2, MIN_WIDTH);
			height = Math.max(height / 2, MIN_HEIGHT);
		}

		paintMap();

	})

});

function clickToRowCol(x, y) {

	var latlong = [height * y / MAP_HEIGHT, width * x / MAP_WIDTH];

	return latlong;
}

function search() {
	var fromBox = document.getElementById("fromArea"); 
	var toBox = document.getElementById("toArea"); 
	if (fromBox.value.length > 0 && toBox.value.length > 0) {
		var option = document.getElementById("pathDiv");
		option.innerHTML = "Calculating ...";
		$(function() {
			var postParameters = {
				from: (fromBox.value),
				to: (toBox.value)
			};
			$.post("/search", postParameters, function(responseJSON){
				var responseObject = JSON.parse(responseJSON);
				var path = responseObject.path;
				option.innerHTML = path;
			})
	
		});
	}
}

function addTilesAndDraw() {
	var ll_min = indexToLatLong(minIndex);
	var ll_max = indexToLatLong(maxIndex);
	//if anchor long is less than ll_min[1], add tiles along the left side, ll_min[1] ++
	//if anchor lat is less than ll_min[0], add tiles along the bottom side ll_min[0] ++
	//if anchor long + width is greater than ll_max[0], add tiles along the right side ll_max[1] ++
	//if anchor lat + height is greater than ll_min[1], add tiles along the top side ll_max[0] ++
	toAdd = [];

	//bottom
	while (ANCHOR_LAT < ll_min[0]) {
		for (var i = minIndex[1]; i <= maxIndex[1]; i++) {
			toAdd.push(minIndex[0] - 1, i);
		}
		minIndex[0] = minIndex[0] - 1;
		ll_min = indexToLatLong(minIndex);
	}

	//top
	while (ANCHOR_LAT > ll_max[0]) {
		for (var i = minIndex[1]; i <= maxIndex[1]; i++) {
			toAdd.push(maxIndex[0] + 1, i);
		}
		maxIndex[0] = maxIndex[0] + 1;
		ll_max = indexToLatLong(maxIndex);
	}

	//left
	while (ANCHOR_LONG < ll_min[1]) {
		for (var i = minIndex[0]; i <= maxIndex[0]; i++) {
			toAdd.push(i, minIndex[1] - 1);
		}
		minIndex[1] = minIndex[1] - 1;
		ll_min = indexToLatLong(minIndex);
	}

	//right
	while (ANCHOR_LONG > ll_max[1]) {
		for (var i = minIndex[0]; i <= maxIndex[0]; i++) {
			toAdd.push(i, maxIndex[1] + 1);
		}
		maxIndex[1] = maxIndex[1] + 1;
		ll_max = indexToLatLong(maxIndex);
	}
	console.log(toAdd);
	var tilesReady = 0;
	var tilesTarget = toAdd.length;

	for (var i = 0; i < toAdd.length; i++) {
		tileMap[indexToString(toAdd[i])] = new Tile(index);
	}	
	paintMap();
}

//updates the visible tile array based on the anchors, width and height
function updateVisible() {
	visibleTiles = [];
	for (t in tileMap) {
		if (tileMap.hasOwnProperty(t)) {
			if (tileMap[t].onScreen()) {
				visibleTiles.push(tileMap[t]);
			}
		}
	}
}


function latLongToXY(lat, lon) {
	var y = (ANCHOR_LAT - lat)/TILE_LAT*lat_over_y; 
	var x = (lon - ANCHOR_LONG)/TILE_LONG*long_over_x; 

	return [x, y]; 
}

//The index is the number of tile rows from the Initial Lat Long
function indexToLatLong(index) {
	var lat = INITIAL_LAT - TILE_LAT*index[0];
	var lon = INTITIAL_LONG + TILE_LONG*index[1];
	return [lat, lon]
}

//string form of index is used as a key in tileMap
function indexToString(index) {
	return "(" + String(index[0]) + ", " + String(index[1]) + ")";
}

function paintPoint(ctx, x, y) {
  ctx.fillRect(x, y, 2, 2)
}

function paintGrid(ctx) {
	ctx.globalAlpha = 0.1;
	ctx.fillStyle = GRID_LINE; 

	var longWidth = MAP_WIDTH / width;
	for (var i = longWidth; i < MAP_WIDTH; i += longWidth) {
		ctx.moveTo(i, 0);
		ctx.lineTo(i, MAP_HEIGHT); 
	} 

	var latHeight = MAP_HEIGHT / height
	for (var j = latHeight; j < MAP_HEIGHT; j += latHeight) {
		ctx.moveTo(0, j);
		ctx.lineTo(MAP_WIDTH, j); 
	} 
	//ctx.stroke(); 
}

function paintMap() {
	if (tilesReady == tilesTarget) {
		updateVisible();
		var ctx = $("#map")[0].getContext("2d"); 
		ctx.clearRect(0, 0, MAP_WIDTH, MAP_HEIGHT); 
		ctx.beginPath(); 

		//paintGrid(ctx); 
		//ctx.stroke(); 
		//ctx.globalAlpha = 1;

		for (t in visibleTiles) {
			visibleTiles[t].paint(ctx);
		}

		ctx.stroke(); 
		paintPath(ctx);

		tilesReady = 0;
		tilesTarget = 0;
	}
}



function paintPath(ctx) {
	if (input_state == 2) {
		// TODO Paint node1
		node1.paint(ctx); 
	} else if (input_state == 3) {
		// TODO Paint Path from node1 and node2
 		node2.paint(ctx); 

 		postParameters = { start : node1.id, end : node2.id }; 

 		$.post("/path", postParameters, function(responseJSON) {
 			var nodes = JSON.parse(responseJSON); 
 			paintNodes(ctx, nodes); 

 			input_state = 1; 
			node1 = null;
			node2 = null; 
 		})

	} else if (input_state == 1) {
		// Nothing
		// TODO Display message saying to click map? 
	} else {
		// TODO 
		alert("Fucked up states");
	}
}

function paintNodes(ctx, nodes) {
	for (var i = 0; i < (nodes.length - 1); i++) {
		var start = nodes[i];
		var end = nodes[i + 1]; 
		ctx.fillStyle = PATH_COLOR; 
		paintLine(ctx, start, end); 
	}
	ctx.stroke(); 
}


function paintLine(ctx, start, end) {
	var p1 = latLongToXY(start.lat, start.lon); 
	var p2 = latLongToXY(end.lat, end.lon); 
	console.log(p1)
	ctx.moveTo(p1[0], p1[1]);
	ctx.lineTo(p2[0], p2[1]);
}

function Node(lat, lon, id) {
	this.lat = lat; 
	this.lon = lon;
	this.id = id; 
}

Node.prototype.paint = function(ctx) {
	var A = latLongToXY(this.lat, this.lon);
	paintPoint(ctx, A[0], A[1]); 
}


function Way(lat1, long1, lat2, long2, id) {
	this.lat1 = lat1; 
	this.lat2 = lat2; 
	this.long1 = long1;
	this.long2 = long2; 
	this.id = id;  
}

//index is a 2 element array (y,x) corresponding to the top left point
function Tile(index) {

	ll = indexToLatLong(index);
	this.minLat = ll[0] - TILE_LAT;
	this.maxLat = ll[0];
	this.minLong = ll[1];
	this.maxLong = ll[1] + TILE_LONG;
	this.index = index;

	var postParameters = { 
		minLat : this.minLat,
		maxLat : this.maxLat,
		minLong : this.minLong,
		maxLong : this.maxLong
	};
	$.post("/ways", postParameters, function(responseJSON) {
		tileMap[indexToString(index)].setWays(JSON.parse(responseJSON));
		tilesReady ++; //handling asynchonity
		paintMap();
	})


}

Tile.prototype.setWays = function(ways) {
	this.ways = ways;


	// var left = [[this.minLat, this.minLong], [this.maxLat, this.minLong]];
	// var right = [[this.maxLat, this.maxLong], [this.minLat, this.maxLong]];
	// var top = [[this.maxLat, this.minLong], [this.maxLat, this.maxLong]];
	// var bottom = [[this.minLat, this.maxLong], [this.minLat, this.minLong]];

	// var box = [left, right, top, bottom];
	// points = [];
	// for (var i in this.ways) {
	// 	var start = [this.ways[i].start.lat, this.ways[i].start.lon]; 
	// 	var end = [this.ways[i].end.lat, this.ways[i].end.lon];
	// 	for (var b in box) {
	// 		I = intersection(box[b][0], box[b][1], start, end);
	// 		if (I != undefined) {
	// 			points.push(I);
	// 		}
	// 	}
	// 	this.ways[i];
	// }
	// if (points.length == 2) {
	// 	this.ways[i].start.lat = points[0][0];
	// 	this.ways[i].start.lon = points[0][1];
	// 	this.ways[i].end.lat = points[1][0];
	// 	this.ways[i].end.lon = points[1][1];
	// } else if (points.length == 1) {
	// 	if (this.within(this.ways[i].start)) {
	// 		this.ways[i].end.lat = points[0][0];
	// 		this.ways[i].end.lon = points[0][1];		
	// 	} else if (this.within(this.ways[i].end)) {
	// 		this.ways[i].start.lat = points[0][0];
	// 		this.ways[i].start.lon = points[0][1];	
	// 	} else {
	// 		alert("ERROR: start or end should be within");
	// 	}
	// } 

}

//returns true if a point is within the bounds of the tile
Tile.prototype.within = function(node) {
	var pt = [node.lat, node.lon];
	return withinBox(pt, this.minLat, this.maxLat, this.minLong, this.maxLong);
}

//returns true if the tile is currently on the screen
Tile.prototype.onScreen = function() {
	return withinBox([this.maxLat, this.minLong], ANCHOR_LAT - height, ANCHOR_LAT + TILE_LAT, ANCHOR_LONG - TILE_LONG, ANCHOR_LONG + width);
}

//returns true if a point is within the bounds of the box
function withinBox(pt, minLat, maxLat, minLong, maxLong) {
	return pt[0] >= minLat && pt[0] <= maxLat &&  pt[1] >= minLong && pt[1] <= maxLong;
}

// all inputs in the for [x, y]
function intersection(start1, end1, start2, end2) {
	var q = start1; 
	var p = start2; 

	var s = diff(end1, start1);
	var r = diff(end2, start2);

	var d = diff(q, p);

	var rxs = crossProduct(r, s); 

	if (rxs == 0) {
		return undefined; 
	}

	var u = crossProduct(d, [r[0] / rxs, r[1] / rxs]);
	var t = crossProduct(d, [s[0] / rxs, s[1] / rxs]);

	if (u <= 0 || u > 1 || t < 0 || t > 1) {
		return undefined; 
	}

	return [q[0] + u * s[0], q[1] + u * s[1]]; 
}

function diff(pt1, pt2) {
	return [pt1[0] - pt2[0], pt1[1] - pt2[1]]
}

function crossProduct(pt1, pt2) {
	return pt1[0]*pt2[1] - pt1[1]*pt2[0]; 
}

Tile.prototype.paint = function(ctx) {
	// TODO Paint all of the ways of a tile. 
	// var tileX = (this.col - topLeftCol) * (MAP_WIDTH / width);
	// var tileY = (this.row - topLeftRow) * (MAP_HEIGHT / height);

	//ctx.strokeText((this.row) + "," + (this.col), tileX + 20, tileY + 20); 

	//var ways = this.ways; 
	//console.log(444)
	for (var i in this.ways) {
		//console.log(this.ways[i]);
		paintWay(ctx, this.ways[i]); 
	}
}

function paintWay(ctx, w) {
	ctx.fillStyle = DEFAULT_WAY;
	paintLine(ctx, w.start, w.end);
}


































































// //hash table to store tiles
// //bigger tiles
// //zoom inside of a tile
// //drag so tiles can be partially on the screen
// //make array "shown tiles" that contains all rendered tiles - change this array on each map view change
// //make method that returns all tiles that should be rendered given some top left corner lat long
// //


// //Get rid of these
// MIN_WIDTH = 5; 
// MIN_HEIGHT = 3; 
// TILE_LAT = 0.001; // Degrees
// TILE_LONG = 0.001; // Degrees

// DEFAULT_WAY = "#0000FF";
// GRID_LINE = "#D1D2F2";
// PATH_COLOR = "#FF0000"; 

// MAP_WIDTH = 500; 
// MAP_HEIGHT = 300; 


// var ANCHOR_LAT; // Top Left Latitude
// var ANCHOR_LONG;  // Top Left Longitude
// var WORLD_WIDTH;  // Number of Tiles in Width 
// var WORLD_HEIGHT;  // Number of Tiles in Height

// var topLeftCol; 
// var topLeftRow; 
// var width; 
// var height;

// var grid; 

// var input_state = 1; 
// var node1; 
// var node2; 

// var lastX;
// var lastY;

// var lastScrollTop = 0; 
// var tol = 3; 

// //lock the mouse wheel
// window.onwheel = function() { 
// 	return false;
// }


// $(function() {

// 	$.get("/anchor", function(responseJSON) {
// 		var extrema = JSON.parse(responseJSON);

// 		//No edge of the world
// 		//max zoom
// 		//min zoom

// 		ANCHOR_LAT = extrema[1] + 4*TILE_LAT; 
// 		WORLD_HEIGHT = Math.ceil((extrema[1] - extrema[0]) / TILE_LAT) + 8; // deg. Lat 

// 		ANCHOR_LONG = extrema[2] - 4*TILE_LONG; 
// 		WORLD_WIDTH = Math.ceil((extrema[3] - extrema[2]) / TILE_LONG) + 8; // deg. Long

// 		grid = new Array(WORLD_HEIGHT);

// 		for (var i = 0; i < WORLD_HEIGHT; i++) {
// 			grid[i] = new Array(WORLD_WIDTH);
// 		}

// 		// TODO Obtain initialial points from program
// 		topLeftRow = 0;
// 		topLeftCol = 0;

// 		width = Math.floor(WORLD_WIDTH / 4); 
// 		height = Math.floor(WORLD_HEIGHT / 4); 

// 		var canvas = $("#map")[0]; 
// 		canvas.width = MAP_WIDTH;
// 		canvas.height = MAP_HEIGHT; 
// 		paintMap();
// 	});

// 	// var content = "<p id=\"intro\">" +
//  //     	 			"Welcome to n degrees of Kevin Bacon!" +
//  //     	 			"</p>" +
// 	//      			"<textarea id = \"fromArea\" style=\"width: 200px; height: 30px;\">" +
// 	//      			"</textarea> Starts with the bacon" +
// 	//      			"<p id = \"fromSuggestion\">" +
// 	//      			"</p>" +
// 	//      			"</br>" +
// 	//      			"<textarea id = \"toArea\" style=\"width: 200px; height: 30px;\">" +
// 	//      			"</textarea> Ends with the bacon" +
// 	//      			"<p id = \"toSuggestion\">" +
// 	//      			"</p>" +
// 	//      			"</br>" +
// 	//      			"<button id = \"searchButton\" onclick=\"search()\">" +
// 	//      			"Search!" +
// 	//      			"</button>";
// 	//var main = document.getElementById("mainDiv"); 
// 	//main.innerHTML = content;
// 	/*
// 	var fromBox = document.getElementById("fromArea"); 
// 	$("#fromArea").bind('keypress', function(event){
// 		if (event.charCode < 48 || event.charCode > 90) {
// 			var postParameters = { text: (fromBox.value)};
// 		} else {
// 			var postParameters = { text: (fromBox.value + event.key)};
// 		}
// 		$.post("/results", postParameters, function(responseJSON){
// 			var responseObject = JSON.parse(responseJSON);
// 			option = document.getElementById("fromSuggestion");
// 			if (responseObject.suggestions.length > 0) {
// 				option.innerHTML = "Did you mean: " + responseObject.suggestions[0];
// 			} else {
// 				option.innerHTML = "Remember that autocorrect and searches are case sensitive!"
// 			}
// 		})
// 	});
// 	var toBox = document.getElementById("toArea"); 
// 	$("#toArea").bind('keypress', function(event){
// 		if (event.charCode < 48 || event.charCode > 90) {
// 			var postParameters = { text: (toBox.value)};
// 		} else {
// 			var postParameters = { text: (toBox.value + event.key)};
// 		}
// 		$.post("/results", postParameters, function(responseJSON){
// 			var responseObject = JSON.parse(responseJSON);
// 			option = document.getElementById("toSuggestion");
// 			if (responseObject.suggestions.length > 0) {
// 				option.innerHTML = "Did you mean: " + responseObject.suggestions[0];
// 			} else {
// 				option.innerHTML = "Remember that autocorrect and searches are case sensitive!"
// 			}
// 		})
// 	});
// 	*/
	
// 	$('#suggest').change(function(event) {

// 		var postParameters = { rawText: $('#suggest').val() };

// 		$.post("/suggestions", postParameters, function(responseJSON) {
// 			$("#list").find('option').remove().end();
// 			var suggestions = JSON.parse(responseJSON);
// 			for (i in suggestions) {
// 				$('#list').append(
// 					$('<option>', {id: "remove", value : suggestions[i]}).text(suggestions[i]));
// 			}
// 		})
// 	})

// 	$('#suggest2').change(function(event) {

// 		var postParameters = { rawText: $('#suggest2').val() };

// 		$.post("/suggestions", postParameters, function(responseJSON) {
// 			$("#list2").find('option').remove().end();
// 			var suggestions = JSON.parse(responseJSON);
// 			for (i in suggestions) {
// 				$('#list2').append(
// 					$('<option>', {id: "remove 2", value : suggestions[i]}).text(suggestions[i]));
// 			}
// 		})
// 	})

// 	$('#suggest3').change(function(event) {

// 		var postParameters = { rawText: $('#suggest3').val() };

// 		$.post("/suggestions", postParameters, function(responseJSON) {
// 			$("#list3").find('option').remove().end();
// 			var suggestions = JSON.parse(responseJSON);
// 			for (i in suggestions) {
// 				$('#list3').append(
// 					$('<option>', {id: "remove 3", value : suggestions[i]}).text(suggestions[i]));
// 			}
// 		})
// 	})

// 	$('#suggest4').change(function(event) {

// 		var postParameters = { rawText: $('#suggest4').val() };

// 		$.post("/suggestions", postParameters, function(responseJSON) {
// 			$("#list4").find('option').remove().end();
// 			var suggestions = JSON.parse(responseJSON);
// 			for (i in suggestions) {
// 				$('#list4').append(
// 					$('<option>', {id: "remove 4", value : suggestions[i]}).text(suggestions[i]));
// 			}
// 		})
// 	})

// 	$("#list").change(function(event) {
// 		$('#suggest').val($("#list option:selected").val());
// 	})

// 	$("#list2").change(function(event) {
// 		$('#suggest2').val($("#list2 option:selected").val());
// 	})

// 	$("#list3").change(function(event) {
// 		$('#suggest3').val($("#list3 option:selected").val());
// 	})

// 	$("#list4").change(function(event) {
// 		$('#suggest4').val($("#list4 option:selected").val());
// 	})

// 	$("#map").mousedown(function() {
// 		lastX = event.pageX - map.offsetLeft; 
// 		lastY = event.pageY - map.offsetTop; 
// 	})

// 	// TODO
// 	$("#map").mouseup(function(event) {
// 		var map = $("#map")[0];

// 		var x = event.pageX - map.offsetLeft; 
// 		var y = event.pageY - map.offsetTop; 

// 		if (x == lastX && y == lastY) {
// 			console.log("Click.");
// 			var latlong = clickToRowCol(x, y);
// 			var postParameters = {
// 				lat : ANCHOR_LAT - latlong[0] * TILE_LAT,
// 				lng : ANCHOR_LONG + latlong[1] * TILE_LONG 
// 			}; 

// 			$.post("/closest", postParameters, function(responseJSON) {
// 				responseObject = JSON.parse(responseJSON);
// 				// Find Take Closest Node
// 				var lat = responseObject.lat;
// 				var lon = responseObject.lon;
// 				var id = responseObject.id; 

// 				if (input_state == 1) {
// 					// Highlight Node
// 					node1 = new Node(lat, lon, id);
// 					input_state = 2; 
// 				} else if (input_state == 2) {
// 					node2 = new Node(lat, lon, id);
// 					input_state = 3; 
// 				} else if (input_state == 3) {
// 					//console.log("Input State 3: ")
// 				} else {
// 					alert("you done fucked up");
// 				}

// 				paintMap(); 
// 			});
// 		} else {
// 			console.log("Drag.");
// 			// Mouse Drag 
// 			var diffX = lastX - x; 
// 			var diffY = lastY - y; 

// 			// Convert
// 			var rowDiffColDiff = clickToRowCol(diffX, diffY)

// 			var rowDiff = Math.floor(rowDiffColDiff[0]);
// 			var colDiff = Math.floor(rowDiffColDiff[1]);

// 			// Check that hasn't been dragged over left and top boundaries. 
// 			topLeftRow = Math.max(topLeftRow + rowDiff, 0); 
// 			topLeftCol = Math.max(topLeftCol + colDiff, 0); 

// 			// Check that hasn't been dragged over right and bottom boundaries.
// 			if (topLeftRow + height > WORLD_HEIGHT) {
// 				topLeftRow = Math.floor(WORLD_HEIGHT - height); 
// 			}

// 			if (topLeftCol + width > WORLD_WIDTH) {
// 				topLeftCol = Math.floor(WORLD_WIDTH - width); 
// 			}

// 			paintMap();
// 		}
// 	})
	
// 	$('html').on('mousewheel', function(event) {
// 		console.log("scrolled");
// 		var delta = event.originalEvent.wheelDelta; 

// 		if (delta < 0) {
// 			// Scroll Down
// 			width = Math.min(width * 2, WORLD_WIDTH);
// 			height = Math.min(height * 2, WORLD_HEIGHT);  
// 		} else {
// 			// Scroll Up
// 			//TODO Fix this so we can zoom in more (more than a tile size)
// 			width = Math.max(width / 2, MIN_WIDTH);
// 			height = Math.max(height / 2, MIN_HEIGHT);
// 		}

// 		paintMap();

// 	})

// });

// function clickToRowCol(x, y) {

// 	var latlong = [height * y / MAP_HEIGHT, width * x / MAP_WIDTH];

// 	return latlong;
// }

// function search() {
// 	var fromBox = document.getElementById("fromArea"); 
// 	var toBox = document.getElementById("toArea"); 
// 	if (fromBox.value.length > 0 && toBox.value.length > 0) {
// 		var option = document.getElementById("pathDiv");
// 		option.innerHTML = "Calculating ...";
// 		$(function() {
// 			var postParameters = {
// 				from: (fromBox.value),
// 				to: (toBox.value)
// 			};
// 			$.post("/search", postParameters, function(responseJSON){
// 				var responseObject = JSON.parse(responseJSON);
// 				var path = responseObject.path;
// 				option.innerHTML = path;
// 			})
	
// 		});
// 	}
// }

// function paintPoint(ctx, x, y) {
//   ctx.fillRect(x, y, 2, 2)
// }

// function paintGrid(ctx) {
// 	ctx.globalAlpha = 0.1;
// 	ctx.fillStyle = GRID_LINE; 

// 	var longWidth = MAP_WIDTH / width;
// 	for (var i = longWidth; i < MAP_WIDTH; i += longWidth) {
// 		ctx.moveTo(i, 0);
// 		ctx.lineTo(i, MAP_HEIGHT); 
// 	} 

// 	var latHeight = MAP_HEIGHT / height
// 	for (var j = latHeight; j < MAP_HEIGHT; j += latHeight) {
// 		ctx.moveTo(0, j);
// 		ctx.lineTo(MAP_WIDTH, j); 
// 	} 
// 	//ctx.stroke(); 
// }

// function paintMap() {
// 	var ctx = $("#map")[0].getContext("2d"); 

// 	ctx.clearRect(0, 0, MAP_WIDTH, MAP_HEIGHT); 

// 	ctx.beginPath(); 

// 	//paintGrid(ctx); 
// 	ctx.stroke(); 
// 	ctx.globalAlpha = 1;

// 	for (var i = topLeftRow; i < (topLeftRow + height); i++) {
// 		for (var j = topLeftCol; j < (topLeftCol + width); j++) {
// 			if (grid[i][j] == null) {
// 				grid[i][j] = new Tile(i, j);
// 			}
// 			grid[i][j].paint(ctx); 
// 		}
// 	}
// 	ctx.stroke(); 

// 	paintPath(ctx);
// }



// function paintPath(ctx) {
// 	if (input_state == 2) {
// 		// TODO Paint node1
// 		node1.paint(ctx); 
// 	} else if (input_state == 3) {
// 		// TODO Paint Path from node1 and node2
//  		node2.paint(ctx); 

//  		postParameters = { start : node1.id, end : node2.id }; 

//  		$.post("/path", postParameters, function(responseJSON) {
//  			var nodes = JSON.parse(responseJSON); 
//  			paintNodes(ctx, nodes); 

//  			input_state = 1; 
// 			node1 = null;
// 			node2 = null; 
//  		})

// 	} else if (input_state == 1) {
// 		// Nothing
// 		// TODO Display message saying to click map? 
// 	} else {
// 		// TODO 
// 		alert("Fucked up states");
// 	}
// }

// function paintNodes(ctx, nodes) {
// 	for (var i = 0; i < (nodes.length - 1); i++) {
// 		var start = nodes[i];
// 		var end = nodes[i + 1]; 
// 		ctx.fillStyle = PATH_COLOR; 
// 		paintLine(ctx, start, end); 
// 	}
// 	ctx.stroke(); 
// }

// function latLongToXY(lat, lon) {
// 	var y = ((ANCHOR_LAT - lat)/TILE_LAT - topLeftRow) / height * MAP_HEIGHT; 
// 	var x = ((lon - ANCHOR_LONG)/TILE_LONG - topLeftCol) / width * MAP_WIDTH; 

// 	return [x, y]; 
// }

// function paintLine(ctx, start, end) {
// 	var p1 = latLongToXY(start.lat, start.lon); 
// 	var p2 = latLongToXY(end.lat, end.lon); 

// 	ctx.moveTo(p1[0], p1[1]);
// 	ctx.lineTo(p2[0], p2[1]);
// }

// function Node(lat, lon, id) {
// 	this.lat = lat; 
// 	this.lon = lon;
// 	this.id = id; 
// }

// Node.prototype.paint = function(ctx) {
// 	var A = latLongToXY(this.lat, this.lon);
// 	paintPoint(ctx, A[0], A[1]); 
// }


// function Way(lat1, long1, lat2, long2, id) {
// 	this.lat1 = lat1; 
// 	this.lat2 = lat2; 
// 	this.long1 = long1;
// 	this.long2 = long2; 
// 	this.id = id;  
// }

// function Tile(row, col) {
// 	this.minLat = ANCHOR_LAT - (row + 1) * TILE_LAT; 
// 	this.maxLat = ANCHOR_LAT - row * TILE_LAT;
// 	this.minLong = ANCHOR_LONG + col * TILE_LONG;
// 	this.maxLong = ANCHOR_LONG + (col + 1) * TILE_LONG;
// 	this.row = row;
// 	this.col = col;

// 	var postParameters = { 
// 		minLat : this.minLat,
// 		maxLat : this.maxLat,
// 		minLong : this.minLong,
// 		maxLong : this.maxLong
// 	}; 

// 	$.post("/ways", postParameters, function(responseJSON) {
// 		grid[row][col].setWays(JSON.parse(responseJSON));
// 	})


// }

// Tile.prototype.setWays = function(ways) {
// 	this.ways = ways;


// 	// var left = [[this.minLat, this.minLong], [this.maxLat, this.minLong]];
// 	// var right = [[this.maxLat, this.maxLong], [this.minLat, this.maxLong]];
// 	// var top = [[this.maxLat, this.minLong], [this.maxLat, this.maxLong]];
// 	// var bottom = [[this.minLat, this.maxLong], [this.minLat, this.minLong]];

// 	// var box = [left, right, top, bottom];
// 	// points = [];
// 	// for (var i in this.ways) {
// 	// 	var start = [this.ways[i].start.lat, this.ways[i].start.lon]; 
// 	// 	var end = [this.ways[i].end.lat, this.ways[i].end.lon];
// 	// 	for (var b in box) {
// 	// 		I = intersection(box[b][0], box[b][1], start, end);
// 	// 		if (I != undefined) {
// 	// 			points.push(I);
// 	// 		}
// 	// 	}
// 	// 	this.ways[i];
// 	// }
// 	// if (points.length == 2) {
// 	// 	this.ways[i].start.lat = points[0][0];
// 	// 	this.ways[i].start.lon = points[0][1];
// 	// 	this.ways[i].end.lat = points[1][0];
// 	// 	this.ways[i].end.lon = points[1][1];
// 	// } else if (points.length == 1) {
// 	// 	if (this.within(this.ways[i].start)) {
// 	// 		this.ways[i].end.lat = points[0][0];
// 	// 		this.ways[i].end.lon = points[0][1];		
// 	// 	} else if (this.within(this.ways[i].end)) {
// 	// 		this.ways[i].start.lat = points[0][0];
// 	// 		this.ways[i].start.lon = points[0][1];	
// 	// 	} else {
// 	// 		alert("ERROR: start or end should be within");
// 	// 	}
// 	// } 

// }

// Tile.prototype.within = function(node) {
// 	var pt = [node.lat, node.lon];
// 	return pt[0] >= this.minLat && pt[0] <= this.maxLat &&  pt[1] >= this.minLong && pt[1] <= this.maxLong;
// }

// // all inputs in the for [x, y]
// function intersection(start1, end1, start2, end2) {
// 	var q = start1; 
// 	var p = start2; 

// 	var s = diff(end1, start1);
// 	var r = diff(end2, start2);

// 	var d = diff(q, p);

// 	var rxs = crossProduct(r, s); 

// 	if (rxs == 0) {
// 		return undefined; 
// 	}

// 	var u = crossProduct(d, [r[0] / rxs, r[1] / rxs]);
// 	var t = crossProduct(d, [s[0] / rxs, s[1] / rxs]);

// 	if (u <= 0 || u > 1 || t < 0 || t > 1) {
// 		return undefined; 
// 	}

// 	return [q[0] + u * s[0], q[1] + u * s[1]]; 
// }

// function diff(pt1, pt2) {
// 	return [pt1[0] - pt2[0], pt1[1] - pt2[1]]
// }

// function crossProduct(pt1, pt2) {
// 	return pt1[0]*pt2[1] - pt1[1]*pt2[0]; 
// }

// Tile.prototype.paint = function(ctx) {
// 	// TODO Paint all of the ways of a tile. 
// 	var tileX = (this.col - topLeftCol) * (MAP_WIDTH / width);
// 	var tileY = (this.row - topLeftRow) * (MAP_HEIGHT / height);

// 	//ctx.strokeText((this.row) + "," + (this.col), tileX + 20, tileY + 20); 

// 	var ways = this.ways; 
// 	//console.log(444)
// 	for (var i in ways) {
// 		//console.log(ways[i]);
// 		paintWay(ctx, ways[i]); 
// 	}

// }

// function paintWay(ctx, w) {
// 	ctx.fillStyle = DEFAULT_WAY;
// 	paintLine(ctx, w.start, w.end);
// }
