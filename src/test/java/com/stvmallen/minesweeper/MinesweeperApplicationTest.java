package com.stvmallen.minesweeper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest
public class MinesweeperApplicationTest extends AbstractTestNGSpringContextTests {
	@Test
	public void testApplicationContext() {
		//no-op: if test passes mean the application context loaded correctly
	}
}
