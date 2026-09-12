import requests

url = "https://jsonplaceholder.typicode.com/todos/1"

response = requests.get(url)

print("Status:", response.status_code)
print("Response:", response.json())