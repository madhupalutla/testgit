package com.ddb.pds.integration;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.datastax.driver.core.utils.UUIDs;
import com.ddb.pds.dao.PartyDataStoreDAO;
import com.ddb.pds.entity.PartyDataStore;

/**
 * Test Cases for for AccountDAO.
 * 
 * @author Nazeer Thowfeek
 */
@Ignore
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Category(IntegrationTest.class)
public class PartyDataStoreDAOPerformanceTest {
	private static Logger logger = Logger.getLogger(PartyDataStoreDAOPerformanceTest.class);
	private static final String EMAIL_ID = "test@test.com";
	private static final String DL_NBR = "3242424242424";
	@Rule
	public ContiPerfRule i = new ContiPerfRule();
	@Autowired
	@Qualifier("partyDataStoreDAOImpl")
	private PartyDataStoreDAO partyDataStoreDAO;
	private PartyDataStore partyDataStore = new PartyDataStore();
	
	public PartyDataStoreDAOPerformanceTest() {
		partyDataStore.setFirstName("Madhu");
		partyDataStore.setLastName("Palutla");
		partyDataStore.setEmailId("test@test.com");
	}
	
	@Test
	public void testCreatePartyDataStore() {
		partyDataStore = getPartyDataStore();
		partyDataStoreDAO.deletePartyDataStore(EMAIL_ID);
		partyDataStoreDAO.createPartyDataStore(partyDataStore);
		logger.info("*****Created a Party*****");
	}
	
	@Test
	@PerfTest(invocations = 1000, threads = 10)
	public void testRetrievePartyDataStore() {
		partyDataStore = getPartyDataStore();
		PartyDataStore partyDataStoreResponse = partyDataStoreDAO.viewPartyDataStore(EMAIL_ID);
		assertTrue(partyDataStoreResponse.getDriverLicense().equals(DL_NBR));
		logger.info("**Retrieved Party");
	}
	
	@Test
	@PerfTest(invocations = 1000, threads = 10)
	public void testCreatePartyDataStores() {
		String emailId = "Fake EMAIL" + UUIDs.timeBased().toString();
		PartyDataStore partyDataStoreTmp = new PartyDataStore();
		partyDataStoreTmp.setEmailId(emailId);
		partyDataStoreTmp.setFirstName("Faker FName");
		;
		partyDataStoreTmp.setLastName("Faket LName");
		partyDataStoreDAO.createPartyDataStore(partyDataStoreTmp);
		logger.info("**Created Party with Primary Key as " + emailId);
		partyDataStoreDAO.deletePartyDataStore(emailId);
		logger.info("**Deleted Party with Primary Key as " + emailId);
	}
	
	@Test
	@PerfTest(invocations = 1, threads = 1)
	public void testDeletePartyDataStore() {
		partyDataStore = getPartyDataStore();
		partyDataStoreDAO.deletePartyDataStore(EMAIL_ID);
		logger.info("*********Deleted Party************");
	}
	
	private PartyDataStore getPartyDataStore() {
		PartyDataStore partyDataStore = new PartyDataStore();
		partyDataStore.setFirstName("Madhu");
		partyDataStore.setLastName("Palutla");
		partyDataStore.setCity("Hyderabad");
		partyDataStore.setSsNumber("471412222");
		partyDataStore.setEmailId(EMAIL_ID);
		partyDataStore.setDriverLicense(DL_NBR);
		return partyDataStore;
	}
}
