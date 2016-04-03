var game = (function(){
	var mapData = {};
	var players = {};
	
	// websocket connector
	var connector = (function() {
		var socket = {};
		
		return {
			connect: function(server) {
				socket = new WebSocket(server);
			},
			
			disconnect: function() {
				// TODO
			},
			
			sendMessage: function(message) {
				socket.send(message);
			},
			
			setOnMessage: function(callback) {
				socket.onmessage = callback;
			},
			
			setOnOpen: function(callback) {
				socket.onopen = callback;
			},
			
			setOnError: function(callback) {
				socket.onerror = callback;
			}
		};
	}());
	// end of connector
	
	// key handling
	document.onkeydown = handleKey;

	function handleKey(e) {
		e.preventDefault();
		var message;
		e = e || window.event;
		
		if (e.keyCode == '38') {
			// up arrow
			message = "up";
		} else if (e.keyCode == '40') {
			// down
			message = "down";
		} else if (e.keyCode == '37') {
			// left
			message = "left";
		} else if (e.keyCode == '39') {
			// right
			message = "right";
		}
		
		connector.sendMessage(message);
	}
	// end of keyhandling
	
	// world drawer
	var worldDrawer = (function() {
		var context = {};
		var canvasWidth = 800;
		var canvasHeight = 600;
		var tileWidth = tileHeight = 16;
		var tiles = new Image();
		tiles.src = 'resources/img/tiles.png';
		
		var tileMap = {
			'0': {
				name: 'grass1',
				x: 0,
				y: 0,
				w: 16,
				h: 16
			},
			'1': {
				name: 'grass2',
				x: 16,
				y: 0,
				w: 16,
				h: 16
			},
			'2': {
				name: 'grass3',
				x: 32,
				y: 0,
				w: 16,
				h: 16
			},
			'3': {
				name: 'grass4',
				x: 48,
				y: 0,
				w: 16,
				h: 16
			},
			'4': {
				name: 'tree1',
				x: 64,
				y: 0,
				w: 16,
				h: 16
			},
			'5': {
				name: 'tree2',
				x: 80,
				y: 0,
				w: 16,
				h: 16
			},
			'6': {
				name: 'tree3',
				x: 96,
				y: 0,
				w: 16,
				h: 16
			}
		};
		
		var drawMap = function(mapData) {
			var tile = {};
			var posX = 0;
			var posY = 0;
			context.fillStyle = "rgb(0, 0, 0)";
			context.fillRect(0, 0, canvasWidth, canvasHeight);
			
			for (var i = 0; i < mapData.length; i++) {
				for (j = 0; j < mapData[i].length; j++) {
					tile = tileMap[mapData[i][j]];
					posX = j * tileWidth;
					posY = i * tileHeight;
					context.drawImage(tiles, tile.x, tile.y, tile.w, tile.h, posX, posY, tileWidth, tileHeight);
				}
			}
		};
		
		var drawBuildings = function(buildingsData) {
			console.log('drawing buildings');
		};
		
		var drawPlayers = function(players) {
			context.fillStyle = "rgb(255, 0, 255)";
			
			for (var i = 0; i < players.length; i++) {
				context.fillRect(players[i].x, players[i].y, tileWidth, tileHeight);
			}			
		};
		
		return {
			
			drawWorld: function(mapData, players) {
				drawMap(mapData);
				drawBuildings();
				drawPlayers(players);
			},
			
			setContext: function(ctx) {
				context = ctx;
			}
		};
	}());
	// end of world drawer
	
	var socketOnMessage = function(event) {
		var data = JSON.parse(event.data);

		console.log(data);
		switch (data.type) {
		case 'map':
			mapData = data.message.map;
			break;
		case 'players':
			players = data.message;
			console.dir(players);
			break;
		default:
			console.log('Unrecognized message from the server: ' + event.data);
		}

		worldDrawer.drawWorld(mapData, players);
		
	};
	
	var socketOnError = function(error) {
		console.log(error);
	};
	
	var socketOnOpen = function() {
		console.log("Connected to the server");
		connector.sendMessage('loadMap');
	};
	
	return {
		start: function(server, gameCanvas) {
			connector.connect(server);
			connector.setOnMessage(socketOnMessage);
			connector.setOnOpen(socketOnOpen);
			connector.setOnError(socketOnError);
			
			worldDrawer.setContext(gameCanvas.getContext('2d'));
		}
	};

	
	
}());