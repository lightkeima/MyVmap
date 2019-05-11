package vmap.a2016.khkt.myvmap.Modules;

import java.util.List;

/**
 * Created by darkt on 1/9/2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
