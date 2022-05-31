package io.teach.infrastructure.service;

import javax.servlet.http.HttpServletRequest;

public interface DynamicServiceProvider {

    public void judge(HttpServletRequest req);
}
