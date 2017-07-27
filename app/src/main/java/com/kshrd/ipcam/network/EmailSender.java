package com.kshrd.ipcam.network;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.kshrd.ipcam.R;
import com.kshrd.ipcam.fragment.FragConfirm;
import com.kshrd.ipcam.network.mail.service.GmailService;

/**
 * Created by rina on 1/22/17.
 */

/**
 * @Class EmailSender
 * @Autho rina
 */

public class EmailSender extends AsyncTask<String,Void,String> {

    private String gen;
          public  EmailSender(){
              gen = new SecretKeyGenerator().getGen();
          }

    /**
     * @Method sendEmail
     * @param receiver person that  receive key
     */
    @Override
    protected String doInBackground(String... receiver) {

        try {
            GmailService gmailService = new GmailService("annabean68@gmail.com","12345anna");
            gmailService.sendMail("Confirm Code",
                    "<html><body  style=' margin-left:45px;'><h2 style='color:#4CAF50;'>Please confirm code below </h2>" +
                            "<h3>Confirm Code:<b style='color:#673AB7;'><a href='#'>"+ getGen() +"</a></b></h3>" +
                            "<p style='color:blue;'>If code above is not working , Please let's send again<p></body></html>"
                    ,"IPCAM",receiver[0]);



        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SendMail", e.getMessage(), e);
        }
        return gen;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("CONFIRM", "onPostExecute: "+s);
    }

    public String getGen() {
        return gen;
    }
}
