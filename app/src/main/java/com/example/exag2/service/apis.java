package com.example.exag2.service;

public class apis {
    public static final String URL_001="http://192.168.55.53:3000/libro/";
    public static final String URL_002="http://192.168.55.53:3000/categoria/";

    public static com.example.exag2.service.LibroService getLibroService(){
        return  Cliente.getClient(URL_001).create(com.example.exag2.service.LibroService.class);
    }

}
