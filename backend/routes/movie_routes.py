from flask import Blueprint, jsonify
from config import TMDB_API_KEY
from utils.tmdb_helper import TMDbHelper


movie_bp = Blueprint('movie', __name__, url_prefix='/movie')

tmdb = TMDbHelper(TMDB_API_KEY).get_instance()

@movie_bp.route('/', methods=['GET'])
def get_movies():
    try:
        movies = tmdb.movies().popular()
        results = []
        for movie in movies:
            details = tmdb.movie(movie.id).details()
            duration = details.runtime if details.runtime else "Unknown duration"
            genres = details.genres[0].name if details.genres and len(details.genres) > 0 else "Unknown genre"
            results.append({
                "id": str(movie.id),
                "title": movie.title or "Unknown Title",
                "img_src": f"https://image.tmdb.org/t/p/w500{movie.poster_path or ''}",
                "type": genres,
                "duration": f"{duration} min" if isinstance(duration, int) else duration,
                "rating": str(movie.vote_average or "0"),
                "synopsis": movie.overview or "No synopsis available.",
                "isPlaying": True
            })
        return jsonify(results)
    except Exception as e:
        return jsonify({"error": str(e)}), 500