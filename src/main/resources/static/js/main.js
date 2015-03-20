
MIN_WIDTH = 5; 
MIN_HEIGHT = 3; 
TILE_LAT = 1; // Degrees
TILE_LONG = 1; // Degrees
DEFAULT_WAY_COLOR = "#000000";
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

function Node(lat, lon, id) {
	this.lat = lat; 
	this.lon = lon;
	this.id = id; 
}

/*
function Way(lat1, long1, lat2, long2, id) {
	this.lat1 = lat1; 
	this.lat2 = lat2; 
	this.long1 = long1;
	this.long2 = long2; 
	this.id = id; 
	this.color = DEFAULT_WAY_COLOR;  
}
*/

function Tile(ways) {
	this.ways = ways; 
}

Tile.prototype.paint = function() {
	// TODO Paint all of the ways of a tile. 
	$("map");
}

$(function() {

	$.get("/anchor", function(extrema){
		// TODO Initialize ANCHOR and WORLD_WIDTH
		
		ANCHOR_LAT = extrema[1]; 
		WORLD_HEIGHT = Math.ceil(extrema[1] - extrema[0]); // deg. Lat 

		ANCHOR_LONG = extrema[2]; 
		WORLD_WIDTH = Math.ceil(extrema[3] - extrema[2]); // deg. Long

		grid = new Array(WORLD_HEIGHT);

		for (var i = 0; i < WORLD_HEIGHT; i++) {
			grid[i] = new Array(WORLD_WIDTH);
		}

		col1 = 0; 
		row1 = 0;
		col2 = Math.floor(WORLD_WIDTH / 2);
		row2 = Math.floor(WORLD_HEIGHT / 2);

	})

	var content = "<p id=\"intro\">" +
     	 			"Welcome to n degrees of Kevin Bacon!" +
     	 			"</p>" +
	     			"<textarea id = \"fromArea\" style=\"width: 200px; height: 30px;\">" +
	     			"</textarea> Starts with the bacon" +
	     			"<p id = \"fromSuggestion\">" +
	     			"</p>" +
	     			"</br>" +
	     			"<textarea id = \"toArea\" style=\"width: 200px; height: 30px;\">" +
	     			"</textarea> Ends with the bacon" +
	     			"<p id = \"toSuggestion\">" +
	     			"</p>" +
	     			"</br>" +
	     			"<button id = \"searchButton\" onclick=\"search()\">" +
	     			"Search!" +
	     			"</button>";
	var main = document.getElementById("mainDiv"); 
	main.innerHTML = content;
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


	var lastX;
	var lastY;
	$("#map").mousedown(function() {
		lastX = event.pageX - map.offsetLeft; 
		lastY = event.pageY - map.offsetRight; 
	})

	// TODO
	$("#map").mouseup(function(event) {
		var map = $("#map")[0];

		var x = event.pageX - map.offsetLeft; 
		var y = event.pageY - map.offsetTop; 

		if (x == lastX && y = lastY) {
			var latlong = clickToRowCol(x, y);
			var postParameters.lat = latlong[0] * TILE_LAT;
			postParameters.lng = latlong[1] * TILE_LONG;

			$.post("/closest", latLong, function(responseJSON)) {
				// TODO

				// Find Take Closest Node
				var lat = responseJSON.lat;
				var lon = responseJSON.lon;
				var id = responseJSON.id; 

				if (input_state == 1) {
					// Highlight Node
					paint(lat, lon);
					node1 = new Node(lat, lon, id);
					input_state = 2; 
				} else {
					paint(lat, lon);
					node2 = new Node(lat, lon, id);
					makePath();

					input_state = 1; 
				}
				

			}
		} else {
			// Mouse Drag 
			var diffX = x - lastX; 
			var diffY = y - lastY; 

			// Convert
			var rowDiffColDiff = clickToRowCol(diffX, diffY)

			var rowDiff = Math.floor(rowDiffColDiff[0]);
			var colDiff = Math.floor(rowDiffColDiff[1]);

			// Check that hasn't been dragged over left and top boundaries. 
			topLeftRow = Math.max(topLeftRow + rowDiff, 0); 
			topLeftCol = Math.max(topLeftCol + colDiff, 0); 

			// Check that hasn't been dragged over right and bottom boundaries.
			if (topLeftRow + height > WORLD_HEIGHT) {
				topLeftRow = WORLD_HEIGHT - height; 
			}

			if (topLeftCol + width > WORLD_WIDTH) {
				topLeftCol = WORLD_WIDTH - width; 
			}

		}
		
		paintMap(); 
	})

	var lastScrollTop = 0; 
	var tol = 3; 

	$("#map").scroll(function(event) {
		var currST = $(this).scrollTop();
		if (Math.abs(currST - lastScrollTop) < tol) {
			// Do nothing
		} else if (currST < lastScrollTop) {
			// Scroll Down
			width = Math.min(width * 2, WORLD_WIDTH);
			height = Math.min(height * 2, WORLD_HEIGHT);  
			paintMap();
		} else if (currST > lastScrollTop) {
			// Scroll Up
			width = Math.max(width / 2, MIN_WIDTH);
			height = Math.max(height / 2, MIN_HEIGHT);
			paintMap();
		}
		lastScrollTop = currST;
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

function makePath() {
	// TODO
	
}

function paint(lat, lon) {
	// TODO
}

function paintMap() {
	var grid = globalBoard.grid; 
	// TODO 
}

