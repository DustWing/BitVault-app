package com.bitvault.server.endpoints;

import com.bitvault.server.cache.ImportCache;
import com.bitvault.util.Result;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EndpointResolver {

    private final Map<String, Supplier<IGetEndpoint<?>>> getMap;
    private final Map<String, Supplier<IPostEndpoint<?>>> postMap;
    private final String getEndpointsStr;
    private final String postEndpointsStr;

    public static EndpointResolver create(ImportCache importCache) {

        Map<String, Supplier<IGetEndpoint<?>>> getMap = new HashMap<>();
        getMap.put("/key", () -> new SecureItemController(importCache));

        Map<String, Supplier<IPostEndpoint<?>>> postMap = new HashMap<>();
        postMap.put("/shareSecureItem", () -> new SecureItemController(importCache));

        return new EndpointResolver(getMap, postMap);
    }

    private EndpointResolver(
            Map<String, Supplier<IGetEndpoint<?>>> getMap,
            Map<String, Supplier<IPostEndpoint<?>>> postMap
    ) {
        this.postMap = postMap;
        this.getMap = getMap;
        this.getEndpointsStr = getMap.keySet().toString();
        this.postEndpointsStr = postMap.keySet().toString();
    }


    public Result<?> get(String uri) {

        Supplier<IGetEndpoint<?>> iGetEndpointSupplier = getMap.get(uri);

        if (iGetEndpointSupplier == null) {
            return Result.error(new EndpointException(
                    "No such endpoint found",
                    HttpResponseStatus.NOT_FOUND,
                    "Available get endpoints %s".formatted(getEndpointsStr)
            ));
        }

        IGetEndpoint<?> iGetEndpoint = getMap.get(uri).get();
        return iGetEndpoint.get();
    }

    public Result<?> post(String uri, String body) {

        Supplier<IPostEndpoint<?>> iPostEndpointSupplier = postMap.get(uri);
        if (iPostEndpointSupplier == null) {
            return Result.error(new EndpointException(
                    "No such endpoint found",
                    HttpResponseStatus.NOT_FOUND,
                    "Available get endpoints %s".formatted(postEndpointsStr)
            ));
        }

        IPostEndpoint<?> iPostEndpoint = postMap.get(uri).get();


        return iPostEndpoint.post(body);

    }


}
