import json, os

def loadjson(filename):
    if os.path.isfile(filename):
        with open(filename, 'r') as fin:
            return json.loads(fin.read())
    return dict()

def fix(value):
    if value.isdigit():
        return int(value)
    if value.replace('.', '', 1).isdigit():
        return float(value)
    return value

def arrange(dictionary, keys = [ ], array = [ ]):
    if 'value' in dictionary.keys():
        array.append((dictionary['value'], keys))
        return array
    for key in [ key for key in dictionary.keys() if key != 'name' ]:
        arrange(dictionary[key], keys + [ (key, dictionary[key]['name']) ], array)
    return array

def get(variable, non_variable, result):
    for nv in non_variable:
        if nv not in result[1]:
            return None, None, False
    for value, key in result[1]:
        if key == variable:
            return result[0], value, True
    return None, None, False

def build(array, variable, non_variable):
    pairs = list()
    dictionary = { key : fix(value) for value, key in non_variable }
    for result in array:
        score, value, contains = get(variable, non_variable, result)
        if contains:
            pairs.append((score, fix(value)))
    pairs.sort(key = lambda pair : pair[1])
    dictionary['scores'] = [ pair[0] for pair in pairs ]
    dictionary[variable] = [ pair[1] for pair in pairs ]
    return dictionary


def savejson(dictionary, filename):
    with open(filename, 'w') as fout:
        fout.write(json.dumps(dictionary))

uniform = loadjson('uniform.json')
gaussian = loadjson('gaussian.json')
info = { 'uniform': { **uniform, 'name': 'distribution' }, 'gaussian': { **gaussian, 'name': 'distribution' } }
savejson(info, 'results.json')
array = arrange(info)
array.sort(reverse = True, key = lambda element: element[0])
savejson(array, 'results-arraged.json')

uniform = [ stuff for stuff in array if ('uniform', 'distribution') in stuff[1] ]
gaussian = [ stuff for stuff in array if ('gaussian', 'distribution') in stuff[1] ]

uniform.sort(reverse = True, key = lambda element: element[0])
gaussian.sort(reverse = True, key = lambda element: element[0])

highest = array[0]

u_dictionary = { key : build(uniform, key, [ (v, k) if k != 'distribution' else ('uniform', 'distribution') for v, k in highest[1] if k != key ]) for key in [ key for value, key in highest[1] if key != 'distribution' ] }
g_dictionary = { key : build(gaussian, key, [ (v, k) if k != 'distribution' else ('gaussian', 'distribution') for v, k in highest[1] if k != key ]) for key in [ key for value, key in highest[1] if key != 'distribution' ] }

import pprint

savejson(u_dictionary, 'to-plot-uniform.json')
savejson(g_dictionary, 'to-plot-gaussian.json')