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
});


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


//
//function writePoem() {
//	$.post("/markov", undefined, function(responseJSON){
//		responseObject = JSON.parse(responseJSON);
//		var textbox = document.getElementById("textArea"); 
//		console.log(responseObject.text);
//		textbox.value = responseObject.text;
//	});
//}
//
//<link rel="stylesheet" href="css/main.css">
//<script src = "js/main.js"></script>
//















//
//
//
//
//
//
//
//
//$(function() {
//	var fromBox = document.getElementById("fromArea"); 
//	$("#fromArea").bind('keypress', function(event){
//		console.log(555);
//		if (event.charCode == 0) {
//			var postParameters = { text: (fromBox.value)};
//		} else {
//			var postParameters = { text: (fromBox.value + event.key)};
//		}
//		$.post("/results", postParameters, function(responseJSON){
//			var responseObject = JSON.parse(responseJSON);
//			option = document.getElementById("fromSuggestion");
//			if (responseObject.suggestions.length > 0) {
//				option.innerHTML = "Did you mean: " + responseObject.suggestions[0];
//			} else {
//				option.innerHTML = "Remember that autocorrect and searches are case sensitive!"
//			}
//		})
//	});
//	var toBox = document.getElementById("toArea"); 
//	$("#toArea").bind('keypress', function(event){
//		if (event.charCode == 0) {
//			var postParameters = { text: (toBox.value)};
//		} else {
//			var postParameters = { text: (toBox.value + event.key)};
//		}
//		$.post("/results", postParameters, function(responseJSON){
//			var responseObject = JSON.parse(responseJSON);
//			option = document.getElementById("toSuggestion");
//			if (responseObject.suggestions.length > 0) {
//				option.innerHTML = "Did you mean: " + responseObject.suggestions[0];
//			} else {
//				option.innerHTML = "Remember that autocorrect and searches are case sensitive!"
//			}
//		})
//	});
//    $("body").on("click", "button" , function() {
//		postParameters = {};
//		//GO THE STARS ROUTE INSTEAD!!!
//		
//		
//
//    	//if (this.class == "actorName") {
//			$.get("/personal/A" + this.id, postParameters, function(responseJSON){
//				console.log(this.id);
//	//			var responseObject = JSON.parse(responseJSON);
//	//			var option = document.getElementById("pathDiv");
//	//			var path = responseObject.path;
//	//			option.innerHTML = "The path is: </br>" + path;
//			})
//    	//} 
////    	else if (this.class == "movieName") {
////			$.get("/personal/M" + this.id, postParameters, function(responseJSON){
////				//			var responseObject = JSON.parse(responseJSON);
////				//			var option = document.getElementById("pathDiv");
////				//			var path = responseObject.path;
////				//			option.innerHTML = "The path is: </br>" + path;
////			})
////		}
//    });
//});
//
//
//function search() {
//	var fromBox = document.getElementById("fromArea"); 
//	var toBox = document.getElementById("toArea"); 
//	$(function() {
//		var postParameters = {
//			from: (fromBox.value),
//			to: (toBox.value)
//		};
//		$.post("/search", postParameters, function(responseJSON){
//			var responseObject = JSON.parse(responseJSON);
//			var option = document.getElementById("pathDiv");
//			var path = responseObject.path;
//			option.innerHTML = "The path is: </br>" + path;
//		})
//
//	});
//}
//

//
//function writePoem() {
//	$.post("/markov", undefined, function(responseJSON){
//		responseObject = JSON.parse(responseJSON);
//		var textbox = document.getElementById("textArea"); 
//		console.log(responseObject.text);
//		textbox.value = responseObject.text;
//	});
//}


// <p id="intro">
//	 Welcome to n degrees of Kevin Bacon!
//	 </p>
// <textarea id = "fromArea" style="width: 200px; height: 30px;">
// </textarea>
// <p id = "fromSuggestion">
// </p> 
// </br>
// <textarea id = "toArea" style="width: 200px; height: 30px;">
// </textarea>
// <p id = "toSuggestion">
// </p> 
// </br>
// <button id = "searchButton" onclick="search()">
// Search!
// </button>