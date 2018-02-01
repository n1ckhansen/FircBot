package com.blackfez.apis.darksky;

public class DarkSkyApiWrapperClient {

	public static void main(String[] args) {
		DarkSkyApiWrapper wrap = DarkSkyApiWrapper.getInstance();
		System.out.println( wrap.getForcastForZip("51503") );
	}

}
