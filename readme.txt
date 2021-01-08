Author: Rohan Saini     UFID:64394526

Programming Assignment 2 ReadMe

IMPORTANT: FLAG -XDignore.symbol.file MUST BE USED WHEN RECOMPILING CLIENT

demo: https://youtu.be/LE8Ct_6R_B8

To run from commandline in machine:

Compile commands (run after cd to src directory) (if not compiled): => SHOULD ALREADY BE COMPILED IN SUBMISSION

    javac JPEGServer.java

    javac -XDignore.symbol.file JPEGClient.java

    (uses library in sun which cannot be used without flag -> relies on older Java library) (will show warnings but compilation will work.)

Run (in this order):

    java JPEGServer

    java JPEGClient

To use the program:

    In client side, type: "Get Images" into terminal -> this loads all the images into a buffer stack in the server

    Then type: "Convert" to receive the image at the top of the buffer stack from the server
    (Note: Convert needs to be typed once for every file to decode and retrieve them individually)

    Next, when prompted in the client program, either enter "yes" or "no" in response to whether you would like to add text to the image. If something else is entered, it will be considered invalid and text will not be added.

    Next, type the message to be added if you typed "yes" in the previous step, else repeat the Convert step.

    All 5 images must be converted/have text added before they can be shown. Once all 5 images are received by the client, they will be displayed in a separate window one after another with their respective text in green.

    As images are printed, they will be decoded and will have the decoding time printed, and image saved as a bitmap file, and text edited image saved as another png file.

    There are 5 images provided to be printed. Very big images cause the JPEG to not be sent through the streams.

    Type "Close" into the client to close the server.

