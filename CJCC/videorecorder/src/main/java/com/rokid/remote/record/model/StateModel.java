package com.rokid.remote.record.model;


import com.rokid.glass.videorecorder.utils.Logger;

/**
 * Author: heshun
 * Date: 2020/4/21 10:21 PM
 * gmail: shunhe1991@gmail.com
 */
//public class StateModel extends BaseObservable {
public class StateModel {

    private String currentState = RecordState.IDLE;


    //    @Bindable
    public String getCurrentState() {
        return currentState;
    }

    public synchronized void setCurrentState(String currentState) {
        if (this.currentState.equals(currentState)) {
            return;
        }
        Logger.i("currentState", currentState);
        this.currentState = currentState;
//        notifyPropertyChanged(BR.currentState);
    }


}
