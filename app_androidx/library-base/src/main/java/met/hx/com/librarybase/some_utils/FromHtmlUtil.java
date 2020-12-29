package met.hx.com.librarybase.some_utils;

public class FromHtmlUtil {

    public static String getRedhtml(String str){

       return  "<font color='red' size='20'>"+str+"</font>";
    }

    public static String getBrhtml(){

        return  "<br>";
    }

    public static String getPrehtml(){

        return  "&nbsp;";
    }
    public static String get3Prehtml(){

        return  "&nbsp;&nbsp;&nbsp;";
    }
}
