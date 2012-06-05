package projet1A.And6a.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class HTTPJson {

	public static HttpResponse postData(String result, JSONObject obj) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams myParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(myParams, 10000);
		HttpConnectionParams.setSoTimeout(myParams, 10000);

		try {

			HttpPost httppost = new HttpPost(result.toString());
			httppost.setHeader("Content-type", "application/json");

			StringEntity se = new StringEntity(obj.toString());
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
			httppost.setEntity(se);

			HttpResponse response = httpclient.execute(httppost);
			// String temp = EntityUtils.toString(response.getEntity());
			// Log.i("tag", temp);

			return response;

		} catch (ClientProtocolException e) {

		} catch (IOException e) {
		}
		return null;
	}

	/*
	 * static public JSONObject doPost(String url, JSONObject obj) throws
	 * JSONException { HttpResponse rep = HTTPJson.postData(url, obj); /* try {
	 * String _response = EntityUtils.toString(rep.getEntity()); final
	 * JSONObject jObject=new JSONObject(_response);
	 * 
	 * Log.d("test1 = ", _response); } catch (ParseException e) { // TODO
	 * Auto-generated catch block Log.d("text1 P", e.getMessage()); } catch
	 * (Exception e) { // TODO Auto-generated catch block Log.d("text1 I",
	 * e.getMessage()); } /
	 * 
	 * final JSONObject jObject = new JSONObject(rep.getEntity().toString());
	 * return jObject; }
	 * 
	 * static public JSONObject doPost(String url) throws JSONException { return
	 * doPost(url, new JSONObject()); }
	 */

	public static JSONObject doPost(String url) throws ClientProtocolException,
			JSONException, IOException {
		return doPost(url, new JSONObject());
	}

	public static JSONObject doPost(String url, JSONObject obj)
			throws ClientProtocolException, JSONException, IOException {

		// Create the httpclient
		HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000); // Timeout
																		          // Limit

		// Prepare a request object
		// HttpPost httppost = new HttpPost(url);

		HttpPost httpPost = new HttpPost(url);
		StringEntity entity1 = new StringEntity(obj.toString(), HTTP.UTF_8);
		entity1.setContentType("application/json");
		httpPost.setEntity(entity1);
		// HttpClient client = new DefaultHttpClient();
		// HttpResponse response = client.execute(httpPost);

		/*
		 * List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		 * postParams.add(new BasicNameValuePair("json", obj.toString()));
		 * UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(postParams);
		 * entity1.setContentEncoding(HTTP.UTF_8);
		 * entity1.setContentType("application/json");
		 * httppost.setEntity(entity1);
		 * 
		 * httppost.setHeader("Content-Type", "application/json");
		 * httppost.setHeader("Accept", "application/json");
		 */

		// httppost.setHeader("Content-type", "application/json");
		/*
		 * StringEntity se = new StringEntity("JSON: " + obj.toString(),
		 * HTTP.UTF_8); se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
		 * "application/json")); httppost.setEntity(se);
		 */
		/*
		 * StringEntity se = new StringEntity( "JSON: " + obj.toString());
		 * se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
		 * "application/json")); httppost.setEntity(se);
		 */

		// Execute the request
		HttpResponse response;

		// Open the webpage.
		response = httpClient.execute(httpPost);

		if (response.getStatusLine().getStatusCode() == 200) {
			// Connection was established. Get the content.

			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {
				// A Simple JSON Response Read
				InputStream instream = entity.getContent();

				// Load the requested page converted to a string into a
				// JSONObject.
				JSONObject myAwway = new JSONObject(
						convertStreamToString(instream));

				// Close the stream.
				instream.close();

				return myAwway;
			}
		}
		return null;
		// return returnString;
	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
