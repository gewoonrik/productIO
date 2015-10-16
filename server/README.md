# productio #

## Deployement

```sh
sbt clean assembly
```
To run the server, run `java -jar <assembly>.jar`

## Development: run locally ##

```sh
$ cd productio
$ ./sbt
> container:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.
