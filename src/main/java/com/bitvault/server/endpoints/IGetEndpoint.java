package com.bitvault.server.endpoints;

import com.bitvault.util.Result;

public interface IGetEndpoint<R> {
    Result<R> get();
}
