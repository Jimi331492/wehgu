package com.wehgu.weixin.entity;

public class NetVisitEntity {
    private String schemes;
    private String ip_or_domain;
    private Integer port;
    private String serlet_context;

    public String getSchemes() {
        return schemes;
    }

    public void setSchemes(String schemes) {
        this.schemes = schemes;
    }

    public String getIp_or_domain() {
        return ip_or_domain;
    }

    public void setIp_or_domain(String ip_or_domain) {
        this.ip_or_domain = ip_or_domain;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSerlet_context() {
        return serlet_context;
    }

    public void setSerlet_context(String serlet_context) {
        this.serlet_context = serlet_context;
    }
}