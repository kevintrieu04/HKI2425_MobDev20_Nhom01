from flask import Blueprint, jsonify
import json

ads_bp = Blueprint('ads', __name__, url_prefix='/ads')

@ads_bp.route('/', methods=['GET'])
def get_ads():
    try:
        with open('ads.json', 'r', encoding='utf-8') as file:
            data = json.load(file)
        return jsonify(data)
    except FileNotFoundError:
        return jsonify({"error": "ads.json file not found"}), 404
    except json.JSONDecodeError:
        return jsonify({"error": "Error decoding ads.json"}), 500