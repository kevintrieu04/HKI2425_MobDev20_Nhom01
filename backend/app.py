from flask import Flask
from config import TMDB_API_KEY
from routes.movie_routes import movie_bp
from routes.search_routes import search_bp
from routes.ads_routes import ads_bp

# Initialize Flask app
app = Flask(__name__)

app.register_blueprint(movie_bp)
app.register_blueprint(search_bp)
app.register_blueprint(ads_bp)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)