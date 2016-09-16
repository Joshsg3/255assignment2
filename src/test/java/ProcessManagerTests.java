
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

/**
 * Test suite for Longest Common Subsequences
 */
public class ProcessManagerTests {

    /**
     * Test for spawning a process
     */
    @Test
    public void templateTest() {

        //  FIXME
        //  program to spawan and arguments
        String prog = "ps";
        String[] args = { "aux" };

        //  create a process manager to interact with `ps`
        ProcessManager p = new ProcessManager(prog, args);
        //  spawn a process that runs `ps aux` and collect the result
        String res = p.spawnAndCollect();
        assertEquals("something", res);
    }

}
