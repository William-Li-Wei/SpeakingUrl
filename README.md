# SpeakingUrl

#### What is SpeakingUrl
SpeakingUrl is a set of server side RESTful APIs, to translate the parameters-based url into speaking url, or the other way around.
It reads a mapping table and looks up for the target url there.

e.g.
 - `/products?gender=female&tag=2` would be translated into `/Women/Boots/Michi/`
 - `/Women/Boots/Michi/` would be translated into `/products?gender=female&tag=2`
  
  
#### How to get started
It's build with Maven, to install Maven, see [https://maven.apache.org/][1]

Download the repo and cd into the project folder, then run 
```
mvn spring-boot:run
```
It would download all the dependencies, compile, test and then start the server. Or run
```
mvn clean package
```
It would build a jar file for you in the target folder and then run the JAR by typing
```
java -jar /PATH_TO_THE_PROJECT/target/speaking-url-0.1.0.jar
```

Now open your browser and visit [http://localhost:8080/products][2]


#### Depenceis
 - ***spring-boot-starter-web***, to build a RESTful web service with Spring.
 - ***commons-collections***, to make use of `BidiMap`, a map that allows bidirectional lookup between key and values.
 - ***junit***, for Unit Test.
 - ***spring-test***, to moc a `HttpServletRequest` during the test.

#### Hightlights
##### 1. Use of Singleton for the mapping table
The mapping table itself is stored in a database, but the lookup should not request the database because of performance reasons.

The instance of the Singleton `mappingTable` would be instantiated with the moc data, and be ready for the lookup.
The Singleton is impleted with `enum`, to make sure there's one and only one instance in the app.

See `src/main/java/app/MappingTable.java` for more details.

##### 2. Use of BidiMap
`BidiMap` is a map that allows bidirectional lookup between key and values.

Looking up for `value` based on `key` is easy and fast in a normal `HashMap`, just use `hashmap.get(key)`, but `HashMap` doesn't allow the inverse lookup.
You can use `Hashtable` for inverse look somehow, but the performance is way too bad. Here we use `BidiMap` and ***trade in space for time***.

We put key values pairs into two maps `paramToUrl` and `urlToParam`. See the following example of bidiMap `paramToUrl`:

|from|to
|--- |:--- 
|gender=female |Women
|gender=male |Mem
|tag=1 |Shoes
|tag=2 |Boots
|tag=11 |Animal-Print
|tag=12 |Batik
|tag=21 |Adidas
|tag=22 |Nike
|... |...

and `urlToParam` is the inversed BidiMap of `paramToUrl`.


##### 3. Improve the performance further
By maintaining a `TopHitUrl`, we put a requesting url and it's speaking-url into a Hashtable. Whenever a new querest is comming in, we lookup the `TopHitUrl` for the translation, and return the result if we got a hit. Unfortunately, if we failed to find a hit, then go for the big mapping table instead, and put this url into the top hit table.

The size of the top hit Hashtable is limited to 500 key-value pairs, to make sure the urls in it are all recently requested.

Here we use `Hashtable` instead of `HashMap` because Hashtable is thread safe and syncronized.

#### Some math around the performance
In our case, we have 2 genders, 10 categories, 10 patterns and 1000 brands. These numbers could make at most 2 * 10 * 10 * 1000 = 200.000 combinations, containing all 4 kind of parameters.
We assume every combination has the same posibility bo be requested.

If we simply put all the combinations into the mapping table, there would be 200.000 key-value pairs in it.
And the lookup would take 100.000 iterations to find the hit in everage.

Now we break the combinations down to 1022 (2 + 10 + 10 + 1000) single paramter/speaking-url-fragment. And the table end up with 1022 records.
In this case, a 4-parameters url request would take only 2/2 + 10/2 + 10/2 + 1000/2 = 511 iterations to find a hit in everage. Much faster, right?

Of course we could find a lot more approaches to improve the performance. We'll implement them in the future.

#### What's next
 - Store the mapping in the DataBase and load it up when the Singleton is instantiated.
 - BidiMap is not thread safe too, but in our case, the content of the mapping table is static. So it's safe to use at the moment. But we do need to make it thread safe in the futre when we want the code to update the mapping tables in a multi-thread environment.
 - Calculate how many times each url is requested, and sort them by that number. Make sure the the most popular url could be found faster.
 - Break down the mapping table into several sub-tables, and use diffent tables for the lookup of a certen category.
 - Make better rules to the url, e.g. `/Women/Shoes/Boots/Michi/Red/...` always keep gender in the most front, then category, brands ... and so on. Then we could teach the api to use the correct mapping table for the lookup.
 
#### Contact
Please contact at liweisadleader@hotmail.com for more descussion.
 
 
  [1]: https://maven.apache.org/
  [2]: http://localhost:8080/products
