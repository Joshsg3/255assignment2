

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
         // FIXME and write the code to create a process
         process = new ProcessBuilder(program, arguements).start();
     }

    /**
     * Spawn a process and collect the results
     */
     public String spawnAndCollect() {
         //    FIXME and write the code
         return "";
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
