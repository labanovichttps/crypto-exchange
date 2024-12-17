package com.labanovich.crypto.exchange.mapper;

import com.labanovich.crypto.exchange.dto.OrderCreateDto;
import com.labanovich.crypto.exchange.entity.Order;
import com.labanovich.crypto.exchange.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdDatetime", ignore = true)
    Order map(OrderCreateDto orderCreateDto, User user);
}
