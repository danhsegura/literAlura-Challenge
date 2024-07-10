package com.aluracursos.literAlura;

public interface IDataConversion {

    <T> T dataQuery(String json, Class<T> clase);
}
