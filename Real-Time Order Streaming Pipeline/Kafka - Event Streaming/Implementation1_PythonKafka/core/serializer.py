import json

def string_serializer(_input):
    if isinstance(_input, str):
        return _input.encode("utf-8")
    return _input

def json_serializer(_input):
    if _input is None:
        return None
    return json.dumps(_input).encode("utf-8")