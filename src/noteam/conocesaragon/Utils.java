package noteam.conocesaragon;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;

public class Utils {

    public static final String API_KEY = "d9b09536c7cdc0617c0b917c85a254b5";

    private static AndroidHttpClient getClient(String urldata) {
        AndroidHttpClient cliente = AndroidHttpClient.newInstance("Conoces Aragon");
        return cliente;
    }

    private static HttpGet getRequest(String urldata) {
        HttpGet request = new HttpGet(urldata);
        request.addHeader("accept", "application/json");
        request.setParams(request.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS,
                Boolean.TRUE));
        return request;
    }

    public static String getResponse(String urldata) {
        try {
            HttpResponse response = getClient(urldata).execute(getRequest(urldata));
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            if (statusCode == 200) {
                return EntityUtils.toString(entity);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
