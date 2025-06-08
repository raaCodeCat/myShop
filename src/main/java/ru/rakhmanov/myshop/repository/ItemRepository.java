package ru.rakhmanov.myshop.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.rakhmanov.myshop.dto.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> getItemsByNameLikeIgnoreCase (
            @Param("search") String search,
            Pageable pageable
    );

    Integer countItemsByNameLikeIgnoreCase(@Param("search") String search);

}
