package wu.loushanyun.com.fivemoduleapp.v.activity;

import android.app.Activity;
import android.os.Bundle;

import wu.loushanyun.com.fivemoduleapp.R;

public class
BaseLocationMapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_location_map);
    }
}
