package com.vjnicacio.smartgames.model;

import java.io.Serializable;
import java.util.Date;

public class PlatformModel implements Serializable {

	// Atributos da plataforma
	public long id;
	public long game_id;
	public String platform;
	public Date inclusion_date;
	public Date edit_date;

	// Métodos getter e setter para acessar e modificar os atributos
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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
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