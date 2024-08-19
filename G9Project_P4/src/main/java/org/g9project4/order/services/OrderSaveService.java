package org.g9project4.order.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.services.BoardInfoService;
import org.g9project4.order.repositories.OrderInfoRepository;
import org.g9project4.order.repositories.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderSaveService {
    private final OrderInfoRepository infoRepository;
    private final OrderItemRepository itemRepository;
    private final BoardInfoService boardInfoService;

}
