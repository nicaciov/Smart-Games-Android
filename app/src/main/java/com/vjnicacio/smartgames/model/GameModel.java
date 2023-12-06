package com.vjnicacio.smartgames.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GameModel implements Serializable {

	// Atributos do jogo
	public long id;
	public String game_name;
	public String game_description;
	public double game_price;
	public String photo;

	// Datas de inclusão e edição
	public Date inclusion_date;
	public Date edit_date;

	// Listas de plataformas e lojas associadas ao jogo
	public List<PlatformModel> listPlatforms;
	public List<StoreModel> listStores;

	// Métodos getter e setter para acessar e modificar os atributos
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGame_name() {
		return game_name;
	}

	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}

	public String getGame_description() {
		return game_description;
	}

	public void setGame_description(String game_description) {
		this.game_description = game_description;
	}

	public double getGame_price() {
		return game_price;
	}

	public void setGame_price(double game_price) {
		this.game_price = game_price;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
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

	public List<PlatformModel> getListPlatforms() {
		return listPlatforms;
	}

	public void setListPlatforms(List<PlatformModel> listPlatforms) {
		this.listPlatforms = listPlatforms;
	}

	public List<StoreModel> getListStores() {
		return listStores;
	}

	public void setListStores(List<StoreModel> listStores) {
		this.listStores = listStores;
	}
}