package com.song.common.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HttpPathUtil {

    /**
     * 获得请求的完整地址
     * 
     * @param request
     * @return
     */
    public static String getBasePath(HttpServletRequest request) {
        StringBuffer url = new StringBuffer();
        String path = request.getContextPath();
        if (!"/".equals(path)) {
            path = path + "/";
        } else {
            path = "/";
        }
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        url.append(scheme).append("://");
        url.append(serverName);

        int port = request.getServerPort();

        if ((scheme.equals("http") && port != 80)
                || (scheme.equals("https") && port != 443)) {
            url.append(":").append(port).append(path);

        } else {
            url.append(path);
        }
        return url.toString();
    }

    /**
     * 全部小写参数名称
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String pn = params.nextElement();
            map.put(pn.toLowerCase(), request.getParameter(pn));
        }

        return map;
    }
}
