package com.example.lims_android.di;

import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;

public class NetEvents extends EventListener {

    @Override public void dnsStart(Call call, String domainName) {
        Log.d("NETEV", "dnsStart " + domainName);
    }

    @Override public void connectStart(Call call, InetSocketAddress addr, @Nullable Proxy proxy) {
        Log.d("NETEV", "connectStart " + addr);
    }

    @Override public void secureConnectStart(Call call) {
        Log.d("NETEV", "secureConnectStart (TLS)");
    }

    @Override public void secureConnectEnd(Call call, @Nullable Handshake handshake) {
        Log.d("NETEV", "secureConnectEnd: " +
                (handshake != null ? handshake.tlsVersion() + " " + handshake.cipherSuite() : "null"));
    }

    @Override public void connectEnd(Call call, InetSocketAddress addr, @Nullable Proxy proxy,
                                     @Nullable Protocol protocol) {
        Log.d("NETEV", "connectEnd " + addr + " proto=" + protocol);
    }

    @Override public void connectFailed(Call call, InetSocketAddress addr, @Nullable Proxy proxy,
                                        @Nullable Protocol protocol, IOException ioe) {
        Log.e("NETEV", "connectFailed " + addr + " proto=" + protocol + " : " + ioe);
    }

    @Override public void callFailed(Call call, IOException ioe) {
        Log.e("NETEV", "callFailed: " + ioe);
    }

    @Override public void callEnd(Call call) {
        Log.d("NETEV", "callEnd");
    }
}
