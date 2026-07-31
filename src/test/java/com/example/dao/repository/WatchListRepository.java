package com.example.dao.repository;

import com.example.demo.entity.WatchList;
import com.example.demo.entity.WatchListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListRepository extends JpaRepository<WatchList, Long> {

}
