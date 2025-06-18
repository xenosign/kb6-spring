package org.example.kb6spring.dto.weather;

import lombok.Data;

@Data
public class Sys{
	private String country;
	private int sunrise;
	private int sunset;
	private int id;
	private int type;
}