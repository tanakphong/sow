package th.co.wesoft.sow;

import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CallWSActivity extends AppCompatActivity {

    protected class TestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String output = WSTest.getDataTest(params[0]);
            Log.i("dlg", "doInBackground: " + output);
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("dlg", "onPostExecute: " + s);
            try {
                XMLParser parser = new XMLParser();
                Document doc = parser.getDomElement(s);
                NodeList nl = doc.getElementsByTagName("GetCardRemainOrUsedResponse");
                Element item = (Element) nl.item(0);
                String CardTypeDesc = parser.getValue(item, "CardTypeDesc");
                String CardTypeCode = parser.getValue(item, "CardTypeCode");
                String CardExpText = parser.getValue(item, "CardExpText");
                String CardExp = parser.getValue(item, "CardExp");
                String RemainWithReserveText = parser.getValue(item, "RemainWithReserveText");
                String RemainWithReserve = parser.getValue(item, "RemainWithReserve");
                Log.i("dlg", "data : "+CardTypeDesc+", "+CardTypeCode+", "+CardExpText+", "+", "+CardExp+", "+", "+RemainWithReserveText+", "+", "+RemainWithReserve);
//                String result = parser.getValue(item, "jsInfo");
//                Log.i("dlg", "onPostExecute: jsInfo -> " + result);
//                JSONObject reader = new JSONObject(result);
//                String table = reader.getString("Table");
//                Log.i("dlg", "Table : " + table);
//                JSONArray jsonArray = new JSONArray(table);
//                JSONObject row = jsonArray.getJSONObject(0);
//                String column1 = row.getString("CompanyName");
//                Log.i("dlg", "column : "+column1);
            } catch (Exception aE) {
                Log.d("dlg", "onPostExecute  Exception : " + aE.getMessage());
            }



            /*for (int j = 0; j < nl.getLength(); j++) {
                Element e = (Element) nl.item(j);

                String product = parser.getValue(e, "PRODUCT").trim();
                String price = parser.getValue(e, "PRICE").trim();
                if (product.equals("Blue Diesel")) {
                    mDieselTextView.setText(price);
                } else if (product.equals("Blue Gasohol E85")) {
                    mE85TextView.setText(price);
                } else if (product.equals("Blue Gasohol E20")) {
                    mE20TextView.setText(price);
                } else if (product.equals("Blue Gasohol 91")) {
                    mGas91TextView.setText(price);
                } else if (product.equals("Blue Gasohol 95")) {
                    mGas95TextView.setText(price);
                }
            }*/
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_ws);


        new Prefs.Builder()
                .setContext(getApplicationContext())
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        String WCFHost = Prefs.getString(ConfigBean.COLUMN_WCF_HOST, "");
        String WCFPost = Prefs.getString(ConfigBean.COLUMN_WCF_PORT, "");
        String url = "http://"+WCFHost+":"+WCFPost+"/WFservice30?wsdl";

        new TestTask().execute(url);
    }
}
