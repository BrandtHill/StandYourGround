## How To Run

* Open project in Eclipse
* Add libs to project
	* Project Properties -> Java Build Path -> Add External JARs -> "$PROJECT/lib/*.jar"
* Add required native libs
	* Project Properties -> Java Build Path -> Expand "lwjgl.jar" -> Click "Native
			library location" -> Click "Edit..." -> Click "External Folder..." ->
			"$PROJECT/lib/$PLATFORM"
* Debug

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

It takes a little while to open, probably due to
loading the music files. Perhaps this ca be made asynchronous?
