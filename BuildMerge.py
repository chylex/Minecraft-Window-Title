import subprocess
import platform
import sys
import os
import time
import shutil
import zipfile

root = os.path.abspath(sys.path[0])
builds = os.path.join(root, "Build")
combined = os.path.join(builds, "Combined")
outputjar = "CustomWindowTitle.jar"

gradlew = "gradlew.bat" if platform.system() == "Windows" else "gradlew"

def build(folder):
    separator = "========={}".format("=" * len(folder))
    
    print(separator)
    print("Building {}".format(folder))
    print(separator)
    
    libs = os.path.join(root, folder, "build", "libs")
    shutil.rmtree(libs)
    
    os.chdir(os.path.join(root, folder))
    subprocess.run([gradlew, "build"])
    
    for fname in os.listdir(libs):
        if "-dev" not in fname:
            global outputjar
            outputjar = fname
            
            jar = os.path.join(builds, "{}.{}".format(folder, fname))
            shutil.copyfile(os.path.join(libs, fname), jar)
            
            jar = zipfile.ZipFile(jar)
            jar.extractall(combined)
            jar.close()
            
            break

if os.path.isdir(builds):
    shutil.rmtree(builds)
    time.sleep(0.25)

os.mkdir(builds)
os.mkdir(combined)

# Forge must run last to overwrite MANIFEST.MF
build("Fabric")
build("Forge")

os.chdir(builds)

with zipfile.ZipFile(outputjar, "w", compression = zipfile.ZIP_DEFLATED, compresslevel = 9) as jar:
    for root, dirs, files in os.walk(combined):
        for fname in files:
            fname = os.path.join(root, fname)
            fname = os.path.normpath(fname)
            jar.write(fname, os.path.relpath(fname, combined))
