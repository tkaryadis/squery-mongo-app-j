package javaapp;

import clojure.java.api.Clojure;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

//use the clojure wrapper in the dependency code(clojureapp),that creates a Java class,
import static cmql_app_clj.interop.Quickstart_api.*;

//our java wrapper over the clojure code in the maven project
import static javaapp.Queries.*;

//original java code
import static javaapp.quickstart.AggregationFramework.*;

/*
src
   clojure
     our clojure packages,namespaces
   java
     javaapp
       quickstart(the original java code from github)
       Main (example on how to import and call clojure code)
       Queries(java wrapper over clojure functions,the clojure code that is inside our maven project)


See the pom.xml

Its made with IntellijIdea,Cursive clojure plugin,and Maven as build tool.
Clojureapp project is made with IntellijIdea,Cursive clojure plugin,and leiningen as build tool.

(if never used clojure,you can install intellijIdea community edition and cursive plugin,that works both maven and
 leiningen,and later move to your enviroment see also https://www.dunebook.com/best-clojure-ide-and-editors/)

Cursive is easy to use,and works good with Java,has strict parenthensis edit,with ctrl+w we select code etc

 */

/*
Before starting

Save the data in the database
1)go to clojureapp (get the code from github)
2)cd data-test
3)$quick-mongo-atlas-datasets-master/dump$ mongorestore sample_training/.
  (install mongorestore if you dont have it https:docs.mongodb.com/database-tools/installation/installation-linux/)
  (sample data part of the mongod-atlas datasets from https:github.com/huynhsamha/quick-mongo-atlas-datasets)

Install all maven dependencies
cd cmql
lein install
cd cmql-js
lein install
cd clojureapp
lein install

Its 3 steps
1)write the clojure code(decide where,in seperate project or in maven project)
2)write a wrapper to call it (decide where to write the wrapper,in clojure code or in java code)
  wrapper is 2 lines of standard code/per wrapped function
3)call it as a normal static Java method

Write the clojure code,we can write the code in 2 places (1)
1)write the Clojure code in another project with other build tool like leiningen that is the most common one
  lein install
  use the maven local dependency that lein install created in a Maven Java app
  see pom.xml (for the dependency)
2)write the Clojure code inside a Maven application,at the clojure directory
  see pom.xml (to configure srcs directories etc)

Here we used both ways
1)clojure code in clojureapp,that we used as dependency (after we did lein install to it)
2)clojure code in maven project,we used it only as example  clojure/queries/connection has clojure code
  inside the maven project

Wrapping the Clojure code (2)
1)in Clojure code
  Clojure programmer makes a Java class from Clojure,and the java programmer calls them as regular Java methods
2)in Java code
  Java programmers makes a wrapper to use the Clojure methods like normal Java methos

Here we used both ways
1)javaapp/Queries have the wrapper implemented in Java,to call the clojure/queries/connection.
2)inside clojureapp.interop.quickstart_api

Calling the Clojure code (3)
import static javaapp.Queries.*;     we import our Java wrapper,that wraps the Maven clojure
import static clojureapp.interop.Quickstart_api.*;  this is to import our Clojure wrapper,that wraps the the clojureapp
in both cases we import static,and call as a normal Static method

*In the maven project we made the wrapper in Java,we could have made the wrapper in Clojure also inside the Maven project
 but that requires AOT compilation.

*/

public class Main
{
  public static void quickstartJavaOriginalCode()
  {
    //Here we run only the aggregation example
    //the full code is in javaapp.quickstart , all files have main also in the original code
    try (MongoClient mongoClient = MongoClients.create()) {
      MongoDatabase db = mongoClient.getDatabase("sample_training");
      MongoCollection<Document> zips = db.getCollection("zips");
      MongoCollection<Document> posts = db.getCollection("posts");
      threeMostPopulatedCitiesInTexas(zips);
      threeMostPopularTags(posts);
    }
  }

  public static void main(String[] args) throws Exception
  {

    //This maven project is only on how to call Clojure from Java
    //to test cmql,use the clojureapp as a leinengein project
    //you can also copy the code of clojureapp inside src/clojure in this project,and test it from here


    //------------------------Call Clojure that we have inside the Maven project----------------------------------------

    //we call the wrapper that we made in javaapp/queries
    //that calls the only Clojure query we have inside the Maven Project
    //wrapper is made so we are able to call it like a static java function
    connect();
    aggregateCommands();
    aggregateMethods();  //does the same like aggregateCommands using the driver methods,and cMQL arguments

    //-----------------------Call the original Java javaapp.quickstart--------------------------------------------------

    quickstartJavaOriginalCode();  //see above,we call original java code from javaapp/quickstart

    //------------------------Call code from the clojureapp dependency--------------------------------------------------

    //call the wrapper clojureapp.interop.Quickstart_api (from the clojureapp project)
    String fromClojure = helloClojureFromJava("Java-Programmer");
    System.out.println(fromClojure);

    runCommands();    //they change the dataset we load,to use them rightly see the code in the Clojureapp
    runMethods();     //they change the dataset we load,to use them rightly see the code in the Clojureapp

    //-----------------------Loading any namespace in the clojureapp dependency-----------------------------------------

    require.invoke(Clojure.read("cmql-app-clj.cmql.commands.read-write.t6aggregation"));
  }
}
