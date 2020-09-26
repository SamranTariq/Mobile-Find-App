package com.example.owner;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

class mail extends AsyncTask<Void, Void, Void> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("Email sending", "sending start");
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Sendmail sender = new Sendmail("iowner88@gmail.com", "iowner97944");
            Log.i("Email sending", "*********************************step*******************");
            sender.addAttachment(Environment.getExternalStorageDirectory().getPath() + "/test.jpg");
            Log.i("Email sending", "*********************step one******************");
            sender.sendMail("test0.1",
                    "succesfully",
                    "iowner88@gmail.com",
                    "itssamrantariq@gmail.com");
            Log.i("Email sending", "*******************go*************************");
        } catch (Exception e) {
            Log.e("mylog", "Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.i("Email sending", "1");
    }
}