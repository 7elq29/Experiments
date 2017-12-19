import requests

def request(url, mock=False):
    return _request(url) if not mock else _mock_request(url)

def _request(url):
    response = requests.get(url)
    if response.ok:
        return response.content
    else:
        return "Cannot find requested page."

def _mock_request(url):
    return "mock content" if url else "Cannot find requested page."
