package main.java;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.*;
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
			//Move written data from buffer to distination
			writer.flush();
 		 } catch (IOException e) {
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
		//Initalises scanner and ensures the process has spawned
 		scanner();
		String test = prompt.toString();
 	    String result = new String();
		//Returns the string value 
 	    Callable<String> run2 = new Callable<String>(){
 	        @Override
 	        public String call() throws Exception{
	 	   	    int count = 1;
	 	   	    boolean found = false;
	 	 	    String result = new String();
				//When called and hasNextLine the next line will be returned 
	 	        while ((scanner.hasNextLine())&&(found == false)&&(count < 50)) { //whilst there is a next line to output
					Callable<String> run = new Callable<String>(){
						@Override
						public String call() throws Exception
						{
							final String tmp2 = scanner.nextLine();
							return tmp2;
						}
					};
					
					RunnableFuture future = new FutureTask(run);
					ExecutorService service = Executors.newSingleThreadExecutor();
					service.execute(future);
					String tmp = "";
					//Calls the above FutureTask 'run' which returns the next line in scanner
					//This function has a timeout that will stop it from searching for a nextLine forever
					try{
						tmp = (String) future.get(timeout + 100, TimeUnit.MILLISECONDS); 
					}
					catch (TimeoutException ex){
						// Timed out. Try to stop the code if possible.
						future.cancel(true);
					}
					service.shutdown();
					//When temp is empty it will join the previous value of result with the value of tmp 
					if(tmp != ""){
						result = String.join("", result, tmp);
					}
					//Checks if the input mateches the prompts specifications
					//If true the found condition will switch to stop the while loop from running again
					if(result.contains(test)){
						found = true;
					}
					//Stops the while loop from running forever
					count++;
					}
					//If the result contains the prompt.toString return the result, otherwise return ""
					if (found){
						return result;
					}else{
						return "";
					}
				}
			};

			RunnableFuture future = new FutureTask(run2);
			ExecutorService service = Executors.newSingleThreadExecutor();
			service.execute(future);
			//Calls the above FutureTask 'run2' which returns the the input
			//This function has a TimeoutException which when reached will cancle the future
			try{
				result = (String) future.get(1, TimeUnit.SECONDS);    // wait 1 second
			}
			catch (TimeoutException ex){
				// timed out. Try to stop the code if possible.
				future.cancel(true);
			}
			service.shutdown();
			//IF result is not blank ("") the function will return result
			if(result != ""){
				return result;
			}
			Thread t1 = new Thread(){
				public void run(){
					try{
						process.waitFor(timeout, TimeUnit.MILLISECONDS); // waitfor process to end or for timeout to happen
					}catch (InterruptedException e) {}
				}
			};
			//This thread is used to make the program sleep the length of the timeout value + 100
			t1.start();
			Thread.sleep(timeout + 100);
			if (t1.isAlive()){
				t1.interrupt();
			}
			RunnableFuture future2 = new FutureTask(run2);
	 	    ExecutorService service2 = Executors.newSingleThreadExecutor();
	 	    service2.execute(future2);
			//Runs the FutureTask run2 with timeout
	 	    try{
	 	        result = (String) future2.get(5000, TimeUnit.MILLISECONDS); 
	 	    }
	 	    catch (TimeoutException ex)
	 	    {
	 	        // timed out. Try to stop the code if possible.
	 	        future2.cancel(true);
	 	    }
	 	    service2.shutdown();
			//If result is still not equal to anything than there was no valid input so we throw an exception
	 	    if (result == ""){
 	    	 	throw new Exception("string not found", null); 
 	     	}
			//If result isn't = "" then the input must match the prompt so we return the correct value
	 	    return result; 
        }
      
		public void scanner(){
			if((isAlive())&&(!init)){
				stdout = process.getInputStream(); //make pipe for output stream
				reader = new BufferedReader(new InputStreamReader(stdout)); // like pipe to buffered reader
				scanner = new Scanner(reader); //make scanner
				init = true;
			}else if((isAlive())&&(init)){
			//If the program is not alive when the scanner function is called it will spawn the program and recall scanner()
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
