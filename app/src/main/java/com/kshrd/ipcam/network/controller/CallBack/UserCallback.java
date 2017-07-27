package com.kshrd.ipcam.network.controller.CallBack;

import com.kshrd.ipcam.entities.user.User;

/**
 * Created by Miss Chea Navy on 1/5/2017.
 */

public interface UserCallback {
    void loadUserByEmail(User user);
    void loadUserById(User user);
    void emailCheckerState(Boolean status);

}
