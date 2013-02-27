# {{name}}

A website written with Clojure and ClojureScript.

## Usage

### Run tests
```bash
lein midje
```

### Start server in dev mode:
```bash
lein ring server &
```

### Start ClojureScript compiler to monitor changes to ClojureScript sources
```bash
lein lein cljsbuild auto &
```

### Start Browser-REPL
```bash
lein trampoline cljsbuild repl-listen
```
Make sure to (re-)load the ClojureScript with REPL-Support enabled (see https://github.com/emezeske/lein-cljsbuild/blob/master/doc/REPL.md).

## License

Copyright (C) {{year}} FIXME


