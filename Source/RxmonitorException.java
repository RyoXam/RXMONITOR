
public class RxmonitorException extends NirvaException{
	private int errorCode = 101;
	private String syst;
	/**
	 * @param arg0
	 */
	public RxmonitorException(String syst, int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		this.syst = syst;
	}

	public final String getSyst() {
		return syst;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}
}
