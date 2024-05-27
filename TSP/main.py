from flask import Flask, request, jsonify
import sqlite3
import uuid

app = Flask(__name__)

# Initialize SQLite database
conn = sqlite3.connect('tsp.db')
cursor = conn.cursor()

# Configure db
with open('./db/config.sql') as sqlf:
    cursor.executescript(sqlf.read())
conn.commit()


@app.route('/tokenize', methods=['POST'])
def tokenize_credit_card():
    # Get credit card data from request
    credit_card_data = request.json

    # Check if all required fields are present
    required_fields = ['pan', 'expire', 'cvv']
    for field in required_fields:
        if field not in credit_card_data:
            return jsonify({'error': f'Missing required field: {field}'}), 400

    # Generate a token for the credit card
    token = str(uuid.uuid4())

    # Store the tokenized credit card data in the database
    conn = sqlite3.connect('tsp.db')
    cursor = conn.cursor()
    cursor.execute('''INSERT INTO credit_cards (token, card_number, expiry_date, cvv)
                      VALUES (?, ?, ?, ?)''', (token, credit_card_data['pan'],
                                               credit_card_data['expire'], credit_card_data['cvv']))
    conn.commit()
    cursor.close()
    conn.close()

    return jsonify({'token': token}), 200


@app.route('/get', methods=['GET'])
def get_credit_card():
    # Get token from request
    token = request.args.get('token')

    # Check if token is provided
    if not token:
        return jsonify({'error': 'Token is missing in request'}), 400

    # Retrieve tokenized credit card data from database
    conn = sqlite3.connect('tsp.db')
    cursor = conn.cursor()
    cursor.execute('''SELECT * FROM credit_cards WHERE token = ?''', (token,))
    credit_card_data = cursor.fetchone()

    conn.commit()
    cursor.close()
    conn.close()

    # Check if token exists in database
    if not credit_card_data:
        return jsonify({'error': 'Invalid token'}), 400

    # Return tokenized credit card data
    return jsonify({'token': credit_card_data[0], 'card_number': credit_card_data[1],
                    'expiry_date': credit_card_data[2], 'cvv': credit_card_data[3]}), 200


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)
