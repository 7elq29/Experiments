import random
import requests
import json

URL = "http://127.0.0.1:5000"


def test():
    url = generate_url()
    response = requests.post(URL+"/"+url)
    if not response.ok:
        print("Cannot connect to server")
    else:
        r = json.loads(response.content.decode('utf-8'))
        if r['status'] == 'fail':
            print('Fail to get shortened url')
        else:
            shortened_url = r['url']
            print(URL + "/" + shortened_url)
            r = requests.get(URL+"/"+shortened_url)
            if r.ok:
                print(r.content)
            else:
                print(r.status_code)


def generate_url(size = 30):
    s = "abcdefghijklomopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_"
    rs = ""
    for _ in range(size):
        rs += random.choice(s)
    return rs

if __name__ == '__main__':
    test()