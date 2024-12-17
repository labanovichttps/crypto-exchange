package com.labanovich.crypto.exchange.mapper;

import com.labanovich.crypto.exchange.dto.UserCreateDto;
import com.labanovich.crypto.exchange.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "wallet", ignore = true)
    @Mapping(target = "updateDatetime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    User map(UserCreateDto userCreateDto, String encodedPassword);
}
