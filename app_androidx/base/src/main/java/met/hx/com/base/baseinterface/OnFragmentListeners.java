package met.hx.com.base.baseinterface;


import androidx.fragment.app.Fragment;

import met.hx.com.base.baseevent.Event;

/**
 * Created by huxu on 2016/5/19.
 */
public interface OnFragmentListeners {
    void getData(Fragment fragment, Event event);
}
