from themoviedb import TMDb

class TMDbHelper:
    def __init__(self, api_key, language="en-US", region="US"):
        self.tmdb = TMDb(key=api_key, language=language, region=region)

    def get_genre_map(self):
        return {
            "Action": 28,
            "Adventure": 12,
            "Animation": 16,
            "Comedy": 35,
            "Crime": 80,
            "Documentary": 99,
            "Drama": 18,
            "Family": 10751,
            "Fantasy": 14,
            "History": 36,
            "Horror": 27,
            "Music": 10402,
            "Mystery": 9648,
            "Romance": 10749,
            "Science Fiction": 878,
            "TV Movie": 10770,
            "Thriller": 53,
            "War": 10752,
            "Western": 37
        }

    def get_instance(self):
        return self.tmdb