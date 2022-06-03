# itsaren sample project

You can start here if you're building from scratch.

Contains a demo HTTP server, Scala 3, cats-effect 3, http4s, but no tapir yet, circe, scalafmt 3.

## How to run:

```
sbt "itsaren-simple/run"
```

## How to use:

```
1. curl -i -XPOST -H"Content-Type:application/json" -d'{"address":"56th street","phone":"0712345678"}' localhost:8080/v1/arns

2. curl -i -H"Accept:application/json" localhost:8080/v1/arns/f0e30a8b-e4f2-41b1-9511-3fc1e80a02f7

3. curl -i -XPUT -H"Content-Type:application/json" -d'{"address":"12th street","phone":"0712345699"}' localhost:8080/v1/arns/f2dcacea-7ba2-4763-a79d-48f835ee5523

4. curl -i -XDELETE localhost:8080/v1/arns/f2dcacea-7ba2-4763-a79d-48f835ee5523

5. curl -i localhost:8080/v1/arns
```
