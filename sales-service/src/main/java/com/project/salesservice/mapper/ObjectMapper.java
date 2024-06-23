package com.project.salesservice.mapper;

public interface ObjectMapper<T,R>{

    R map(T t);

    T reverseMap(R r);
}
