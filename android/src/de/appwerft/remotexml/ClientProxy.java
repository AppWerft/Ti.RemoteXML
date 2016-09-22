/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package de.appwerft.remotexml;

import java.net.URI;
import java.net.URISyntaxException;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

// https://github.com/luis1987/PayPalAppcelerator/blob/master/android/src/com/bea/paypal/ModulopaypalModule.java
// example :https://github.com/paypal/PayPal-Android-SDK/blob/master/SampleApp/src/main/java/com/paypal/example/paypalandroidsdkexample/SampleActivity.java

@Kroll.proxy(creatableInModule = RemotexmlModule.class)
public class ClientProxy extends KrollProxy {
	final String LCAT = "remXML ⚫️";
	private String url;
	private boolean post = false;
	public KrollFunction onLoadCallback;
	public KrollFunction onErrorCallback;
	Context ctx = TiApplication.getInstance().getApplicationContext();

	// http://stackoverflow.com/questions/1823264/quickest-way-to-convert-xml-to-json-in-java
	public ClientProxy() {
		super();
		Log.d(LCAT, "ClientProxy created");
	}

	@Override
	public void handleCreationDict(KrollDict options) {
		Log.d(LCAT, "handleCreationDict");
		if (options.containsKeyAndNotNull(TiC.PROPERTY_URL)) {
			Log.d(LCAT, "containsKeyAndNotNull=" + TiC.PROPERTY_URL + "  "
					+ TiC.PROPERTY_ONLOAD);
			final URI uri;
			try {
				Log.d(LCAT, "url=" + options.getString(TiC.PROPERTY_URL));
				uri = new URI(options.getString(TiC.PROPERTY_URL));
				this.url = uri.toString();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		if (options.containsKeyAndNotNull("post")) {
			if (options.containsKeyAndNotNull("post")) {
				this.post = options.getBoolean("post");
			}
		}

		if (options.containsKeyAndNotNull(TiC.PROPERTY_ONLOAD)) {
			Object cb = options.get(TiC.PROPERTY_ONLOAD);
			if (cb instanceof KrollFunction) {
				Log.d(LCAT, TiC.PROPERTY_ONLOAD + " imported to this.onLoad");
				this.onLoadCallback = (KrollFunction) cb;
			}
		}
		if (options.containsKeyAndNotNull(TiC.PROPERTY_ONERROR)) {
			Object cb = options.get(TiC.PROPERTY_ONERROR);
			if (cb instanceof KrollFunction) {
				this.onErrorCallback = (KrollFunction) cb;
			}
		}
		super.handleCreationDict(options);
		Log.d(LCAT, "props imported");
		AsyncHttpClient client = new AsyncHttpClient();

		client.get(ctx, url, new XMLResponseHandler());
		Log.d(LCAT, "action started");
	}

	private final class XMLResponseHandler extends AsyncHttpResponseHandler {
		@Override
		public void onFailure(int status, Header[] header, byte[] response,
				Throwable arg3) {
			if (onErrorCallback != null)

				onErrorCallback.call(getKrollObject(), new KrollDict());

		}

		@Override
		public void onSuccess(int status, Header[] header, byte[] response) {
			try {
				onLoad(new KrollDict(org.jsonjava.XML.toJSONObject(
						new String(response)).toMap()));
			} catch (org.jsonjava.JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void onLoad(KrollDict data) {
		if (onLoadCallback != null) {
			KrollDict res = new KrollDict();
			res.put("data", data);
			res.put("runtime", 0);
			res.put("parsetime", 0);
			Log.d(LCAT, res.toString());
			onLoadCallback.call(getKrollObject(), res);
		} else
			Log.e(LCAT, "no onLoadCallback callback found");
	}

}