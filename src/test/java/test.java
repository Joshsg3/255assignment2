
package test.java;
import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;
import main.java.*;

public class test {
	
	@Test
	public void main() {
		ProcessManager main = new ProcessManager("C:/Users/Joshua/workspace/comp255 assignment1/src/test/java/test.bat", null);
		String tmp;
		main.spawn();
//		try {
//			tmp = main.expect(Pattern.compile("hel"), 1);
//			System.out.println(tmp);
//		} catch (Exception e) {
//		}
		System.out.println("test");
		main.send("12345678");
//		try {
//			tmp = main.expect(Pattern.compile("hel"), 3000);
//			System.out.println("test3");
//			System.out.println(tmp);
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
		main.send("12345678");
//		try {
//			System.out.println("test6");
//			tmp = main.expect(Pattern.compile("1234"), 300);
//			System.out.println("test3");
//			System.out.println(tmp);
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
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
