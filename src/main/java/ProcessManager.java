import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PipedInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.util.Scanner;

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
     * 
     *  @see   processbuilder
     */
     public void spawn() {
		 String[] tmp = new String[arguments.length + 1];
		 tmp[0] = program;
		 for(int i = 0; i < arguments.length; i++){
			 tmp[i+1] = arguments[i];
		 }
		 
		 try {
			 process = new ProcessBuilder(tmp).start();
			} catch (Exception NullPointerException) {
				System.out.println("tmp was null");
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
     */
     public String spawnAndCollect() {
		 this.spawn();
		 
		 OutputStream stdin = process.getOutputStream();
		 InputStream stdout = process.getInputStream();

		 BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
		 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
		 
		 Scanner scanner = new Scanner(stdout);

	     String result = new String();
	     while (scanner.hasNextLine()) {
	    	 String tmp = scanner.nextLine();
	         System.out.println(tmp);
		     result.join("", result, tmp);
	     }
	     
         return result;
     }

     /**
      * Spawn a process and collect the results or throw an
      * exception if no answer before the timout
      *
      * @param  timeout     The timeout in milliseconds
      */
      public String spawnAndCollectWithTimeout(int timeout) {
          //    FIXME and write the code
          return "";
      }

     /**
      * Kill the process
      */
      public void destroy() {
          //    FIXME and write the code to kill the process
          process.destroy();
      }
}
