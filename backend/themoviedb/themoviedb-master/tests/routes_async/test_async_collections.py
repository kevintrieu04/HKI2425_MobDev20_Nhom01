# -*- coding: utf-8 -*-
from unittest.mock import patch

import pytest

from themoviedb import aiotmdb, schemas


@pytest.mark.asyncio
async def test_collection_details(get_data, assert_data):
    data = get_data("collections/details")
    collection_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        collection = await aiotmdb.aioTMDb().collection(collection_id).details()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/collection/{collection_id}",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(collection, schemas.Collection)
    assert assert_data(collection, data)


@pytest.mark.asyncio
async def test_collection_images(get_data, assert_data):
    data = get_data("collections/images")
    collection_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        images = await aiotmdb.aioTMDb().collection(collection_id).images()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/collection/{collection_id}/images",
            params={
                "api_key": "TEST_TMDB_KEY",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(images, schemas.Images)
    assert assert_data(images, data)


@pytest.mark.asyncio
async def test_collection_translations(get_data, assert_data):
    data = get_data("collections/translations")
    collection_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        translations = await aiotmdb.aioTMDb().collection(collection_id).translations()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/collection/{collection_id}/translations",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(translations, schemas.Translations)
    assert assert_data(translations, data)
