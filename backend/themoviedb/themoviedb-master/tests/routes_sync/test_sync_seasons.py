# -*- coding: utf-8 -*-
from unittest.mock import patch

import pytest

from themoviedb import schemas, tmdb


def test_season_details(get_data, assert_data):
    data = get_data("tv_seasons/details")
    tv_id = 123
    season_id = 456

    with patch("themoviedb.routes_sync._base.Session.request") as mocked:
        mocked.return_value.__enter__.return_value.json.return_value = data
        season = tmdb.TMDb().season(tv_id, season_id).details()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/tv/{tv_id}/season/{season_id}",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(season, schemas.Season)
    assert assert_data(season, data)


@pytest.mark.skip
def test_season_details_full(get_data, assert_data):
    data = get_data("tv_seasons/details_full")
    tv_id = 123
    season_id = 456

    with patch("themoviedb.routes_sync._base.Session.request") as mocked:
        mocked.return_value.__enter__.return_value.json.return_value = data
        season = (
            tmdb.TMDb()
            .season(tv_id, season_id)
            .details(
                append_to_response="alternative_titles,changes,credits,external_ids,images,keywords,lists,recommendations,release_dates,reviews,similar,translations,videos,watch/providers"
            )
        )
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/tv/{tv_id}/season/{season_id}",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
                "append_to_response": "alternative_titles,changes,credits,external_ids,images,keywords,lists,recommendations,release_dates,reviews,similar,translations,videos,watch/providers",  # noqa: E501
            },
        )

    assert isinstance(season, schemas.Season)
    assert assert_data(season, data)


def test_season_aggregate_credits(get_data, assert_data):
    data = get_data("tv_seasons/aggregate_credits")
    tv_id = 123
    season_id = 456

    with patch("themoviedb.routes_sync._base.Session.request") as mocked:
        mocked.return_value.__enter__.return_value.json.return_value = data
        aggregate_credits = tmdb.TMDb().season(tv_id, season_id).aggregate_credits()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/tv/{tv_id}/season/{season_id}/aggregate_credits",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(aggregate_credits, schemas.Credits)
    assert assert_data(aggregate_credits, data)


def test_season_credits(get_data, assert_data):
    data = get_data("tv_seasons/credits")
    tv_id = 123
    season_id = 456

    with patch("themoviedb.routes_sync._base.Session.request") as mocked:
        mocked.return_value.__enter__.return_value.json.return_value = data
        credits_ = tmdb.TMDb().season(tv_id, season_id).credits()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/tv/{tv_id}/season/{season_id}/credits",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(credits_, schemas.Credits)
    assert assert_data(credits_, data)


def test_season_external_ids(get_data, assert_data):
    data = get_data("tv_seasons/external_ids")
    tv_id = 123
    season_id = 456

    with patch("themoviedb.routes_sync._base.Session.request") as mocked:
        mocked.return_value.__enter__.return_value.json.return_value = data
        external_ids = tmdb.TMDb().season(tv_id, season_id).external_ids()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/tv/{tv_id}/season/{season_id}/external_ids",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(external_ids, schemas.ExternalIDs)
    assert assert_data(external_ids, data)


def test_season_images(get_data, assert_data):
    data = get_data("tv_seasons/images")
    tv_id = 123
    season_id = 456

    with patch("themoviedb.routes_sync._base.Session.request") as mocked:
        mocked.return_value.__enter__.return_value.json.return_value = data
        images = tmdb.TMDb().season(tv_id, season_id).images()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/tv/{tv_id}/season/{season_id}/images",
            params={
                "api_key": "TEST_TMDB_KEY",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(images, schemas.Images)
    assert assert_data(images, data)


def test_season_translations(get_data, assert_data):
    data = get_data("tv_seasons/translations")
    tv_id = 123
    season_id = 456

    with patch("themoviedb.routes_sync._base.Session.request") as mocked:
        mocked.return_value.__enter__.return_value.json.return_value = data
        translations = tmdb.TMDb().season(tv_id, season_id).translations()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/tv/{tv_id}/season/{season_id}/translations",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(translations, schemas.Translations)
    assert assert_data(translations, data)


def test_season_videos(get_data, assert_data):
    data = get_data("tv_seasons/videos")
    tv_id = 123
    season_id = 456

    with patch("themoviedb.routes_sync._base.Session.request") as mocked:
        mocked.return_value.__enter__.return_value.json.return_value = data
        videos = tmdb.TMDb().season(tv_id, season_id).videos()
        mocked.assert_called_with(
            "GET",
            f"https://api.themoviedb.org/3/tv/{tv_id}/season/{season_id}/videos",
            params={
                "api_key": "TEST_TMDB_KEY",
                "language": "TEST_TMDB_LANGUAGE",
                "region": "TEST_TMDB_REGION",
                "watch_region": "TEST_TMDB_REGION",
            },
        )

    assert isinstance(videos, schemas.Videos)
    assert assert_data(videos, data)
