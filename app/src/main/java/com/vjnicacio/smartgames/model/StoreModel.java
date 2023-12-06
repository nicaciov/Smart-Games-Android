package com.vjnicacio.smartgames.model;

import java.util.Date;

public class StoreModel {

	public long id;
	public long game_id;
	public String store;
	public String lat;
	public String lon;
	public Date inclusion_date;
	public Date edit_date;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGame_id() {
		return game_id;
	}

	public void setGame_id(long game_id) {
		this.game_id = game_id;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public Date getInclusion_date() {
		return inclusion_date;
	}

	public void setInclusion_date(Date inclusion_date) {
		this.inclusion_date = inclusion_date;
	}

	public Date getEdit_date() {
		return edit_date;
	}

	public void setEdit_date(Date edit_date) {
		this.edit_date = edit_date;
	}

}
