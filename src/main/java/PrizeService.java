import java.util.ArrayList;
import java.util.List;

/**
 * Created by pawel on 2/26/14.
 */
public class PrizeService {

	private EligibilityService eligibilityService;

	/**
	 *
	 * @param accountNumber
	 * @param packages
	 * @throws InvalidAccountNumberException
	 */
	public List<String> getPrizes(String accountNumber, List<String> packages) throws InvalidAccountNumberException {
		if (!this.isValidAccountNumber(accountNumber)) {
			throw new InvalidAccountNumberException();
		}
		List<String> prizes = new ArrayList<String>();
		if (!this.eligibilityService.isEligible(accountNumber)) {
			return prizes;
		}
		prizes.add("test");
		return prizes;
	}

	public void setEligibilityService(EligibilityService eligibilityService) {
		this.eligibilityService = eligibilityService;
	}

	/**
	 * account number is expected to be 10 characters longs
	 *
	 * @param accountNumber
	 * @return
	 */
	private boolean isValidAccountNumber(String accountNumber) {
		return (accountNumber.length() == 10);
	}
}
