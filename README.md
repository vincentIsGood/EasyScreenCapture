# Easy Screen Capture
This project is made to make life easier to capture the screen of your computer (currently only Windows is supported)
with some of the shortcuts provided by the program.

## Features
- capture screen / audio while in Fullscreen mode (eg. gaming. Tested on Apex Legends)
- while you are focusing on another program, you can still use the shortcuts to access the program
- if you want another recording, you can just press record again 
  (ie. will stop recording and start another one automatically. 
   ie. `CTRL + SHIFT + 1` then `CTRL + SHIFT + 4` works as expected and creates 2 separate files)

## Future
Will add options to the command line to set parameters like compression ratio, frame rate, etc.

## Help menu
```sh
$ java -jar EasyScreenCapture-1.0.jar
[INFO] [c.v.a.w.ScreenCaptureWindows.class:17] Key map for Easy Screen Capture:

Program:
CTRL + SHIFT + : -> kill program
CTRL + SHIFT + ` -> toggle program (enable / disable)

Capture:
CTRL + SHIFT + 1 -> Cap screen with desktop audio
CTRL + SHIFT + 2 -> Cap screen only
CTRL + SHIFT + 3 -> Cap screen with microphone
CTRL + SHIFT + 4 -> Cap desktop audio only
CTRL + SHIFT + 5 -> Cap microphone audio only

Control:
CTRL + SHIFT + Q -> End recording
CTRL + SHIFT + K -> Kill ffmpeg recorder
```

## Caution
If you encounter any unstoppable errors, you can press CTRL + C to force the command line program to end.