package jp.ac.anan_nct.pokereg.util.net;

import java.io.*;
import java.net.*;

import jp.ac.anan_nct.pokereg.config.Configuration;

import android.os.AsyncTask;

public class AsyncConnection extends AsyncTask<Void, Void, Void>{
	
	public interface Callback{
		void execute(InputStream is) throws IOException;
	}
	
	public AsyncConnection(Callback cb,String url) throws MalformedURLException{
		this.cb = cb;
		this.url = new URL(url);
	}
	
	private Callback cb;
	private URL url;
	
	@Override
	protected Void doInBackground(Void... params) {
        HttpURLConnection connection = null;
        return null;
	}
	
	private HttpURLConnection getConnection() throws IOException{
		if(Configuration.getInstance().isUseProxy()){
	        Proxy proxy = new Proxy(Proxy.Type.HTTP,
	        		new InetSocketAddress(
	        				Configuration.getInstance().getProxyHost(),
	        				Configuration.getInstance().getProxyPort()));
	        return (HttpURLConnection)this.url.openConnection(proxy);
		}else{
			return (HttpURLConnection)this.url.openConnection();
		}
	}
}


