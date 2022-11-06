package com.bitvault.server;

public interface ServerListener {

    void onMessage(final String msg);

    void onError(final Throwable throwable);

}
