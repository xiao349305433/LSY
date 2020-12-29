package met.hx.com.base.baseinterface;

import com.amap.api.services.geocoder.GeocodeResult;

/**
 * Created by huxu on 2018/1/4.
 */

public interface OnMyGeocodeSearchedListener {
    void OnMyGeocodeSearched(GeocodeResult geocodeResult, int i);
}
