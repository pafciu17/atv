import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
	public void testGetPrizeForNotEligibilityCustomer() {
		String accountNumber = "1234567890";
		EligibilityService eligibilityService = mock(EligibilityService.class);
		when(eligibilityService.isEligible(accountNumber)).thenReturn(false);

		PrizeService prizeService = new PrizeService();
		prizeService.setEligibilityService(eligibilityService);
		try {
			List<String> prizeList = null;
			prizeList = prizeService.getPrizes(accountNumber, Arrays.<String>asList("SPORTS"));
			assertEquals("For ineligible customer prize list is empty", 0, prizeList.size());
		} catch (InvalidAccountNumberException e) {
			fail("InvalidAcountNumberException has been thrown for correct account number");
		}
	}


}
