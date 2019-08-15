package cf.digul.shortener.util;

import java.math.BigInteger;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShortUrlGenerator {
	private static final Logger logger = LogManager.getLogger(ShortUrlGenerator.class);
	
	private static final String CODEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int CODEC_BASE = CODEC.length();
	
	/**
	 * 몽고디비에서 생성된 16진법 binary string형식 id로 base62 encoding 한다.
	 * @param binaryString
	 * @return base62 string
	 */
	public static String encode(String str) {	
		if(str == null || str.length() == 0) {
			logger.debug("## ShortUrlGenerator ## cannot encode empty data");
			return "";
		}
		String realStr = null;
		try {
			realStr = String.valueOf(Hex.decodeHex(str));	// 입력된 16진수를 아스키코드로 하는 문자열 
		} catch (DecoderException e) {
			logger.error(e.getLocalizedMessage());
			return "error";
		}	
		logger.debug(String.format("## ShortUrlGenerator ## convert [ %s ] to [ %s ]", str, realStr));
		
		byte[] byteArray = realStr.getBytes();					// 문자열 -> byte array -> int 변환
		StringBuffer sb = new StringBuffer();
		int intValue = new BigInteger(byteArray).intValue();
		logger.debug(String.format("## ShortUrlGenerator ## convert to int value [ %d ]", intValue));

		do {
			char charValue = CODEC.charAt(intValue % CODEC_BASE);
			intValue /= CODEC_BASE;
			sb.append(charValue);
		} while(intValue > 0);			// base62 문자 매핑
		
		str = sb.toString();
		logger.debug("## ShortUrlGenerator ## convert finished : " + str);
		
		return str;	
	}
}
