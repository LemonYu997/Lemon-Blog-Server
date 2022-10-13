package lemonyu997.top.lemonapi.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

//获取Ip信息
@Slf4j
public class IpUtils {

    private static Logger logger = LoggerFactory.getLogger(IpUtils.class);

    //获取IP地址
    //使用Nginx等反向代理软件，则不能通过request.getRemoteAddr()获取IP地址
    //如果使用了多级反向代理，X-Forwarded-For的值并不止一个，而是一串IP地址
    //X-Forward-For中的第一个非unknow的有效IP字符串，则为真实IP地址
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        String unknow = "unknow";
        String seperator = ",";

        try {
            ip = request.getHeader("x-forward-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            logger.error("IPUtils ERROR ", e);
        }
        //使用代理，则获取第一个IP地址
        if (StringUtils.isEmpty(ip) && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}
