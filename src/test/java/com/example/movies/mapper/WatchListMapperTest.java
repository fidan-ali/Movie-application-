package com.example.movies.mapper;

import com.example.movies.dao.entity.WatchListEntity;
import com.example.movies.dto.WatchListRequestDto;
import com.example.movies.dto.WatchListResponseDto;
import com.example.movies.dto.WatchlistListResponseDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static com.example.movies.constant.MovieApiTestConstants.FIRST_INDEX;
import static com.example.movies.constant.MovieApiTestConstants.ID;
import static com.example.movies.constant.MovieApiTestConstants.ITEM_ID_1;
import static com.example.movies.constant.MovieApiTestConstants.ITEM_ID_2;
import static com.example.movies.constant.MovieApiTestConstants.SECOND_INDEX;
import static com.example.movies.constant.MovieApiTestConstants.SECOND_WATCH_LIST_NAME;
import static com.example.movies.constant.MovieApiTestConstants.TWO;
import static com.example.movies.constant.MovieApiTestConstants.WATCH_LIST_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WatchListMapperTest {

    private final WatchListMapper mapper =
            Mappers.getMapper(WatchListMapper.class);

    @Test
    void shouldMapRequestDtoToEntity() {

        WatchListRequestDto request =
                new WatchListRequestDto(WATCH_LIST_NAME);

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
                result.id()
        );

        assertEquals(
                WATCH_LIST_NAME,
                result.name()
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
                result.get(FIRST_INDEX).id()
        );

        assertEquals(
                WATCH_LIST_NAME,
                result.get(FIRST_INDEX).name()
        );

        assertEquals(
                ITEM_ID_2,
                result.get(SECOND_INDEX).id()
        );

        assertEquals(
                SECOND_WATCH_LIST_NAME,
                result.get(SECOND_INDEX).name()
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
                result.watchlists().size()
        );

        assertEquals(
                ITEM_ID_1,
                result.watchlists()
                        .get(FIRST_INDEX)
                        .id()
        );

        assertEquals(
                WATCH_LIST_NAME,
                result.watchlists()
                        .get(FIRST_INDEX)
                        .name()
        );

        assertEquals(
                ITEM_ID_2,
                result.watchlists()
                        .get(SECOND_INDEX)
                        .id()
        );

        assertEquals(
                SECOND_WATCH_LIST_NAME,
                result.watchlists()
                        .get(SECOND_INDEX)
                        .name()
        );
    }
}