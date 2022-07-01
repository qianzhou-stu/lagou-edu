package com.lagou.gateway.service;

public interface IPermissionService {
    /**
     * @param authentication
     * @param userId
     * @param method
     * @param url
     * @return
     */
    boolean permission(String authentication, String userId, String url, String method);
}
