package com.cloud.servicemanage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author RocLi
 *
 * <p>Description: </p>
 *
 * 2018年5月21日
 *
 */
@Component
@ConfigurationProperties(prefix = "wechat")
public class MyConfig {


	private String WXTOKEN;//KmYrThM1VMTam6Nm6cuaRpxzZ716XtUN

	private String WXAPPID;//wx5a8a62bc1e22e8d0

	private String WXAPPSECRET;//e7c140f3c6dbdfe92676bd14b7001f8e

	private String WXURLPATH;//http://www.jztec.net

	private String WXAESKEY;

	private String WXMCHID;

	private String WXMCHKEY;

	private String WXKEYPATH;

	public String getWXTOKEN() {
		return WXTOKEN;
	}

	public void setWXTOKEN(String wXTOKEN) {
		WXTOKEN = wXTOKEN;
	}

	public String getWXAPPID() {
		return WXAPPID;
	}

	public void setWXAPPID(String wXAPPID) {
		WXAPPID = wXAPPID;
	}

	public String getWXAPPSECRET() {
		return WXAPPSECRET;
	}

	public void setWXAPPSECRET(String wXAPPSECRET) {
		WXAPPSECRET = wXAPPSECRET;
	}

	public String getWXURLPATH() {
		return WXURLPATH;
	}

	public void setWXURLPATH(String wXURLPATH) {
		WXURLPATH = wXURLPATH;
	}

	public String getWXAESKEY() {
		return WXAESKEY;
	}

	public void setWXAESKEY(String wXAESKEY) {
		WXAESKEY = wXAESKEY;
	}

	public String getWXMCHID() {
		return WXMCHID;
	}

	public void setWXMCHID(String wXMCHID) {
		WXMCHID = wXMCHID;
	}

	public String getWXMCHKEY() {
		return WXMCHKEY;
	}

	public void setWXMCHKEY(String wXMCHKEY) {
		WXMCHKEY = wXMCHKEY;
	}

	public String getWXKEYPATH() {
		return WXKEYPATH;
	}

	public void setWXKEYPATH(String wXKEYPATH) {
		WXKEYPATH = wXKEYPATH;
	}



}
