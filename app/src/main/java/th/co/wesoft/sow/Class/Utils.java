package th.co.wesoft.sow.Class;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import jcifs.smb.SmbFile;
import th.co.wesoft.sow.R;

/**
 * Created by USER275 on 9/6/2017.
 */

public  class Utils {

    public static int setTheme(String theme) {
        int resTheme;
//        <!--Black White Red Green Blue Dark Blue Yellow Orange Pink-->
        switch (theme){
            case "Black":
                resTheme= R.style.AppTheme_Black;
                break;
            case "White":
                resTheme= R.style.AppTheme_White;
                break;
            case "Red":
                resTheme= R.style.AppTheme_Red;
                break;
            case "Green":
                resTheme= R.style.AppTheme_Green;
                break;
            case "Blue":
                resTheme= R.style.AppTheme_Blue;
                break;
            case "Dark Blue":
                resTheme= R.style.AppTheme_DarkBlue;
                break;
            case "Yellow":
                resTheme= R.style.AppTheme_Yellow;
                break;
            case "Orange":
                resTheme= R.style.AppTheme_Orange;
                break;
            case "Pink":
                resTheme= R.style.AppTheme_Pink;
                break;
            default:
                resTheme= R.style.AppTheme_DarkBlue;
        }
        return resTheme;

    }

    public static int getTextColor(String theme) {
        int resColor;
//        <!--Black White Red Green Blue Dark Blue Yellow Orange Pink-->
        switch (theme){
            case "Black":
                resColor= Color.WHITE;
                break;
            case "White":
                resColor= Color.BLACK;
                break;
            case "Red":
                resColor= Color.WHITE;
                break;
            case "Green":
                resColor= Color.WHITE;
                break;
            case "Blue":
                resColor= Color.WHITE;
                break;
            case "Dark Blue":
                resColor= Color.WHITE;
                break;
            case "Yellow":
                resColor= Color.BLACK;
                break;
            case "Orange":
                resColor= Color.BLACK;
                break;
            case "Pink":
                resColor= Color.BLACK;
                break;
            default:
                resColor= Color.WHITE;
        }
        return resColor;

    }

    public static String getIPAddress() {
        ArrayList<String> tmpNetwork = new ArrayList<>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        inetAddress.getAddress();
                        if (!inetAddress.getHostAddress().toString().startsWith("fe80")) {
                            tmpNetwork.add(inetAddress.getHostAddress().toString());
                        }
                    }
                }
            }
            return tmpNetwork.get(0);
        } catch (SocketException ex) {
            Log.i("dlg", "getIPAddress : " + ex.getMessage());
            return null;
        }
    }

    public static String GetAppPath(Context context){
        String extStoreage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String packageName = context.getPackageName();
        return  extStoreage + "/Android/data/" + packageName;
    }

    public static void CreateDirectory(File fileOrFolder){
        if (!fileOrFolder.exists()) {
            fileOrFolder.mkdirs();
        }
    }

    public static void RemoveDirectory(File fileOrFolder){
        if (fileOrFolder.isDirectory())
        {
            String[] children = fileOrFolder.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(fileOrFolder, children[i]).delete();
            }
        }
        fileOrFolder.delete();
    }

    public static boolean DownloadFileFormSMB(SmbFile newfile, File oldfile){
        try{
            if(newfile.length() != oldfile.length()) {
                InputStream is = newfile.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                //We create an array of bytes
                byte[] data = new byte[250];
                int current = 0;

                while ((current = bis.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, current);
                }

                FileOutputStream fos = new FileOutputStream(oldfile);
                fos.write(buffer.toByteArray());
                fos.flush();
                fos.close();
                buffer.flush();
                buffer.close();
                bis.close();
                return true;
            }else{
                return false;
            }
        }catch (Exception aE){
            aE.printStackTrace();
            return false;
        }
    }


}
