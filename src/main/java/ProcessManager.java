import java.io.BufferedReader;
import java.io.PipedInputStream;

/**
 * Create a running process and manage interaction with it
 */
public class ProcessManager {

    /**
     * The strings for which the analyser is built
     */
    String      program;
    String[]    arguments;
    Process myprocess;

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
     * 
     *  @see   processbuilder
     */
     public void spawn() {
         process = new ProcessBuilder(program, arguements).start();
     }

    /**
     * Spawn a process and collect the results
     * uses the getInputStream() from the process created in the spawn (called at the start)
     * puts the getinputstream through  an input stream reader and then into the pipe
     * on the other end of the pipe the output is read into a buffered reader 
     * and from that a line is read from the buffered reader to the output of the program
     *  @param  output         pipeline input from the program
     *  @param  input          pipeline output to the output of the function
     */
     public String spawnAndCollect() {
		 this.spawn();
		 
		 final PipedOutputStream output = new PipedOutputStream();
         final PipedInputStream  input  = new PipedInputStream(output);

         InputStreamReader in = new InputStreamReader(process.getInputStream());
		 while (in.ready()){
	         output.write(in.read());
		 }
		 BufferedReader out = new BufferedReader(input);
		 
         return out.readLine();
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
