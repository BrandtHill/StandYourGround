## How To Run

* Open project in Eclipse
* Add required native libs
	* Project Properties -> Java Build Path -> Expand "lwjgl.jar" -> Click "Native
			library location" -> Click "Edit..." -> Click "External Folder..." ->
			"$PROJECT/lib/$PLATFORM"
* Debug or Run (with game.Program as configured main class)

## How To Play
##### In Game
 * Movement: `W` `A` `S` `D`
 * Weapon Selection: `1` `2` `3`
 * Shooting: `Left Click`
 * Reloading: `R`
 * Pause: `Escape`
##### Store/Pregame Menu
 * Save Game: `1` `2` `3`
 * Buy/Select: `Left Click`
 * Next Screen: `Space`
##### Start Menu
 * Load Game: `1` `2` `3`
 * Start Game: `Space`

## How To Build .jar

* Project -> Build All results in a `SYG.jar` in `bin/`

## Packaging .jar For Release

* Create a directory called "StandYourGround"
* Put the `SYG.jar` in it
* Put the $PLATFORM libraries in it (`.dll` files)
* Copy the `res/` directory into it
	
You should have a folder with the following inside it:

* `res/`
* `SYG.jar`
* A handful or two of DLL files

Now just zip it up and it should be good to go! It can be run by opening
`SYG.jar` once unzipped.
