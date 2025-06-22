package ru.rakhmanov.myshop.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.db.ItemWithCount;
import ru.rakhmanov.myshop.dto.db.entity.Item;

public interface ItemRepository extends R2dbcRepository<Item, Long> {

    @Query("""
        select i.*, coalesce(oi.count, 0) as count
        from items i
        left join orderitems oi on i.id = oi.item_id
            and oi.order_id = (select id from orders where user_id = :userId and is_paid = false limit 1)
        where i.id = :itemId
    """)
    Mono<ItemWithCount> findByItemIdWithCountInCart(
            @Param("itemId") Long itemId,
            @Param("userId") Long userId
    );

}
