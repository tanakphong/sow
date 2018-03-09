package th.co.wesoft.sow;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WSTest {

    //private static final String URL = "http://192.168.3.33:1002/WFservice30?wsdl";
    private static final String SOAP_ACTION = "http://tempuri.org/IWFServiceV30/GetCardRemainOrUsed";
    private static final String OPERATION_NAME = "GetCardRemainOrUsed";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static Object resultRequestSOAP = null;
    private static final int TIMEOUT = 30000;


    public static String getDataTest(String url) {

        SoapObject request = new SoapObject(NAMESPACE, OPERATION_NAME);
        request.addProperty("encodePWD", "admin");
        request.addProperty("CardNo", "10570188");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        HttpsUtil.allowAllSSL();
        HttpTransportSE androidHttpTransport = new HttpTransportSE(url, TIMEOUT);
        androidHttpTransport.debug = true;

        try {
            androidHttpTransport.reset();
/*            if(androidHttpTransport!=null){
                androidHttpTransport.reset();
                try {
                    androidHttpTransport.getServiceConnection().disconnect();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }*/
            androidHttpTransport.call(SOAP_ACTION, envelope);
            resultRequestSOAP = envelope.getResponse();
//            Log.i("dlg", "resultRequestSOAP.toString() : "+resultRequestSOAP.toString());
//            Log.i("dlg", "soap request " + androidHttpTransport.requestDump);
//            Log.i("dlg", "soap response" + androidHttpTransport.responseDump);

            return androidHttpTransport.responseDump;
        } catch (Exception aE) {
            Log.i("dlg", "Exception " + aE.toString());
        }

        return null;
    }

}
