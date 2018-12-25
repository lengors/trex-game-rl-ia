import os, sys, re, subprocess

def delete(path):
    if os.path.isdir(path):
        for obj in os.listdir(path):
            delete(os.path.join(path, obj))
        os.rmdir(path)
    elif os.path.isfile(path):
        os.remove(path)

def find(path, pattern):
    try:
        if os.path.isdir(path):
            files = [ ]
            for obj in os.listdir(path):
                files += find(os.path.join(path, obj), pattern)
            return files
        elif os.path.isfile(path) and re.match(pattern, os.path.basename(path)) is not None:
            return [ path ]
        return [ ]
    except PermissionError:
        return [ ]

if sys.platform == 'win32':
    if not any([ 'jdk1.8' in os.path.normpath(path) for path in os.environ['PATH'].split(os.pathsep) ]):
        for path in find('C:\\Program Files\\Java', 'javac.exe'):
            if 'jdk1.8' in os.path.normpath(path):
                os.environ['PATH'] += os.pathsep + os.path.dirname(path)

if len(sys.argv) >= 2:
    if sys.argv[1] == '--compile' and len(sys.argv) == 2:
        delete('bin')
        os.mkdir('bin')
        dependencies = os.pathsep.join(find(os.path.join('res', 'dependencies'), '.+\.jar'))
        call = [ 'javac', '-cp', dependencies, '-d', 'bin' ] + find('src', '.+\.java')
        subprocess.call(call, shell = sys.platform == 'win32')
    elif sys.argv[1] == '--run' and len(sys.argv) == 3:
        dependencies = os.pathsep.join(find(os.path.join('res', 'dependencies'), '.+\.jar'))
        dependencies = 'bin{}{}'.format(os.pathsep, dependencies)
        subprocess.call([ 'java', '-cp', dependencies, sys.argv[2] ])
    else:
        print('Usage: python[3] java.py [-run classname | -compile]')
else:
    print('Usage: python[3] java.py [-run classname | -compile]')
