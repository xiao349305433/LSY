package met.hx.com.librarybase.some_utils;


import android.text.TextUtils;

/**
 * XHStringUtil
 * 
 * @author fengx
 * 
 */
public class XHStringUtil {

	public static boolean isEmpty(String text, boolean trimFlg) {

		if (TextUtils.isEmpty(text)||text.equals("null")||text.equals("NULL")) {
			return true;
		}

		String value = text;
		if (trimFlg) {
			value = text.trim();
		}

		return value.length() == 0;
	}



}
