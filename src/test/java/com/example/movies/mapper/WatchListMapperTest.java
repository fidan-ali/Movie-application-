package com.example.movies.mapper;

import com.example.movies.dao.entity.WatchListEntity;
import com.example.movies.dto.WatchListRequestDto;
import com.example.movies.dto.WatchListResponseDto;
import com.example.movies.dto.WatchlistListResponseDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static com.example.movies.constant.MovieApiTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WatchListMapperTest {

    private final WatchListMapper mapper =
            Mappers.getMapper(WatchListMapper.class);

    @Test
    void shouldMapRequestDtoToEntity() {

        WatchListRequestDto request =
                new WatchListRequestDto();

        request.setName(WATCH_LIST_NAME);

        WatchListEntity result =
                mapper.toEntity(request);

        assertEquals(
                WATCH_LIST_NAME,
                result.getName()
        );
    }

    @Test
    void shouldMapEntityToResponseDto() {

        WatchListEntity watchList =
                new WatchListEntity();

        watchList.setId(ID);
        watchList.setName(WATCH_LIST_NAME);

        WatchListResponseDto result =
                mapper.toResponse(watchList);

        assertEquals(
                ID,
                result.getId()
        );

        assertEquals(
                WATCH_LIST_NAME,
                result.getName()
        );
    }

    @Test
    void shouldMapEntityListToResponseDtoList() {

        WatchListEntity watchList1 =
                new WatchListEntity();

        watchList1.setId(ITEM_ID_1);
        watchList1.setName(WATCH_LIST_NAME);

        WatchListEntity watchList2 =
                new WatchListEntity();

        watchList2.setId(ITEM_ID_2);
        watchList2.setName(SECOND_WATCH_LIST_NAME);

        List<WatchListEntity> watchLists =
                List.of(watchList1, watchList2);

        List<WatchListResponseDto> result =
                mapper.toResponseList(watchLists);

        assertEquals(
                TWO,
                result.size()
        );

        assertEquals(
                ITEM_ID_1,
                result.get(FIRST_INDEX).getId()
        );

        assertEquals(
                WATCH_LIST_NAME,
                result.get(FIRST_INDEX).getName()
        );

        assertEquals(
                ITEM_ID_2,
                result.get(SECOND_INDEX).getId()
        );

        assertEquals(
                SECOND_WATCH_LIST_NAME,
                result.get(SECOND_INDEX).getName()
        );
    }

    @Test
    void shouldMapEntityListToListResponseDto() {

        WatchListEntity watchList1 =
                new WatchListEntity();

        watchList1.setId(ITEM_ID_1);
        watchList1.setName(WATCH_LIST_NAME);

        WatchListEntity watchList2 =
                new WatchListEntity();

        watchList2.setId(ITEM_ID_2);
        watchList2.setName(SECOND_WATCH_LIST_NAME);

        List<WatchListEntity> watchLists =
                List.of(watchList1, watchList2);

        WatchlistListResponseDto result =
                mapper.toListDto(watchLists);

        assertEquals(
                TWO,
                result.getWatchlists().size()
        );

        assertEquals(
                ITEM_ID_1,
                result.getWatchlists()
                        .get(FIRST_INDEX)
                        .getId()
        );

        assertEquals(
                WATCH_LIST_NAME,
                result.getWatchlists()
                        .get(FIRST_INDEX)
                        .getName()
        );

        assertEquals(
                ITEM_ID_2,
                result.getWatchlists()
                        .get(SECOND_INDEX)
                        .getId()
        );

        assertEquals(
                SECOND_WATCH_LIST_NAME,
                result.getWatchlists()
                        .get(SECOND_INDEX)
                        .getName()
        );
    }
}