import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsyncRestClient {

    String baseUrl = "";
    String apiVersion = "";
    String accessKey = "";

    AsyncRestClient(Context ctx) {
        baseUrl = ""
        apiVersion = ""
        accessKey = ""
    }

    public void postAsync(HashMap<String, String> paramList, String api) { //api - variable URL part
        AsyncTaskParams params = new AsyncTaskParams(paramList, api);
        new AsyncClient().execute(params);
    }

    private class AsyncClient extends AsyncTask<AsyncRestClient.AsyncTaskParams, Integer, JSONObject> {
        @Override
        protected JSONObject doInBackground(AsyncTaskParams... params) {

            return postData(params[0].params, params[0].api);
        }

        public JSONObject postData(HashMap<String, String> values, String api) {
            HttpClient httpclient = new DefaultHttpClient();
            // specify the URL you want to post to
            HttpPost httppost = new HttpPost(URL);
            try {
                String token = ACCESS_TOKEN;
                if (!token.isEmpty()) {
                    httppost.setHeader("", token);
                }
                httppost.setHeader("", "");

                List<NameValuePair> postParams = new ArrayList<NameValuePair>();

                for (Map.Entry<String, String> value : values.entrySet()) {
                    postParams.add(new BasicNameValuePair(value.getKey(), value.getValue()));
                }

                if (!postParams.isEmpty()) {
                    httppost.setEntity(new UrlEncodedFormEntity(postParams));
                }

                HttpResponse response = httpclient.execute(httppost);

                if (response != null) {
                    Reader in = new BufferedReader(
                            new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    char[] buf = new char[1000];
                    int l = 0;
                    while (l >= 0) {
                        builder.append(buf, 0, l);
                        l = in.read(buf);
                    }
                    JSONObject responseJson = new JSONObject(builder.toString());
                    return responseJson;
                }
            } catch (IOException e) {
                // process execption
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            if (json != null) {
                if (JSONHelper.GetIntValueOrEmpty(json, "status") == 200) {
                    try {
                        //do something with json
                    } catch (JSONException ex) {

                    }
                }
            }
        }
    }

    private static class AsyncTaskParams {
        HashMap<String, String> params;
        String api;


        AsyncTaskParams(HashMap<String, String> params, String api) {
            this.params = params;
            this.api = api;
        }
    }
}