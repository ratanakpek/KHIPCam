/*
package com.kshrd.ipcam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

*/
/**
 * Created by rina on 1/30/17.
 *//*


class RecordStream extends Thread {

    String tag = "MjpegDownloader";
    private boolean run = true;

    public void cancel() {
        run = false;
    }

    public boolean isRunning() {
        return run;
    }

    @Override
    public void run() {
        while (run) {

            HttpURLConnection connection = null;
            BufferedInputStream bis = null;
            URL serverUrl = null;

            try {
                serverUrl = new URL(url);

                connection = (HttpURLConnection) serverUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();

                //determine boundary pattern
                //use the whole header as separator in case boundary locate in difference chunks
                Pattern pattern = Pattern.compile("--[_a-zA-Z0-9]*boundary\\s+(.*)\\r\\n\\r\\n", Pattern.DOTALL);
                Matcher matcher;

                bis = new BufferedInputStream(connection.getInputStream());
                byte[] image = new byte[0], read = new byte[4096], tmpCheckBoundry;
                int readByte, boundaryIndex;
                String checkHeaderStr, boundary;

                //always keep reading images from server
                while (run) {
                    try {
                        readByte = bis.read(read);
                        //no more data
                        if (readByte == -1) {
                            break;
                        }

                        tmpCheckBoundry = addByte(image, read, 0, readByte);
                        checkHeaderStr = new String(tmpCheckBoundry, "ASCII");

                        matcher = pattern.matcher(checkHeaderStr);
                        if (matcher.find()) {
                            //boundary is found
                            boundary = matcher.group(0);
                            boundaryIndex = checkHeaderStr.indexOf(boundary);
                            boundaryIndex -= image.length;

                            if (boundaryIndex > 0) {
                                image = addByte(image, read, 0, boundaryIndex);
                            } else {
                                image = delByte(image, -boundaryIndex);
                            }

                            Bitmap outputImg = BitmapFactory.decodeByteArray(image, 0, image.length);
                            if (outputImg != null) {
                                if (run) {
                                    newFrame(outputImg);
                                }
                            } else {
                                Log.e(tag, "Read image error");
                            }

                            int headerIndex = boundaryIndex + boundary.length();

                            image = addByte(new byte[0], read, headerIndex, readByte - headerIndex);
                        } else {
                            image = addByte(image, read, 0, readByte);
                        }
                    } catch (Exception e) {
                        Log.e(tag, e.getMessage());
                        break;
                    }
                }

            } catch (Exception e) {
                Log.e(tag, e.getMessage());
            }

            try {

                try {
                    bis.close();                        // HERE IS CHIVORN IMPROVE CODE . JUST CATCH WITH NULL POINTER. BECAUSE HAVE ERROR SOMETIMES.
                } catch (NullPointerException e) {
                }

                connection.disconnect();
                Log.i(tag, "disconnected with " + url);
            } catch (Exception e) {
                Log.e(tag, e.getMessage());
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.e(tag, e.getMessage());
            }
        }
    }

    private byte[] addByte(byte[] base, byte[] add, int addIndex, int length) {
        byte[] tmp = new byte[base.length + length];
        System.arraycopy(base, 0, tmp, 0, base.length);
        System.arraycopy(add, addIndex, tmp, base.length, length);
        return tmp;
    }

    private byte[] delByte(byte[] base, int del) {
        byte[] tmp = new byte[base.length - del];
        System.arraycopy(base, 0, tmp, 0, tmp.length);
        return tmp;
    }

    private void newFrame(Bitmap bitmap) {

    }
}*/
