package com.morristaedt.mirror.modules;

import android.content.res.Resources;
import android.os.AsyncTask;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.morristaedt.mirror.R;
import com.morristaedt.mirror.requests.ForecastResponse;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit.client.OkClient;

/**
 * Created by orion on 2/26/16.
 */
public class GuageModule {
    public interface GuageListener
    {
        void receiveGuageHeight( String height );
    }

    private static final BiMap<String, String> namespaces = HashBiMap.create();
    static
    {
        namespaces.put("om","http://www.opengis.net/om/2.0");
        namespaces.put("xlink","http://www.w3.org/1999/xlink");
        namespaces.put("wml2","http://www.opengis.net/waterml/2.0");
    }

    public static final void getGuageHeight( final Resources resources, final GuageListener listener )
    {
        new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url( resources.getString(R.string.usgs_endpoint) )
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    ResponseBody body = response.body();
                    String responseStr = body.string();
                    body.close();
                    return responseStr;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                XPathFactory factory = XPathFactory.newInstance();
                XPath xpath = factory.newXPath();
                xpath.setNamespaceContext(
                        new NamespaceContext() {
                            @Override
                            public String getNamespaceURI(String prefix) {
                                return namespaces.get(prefix);
                            }

                            @Override
                            public String getPrefix(String namespaceURI) {
                                return namespaces.inverse().get(namespaceURI);
                            }

                            @Override
                            public Iterator getPrefixes(String namespaceURI) {
                                return namespaces.keySet().iterator();
                            }
                        });
                String result = null;
                try {
                    Document doc = (Document) xpath.evaluate("/",
                            new InputSource( new StringReader(s) ),
                            XPathConstants.NODE);

                    String xPathStr = resources.getString(R.string.usgs_xpath);
                    result = xpath.evaluate( xPathStr, doc);
                    listener.receiveGuageHeight(
                            String.format("%s ft", result) );
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
