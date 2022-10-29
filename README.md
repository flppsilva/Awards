# Golden Raspberry Awards

### Requirements

* Java 17+

### Running the tests

#### Unix like (Linux, Mac)
```
./mvnw test
```

#### Windows
```
mvnw.cmd test
```

### Running the project

#### Unix like (Linux, Mac)
```
./mvnw spring-boot:run
```

#### Windows
```
mvnw.cmd spring-boot:run
```

### Getting the results
```
curl http://localhost:8080/reward-interval
```

#### Return example
```json
{"min":[{"producer":"Joel Silver","interval":1,"previousWin":1990,"followingWin":1991}],"max":[{"producer":"Matthew Vaughn","interval":13,"previousWin":2002,"followingWin":2015}]}
```