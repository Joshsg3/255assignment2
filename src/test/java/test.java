
package test.java;
import static org.junit.Assert.*;

import org.junit.Test;
import main.java.*;

public class test {
	
	@Test
	public void tester() {
		ProcessManager main = new ProcessManager("C:/Users/Joshua/workspace/comp255_assignment1/src/test/java/test.bat", null);
		String tmp = main.spawnAndCollect();
		assertEquals("hellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohello", tmp);
	}
	
	@Test
	public void tester2(){
		String[] temp = new String[1];
		temp[0] = "exit";
		ProcessManager main = new ProcessManager("C:/Users/Joshua/workspace/comp255_assignment1/src/test/java/test.bat", null);
		String tmp = main.spawnAndCollectWithTimeout(5);
		assertEquals("timedout", tmp);
	}
	
	@Test
	public void destroytest(){
		ProcessManager main = new ProcessManager("cmd.exe", null);
		main.spawn();
		main.destroy();
		assertFalse(main.isAlive());
	}
	@Test
	public void notdestroytest(){
		ProcessManager main = new ProcessManager("cmd.exe", null);
		main.spawn();
		assertTrue(main.isAlive());
	}
}
