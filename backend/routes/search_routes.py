from flask import Blueprint, jsonify, request
from config import TMDB_API_KEY
from utils.tmdb_helper import TMDbHelper


search_bp = Blueprint('search', __name__, url_prefix='/search')

tmdb_helper = TMDbHelper(TMDB_API_KEY)
tmdb = tmdb_helper.get_instance()
genre_map = tmdb_helper.get_genre_map()

@search_bp.route('/', methods=['GET'])
def search_movies():
    query = request.args.get('query', '')
    category = request.args.get('category', None)
    year = request.args.get('year', None, type=int)
    country = request.args.get('country', None)
    page = request.args.get('page', 1, type=int)

    try:
        genre_id = genre_map.get(category, None)

        if query:
            search_results = tmdb.search().movies(query=query, page=page)
        else:
            search_params = {"page": page}
            if genre_id:
                search_params["with_genres"] = genre_id
            if year:
                search_params["primary_release_year"] = year
            if country:
                search_params["watch_region"] = country

            search_results = tmdb.discover().movie(**search_params)

        results = []
        for movie in search_results:
            release_date = (
                movie.release_date.strftime("%d/%m/%Y")
                if hasattr(movie, 'release_date') and movie.release_date
                else "Unknown"
            )
            vote_avg = movie.vote_average if hasattr(movie, 'vote_average') else "0"

            results.append({
                "id": str(movie.id),
                "title": movie.title or "Unknown Title",
                "img_src": f"https://image.tmdb.org/t/p/w500{movie.poster_path or ''}",
                "duration": f"{movie.runtime} min" if hasattr(movie, 'runtime') and movie.runtime else "Unknown duration",
                "rating": str(vote_avg),
                "release_date": release_date,
                "synopsis": movie.overview or "No synopsis available.",
            })

        return jsonify(results)
    except Exception as e:
        return jsonify({"error": str(e)}), 500