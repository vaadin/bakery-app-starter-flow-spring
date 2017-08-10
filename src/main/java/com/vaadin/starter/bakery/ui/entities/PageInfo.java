package com.vaadin.starter.bakery.ui.entities;

public class PageInfo {
	private String link;
	private String icon;
	private String title;

	public PageInfo() {

	}

	public PageInfo(String link, String icon, String title) {
		this.link = link;
		this.icon = icon;
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
