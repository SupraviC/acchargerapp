package com.supravin.accharger;
//NoFeedback(5)WithFeedback,Chargyfi_Update
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AppUpdateActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private final String StorezipFileLocation = Environment.getExternalStorageDirectory() + "/sour";
    private final String DirectoryName = Environment.getExternalStorageDirectory() + "/Folder1/";
    private final String path = Environment.getExternalStorageDirectory()+"/Folder1/app-debug.apk";
//    String Url = "http://www.chargyfi.com/update_status/3/Chargyfi_Update.zip";
    //"https://www.chargyfi.com/update_status/chargyfiupdate/Chargyfi_Update.zip"
    private final String Url ="https://www.chargyfi.com/supracharge/chargerupdate/Charger_Update.zip";
    private TextView t11;
    int c_version,n_version;
    String cid;


    private ProgressBar Progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_update);
        Progressbar = (ProgressBar)findViewById(R.id.progressBar2);
        t11=(TextView)findViewById(R.id.textView11);


        //Get the bundle
        Bundle bundle = getIntent().getExtras();

//Extract the data…
         cid = bundle.getString("Cid");

        PackageInfo pInfo = null;
        try
        {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        int version = pInfo.versionCode;
        c_version=version;
    //    Toast.makeText(this, String.valueOf(c_version), Toast.LENGTH_SHORT).show();

//        if(startinternet1())
//        {
////            Toast.makeText(AppUpdateActivity.this, "Started Internet", Toast.LENGTH_LONG).show();
//
//        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            public void run()
          {

               if(startinternet1())
                {
                    DownloadZipfile mew = new DownloadZipfile();
                    mew.execute(Url);

                }


            }
        }, 1000 * 30 );



    }
    private boolean startinternet1()
    {

        boolean HaveConnectedtoi = false;
        final ConnectivityManager conman = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        Method dataMtd = null;
        try
        {
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        } catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
//        Toast.makeText(MainActivity.this, " INTERNET AVAILABLE", Toast.LENGTH_SHORT).show();
        dataMtd.setAccessible(true);

        try
        {
            dataMtd.invoke(conman, true);
            HaveConnectedtoi = true;
//            lwv1=new LoadWebView(InternetConnection.this);
//
//            lwv1.SetLoadFlagActive();
        } catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return HaveConnectedtoi;
    }

    class DownloadZipfile extends AsyncTask<String, String, String>
    {
        String result = "";

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
//            mProgressDialog = new ProgressDialog(AppUpdateActivity.this);
//            mProgressDialog.setMessage("Downloading...");
//            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... aurl)
        {
            int count;

            try
            {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                Log.e("Zip file1",StorezipFileLocation);
                OutputStream output = new FileOutputStream(StorezipFileLocation);
                Log.e("Zip file",StorezipFileLocation);

                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1)
                {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.close();
                input.close();
                result = "true";

            }
            catch (Exception e)
            {

                result = "false";
            }
            return null;

        }

        protected void onProgressUpdate(String... progress)
        {

            Log.d("ANDRO_ASYNC", progress[0]);
//            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
//            t11.setText(Integer.parseInt(progress[0]));
            Progressbar.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused)
        {

//            mProgressDialog.dismiss();
            Progressbar.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("true"))
            {
                unzip();

            }
            else
            {
              //  Toast.makeText(AppUpdateActivity.this, "************", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), InitiatingActivityMannual.class);
                startActivity(i);
            }

        }
    }
    //This is the method for unzip file which is store your location. And unzip folder will  store as per your desire location.

    private void unzip() {
        mProgressDialog = new ProgressDialog(AppUpdateActivity.this);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        new UnZipTask().execute(StorezipFileLocation, DirectoryName);
    }


    private class UnZipTask extends AsyncTask<String, Void, Boolean>
    {
        @SuppressWarnings("rawtypes")
        @Override
        protected Boolean doInBackground(String... params)
        {
            String filePath = params[0];
            String destinationPath = params[1];

            File archive = new File(filePath);
            try
            {
                ZipFile zipfile = new ZipFile(archive);
                for (Enumeration e = zipfile.entries(); e.hasMoreElements(); )
                {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    unzipEntry(zipfile, entry, destinationPath);
                }


                UnzipUtil d = new UnzipUtil(StorezipFileLocation, DirectoryName);
                d.unzip();


            }
            catch (Exception e)
            {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            mProgressDialog.dismiss();


//            final String strPath = path;
//            if (TextUtils.isEmpty(strPath))
//            {
//                return;
//            }
//            boolean isRoot = new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
//            if (!isRoot)
//            {
////                Toast.makeText(this, "手机没有Root", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            new Thread()
//            {
//                @Override
//                public void run()
//                {
//                    RootInstaller installer = new RootInstaller();
//                    final boolean result = installer.install(strPath);
//                    runOnUiThread(new Runnable()
//                    {
//                        @Override
//                        public void run()
//                        {
//                            if (result)
//                            {
////                                Toast.makeText(MainActivity.this, "安装成功", Toast.LENGTH_SHORT).show();
//                                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.virajmohite.a3phasecharger");
//                                if (launchIntent != null)
//                                {
//                                    startActivity(launchIntent);//null pointer check in case package name was not found
//                                }
//                            }
//                            else
//                            {
////                                Toast.makeText(MainActivity.this, "安装失败", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//            }.start();
     //   CurrentVersionCode();


                    Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            public void run()
            {


                    final PackageManager pm = getPackageManager();
                    String apkName = "app-debug.apk";
                    String fullPath = path;
                    PackageInfo info = pm.getPackageArchiveInfo(fullPath, 0);
                   // Toast.makeText(AppUpdateActivity.this, "VersionCode : " + info.versionCode + ", VersionName : " + info.versionName , Toast.LENGTH_SHORT).show();
                    n_version=info.versionCode;
                Log.e("NV",String.valueOf(n_version));

              //  Toast.makeText(AppUpdateActivity.this,c_version, Toast.LENGTH_SHORT).show();
                if(c_version<n_version)
                {
                    Log.e("Towards Updated Message","yes");
                    SendUpdatedMessage();
                }
                else if(c_version>n_version)
                {
                    Log.e("Cv>nv",String.valueOf(c_version>n_version));

                    Intent i = new Intent(AppUpdateActivity.this, InitiatingActivityMannual.class);

                    startActivity(i);
                }
                else if(c_version==n_version)
                {
                    Log.e("Cv=nv","yes");
                    Intent i = new Intent(AppUpdateActivity.this, InitiatingActivityMannual.class);

                    startActivity(i);
                }

                    }
              }, 1000 * 9 );

//        if(c_version<n_version)
//        {
//            Log.e("Towards Updated Message","yes");
//            SendUpdatedMessage();
//        }
//        else if(c_version>n_version)
//        {
//            Log.e("Cv>nv",String.valueOf(c_version>n_version));
//
//            Intent i = new Intent(AppUpdateActivity.this, InitiatingActivityMannual.class);
//
//            startActivity(i);
//        }
//        else if(c_version==n_version)
//        {
//            Log.e("Cv=nv","yes");
//            Intent i = new Intent(AppUpdateActivity.this, InitiatingActivityMannual.class);
//
//            startActivity(i);
//        }
//            final PackageManager pm = getPackageManager();
//            String apkName = "app-debug.apk";
//            String fullPath = path;
//            PackageInfo info = pm.getPackageArchiveInfo(fullPath, 0);
//            Toast.makeText(AppUpdateActivity.this, "VersionCode : " + info.versionCode + ", VersionName : " + info.versionName , Toast.LENGTH_SHORT).show();
//         //   Toast.makeText(this, "VersionCode : " + info.versionCode + ", VersionName : " + info.versionName , Toast.LENGTH_LONG).show();
//            Log.e("VersionCode" , String.valueOf(info.versionCode));
//            Log.e("VersionName" , String.valueOf(info.versionName));
        }

        private void unzipEntry(ZipFile zipfile, ZipEntry entry, String outputDir) throws IOException
        {

            if (entry.isDirectory())
            {
                createDir(new File(outputDir, entry.getName()));
                return;
            }

            File outputFile = new File(outputDir, entry.getName());
            if (!outputFile.getParentFile().exists())
            {
                createDir(outputFile.getParentFile());
            }

            // Log.v("", "Extracting: " + entry);
            BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            //noinspection EmptyTryBlock
            try
            {

            }
            finally
            {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        }

        private void createDir(File dir)
        {
            if (dir.exists())
            {
                return;
            }
            if (!dir.mkdirs())
            {
                throw new RuntimeException("Can not create dir " + dir);
            }
        }
    }

    private void SendUpdatedMessage()
    {
        {
            class SendUpdatedMessage extends AsyncTask<Void,Void,String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();


                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
//                loading.dismiss();

                    if(s.equalsIgnoreCase("Success")){

            final String strPath = path;
            if (TextUtils.isEmpty(strPath))
            {
                return;
            }
            boolean isRoot = new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
            if (!isRoot)
            {
//                Toast.makeText(this, "手机没有Root", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread()
            {
                @Override
                public void run()
                {
                    RootInstaller installer = new RootInstaller();
                    final boolean result = installer.install(strPath);
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (result)
                            {
//                                Toast.makeText(MainActivity.this, "安装成功", Toast.LENGTH_SHORT).show();
                                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.supravin.accharger");
                                if (launchIntent != null)
                                {
                                    startActivity(launchIntent);//null pointer check in case package name was not found
                                }
                            }
                            else
                            {
//                                Toast.makeText(MainActivity.this, "安装失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }.start();


                    }
                    else if(s.equalsIgnoreCase("Success1")){

                        Intent i = new Intent(AppUpdateActivity.this, InitiatingActivityMannual.class);

                        startActivity(i);

                    }
                    else {
                        Intent i = new Intent(AppUpdateActivity.this, InitiatingActivityMannual.class);

                        startActivity(i);
                    }


                }

                @Override
                protected String doInBackground(Void... params) {





                    RequestHandler rh = new RequestHandler();
                    HashMap<String,String> param = new HashMap<String,String>();
                    param.put("charger_id", cid);




                    //  param.put(KEY_IMAGE,imgl);
                    //noinspection UnnecessaryLocalVariable
                    String result = rh.sendPostRequest("https://www.chargyfi.com/supracharge/status_change.php", param);
                    return result;
                }
            }

            SendUpdatedMessage u = new SendUpdatedMessage();
            u.execute();

        }

    }

    private void CurrentVersionCode()
    {
        PackageInfo pInfo = null;
        try
        {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        int version = pInfo.versionCode;
        c_version=version;
//        Toast.makeText(this, c_version, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

}
