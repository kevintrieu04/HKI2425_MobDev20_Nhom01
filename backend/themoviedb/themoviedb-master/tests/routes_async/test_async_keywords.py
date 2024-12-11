# -*- coding: utf-8 -*-
from unittest.mock import patch

import pytest

from themoviedb import aiotmdb, schemas


@pytest.mark.asyncio
async def test_keyword_details(get_data, assert_data):
    data = get_data("keywords/details")
    keyword_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        keyword = await aiotmdb.aioTMDb().keyword(keyword_id).details()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/keyword/{keyword_id}",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(keyword, schemas.Keyword)
    assert assert_data(keyword, data)


@pytest.mark.asyncio
async def test_keyword_movies(get_data, assert_data):
    data = get_data("keywords/movies")
    keyword_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        movies = await aiotmdb.aioTMDb().keyword(keyword_id).movies()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/keyword/{keyword_id}/movies",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "page": 1,
                "include_adult": "false",
            },
        )

    assert isinstance(movies, schemas.Movies)
    assert assert_data(movies, data)
