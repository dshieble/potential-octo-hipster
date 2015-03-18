WORLD_WIDTH = 500; 
WORLD_HEIGHT = 300; 
MIN_WIDTH = 5; 
MIN_HEIGHT = 3; 

var TILE_SIZE = 1; // Degrees

var width = 50; 
var height = 30; 

$(function() {
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


	// TODO
	$("#map").bind('click', function(event) {
		// TODO Get LatLong of Click
		var map = $("#map")[0];

		var x = event.pageX - board.offsetLeft; 
		var y = event.pageY - board.offsetTop; 

		var latlong = clickToCoordinate(x, y); 

		$.post("/closest", postParameters, function(responseJSON)) {
			// TODO

			// Find Take Closest Node

			// Highlight Node

		}
	})

	var lastScrollTop = 0; 
	var tol = 3; 
	$("#map").scroll(function(event) {
		var currST = $(this).scrollTop();
		if (Math.abs(currST - lastScrollTop) < tol) {

		} else if (currST < lastScrollTop) {
			// Scroll Down
			width = Math.min(width * 2, WORLD_WIDTH);
			height = Math.min(height * 2, WORLD_HEIGHT);  

		} else if (currST > lastScrollTop) {
			// Scroll Up
			width = Math.max(width / 2, MIN_WIDTH);
			height = Math.max(height / 2, MIN_HEIGHT);
		}
		lastScrollTop = currST; 
	})

});

function clickToCoordiante(x, y) {
	var latlong = [x, y];
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

function paintMap() {
	var grid = globalBoard.grid; 
}


