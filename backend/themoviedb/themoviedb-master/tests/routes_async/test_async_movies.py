# -*- coding: utf-8 -*-
from unittest.mock import patch

import pytest

from themoviedb import aiotmdb, schemas


@pytest.mark.asyncio
async def test_movie_details(get_data, assert_data):
    data = get_data("movies/details")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        movie = await aiotmdb.aioTMDb().movie(movie_id).details()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "include_image_language": "null",
            },
        )

    assert isinstance(movie, schemas.Movie)
    assert assert_data(movie, data)


@pytest.mark.asyncio
async def test_movie_details_full(get_data, assert_data):
    data = get_data("movies/details_full")
    data.pop("changes")  # TODO
    data.pop("lists")  # TODO
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        movie = (
            await aiotmdb.aioTMDb()
            .movie(movie_id)
            .details(
                append_to_response="alternative_titles,changes,credits,external_ids,images,keywords,lists,recommendations,release_dates,reviews,similar,translations,videos,watch/providers"
            )
        )
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "include_image_language": "null",
                "append_to_response": "alternative_titles,changes,credits,external_ids,images,keywords,lists,recommendations,release_dates,reviews,similar,translations,videos,watch/providers",  # noqa: E501
            },
        )

    assert isinstance(movie, schemas.Movie)
    assert assert_data(movie, data)


@pytest.mark.asyncio
async def test_movie_alternative_titles(get_data, assert_data):
    data = get_data("movies/alternative_titles")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        alternative_titles = await aiotmdb.aioTMDb().movie(movie_id).alternative_titles()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/alternative_titles",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(alternative_titles, schemas.AlternativeTitles)
    assert assert_data(alternative_titles, data)


@pytest.mark.asyncio
async def test_movie_credits(get_data, assert_data):
    data = get_data("movies/credits")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        credits_ = await aiotmdb.aioTMDb().movie(movie_id).credits()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/credits",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(credits_, schemas.Credits)
    assert assert_data(credits_, data)


@pytest.mark.asyncio
async def test_movie_external_ids(get_data, assert_data):
    data = get_data("movies/external_ids")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        external_ids = await aiotmdb.aioTMDb().movie(movie_id).external_ids()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/external_ids",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(external_ids, schemas.ExternalIDs)
    assert assert_data(external_ids, data)


@pytest.mark.asyncio
async def test_movie_images(get_data, assert_data):
    data = get_data("movies/images")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        images = await aiotmdb.aioTMDb().movie(movie_id).images()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/images",
            params={
                "api_key": "TEST_TMDB_KEY",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(images, schemas.Images)
    assert assert_data(images, data)


@pytest.mark.asyncio
async def test_movie_keywords(get_data, assert_data):
    data = get_data("movies/keywords")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        keywords = await aiotmdb.aioTMDb().movie(movie_id).keywords()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/keywords",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(keywords, schemas.Keywords)
    assert assert_data(keywords, data)


@pytest.mark.asyncio
async def test_movie_recommendations(get_data, assert_data):
    data = get_data("movies/recommendations")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        recommendations = await aiotmdb.aioTMDb().movie(movie_id).recommendations()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/recommendations",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "page": 1,
            },
        )

    assert isinstance(recommendations, schemas.Movies)
    assert assert_data(recommendations, data)


@pytest.mark.asyncio
async def test_movie_similar(get_data, assert_data):
    data = get_data("movies/similar")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        similar = await aiotmdb.aioTMDb().movie(movie_id).similar()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/similar",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "page": 1,
            },
        )

    assert isinstance(similar, schemas.Movies)
    assert assert_data(similar, data)


@pytest.mark.asyncio
async def test_movie_lists(get_data, assert_data):
    data = get_data("movies/lists")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        lists = await aiotmdb.aioTMDb().movie(movie_id).lists()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/lists",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "page": 1,
            },
        )

    assert isinstance(lists, schemas.ItemsList)
    assert assert_data(lists, data)


