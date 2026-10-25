package com.kasa.api.model;
import java.time.LocalDateTime;
public record Account(Integer id,String name,String type,LocalDateTime createdAt) {

}
