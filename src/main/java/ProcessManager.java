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
import java.util.regex.Pattern;

/**
 * Create a running process and manage interaction with it
 */
public class ProcessManager {

    /**
     * The strings for which the analyzer is built
     */
    String      program;
    String[]    arguments;
    Process process;
    InputStream stdout;
    BufferedReader reader;
    Scanner scanner;
    Boolean init = false;
    Boolean init2 = false;
    BufferedWriter writer;
    OutputStream stdin;

    // FIXME if you need to add more variables

    /**
     * Make a running process with processbuilder
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
     public void spawn(){
		 String[] tmp;
    	 if (arguments == null){ //if no arguements are given
    		 tmp = new String[1];
    		 tmp[0] = program;
    	 }else { // if arguements are given
    		 tmp = new String[arguments.length + 1];
    		 tmp[0] = program;
    		 for(int i = 0; i < arguments.length; i++){ // add the arguements onto the program name
    			 tmp[i+1] = arguments[i];
    		 } 		 
    	 } // make the process builder
		ProcessBuilder pb = new ProcessBuilder(tmp); 
		try { // try to start the program
			process = pb.start();
		} catch (IOException e) {
			System.out.println("failed to start");
		}
     }

    /**
     * Spawn a process and collect the results
     * 
     * spawns the process, connects the output stream and input stream  to stdin and stdout
     * puts them into the buffer reader and writer
     * 
     *  @see OutputStream
     *  @see InputStream
     *  @see Process
     *  @see BufferedReader
     *  @see BufferedWriter
     *  @see Scanner
     *  @see process.waitfor()
     */
     @SuppressWarnings({ "unused", "resource" })
	public String spawnAndCollect(){
		 scanner();

	     String result = new String(); // for output
	     try {
			int errCode = process.waitFor(); // run process till it finishes
		} catch (InterruptedException e) {
		}
	     boolean temp = false;
	     while (scanner.hasNextLine()) { // whilst the program has a next time in the output
	    	 if(temp){ // ignore the output of the program name
	    		 String tmp = scanner.nextLine(); //get that line
	    		 result = String.join("", result, tmp); //add that line to the result string
	    	 }
	    	 temp = true;
	     }

	     this.destroy(); //end the process
         return result; 
     }

     /**
      * Spawn a process and collect the results or throw an
      * exception if no answer before the timeout
      *
      * @param  timeout     The timeout in milliseconds
      * @see spawnAndCollect()
      * @see process.waitfor(timeout, timeunit)
      * @see destroy()
      */
      public String spawnAndCollectWithTimeout(int timeout){
    	  scanner();

 	     String result = new String();
 	    boolean temp = true;
 	     try {
 			temp = process.waitFor(timeout, TimeUnit.MILLISECONDS); // waitfor process to end or for timeout to happen
 		} catch (InterruptedException e) {
 		}
 	     if (!(temp)){ // if timeout occurs
 	    	 this.destroy(); //kill process
 	    	 return "timedout"; //return result
 	     }
 	     while (scanner.hasNextLine()) { //whilst there is a next line to output
 	    	 if(!(temp)){ //ignore output of program name
 	    		 String tmp = scanner.nextLine(); // get that line
 	    		 result = String.join("", result, tmp); //add it to the result
 	    	 }
 	    	 temp = false;
 	     }

 	     this.destroy(); //kill process
         return result; 
          
      }

     /**
      * Kill the process
      */
      public void destroy() {
          process.destroy();
      }
      
      public void writer(){
    	  if((isAlive())&&(!init2)){
    	    stdin = process.getOutputStream(); //make pipe for input stream
    	  	writer = new BufferedWriter(new OutputStreamWriter(stdin)); // like pipe to buffered reader
    		init2 = true;
    	  }else if((isAlive())&&(init2)){
    		  
    	  }else if((!isAlive())){
    		  spawn();
    		  writer();
    	  }
      }
      
      /**
      * Send a string to the process
      */
      public boolean send(String s) {
    	  writer();
  		 try {
			writer.write(s, 0, s.length());
			writer.flush();
			System.out.println("sent");
 		 } catch (IOException e) {
 			 System.out.println(e.getMessage());
			System.out.println("Process Error");
			return false;
 		 }
    	return true;
      }
      /**
      * Send a string to the process and collect the result
      * upto a ‘prompt‘ or throw an
      * exception if the prompt is not seen before the timeout
      *
      * @param timeout The timeout in milliseconds
      * @param prompt The expected prompt
      */
    	public String expect(Pattern prompt, int timeout) throws Exception{
 		 scanner();
		 String test = prompt.toString();
 	     String result = new String();
 	     boolean temp = true;
 	     boolean found = false;
 	     int count = 0;
 	     while ((scanner.hasNextLine())&&(found == false)&&(count < 50)) { //whilst there is a next line to output
 	    	 if(!(temp)){ //ignore output of program name
 	    		 String tmp = scanner.nextLine(); // get that line
 	    		 System.out.println(tmp);
 	    		 System.out.println("000000");
 	    		 result = String.join("", result, tmp); //add it to the result
 	    	 }
 	    	 if(result.contains(test)){
				 found = true;
			 }
 	    	 count++;
 	    	 temp = false;
 	     }
	 	 if(!found){
	 	     System.out.println("no line");
	 	     try {
	 			temp = process.waitFor(timeout, TimeUnit.MILLISECONDS); // waitfor process to end or for timeout to happen
	 		} catch (InterruptedException e) {
	 		}
	 	     while ((scanner.hasNextLine())&&(found == false)&&(count < 50)) { //whilst there is a next line to output
	 	    	 if(!(temp)){ //ignore output of program name
	 	    		 String tmp = scanner.nextLine(); // get that line
	 	    		 System.out.println(tmp);
	 	    		 System.out.println("000000");
	 	    		 result = String.join("", result, tmp); //add it to the result
	 	    	 }
	 	    	 if(result.contains(test)){
					 found = true;
				 }
	 	    	 count++;
	 	    	 temp = false;
	 	     }

	 	     if (found == false){
 	    	 	throw new Exception("string not found", null); 
 	     	}
	 	 }
         return result; 
      }
      
      public void scanner(){
    	  if((isAlive())&&(!init)){
    		  stdout = process.getInputStream(); //make pipe for output stream
    		  reader = new BufferedReader(new InputStreamReader(stdout)); // like pipe to buffered reader
    		  scanner = new Scanner(reader); //make scanner
    		  init = true;
    	  }else if((isAlive())&&(init)){
    		  
    	  }else if((!isAlive())){
    		  spawn();
    		  scanner();
    	  }
      }
    	
      public Boolean isAlive(){ //returns if the process is alive
		try{
    	  return(process.isAlive());
		}catch(java.lang.NullPointerException e){
			System.out.println("process not up");
			return(false);
		}
    	  
      }
}
