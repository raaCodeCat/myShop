package ru.rakhmanov.myshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rakhmanov.myshop.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
