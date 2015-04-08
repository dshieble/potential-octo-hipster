
MIN_WIDTH = 5; 
MIN_HEIGHT = 3; 
TILE_LAT = 0.001; // Degrees
TILE_LONG = 0.001; // Degrees

DEFAULT_WAY = "#0000FF";
GRID_LINE = "#D1D2F2";
PATH_COLOR = "#FF0000"; 

MAP_WIDTH = 500; 
MAP_HEIGHT = 300; 


var ANCHOR_LAT; // Top Left Latitude
var ANCHOR_LONG;  // Top Left Longitude
var WORLD_WIDTH;  // Number of Tiles in Width 
var WORLD_HEIGHT;  // Number of Tiles in Height

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

//lock the mouse wheel
window.onwheel = function() { 
	return false;
}


$(function() {

	$.get("/anchor", function(responseJSON) {
		// TODO Initialize ANCHOR and WORLD_WIDTH
		var extrema = JSON.parse(responseJSON);

		ANCHOR_LAT = extrema[1] + 4*TILE_LAT; 
		WORLD_HEIGHT = Math.ceil((extrema[1] - extrema[0]) / TILE_LAT) + 8; // deg. Lat 

		ANCHOR_LONG = extrema[2] - 4*TILE_LONG; 
		WORLD_WIDTH = Math.ceil((extrema[3] - extrema[2]) / TILE_LONG) + 8; // deg. Long

		grid = new Array(WORLD_HEIGHT);

		for (var i = 0; i < WORLD_HEIGHT; i++) {
			grid[i] = new Array(WORLD_WIDTH);
		}

		// TODO Obtain initialial points from program
		topLeftRow = 0;
		topLeftCol = 0;

		width = Math.floor(WORLD_WIDTH / 4); 
		height = Math.floor(WORLD_HEIGHT / 4); 

		var canvas = $("#map")[0]; 
		canvas.width = MAP_WIDTH;
		canvas.height = MAP_HEIGHT; 
		paintMap();
	});

	// var content = "<p id=\"intro\">" +
 //     	 			"Welcome to n degrees of Kevin Bacon!" +
 //     	 			"</p>" +
	//      			"<textarea id = \"fromArea\" style=\"width: 200px; height: 30px;\">" +
	//      			"</textarea> Starts with the bacon" +
	//      			"<p id = \"fromSuggestion\">" +
	//      			"</p>" +
	//      			"</br>" +
	//      			"<textarea id = \"toArea\" style=\"width: 200px; height: 30px;\">" +
	//      			"</textarea> Ends with the bacon" +
	//      			"<p id = \"toSuggestion\">" +
	//      			"</p>" +
	//      			"</br>" +
	//      			"<button id = \"searchButton\" onclick=\"search()\">" +
	//      			"Search!" +
	//      			"</button>";
	//var main = document.getElementById("mainDiv"); 
	//main.innerHTML = content;
	/*
	var fromBox = document.getElementById("fromArea"); 
	$("#fromArea").bind('keypress', function(event){
		if (event.charCode < 48 || event.charCode > 90) {
			var postParameters = { text: (fromBox.value)};
		} else {
			var postParameters = { text: (fromBox.value + event.key)};
		}
		$.post("/results", postParameters, function(responseJSON){
			var responseObject = JSON.parse(responseJSON);
			option = document.getElementById("fromSuggestion");
			if (responseObject.suggestions.length > 0) {
				option.innerHTML = "Did you mean: " + responseObject.suggestions[0];
			} else {
				option.innerHTML = "Remember that autocorrect and searches are case sensitive!"
			}
		})
	});
	var toBox = document.getElementById("toArea"); 
	$("#toArea").bind('keypress', function(event){
		if (event.charCode < 48 || event.charCode > 90) {
			var postParameters = { text: (toBox.value)};
		} else {
			var postParameters = { text: (toBox.value + event.key)};
		}
		$.post("/results", postParameters, function(responseJSON){
			var responseObject = JSON.parse(responseJSON);
			option = document.getElementById("toSuggestion");
			if (responseObject.suggestions.length > 0) {
				option.innerHTML = "Did you mean: " + responseObject.suggestions[0];
			} else {
				option.innerHTML = "Remember that autocorrect and searches are case sensitive!"
			}
		})
	});
	*/
	
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

	$("#map").mousedown(function() {
		lastX = event.pageX - map.offsetLeft; 
		lastY = event.pageY - map.offsetTop; 
	})

	// TODO
	$("#map").mouseup(function(event) {
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
		} else {
			console.log("Drag.");
			// Mouse Drag 
			var diffX = lastX - x; 
			var diffY = lastY - y; 

			// Convert
			var rowDiffColDiff = clickToRowCol(diffX, diffY)

			var rowDiff = Math.floor(rowDiffColDiff[0]);
			var colDiff = Math.floor(rowDiffColDiff[1]);

			// Check that hasn't been dragged over left and top boundaries. 
			topLeftRow = Math.max(topLeftRow + rowDiff, 0); 
			topLeftCol = Math.max(topLeftCol + colDiff, 0); 

			// Check that hasn't been dragged over right and bottom boundaries.
			if (topLeftRow + height > WORLD_HEIGHT) {
				topLeftRow = Math.floor(WORLD_HEIGHT - height); 
			}

			if (topLeftCol + width > WORLD_WIDTH) {
				topLeftCol = Math.floor(WORLD_WIDTH - width); 
			}

			paintMap();
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
	var ctx = $("#map")[0].getContext("2d"); 

	ctx.clearRect(0, 0, MAP_WIDTH, MAP_HEIGHT); 

	ctx.beginPath(); 

	//paintGrid(ctx); 
	ctx.stroke(); 
	ctx.globalAlpha = 1;

	for (var i = topLeftRow; i < (topLeftRow + height); i++) {
		for (var j = topLeftCol; j < (topLeftCol + width); j++) {
			if (grid[i][j] == null) {
				grid[i][j] = new Tile(i, j);
			}
			grid[i][j].paint(ctx); 
		}
	}
	ctx.stroke(); 

	paintPath(ctx);
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

function latLongToXY(lat, lon) {
	var y = ((ANCHOR_LAT - lat)/TILE_LAT - topLeftRow) / height * MAP_HEIGHT; 
	var x = ((lon - ANCHOR_LONG)/TILE_LONG - topLeftCol) / width * MAP_WIDTH; 

	return [x, y]; 
}

function paintLine(ctx, start, end) {
	var p1 = latLongToXY(start.lat, start.lon); 
	var p2 = latLongToXY(end.lat, end.lon); 

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

function Tile(row, col) {
	this.minLat = ANCHOR_LAT - (row + 1) * TILE_LAT; 
	this.maxLat = ANCHOR_LAT - row * TILE_LAT;
	this.minLong = ANCHOR_LONG + col * TILE_LONG;
	this.maxLong = ANCHOR_LONG + (col + 1) * TILE_LONG;
	this.row = row;
	this.col = col;

	var postParameters = { 
		minLat : this.minLat,
		maxLat : this.maxLat,
		minLong : this.minLong,
		maxLong : this.maxLong
	}; 

	$.post("/ways", postParameters, function(responseJSON) {
		grid[row][col].setWays(JSON.parse(responseJSON));
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

Tile.prototype.within = function(node) {
	var pt = [node.lat, node.lon];
	return pt[0] >= this.minLat && pt[0] <= this.maxLat &&  pt[1] >= this.minLong && pt[1] <= this.maxLong;
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
	var tileX = (this.col - topLeftCol) * (MAP_WIDTH / width);
	var tileY = (this.row - topLeftRow) * (MAP_HEIGHT / height);

	//ctx.strokeText((this.row) + "," + (this.col), tileX + 20, tileY + 20); 

	var ways = this.ways; 
	//console.log(444)
	for (var i in ways) {
		//console.log(ways[i]);
		paintWay(ctx, ways[i]); 
	}

}

function paintWay(ctx, w) {
	ctx.fillStyle = DEFAULT_WAY;
	paintLine(ctx, w.start, w.end);
}
