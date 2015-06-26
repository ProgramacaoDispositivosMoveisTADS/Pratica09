package br.edu.ifpe.tads.pdm.pratica09;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadService  extends IntentService {
    private static final String TAG = "DownloadService";
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public DownloadService() {
        super("DownloadService");
    }
    @Override
    public void onHandleIntent(Intent intent) {
        Log.i(TAG, "DownloadService onHandleIntent");
        boolean success = false;
        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        String contentType = "";
        File output = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), fileName);
        InputStream stream = null;
        FileOutputStream fos = null;
        try {
            java.net.URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            contentType = connection.getContentType();
            stream = new BufferedInputStream(connection.getInputStream());
            fos = new FileOutputStream(output.getPath());
            Log.i(TAG, "DownloadService downdloading...");
            int next;
            byte [] BUF = new byte [512];
            while ((next = stream.read(BUF,0, 512)) != -1) {
                fos.write(BUF, 0, next);
            }
            success = true;
            Log.i(TAG, "DownloadService finished downloading.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        publishResults(fileName, output.getAbsolutePath(), contentType, success);
    }
    private void publishResults(String fileName, String filepath,
                                String contentType, boolean success) {
        Intent newIntent;
        String msg;
        if (success) {
            Uri uri = Uri.parse("file://" + filepath);
            newIntent = new Intent(Intent.ACTION_VIEW);
            newIntent.setDataAndType(uri, contentType);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            msg = "Completed: " + fileName;
        } else {
            newIntent = new Intent(this, MainActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            msg = "Failed: " + filepath;
        }
        showNotification(newIntent, msg);
    }
    private void showNotification(Intent intent, String msg) {
        //Intent newIntent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
               getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Contrói a notificação que será exibida ao usuário
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText(msg);
        builder.setContentIntent(pendingNotificationIntent);
        builder.setAutoCancel(true);
        Notification notification = builder.getNotification();
        // Obter o gerenciador de notificações do sistema e exibe a notificação
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
