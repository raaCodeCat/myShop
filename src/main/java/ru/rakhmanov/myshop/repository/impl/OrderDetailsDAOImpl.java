package ru.rakhmanov.myshop.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rakhmanov.myshop.dto.db.view.OrderDetails;
import ru.rakhmanov.myshop.repository.OrderDetailsDAO;

import java.math.BigDecimal;

@Repository
@RequiredArgsConstructor
public class OrderDetailsDAOImpl implements OrderDetailsDAO {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<OrderDetails> getOrderById(Long orderId) {
        return template.select(OrderDetails.class)
                .from("order_details")
                .matching(Query.query(Criteria.where("order_id").is(orderId)))
                .all();
    }

    @Override
    public Flux<OrderDetails> getAllOrdersByUserId(Long userId) {
        return null;
    }

    @Override
    public Mono<Boolean> existsByOrderId(Long orderId) {
        return template.select(OrderDetails.class)
                .from("order_details")
                .matching(Query.query(
                        Criteria.where("order_id").is(orderId)
                 ))
                .first()
                .hasElement();
    }

    @Override
    public Mono<BigDecimal> getOrderTotalSum(Long orderId) {
        return template.getDatabaseClient()
                .sql("select sum(price * count) as sum from order_details where order_id = :orderId")
                .bind("orderId", orderId)
                .fetch()
                .one()
                .map(result -> (BigDecimal) result.get("sum"));
    }
}
