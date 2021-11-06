package javaapp;


import clojure.java.api.Clojure;
import clojure.lang.IFn;

//wrapper over clojure functions,to be called as java functions
public class Queries
{
  //Dont change this,its reusable code,add your function bellow
  //usefull in all java apps
  //reads a clojure function from a namespace and it returns it as IFn
  public static IFn require = Clojure.var("clojure.core", "require");
  public static IFn getClojureFn(String ns, String fnName)
  {
    require.invoke(Clojure.read(ns));
    IFn fn = Clojure.var(ns, fnName);
    return fn;
  }

  //Add the functions you want to wrap bellow (the above code will be re-used,dont change it)

  //Java wrapper for the 3 Clojure functions that we have in the Maven Project,in clojure/queries/queries.aggregate-framework
  //we could have more,but here we mostly use the Clojure as maven dependency,not in same project

  private static IFn connectFn= getClojureFn("queries.agreegate-framework","connect");
  public static void connect()
  {
    connectFn.invoke();
  }   //if it has arguments i put them in invoke

  private static IFn aggregateMethodsFn= getClojureFn("queries.agreegate-framework","aggregate-methods");
  public static void aggregateMethods()
  {
    aggregateMethodsFn.invoke();
  }

  private static IFn aggregateCommandsFn= getClojureFn("queries.agreegate-framework","aggregate-commands");
  public static void aggregateCommands()
  {
    aggregateCommandsFn.invoke();
  }
}