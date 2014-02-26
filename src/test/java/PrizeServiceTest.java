import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by pawel on 2/26/14.
 */


public class PrizeServiceTest {

	@Test
	public void testGetPrizesAgainstIncorrectInput() {
		PrizeService prizeService = new PrizeService();
		prizeService.getPrizes("", Arrays.asList());
	}

}
