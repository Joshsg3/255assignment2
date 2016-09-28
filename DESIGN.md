
This file should describe your design choices and motivates the choices.
I chose to save the process to a variable so that it is accessable and as a global variable for use later in the project
the try and catch phrase was used so that exceptions didn't have to be added to the functions used for ease of use

in the second part I used the spawn function since I can still access the process as it is a global variable to create the process
I then use the inputstream and outputstream in stdin and stdout to take the input and output stream of the process
the 2 streams then get converted to buffered streams and then a scanner to check the output stream and put it into a string and add to that string
and output the output to the console, whilst the scanner is recieving data, after the scanner is finished recieving data, the string that has been added to is output of the function
