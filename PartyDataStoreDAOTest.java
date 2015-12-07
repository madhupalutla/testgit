package com.ddb.pds.integration;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ddb.pds.dao.PartyDataStoreDAO;
import com.ddb.pds.entity.PartyDataStore;

import junit.framework.Assert;

/**
 * Test Cases for for PartyDAODataStoreDAO.
 * 
 * @author Madhu Palutla
 */
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Category(IntegrationTest.class)
public class PartyDataStoreDAOTest {
	private static PartyDataStore partyDataStore = getPartyDataStore();
	@Autowired
	@Qualifier("partyDataStoreDAOImpl")
	private PartyDataStoreDAO partyDataStoreDAO;
	
	@Test
	public void testCreatePartyDataStore() {
		partyDataStoreDAO.createPartyDataStore(partyDataStore);
	}
	
	@Test
	public void testGetPartyDataStore() {
		PartyDataStore partyDataStore = partyDataStoreDAO.viewPartyDataStore("test@test.com");
		Assert.assertNotNull(partyDataStore);
	}
	
	@Test
	public void testUpdatePartyDataStore() {
		partyDataStore.setFirstName(partyDataStore.getFirstName() + " - Updated");
		partyDataStoreDAO.updatePartyDataStore(partyDataStore);
		PartyDataStore partyDataStoreyFromDb = getPartyDataStoreFromDb(partyDataStore.getEmailId().toString());
		Assert.assertEquals(partyDataStore.getEmailId(), partyDataStoreyFromDb.getEmailId());
	}
	
	@Test
	public void testDeletePartyDataStore() {
		partyDataStoreDAO.deletePartyDataStore(partyDataStore.getEmailId());
		PartyDataStore partyDataStoreyFromDb = getPartyDataStoreFromDb(partyDataStore.getEmailId().toString());
		Assert.assertEquals(partyDataStoreyFromDb, null);
	}
	
	public PartyDataStore getPartyDataStoreFromDb(String emailId) {
		PartyDataStore partyDataStore = partyDataStoreDAO.viewPartyDataStore(emailId);
		if (partyDataStore.getEmailId().toString().equals(emailId)) {
			return partyDataStore;
		}
		return null;
	}
	
	private static PartyDataStore getPartyDataStore() {
		PartyDataStore partyDataStore = new PartyDataStore();
		partyDataStore.setFirstName("Madhu");
		partyDataStore.setLastName("Palutla");
		partyDataStore.setCity("Hyderabad");
		partyDataStore.setSsNumber("471412222");
		partyDataStore.setEmailId("test@test.com");
		return partyDataStore;
	}
}
