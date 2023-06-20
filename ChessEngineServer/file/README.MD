SERVER

Starten:
	
	Server läuft auf Raspberry Pi (man benötigt 2 Fenster):
	-Commands: 
		
		1.
		cd java/ChessGameServer/
		java -jar ChessEngineServer.jar
		
		2.
		ngrok tcp 8080 --region eu