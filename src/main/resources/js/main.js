var socket = new WebSocket("ws://10.19.12.184:8080/WebSocketsTest/ws");
var canvas = document.createElement("canvas");
var ctx = canvas.getContext("2d");
var canvasWidth = 800;
var canvasHeight = 600;
canvas.width = canvasWidth;
canvas.height = canvasHeight;
document.getElementById("canvas").appendChild(canvas);
var directions = ["left", "right", "up", "down"];

//shim layer with setTimeout fallback
window.requestAnimFrame = (function(){
  return  window.requestAnimationFrame || 
          window.webkitRequestAnimationFrame || 
          window.mozRequestAnimationFrame || 
          window.oRequestAnimationFrame || 
          window.msRequestAnimationFrame || 
          function(/* function FrameRequestCallback */ callback, /* DOMElement Element */ element){
            window.setTimeout(callback, 1000 / 60);
          };
})();

socket.onmessage = function(event) {
	console.log(event.data);
	var data = JSON.parse(event.data);
	ctx.fillStyle = "rgb(0, 0, 0)";
	ctx.fillRect(0, 0, canvasWidth, canvasHeight);
	ctx.fillStyle = "rgb(255, 255, 255)";
	
	for (var i = 0; i < data.length; i++) {
		ctx.fillRect(data[i].x, data[i].y, 10, 10);
	}
};

socket.onopen = function() {
	console.log('Connected');
	sendRandomDirection();
};


socket.onerror = function(e) {
	console.log('error: ' + e)
};

function sendRandomDirection() {
	setTimeout(sendRandomDirection, 1000);
	var message = directions[Math.round(Math.random() * 3)];
	console.log(message);
	socket.send(message);
}