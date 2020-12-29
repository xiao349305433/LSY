package met.hx.com.base.baseconfig;

import android.content.Context;

import java.util.Locale;

/**
 * Created by huxu on 2017/2/17.
 */

public class TextStyleConfig {
    public static void initTextStyle(Context application) {
        //字体选择，中文字体和英文字体
        Locale locale = application.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.equals("zh")) {
//            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                    .setDefaultFontPath("fonts/Mawns_Handwriting.ttf")
//                    .setFontAttrId(R.attr.fontPath)
//                    .build());
        } else if (language.equals("en")) {
//            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                    .setDefaultFontPath("fonts/Mawns_Handwriting.ttf")
//                    .setFontAttrId(R.attr.fontPath)
//                    .build());
        }
    }
}
