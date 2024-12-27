package com.labanovich.crypto.exchange.service;

import com.labanovich.crypto.exchange.enums.OrderStatus;
import com.labanovich.crypto.exchange.enums.OrderType;
import com.labanovich.crypto.exchange.mapper.OrderMapper;
import com.labanovich.crypto.exchange.repository.OrderRepository;
import com.labanovich.crypto.exchange.util.builder.OrderCreateDtoTestDataBuilder;
import com.labanovich.crypto.exchange.util.builder.OrderTestDataBuilder;
import com.labanovich.crypto.exchange.util.builder.UserTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private UserService userService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private WalletService walletService;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetails userDetails;
    private static final String TEST_USERNAME = "labanovich";


    void initSecurity() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(TEST_USERNAME);
    }

    @Test
    void shouldMatchAllOrders() {
        var pendingOrders = List.of(OrderTestDataBuilder.aOrder().build());
        doReturn(pendingOrders)
            .when(orderRepository).findByStatus(OrderStatus.PENDING);
        orderService.matchAllOrders();

        verify(orderRepository).findByStatus(OrderStatus.PENDING);
        verify(orderRepository, times(pendingOrders.size())).save(any());
    }

    @Test
    void shouldCreateOrder() {
        initSecurity();
        var orderCreateDto = OrderCreateDtoTestDataBuilder.aOrderCreateDto().build();
        var user = UserTestDataBuilder.aUser().withUsername(TEST_USERNAME).build();
        var order = OrderTestDataBuilder.aOrder().withUser(user).build();
        doReturn(user).when(userService).get(any());
        doReturn(order).when(orderMapper).map(orderCreateDto, user);
        orderService.create(orderCreateDto);
        verify(userService).get(TEST_USERNAME);
        verify(orderMapper).map(orderCreateDto, user);
        verify(orderRepository).save(any());
    }

    @Test
    void shouldMatchOrders() {
        var newOrder = OrderTestDataBuilder.aOrder().withType(OrderType.BUY).build();
        var orderToMatch = OrderTestDataBuilder.aOrder().withType(OrderType.SELL).withAmount(BigDecimal.valueOf(50)).build();
        var ordersToMatch = List.of(orderToMatch);
        doReturn(ordersToMatch).when(orderRepository).findByCurrencyAndTypeAndStatus(any(), any(), any());
        orderService.matchOrders(newOrder);
        verify(orderRepository).findByCurrencyAndTypeAndStatus(any(), any(), any());
        verify(walletService).depositForOrder(any(), any(), any());
        verify(walletService).withdrawForOrder(any(), any(), any());
        verify(orderRepository, times(ordersToMatch.size() + 1)).save(any());
    }
}