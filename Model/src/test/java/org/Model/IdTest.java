package org.Model;



import org.Model.assets.Id;
import org.Model.assets.Merveille;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.*;
import org.junit.jupiter.api.Test;
public class IdTest {
	
	
	Id id = new Id();
	Id test = new Id("Kloril");
	
	@Test
	public void testGetNom() {
		assertEquals("Kloril",test.getNom());
	}
	
	@Test
	public void testSetNom() {
		id.setNom("Kloril");
		assertEquals("Kloril",id.getNom());
	}
}