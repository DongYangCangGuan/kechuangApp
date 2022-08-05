package com.cloud.servicewechat.entity.weixin;

/**
 * @Author:cat
 * @Description 公众号菜单Model
 * @Date: 2018-04-12  10:00
 * @Modified By:
 */
public class MenuModel {

	private String id;
	private String name;
	private String type;
	private String key;
	private String url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
