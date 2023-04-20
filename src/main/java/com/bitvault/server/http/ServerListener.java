package com.bitvault.server.http;

import com.bitvault.util.Result;

public interface ServerListener {

    void onMessage(final Result<String> msg);

}
