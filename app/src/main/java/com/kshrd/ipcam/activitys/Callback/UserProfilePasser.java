package com.kshrd.ipcam.activitys.Callback;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;

/**
 * Created by User on 1/12/2017.
 */

public interface UserProfilePasser {
    void userProfilePasser(int user_id, MultipartBody.Part user_profile);
}
