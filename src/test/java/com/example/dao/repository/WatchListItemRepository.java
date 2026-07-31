package com.example.dao.repository;

import com.example.demo.entity.User;
import com.example.demo.entity.WatchListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListItemRepository extends JpaRepository<WatchListItem, Long> {

}
