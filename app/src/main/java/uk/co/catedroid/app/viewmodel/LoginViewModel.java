package uk.co.catedroid.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    public enum LOGIN_VIEW_STATES {
        LOGIN_FORM,
        LOGIN_IN_PROGRESS,
        LOGGED_IN
    }

    private final MutableLiveData<LOGIN_VIEW_STATES> state = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<LOGIN_VIEW_STATES> getState() {
        return state;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void performLogin(String username, String password) {
        state.setValue(LOGIN_VIEW_STATES.LOGIN_IN_PROGRESS);

        if (username.equals("") || password.equals("")) {
            error.setValue("Please provide both a username and password");
            state.setValue(LOGIN_VIEW_STATES.LOGIN_FORM);
            return;
        }

        // TODO: Perform some network operation to log in here...

        // For testing
        if (username.equals("cate") && password.equals("cate")) {
            state.setValue(LOGIN_VIEW_STATES.LOGGED_IN);
        } else if (username.equals("error")){
            error.setValue("Invalid username/password");
            state.setValue(LOGIN_VIEW_STATES.LOGIN_FORM);
        }
    }
}
