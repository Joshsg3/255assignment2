package main.java;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Create a running process and manage interaction with it
 */
public class ProcessManager {

    /**
     * The strings for which the analyser is built
     */
    String      program;
    String[]    arguments;
    Process process;

    // FIXME if you need to add more variables

    /**
     * Make aa running process with
     *
     *  @param  executable    The program to run
     *  @param  args          The arguments of the program
     *  @param  Process       The actual process
     */
    public ProcessManager(String executable, String[] args) {
        program = executable;
        arguments = args;
    }

    /**
     * Spawn a process through the processbuilder
     * had to join the program name and arguements into the same string array for the process builder 
     * and use a try and catch for the possible exception
     * @throws IOException 
     * 
     *  @see   processbuilder
     */
     public void spawn() throws IOException {
		 String[] tmp;
    	 if (arguments == null){
    		 tmp = new String[1];
    		 tmp[0] = program;
    	 }else {
    		 tmp = new String[arguments.length + 1];
    		 tmp[0] = program;
    		 for(int i = 0; i < arguments.length; i++){
    			 tmp[i+1] = arguments[i];
    		 } 		 
    	 }
		ProcessBuilder pb = new ProcessBuilder(tmp); 
		process = pb.start();
     }

    /**
     * Spawn a process and collect the results
     * 
     * spawns the process, connects the output stream and input stream  to stdin and stdout
     * puts them into the buffer reader and writer
     * @throws IOException 
     * 
     *  @see OutputStream
     *  @see InputStream
     *  @see Process
     *  @see BufferedReader
     *  @see BufferedWriter
     *  @see Scanner
     */
     @SuppressWarnings({ "unused", "resource" })
	public String spawnAndCollect() throws IOException {
		 this.spawn();
		 
		 InputStream stdout = process.getInputStream();

		 BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
		 
		 Scanner scanner = new Scanner(reader);

	     String result = new String();
	     try {
			int errCode = process.waitFor();
		} catch (InterruptedException e) {
		}
	     boolean temp = false;
	     while (scanner.hasNextLine()) {
	    	 if(temp){
	    		 String tmp = scanner.nextLine();
	    		 result = String.join("", result, tmp);
	    	 }
	    	 temp = true;
	     }
	     
         return result;
     }

     /**
      * Spawn a process and collect the results or throw an
      * exception if no answer before the timout
      *
      * @param  timeout     The timeout in milliseconds
     * @throws IOException 
      */
      public String spawnAndCollectWithTimeout(int timeout) throws IOException {
    	  this.spawn();
 		 
 		 InputStream stdout = process.getInputStream();

 		 BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
 		 
 		 Scanner scanner = new Scanner(reader);

 	     String result = new String();
 	    boolean temp = true;
 	     try {
 			temp = process.waitFor(timeout, TimeUnit.MILLISECONDS);
 		} catch (InterruptedException e) {
 		}
 	     if (!(temp)){
 	    	 return "timedout";
 	     }
 	     while (scanner.hasNextLine()) {
 	    	 if(!(temp)){
 	    		 String tmp = scanner.nextLine();
 	    		 result = String.join("", result, tmp);
 	    	 }
 	    	 temp = false;
 	     }
 	     
          return result;
      }

     /**
      * Kill the process
      */
      public void destroy() {
          //    FIXME and write the code to kill the process
          process.destroy();
      }
}
