"""Build this small unobfuscated 26.x mod against an existing Prism installation.

No Gradle, network, Fabric API or account access is needed. Uses the installed
Minecraft client and Mixin JARs, and Java 25's compiler. Does not install the mod.
"""
from pathlib import Path
import argparse, hashlib, json, subprocess, zipfile

ROOT = Path(__file__).resolve().parent

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('--prism-root', type=Path, default=Path.home()/'AppData/Roaming/PrismLauncher')
    ap.add_argument('--jdk', type=Path)
    ap.add_argument('--minecraft', default='26.1', help='Compile against this installed client (default: oldest supported release)')
    ap.add_argument('--client-jar', type=Path, help='Explicit official client JAR, for compatibility testing')
    args = ap.parse_args()
    jdk = args.jdk or args.prism_root/'java/java-runtime-epsilon'
    compiler = jdk/'bin'/('javac.exe' if (jdk/'bin/javac.exe').exists() else 'javac')
    client = args.client_jar or args.prism_root/f'libraries/com/mojang/minecraft/{args.minecraft}/minecraft-{args.minecraft}-client.jar'
    mixin = args.prism_root/'libraries/net/fabricmc/sponge-mixin/0.17.0+mixin.0.8.7/sponge-mixin-0.17.0+mixin.0.8.7.jar'
    for path in (compiler, client, mixin):
        if not path.is_file(): raise FileNotFoundError(path)
    import os
    deps = [client, mixin] + sorted((args.prism_root/'libraries').rglob('*.jar'))
    output = ROOT/'build/classes-1.1.0'
    output.mkdir(parents=True, exist_ok=True)
    sources = sorted((ROOT/'src/main/java').rglob('*.java'))
    # An argument file avoids Windows command-length limits and quotes spaces.
    opts = ['--release', '25', '-encoding', 'UTF-8', '-proc:none', '-classpath',
            os.pathsep.join(str(p) for p in deps), '-d', str(output)] + [str(p) for p in sources]
    argfile = ROOT/'build/javac.args'
    argfile.write_text('\n'.join('"'+a.replace('\\','/').replace('"','\\"')+'"' for a in opts), encoding='utf8')
    subprocess.run([str(compiler), '@'+str(argfile)], check=True)
    jar = ROOT/'build/studio-window-title-1.1.0+mc26.jar'
    entries = [(p, p.relative_to(output).as_posix()) for p in output.rglob('*.class')]
    resource_root = ROOT/'src/main/resources'
    entries += [(resource_root/name, name) for name in ('fabric.mod.json','studio-window-title.mixins.json')]
    entries += [(ROOT/'LICENSE', 'LICENSE')]
    with zipfile.ZipFile(jar, 'w', compression=zipfile.ZIP_DEFLATED) as z:
        for p, name in sorted(entries, key=lambda pair:pair[1]):
            info = zipfile.ZipInfo(name, (2026,10,8,0,0,0))
            info.compress_type = zipfile.ZIP_DEFLATED
            z.writestr(info,p.read_bytes())
    sha = hashlib.sha256(jar.read_bytes()).hexdigest()
    receipt = {'jar':jar.name,'sha256':sha,'minecraft_range':'>=26.1 <26.5','fabric_loader_min':'0.18.4',
               'compiled_against':args.minecraft,'client_sha1':hashlib.sha1(client.read_bytes()).hexdigest(),
               'java':25,'class_entries':[name for _,name in entries if name.endswith('.class')]}
    (ROOT/'build/build-receipt-1.1.0.json').write_text(json.dumps(receipt,indent=2)+'\n')
    print(json.dumps(receipt,indent=2))

if __name__ == '__main__': main()
