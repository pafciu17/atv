import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by pawel on 2/26/14.
 */


public class PrizeServiceTest {

	@Test
	public void testGetPrizesAgainstIncorrectAccountNumber() {
		try {
			PrizeService prizeService = new PrizeService();
			EligibilityService eligibilityService = mock(EligibilityService.class);
			when(eligibilityService.isEligible("")).thenThrow(new InvalidAccountNumberException());
			prizeService.setEligibilityService(eligibilityService);
			prizeService.getPrizes("", Arrays.<String>asList());
			fail("PrizeService did not throw InvalidAccountNumberException");
		} catch (InvalidAccountNumberException e) {
		} catch (FailureException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetPrizeForNotEligibleCustomer() {
		PrizeService prizeService = new PrizeService();
		prizeService.setPackagePrizeMapping(this.getPrizeMapping());
		List<String> prizes = Arrays.<String>asList("SPORTS");

		List<String> prizeList = this.getPrizeListForCustomerByEligibility(prizeService, prizes, false);
		assertEquals("For ineligible customer prize list is empty", 0, prizeList.size());
	}

	@Test
	public void testGetPrizeForEligibleCustomer() {
		PrizeService prizeService = new PrizeService();
		prizeService.setPackagePrizeMapping(this.getPrizeMapping());
		List<String> prizes = Arrays.<String>asList("SPORTS");

		List<String> prizeList = this.getPrizeListForCustomerByEligibility(prizeService, prizes, true);
		assertEquals("For eligible customer with SPORTS " +
				"package prize list contains 1 prize", 1, prizeList.size());
		assertEquals("FREE SPORTING EVENT TICKET is the prize for customer with SPORTS package",
				"FREE SPORTING EVENT TICKETS", prizeList.get(0));

		prizes = Arrays.<String>asList("KIDS");
		prizeList = this.getPrizeListForCustomerByEligibility(prizeService, prizes, true);
		assertEquals("For eligible customer with KIDS " +
				"package prize list is empty", 0, prizeList.size());

		prizes = Arrays.<String>asList("SPORTS", "MOVIES");
		prizeList = this.getPrizeListForCustomerByEligibility(prizeService, prizes, true);
		assertEquals("For eligible customer with SPORTS and MOVIES " +
				"packages prize list contains 2 prizes", 2, prizeList.size());

		prizes = Arrays.<String>asList("GOSSIP", "MOVIES");
		prizeList = this.getPrizeListForCustomerByEligibility(prizeService, prizes, true);
		assertEquals("For eligible customer with MOVIES and GOSSIP " +
				"packages prize list contains 1 prize", 1, prizeList.size());

		assertEquals("FREE MOVIE TICKETS is the prize for customer with GOSSIP and/or MOVIES package",
				"FREE MOVIE TICKETS", prizeList.get(0));

	}

	@Test
	public void testEligibilityServiceFailure() {
		String accountNumber = "1234567890";
		PrizeService prizeService = new PrizeService();
		prizeService.setPackagePrizeMapping(this.getPrizeMapping());

		EligibilityService eligibilityService = mock(EligibilityService.class);
		List<String> prizes = Arrays.<String>asList("SPORTS");
		try {
			when(eligibilityService.isEligible(accountNumber)).thenThrow(new FailureException());
		} catch (InvalidAccountNumberException e) {
		} catch (FailureException e) {
			e.printStackTrace();
		}
		prizeService.setEligibilityService(eligibilityService);
		List<String> prizeList = null;
		try {
			prizeList = prizeService.getPrizes(accountNumber, prizes);
			assertEquals("In case of EligibilityService failure exception prize empty list should be returned",
					0, prizeList.size());
		} catch (InvalidAccountNumberException e) {
			fail("Invalid account exception should not be thrown for proper account number");
		}
	}


	private List<String> getPrizeListForCustomerByEligibility(PrizeService prizeService,
															  List<String> prizes, boolean isEligible) {
		String accountNumber = "1234567890";
		prizeService.setPackagePrizeMapping(this.getPrizeMapping());
		EligibilityService eligibilityService = mock(EligibilityService.class);
		try {
			when(eligibilityService.isEligible(accountNumber)).thenReturn(isEligible);
		} catch (InvalidAccountNumberException e) {
		} catch (FailureException e) {
			e.printStackTrace();
		}
		prizeService.setEligibilityService(eligibilityService);
		List<String> prizeList = null;
		try {
			prizeList = prizeService.getPrizes(accountNumber, prizes);
		} catch (InvalidAccountNumberException e) {
			fail("InvalidAccountNumberException has been thrown for correct account number");
		}
		return prizeList;
	}


	/**
	 * prepare used mapping for package -> prize
	 *
	 * @return
	 */
	private Map<String,String> getPrizeMapping() {
		HashMap<String, String> packagePrizeMap = new HashMap<String, String>();
		packagePrizeMap.put("SPORTS", "FREE SPORTING EVENT TICKETS");
		packagePrizeMap.put("MOVIES", "FREE MOVIE TICKETS");
		packagePrizeMap.put("GOSSIP", "FREE MOVIE TICKETS");
		return packagePrizeMap;
	}
}
