from flask import Flask, jsonify
import json

app = Flask(__name__)

# Hàm tiện ích để đọc dữ liệu từ file JSON
def read_json(file_name):
    with open(file_name, 'r', encoding='utf-8') as file:
        return json.load(file)

# Route trả về danh sách phim (trong file JSON là danh sách các đối tượng)
@app.route('/movie', methods=['GET'])
def get_movies():
    data = read_json('movie.json')  # Đọc trực tiếp danh sách từ file
    return jsonify(data)  # Trả về toàn bộ danh sách

# Route trả về danh sách quảng cáo (hoặc dữ liệu tương tự)
@app.route('/ads', methods=['GET'])
def get_ads():
    data = read_json('ads.json')  # Đọc trực tiếp danh sách từ file
    return jsonify(data)  # Trả về toàn bộ danh sách

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
