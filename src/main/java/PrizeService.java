import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pawel on 2/26/14.
 */
public class PrizeService {

	private EligibilityService eligibilityService;
	/**
	 * the mapping for package->prize is being injected from outside
	 */
	private Map<String, String> packagePrizeMapping;

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

		String prize;
		for (String pack : packages) {
			prize = this.packagePrizeMapping.get(pack);
			if (prize != null && !prizes.contains(prize)) {
				prizes.add(prize);
			}
		}
		return prizes;
	}

	public void setPackagePrizeMapping(Map<String,String> packagePrizeMapping) {
		this.packagePrizeMapping = packagePrizeMapping;
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
