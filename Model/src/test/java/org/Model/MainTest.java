package org.Model;

import org.Model.assets.Carte;
import org.Model.assets.Main;



import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MainTest{

	
	
	Main main = new Main();
	Carte c = new Carte();
	@Test
	public void testAdd() {
		
		assertEquals(0,main.size());
		main.add(c);
		assertEquals(1,main.size());
		main.add(c);
		assertEquals(2,main.size());
		main.add(c);
		assertEquals(3,main.size());
		main.add(c);
		assertEquals(4,main.size());
		main.add(c);
		assertEquals(5,main.size());
		main.add(c);
		assertEquals(6,main.size());
		main.add(c);
		assertEquals(7,main.size());
		main.add(c);
		assertEquals(7,main.size());
		
	}
	@Test
	public void testRemove() {
		assertEquals(0,main.size());
		main.add(c);
		main.add(c);
		assertEquals(2,main.size());
		main.remove(c);
		assertEquals(1,main.size());
	}

}