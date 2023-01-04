package com.bitvault.server.http;

public interface ServerListener {

    void onMessage(final String msg);

    void onError(final Throwable throwable);

}
