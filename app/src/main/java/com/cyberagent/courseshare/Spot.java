package com.cyberagent.courseshare;

import com.google.android.gms.maps.model.LatLng;

import java.net.URI;

/**
 * メインとなる場所を表すクラス
 * Created by tatsuya tanaka on 9/11/2014.
 */
public class Spot {
	private String name;
	private LatLng coordinates;
	private String description;
	private URI imageUri; 				// リスト化するかどうか。。。

	public Spot(String name, LatLng coordinates) {
		this(name, coordinates, null);
	}

	public Spot(String name, LatLng coordinates, String description) {
		this.name = name;
		this.coordinates = coordinates;
		this.description = description;
	}

	public Spot(String name, LatLng coordinates, String description, URI imageUri) {
		this.name = name;
		this.coordinates = coordinates;
		this.description = description;
		this.imageUri = imageUri;
	}

	public String getName() {
		return this.name;
	}

	public LatLng getCoordinates() {
		return this.coordinates;
	}

	public String getDescription() {
		return this.description;
	}

	public URI getImageURI() {
		return this.imageUri;
	}
}
