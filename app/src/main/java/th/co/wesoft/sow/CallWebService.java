package th.co.wesoft.sow;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class CallWebService {

    //private static final String URL = "http://192.168.3.33:1002/WFservice30?wsdl";
//    private static final String SOAP_ACTION = "http://tempuri.org/IWFServiceV30/GetTerminalInfoJS";
//    private static final String OPERATION_NAME = "GetTerminalInfoJS";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static Object resultRequestSOAP = null;
    private static final int TIMEOUT = 30000;

    public static String GetValidServiceServer(String url, String encode_pwd) {

        String SOAP_ACTION = "http://tempuri.org/IWFServiceV30/ValidServiceServer";
        String OPERATION_NAME = "ValidServiceServer";
        SoapObject request = new SoapObject(NAMESPACE, OPERATION_NAME);
        request.addProperty("encodePWD", encode_pwd);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        HttpsUtil.allowAllSSL();
        HttpTransportSE androidHttpTransport = new HttpTransportSE(url, TIMEOUT);
        androidHttpTransport.debug = true;

        try {
            androidHttpTransport.reset();
            androidHttpTransport.call(SOAP_ACTION, envelope);
//            resultRequestSOAP = envelope.getResponse(2);
            SoapObject soapObj = (SoapObject) envelope.bodyIn;
//            Log.i("dlg", "soapObj.getProperty(0) : " + soapObj.getProperty(0));
//            Log.i("dlg", "soapObj.getProperty(1) : " + soapObj.getProperty(1));
//            Log.i("dlg", "soap request " + androidHttpTransport.requestDump);
//            Log.i("dlg", "soap response" + androidHttpTransport.responseDump);
            return "{\"Return\":\"" + String.valueOf(soapObj.getProperty(0)) + "\",\"Exception\":\"" + String.valueOf(soapObj.getProperty(1)) + "\"}";
        } catch (ConnectException aE) {
//            Log.i("dlg", "ConnectException " + aE.toString());
            return "{\"Return\":\"false\",\"Exception\":\"Can't connect to WF Service.\"}";
        } catch (ClassCastException aE) {
//            Log.i("dlg", "ClassCastException " + aE.toString());
            return "{\"Return\":\"false\",\"Exception\":\"Function not found.\"}";
        } catch (SocketTimeoutException aE) {
            Log.i("dlg", "SocketTimeoutException " + aE.toString());
            return "{\"Return\":\"true\",\"Exception\":\"Time out.\",\"Data\":\"\"}";
        } catch (Exception aE) {
//            Log.i("dlg", "Exception " + aE.toString());
            return "{\"Return\":\"false\",\"Exception\":\"" + aE.getMessage() + "\"}";
        }
    }

    public static String GetTerminalInfoJS(String url, String encode_pwd, String ip) {

        String SOAP_ACTION = "http://tempuri.org/IWFServiceV30/GetTerminalInfoJS";
        String OPERATION_NAME = "GetTerminalInfoJS";
        SoapObject request = new SoapObject(NAMESPACE, OPERATION_NAME);
        request.addProperty("encodePWD", encode_pwd);
        request.addProperty("TerminalIPorTermCode", ip);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        HttpsUtil.allowAllSSL();
        HttpTransportSE androidHttpTransport = new HttpTransportSE(url, TIMEOUT);
        androidHttpTransport.debug = true;

        try {
            androidHttpTransport.reset();
            androidHttpTransport.call(SOAP_ACTION, envelope);
//            resultRequestSOAP = envelope.getResponse(2);
            SoapObject soapObj = (SoapObject) envelope.bodyIn;
//            Log.i("dlg", "soapObj.getProperty(0) : " + soapObj.getProperty(0));
//            Log.i("dlg", "soapObj.getProperty(1) : " + soapObj.getProperty(1));
//            Log.i("dlg", "soapObj.getProperty(1) : " + soapObj.getProperty(2));
            String table;
            try {
                JSONObject tables = new JSONObject(String.valueOf(soapObj.getProperty(2)));
                table = tables.getString("Table");
                table = table.substring(1, table.length() - 1);
                Log.i("dlg", "table : " + table);
            } catch (JSONException aE) {
                table = "\"\"";
            }
//            Log.i("dlg", "soap request " + androidHttpTransport.requestDump);
//            Log.i("dlg", "soap response" + androidHttpTransport.responseDump);
            return "{\"Return\":" + String.valueOf(soapObj.getProperty(0)) + ",\"Exception\":\"" + String.valueOf(soapObj.getProperty(1)) + "\",\"Data\":" + table + "}";
        } catch (ConnectException aE) {
            Log.i("dlg", "ConnectException " + aE.toString());
            return "{\"Return\":false,\"Exception\":\"Can't connect to WF Service.\",\"Data\":\"\"}";
        } catch (ClassCastException aE) {
            Log.i("dlg", "ClassCastException " + aE.toString());
            return "{\"Return\":false,\"Exception\":\"Function not found.\",\"Data\":\"\"}";
        } catch (SocketTimeoutException aE) {
            Log.i("dlg", "SocketTimeoutException " + aE.toString());
//            return "{\"Return\":\"false\",\"Exception\":\"Time out.\",\"Data\":\"\"}";
            return "{\"Return\":false,\"Exception\":\"Can't connect to WF Service.\",\"Data\":\"\"}";
        } catch (Exception aE) {
            Log.i("dlg", "Exception " + aE.toString());
//            return "{\"Return\":\"false\",\"Exception\":\""+aE.getMessage()+"\"}";
//            return "{\"Return\":\"false\",\"Exception\":\"+aE.getMessage()+\",\"Data\":\"\"}";
            return "{\"Return\":false,\"Exception\":\"Can't connect to WF Service.\",\"Data\":\"\"}";
        }

    }

    public static String GetCardRemainOrUsed(String url, String encode_pwd, String cardno) {

        String SOAP_ACTION = "http://tempuri.org/IWFServiceV30/GetCardRemainOrUsed";
        String OPERATION_NAME = "GetCardRemainOrUsed";
        SoapObject request = new SoapObject(NAMESPACE, OPERATION_NAME);
        request.addProperty("encodePWD", encode_pwd);
        request.addProperty("CardNo", cardno);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        HttpsUtil.allowAllSSL();
        HttpTransportSE androidHttpTransport = new HttpTransportSE(url, TIMEOUT);
        androidHttpTransport.debug = true;

        try {
            androidHttpTransport.reset();
            androidHttpTransport.call(SOAP_ACTION, envelope);
//            resultRequestSOAP = envelope.getResponse(2);
            SoapObject soapObj = (SoapObject) envelope.bodyIn;
//            Log.i("dlg", "soapObj.getProperty(0) : " + soapObj.getProperty(0));
//            Log.i("dlg", "soapObj.getProperty(1) : " + soapObj.getProperty(1));
//            Log.i("dlg", "soapObj.getProperty(2) : " + soapObj.getProperty(2));
//            Log.i("dlg", "soapObj.getProperty(3) : " + soapObj.getProperty(3));
//            Log.i("dlg", "soapObj.getProperty(4) : " + soapObj.getProperty(4));
//            Log.i("dlg", "soapObj.getProperty(5) : " + soapObj.getProperty(5));
//            Log.i("dlg", "soapObj.getProperty(6) : " + soapObj.getProperty(6));
//            Log.i("dlg", "soapObj.getProperty(7) : " + soapObj.getProperty(7));
//            Log.i("dlg", "soapObj.getProperty(8) : " + soapObj.getProperty(8));
//            Log.i("dlg", "soapObj.getProperty(9) : " + soapObj.getProperty(9));
//            Log.i("dlg", "soapObj.getProperty(10) : " + soapObj.getProperty(10));
//            Log.i("dlg", "soapObj.getProperty(11) : " + soapObj.getProperty(11));
//            Log.i("dlg", "soapObj.getProperty(12) : " + soapObj.getProperty(12));
//            Log.i("dlg", "soapObj.getProperty(13) : " + soapObj.getProperty(13));
//            Log.i("dlg", "soapObj.getProperty(14) : " + soapObj.getProperty(14));
//            Log.i("dlg", "soap request " + androidHttpTransport.requestDump);
//            Log.i("dlg", "soap response" + androidHttpTransport.responseDump);
//            return "{\"Return\":\"" + String.valueOf(soapObj.getProperty(0)) + "\",\"Exception\":\"" + String.valueOf(soapObj.getProperty(1)) + "\",\"Data\":\"\"}";
            return "{\"Return\":\"" + soapObj.getProperty(0) +
                    "\",\"Exception\":\"" + soapObj.getProperty(1) +
                    "\",\"CardPolicy\":\"" + soapObj.getProperty(2) +
                    "\",\"CardTypeCode\":\"" + soapObj.getProperty(3) +
                    "\",\"CardTypeDesc\":\"" + soapObj.getProperty(4) +
                    "\",\"RemainWithReserve\":\"" + soapObj.getProperty(5) +
                    "\",\"Reserve\":\"" + soapObj.getProperty(6) +
                    "\",\"UsedValue\":\"" + soapObj.getProperty(7) +
                    "\",\"CreditBalance\":\"" + soapObj.getProperty(8) +
                    "\",\"CardExp\":\"" + soapObj.getProperty(9) +
                    "\",\"CardExpText\":\"" + soapObj.getProperty(10) +
                    "\",\"CreditBalanceText\":\"" + soapObj.getProperty(11) +
                    "\",\"RemainWithReserveText\":\"" + soapObj.getProperty(12) +
                    "\",\"UsedValueText\":\"" + soapObj.getProperty(13) +
                    "\",\"MemName\":\"" + soapObj.getProperty(14) +
                    "\",\"LimitUseUnit\":\"" + soapObj.getProperty(15) +
                    "\",\"LimitRemain\":\"" + soapObj.getProperty(16) + "\"}";
        } catch (ConnectException aE) {
            Log.i("dlg", "ConnectException " + aE.toString());
            return "{\"Return\":\"true\",\"Exception\":\"Can't connect to WF Service.\"}";
        } catch (ClassCastException aE) {
            Log.i("dlg", "ClassCastException " + aE.toString());
            return "{\"Return\":\"true\",\"Exception\":\"Function not found.\"}";
        } catch (SocketTimeoutException aE) {
            Log.i("dlg", "SocketTimeoutException " + aE.toString());
            return "{\"Return\":\"true\",\"Exception\":\"Time out.\"}";
        } catch (Exception aE) {
            Log.i("dlg", "Exception " + aE.toString());
//            return "{\"Return\":\"false\",\"Exception\":\""+aE.getMessage()+"\"}";
            return "{\"Return\":\"true\",\"Exception\":\"+aE.getMessage()+\"}";
        }
    }

    public static String GetDataRep_ProdInfoJS(String host, String port, String encode_pwd, String deviceId, String barcode) {

        String URL = "http://" + host + ":" + port + "/WPservice20?wsdl";
        String SOAP_ACTION = "http://tempuri.org/IWPServiceV20/GetDataRep_ProdInfoJS";
        String OPERATION_NAME = "GetDataRep_ProdInfoJS";
        SoapObject request = new SoapObject(NAMESPACE, OPERATION_NAME);
        request.addProperty("encodePWD", encode_pwd);
        request.addProperty("DeviceID", deviceId);
        request.addProperty("Barcode", barcode);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        HttpsUtil.allowAllSSL();
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, TIMEOUT);
        androidHttpTransport.debug = true;

        try {
            androidHttpTransport.reset();
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject soapObj = (SoapObject) envelope.bodyIn;

//            Log.d("dlg", "GetDataRep_ProdInfoJS: "+soapObj);

            String table;
            try {
                JSONObject tables = new JSONObject(String.valueOf(soapObj.getProperty(1)));
                table = tables.getString("Table");
                table = table.substring(1, table.length() - 1);
//                Log.d("dlg", "table : " + table);
            } catch (JSONException aE) {
                table = "\"\"";
            }
            return "{\"Return\":true" +
                    ",\"Exception\":\"" + soapObj.getProperty(2).toString().replace("anyType{}","") +
                    "\",\"Data\":" + table +
                    "}";
        } catch (ConnectException aE) {
            Log.i("dlg", "ConnectException: " + aE.toString());
//            return "{\"Return\":\"false\",\"Exception\":\"Can't connect to WP Service.\"}";
            return "{\"Return\":false,\"Exception\":\"failed to connect to /" + host + " (port " + port + ")\"}";
        } catch (ClassCastException aE) {
            Log.i("dlg", "ClassCastException: " + aE.toString());
            return "{\"Return\":false,\"Exception\":\"Function not found.\"}";
        } catch (SocketTimeoutException aE) {
            Log.i("dlg", "SocketTimeoutException: " + aE.toString());
//            return "{\"Return\":\"true\",\"Exception\":\"Time out.\"}";
            return "{\"Return\":false,\"Exception\":\"failed to connect to /" + host + " (port " + port + ")\"}";
        } catch (Exception aE) {
            Log.i("dlg", "Exception: " + aE.toString());
//            return "{\"Return\":\"true\",\"Exception\":\"+aE.getMessage()+\"}";
            return "{\"Return\":false,\"Exception\":\"failed to connect to /" + host + " (port " + port + ")\"}";
        }
    }

    public static String CloseLicModule(String host, String port, String encode_pwd, String deviceId) {

        String URL = "http://" + host + ":" + port + "/WPservice20?wsdl";
        String SOAP_ACTION = "http://tempuri.org/IWPServiceV20/CloseLicModule";
        String OPERATION_NAME = "CloseLicModule";
        SoapObject request = new SoapObject(NAMESPACE, OPERATION_NAME);
        request.addProperty("encodePWD", encode_pwd);
        request.addProperty("DeviceID", deviceId);
        request.addProperty("MyModule", 21);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        HttpsUtil.allowAllSSL();
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, TIMEOUT);
        androidHttpTransport.debug = true;

        try {
            androidHttpTransport.reset();
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject soapObj = (SoapObject) envelope.bodyIn;

            return "{\"Return\":true" +
                    ",\"Exception\":\"" + soapObj.getProperty(1).toString().replace("anyType{}","") +
                    "\"}";
        } catch (ConnectException aE) {
            Log.i("dlg", "ConnectException: " + aE.toString());
            return "{\"Return\":false,\"Exception\":\"failed to connect to /" + host + " (port " + port + ")\"}";
        } catch (ClassCastException aE) {
            Log.i("dlg", "ClassCastException: " + aE.toString());
            return "{\"Return\":false,\"Exception\":\"Function not found.\"}";
        } catch (SocketTimeoutException aE) {
            Log.i("dlg", "SocketTimeoutException: " + aE.toString());
            return "{\"Return\":false,\"Exception\":\"failed to connect to /" + host + " (port " + port + ")\"}";
        } catch (Exception aE) {
            Log.i("dlg", "Exception: " + aE.toString());
            return "{\"Return\":false,\"Exception\":\"failed to connect to /" + host + " (port " + port + ")\"}";
        }
    }
}
