<!DOCTYPE html>
<html>
  
  
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <script src = "js/jquery-2.1.1.js"></script>
    <script src = "js/main.js"></script>
  </head>
  
  <style>
body {
    font-size: 20px;
	color:white;
	background-color:#000000;
	background-image: url(../images/e0a7a2_5b177e6280845ad0ea806c4f8c45aec0.jpg);
	background-size:cover;
}

#pathDiv {
    position: absolute;
    top: 10%;
    right: 25%;
    width: 30%;
    height: 50%;
    text-align:center;
}

a {
	color:red;
}

.movie {
	color:blue;
}

.invisible {
	visibility: hidden;
}

  </style>

  <body>
     
     <div id = "mainDiv">
     </div>
     

     
     <div id = "pathDiv">
		${names}
     </div>
     

  </body>


</html>
