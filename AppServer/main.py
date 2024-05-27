import json
from flask import Flask, request, jsonify
import sqlite3
import requests
import uuid

app = Flask(__name__)

# Initialize SQLite database
conn = sqlite3.connect('appserver.db')
cursor = conn.cursor()

# Configure db
with open('./db/config.sql') as sqlf:
    cursor.executescript(sqlf.read())
conn.commit()


@app.route('/addcard', methods=['POST'])
def add_credit_card():
    # Get credit card data from request
    data = request.json

    # Check if all required fields are provided
    if 'pan' not in data or 'expire' not in data or 'cvv' not in data:
        return jsonify({'error': 'Missing required fields'}), 400

    # load config
    tsp_host = ''
    with open('./config.json') as config:
        tsp_host = json.load(config)['TSP_HOST']

    # Tokenize credit card data
    tsp_response = requests.post(f'{tsp_host}/tokenize', json=data)
    token = tsp_response.json()['token']

    # Add to database
    conn = sqlite3.connect('appserver.db')
    cursor = conn.cursor()
    cursor.execute('''INSERT INTO tokens (token, user_id)
                      VALUES (?, ?)''', (token, str(uuid.uuid4())))
    
    conn.commit()
    cursor.close()

    return jsonify({'token': token}), 200

@app.route('/getcard', methods=['POST'])
def get_card():
    data = request.json

    # Check if all required fields are provided
    if 'token' not in data:
        return jsonify({'error': 'Missing token field'}), 400

    # load config
    tsp_host = ''
    with open('./config.json') as config:
        tsp_host = json.load(config)['TSP_HOST']

    # Tokenize credit card data
    tsp_response = requests.get(f'{tsp_host}/get?token={data['token']}')
    data = tsp_response.json()

    return jsonify({'data': data}), 200


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050, debug=False)
