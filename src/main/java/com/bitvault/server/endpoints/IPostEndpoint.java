package com.bitvault.server.endpoints;

import com.bitvault.util.Result;

public interface IPostEndpoint<R> {


    Result<R> post(String body);
}
