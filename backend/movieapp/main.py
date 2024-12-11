from flask import Flask, jsonify, request
import requests
import json

app = Flask(__name__)

# TMDB Authorization Token (Bearer Token)
BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzY2ZhZTI1YmZiYzRlYjQyM2JmM2MxNWIxNjg5ODJiOSIsIm5iZiI6MTczMzM1NTc2My4wOCwic3ViIjoiNjc1MGU4ZjM0NDg0N2U5N2RmZjQxYWM3Iiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.aj-iNUSRQu2gTi6uri5-uiL1eYJEI9p-f_DXC5iJs5g"
TMDB_BASE_URL = "https://api.themoviedb.org/3"

# TMDB Headers với Bearer Token
HEADERS = {
    "accept": "application/json",
    "Authorization": f"Bearer {BEARER_TOKEN}"
}


# đọc dữ liệu từ file JSON
def read_json(file_name):
    with open(file_name, 'r', encoding='utf-8') as file:
        return json.load(file)


# Route: Lấy danh sách phim phổ biến
@app.route('/movie', methods=['GET'])
def get_movies():
    url = f"{TMDB_BASE_URL}/movie/popular?language=en-US&page=1"
    response = requests.get(url, headers=HEADERS)

    if response.status_code == 200:
        data = response.json()
        results = []
        for movie in data['results']:
            results.append({
                "id": str(movie.get("id")),
                "title": movie.get("title", "Unknown Title"),
                "img_src": f"https://image.tmdb.org/t/p/w500{movie.get('poster_path', '')}",
                "type": "Unknown",
                "duration": "120 min",
                "rating": str(movie.get("vote_average", "0")),
                "synopsis": movie.get("overview", "No synopsis available."),
                "isPlaying": True
            })
        return jsonify(results)
    else:
        return jsonify({"error": "Failed to fetch data from TMDB"}), response.status_code


# Route: Tìm kiếm phim theo từ khóa
@app.route('/search', methods=['GET'])
def search_movies():
    query = request.args.get('query')  # Lấy từ khóa từ query parameter
    if not query:
        return jsonify({"error": "Query parameter is required"}), 400

    url = f"{TMDB_BASE_URL}/search/movie?language=en-US&query={query}"
    response = requests.get(url, headers=HEADERS)

    if response.status_code == 200:
        data = response.json()
        return jsonify(data['results'])
    else:
        return jsonify({"error": "Failed to fetch data from TMDB"}), response.status_code


# Route: Lấy danh sách quảng cáo từ file JSON
@app.route('/ads', methods=['GET'])
def get_ads():
    try:
        data = read_json('ads.json')
        return jsonify(data)
    except FileNotFoundError:
        return jsonify({"error": "ads.json file not found"}), 404
    except json.JSONDecodeError:
        return jsonify({"error": "Error decoding ads.json"}), 500


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
