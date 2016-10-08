package test.java;
import org.junit.runner.JUnitCore;
import main.java.*;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

    public static void main(String args[]) {

        Result res = JUnitCore.runClasses(ProcessManagerTests.class);

        if (res.wasSuccessful() != true) {
            System.out.println("Some tests failed: ");
            //  print the failures
            for (Failure f: res.getFailures()) {
                System.out.println("[Test failed] " + f.toString());
            }
        } else {
            System.out.println("All tests passed!");
        }
    }
}