@pytest.mark.asyncio
async def test_movie_release_dates(get_data, assert_data):
    data = get_data("movies/release_dates")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        release_dates = await aiotmdb.aioTMDb().movie(movie_id).release_dates()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/release_dates",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(release_dates, schemas.ReleaseDates)
    assert assert_data(release_dates, data)


@pytest.mark.asyncio
async def test_movie_reviews(get_data, assert_data):
    data = get_data("movies/reviews")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        reviews = await aiotmdb.aioTMDb().movie(movie_id).reviews()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/reviews",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "page": 1,
            },
        )

    assert isinstance(reviews, schemas.Reviews)
    assert assert_data(reviews, data)


@pytest.mark.asyncio
async def test_movie_translations(get_data, assert_data):
    data = get_data("movies/translations")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        translations = await aiotmdb.aioTMDb().movie(movie_id).translations()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/translations",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(translations, schemas.Translations)
    assert assert_data(translations, data)


@pytest.mark.asyncio
async def test_movie_videos(get_data, assert_data):
    data = get_data("movies/videos")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        videos = await aiotmdb.aioTMDb().movie(movie_id).videos()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/videos",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "page": 1,
            },
        )

    assert isinstance(videos, schemas.Videos)
    assert assert_data(videos, data)


@pytest.mark.asyncio
async def test_movie_watch_providers(get_data, assert_data):
    data = get_data("movies/watch_providers")
    movie_id = 123

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        watch_providers = await aiotmdb.aioTMDb().movie(movie_id).watch_providers()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/movie/{movie_id}/watch/providers",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(watch_providers, schemas.WatchProviders)
    assert assert_data(watch_providers, data)


@pytest.mark.asyncio
async def test_movies_latest(get_data, assert_data):
    data = get_data("movies/latest")

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        movie = await aiotmdb.aioTMDb().movies().latest()
        mocked.assert_called_with(
            "GET",
            "https://api.themoviedb.org/3/movie/latest",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(movie, schemas.Movie)
    assert assert_data(movie, data)


@pytest.mark.asyncio
async def test_movies_now_playing(get_data, assert_data):
    data = get_data("movies/now_playing")

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        movies = await aiotmdb.aioTMDb().movies().now_playing()
        mocked.assert_called_with(
            "GET",
            "https://api.themoviedb.org/3/movie/now_playing",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "page": 1,
            },
        )

    assert isinstance(movies, schemas.Movies)
    assert assert_data(movies, data)


@pytest.mark.asyncio
async def test_movies_popular(get_data, assert_data):
    data = get_data("movies/popular")

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        movies = await aiotmdb.aioTMDb().movies().popular()
        mocked.assert_called_with(
            "GET",
            "https://api.themoviedb.org/3/movie/popular",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "page": 1,
            },
        )

    assert isinstance(movies, schemas.Movies)
    assert assert_data(movies, data)


@pytest.mark.asyncio
async def test_movies_top_rated(get_data, assert_data):
    data = get_data("movies/top_rated")

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        movies = await aiotmdb.aioTMDb().movies().top_rated()
        mocked.assert_called_with(
            "GET",
            "https://api.themoviedb.org/3/movie/top_rated",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "page": 1,
            },
        )

    assert isinstance(movies, schemas.Movies)
    assert assert_data(movies, data)


@pytest.mark.asyncio
async def test_movies_upcoming(get_data, assert_data):
    data = get_data("movies/upcoming")

    with patch("themoviedb.routes_async._base.ClientSession.request") as mocked:
        mocked.return_value.__aenter__.return_value.json.return_value = data
        movies = await aiotmdb.aioTMDb().movies().upcoming()
        mocked.assert_called_with(
            "GET",
            "https://api.themoviedb.org/3/movie/upcoming",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "page": 1,
            },
        )

    assert isinstance(movies, schemas.Movies)
    assert assert_data(movies, data)
