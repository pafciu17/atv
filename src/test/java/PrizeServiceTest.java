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
	public void testGetPrizesAgainstIncorrectInput() {
		try {
			PrizeService prizeService = new PrizeService();
			prizeService.getPrizes("", Arrays.<String>asList());
			fail("PrizeService did not throw InvalidAccountNumberException");
		} catch (InvalidAccountNumberException e) {
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
	public void testGetPrizeForEligible() {
		PrizeService prizeService = new PrizeService();
		prizeService.setPackagePrizeMapping(this.getPrizeMapping());
		List<String> prizes = Arrays.<String>asList("SPORTS");

		List<String> prizeList = this.getPrizeListForCustomerByEligibility(prizeService, prizes, true);
		assertEquals("For ineligible customer prize list is empty", 1, prizeList.size());


		System.out.println(prizeList.get(0));
	}

	private List<String> getPrizeListForCustomerByEligibility(PrizeService prizeService,
															  List<String> prizes, boolean isEligible) {
		String accountNumber = "1234567890";
		EligibilityService eligibilityService = mock(EligibilityService.class);
		when(eligibilityService.isEligible(accountNumber)).thenReturn(isEligible);
		prizeService.setEligibilityService(eligibilityService);
		List<String> prizeList = null;
		try {
			prizeList = prizeService.getPrizes(accountNumber, prizes);
		} catch (InvalidAccountNumberException e) {
			fail("InvalidAcountNumberException has been thrown for correct account number");
		}
		return prizeList;
	}


	/**
	 * prepare used mapping for package -> price
	 *
	 * @return
	 */
	public Map<String,String> getPrizeMapping() {
		HashMap<String, String> packagePrizeMap = new HashMap<String, String>();
		packagePrizeMap.put("SPORTS", "FREE SPORTING EVENT TICKETS");
		packagePrizeMap.put("MOVIES", "FREE MOVIE TICKETS");
		packagePrizeMap.put("GOSSIP", "FREE MOVIE TICKETS");
		return packagePrizeMap;
	}
}
