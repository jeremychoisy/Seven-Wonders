package org.Model;

import org.Model.assets.Jeton;



import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.*;
import org.junit.jupiter.api.Test;
public class JetonTest {

	

	Jeton testGet = new Jeton("Défaite", 1);
	Jeton test = new Jeton();
	@Test
	public void testGetValeur() {
		assertEquals(-1,testGet.getValeur());
	}
	@Test
	public void testSetValeur() {
		test.setValeur(5);
		assertEquals(5,test.getValeur());
		
		
	}
}