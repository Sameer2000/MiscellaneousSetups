package com.app.src;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Class TestDebug.
 */
public class TestLock
{

   /**
    * The main method.
    *
    * @param args the arguments
    */
   public static void main( String[] args )
   {

      MyRunnable runnable = new MyRunnable();
      new Thread( runnable, "Thread 1" ).start();
      new Thread( runnable, "Thread 2" ).start();
   }

}

class MyRunnable implements Runnable
{

   Lock lock;

   int i = 1;

   public MyRunnable()
   {
      this.lock = new ReentrantLock();
   }

   @Override
   public void run()
   {
//      doSomething();
      checkComm();
   }

   Object o = new Object();
   public void checkComm()
   {
      synchronized ( o )
      {
         i++;
         System.out.println( "current value of "+Thread.currentThread().getName()+ " : "  +i );
      }
   }

   public void doSomething()
   {
      System.out.println( Thread.currentThread().getName() + " is waiting for lock. " );
      lock.lock();

      System.out.println( Thread.currentThread().getName() + " is acquired the lock" );

      try
      {
         System.out.println( Thread.currentThread().getName() + " is sleeping" );
         Thread.sleep( 3000 );
      }
      catch ( Exception e )
      {
         e.printStackTrace();
      }

      System.out.println( Thread.currentThread().getName() + " is releasing lock" );

      lock.unlock();

   }

   //   
   //   
   //   Thread 2 is waiting for lock. 
   //   Thread 1 is waiting for lock. 
   //   Thread 2 is acquired the lock
   //   Thread 2 is sleeping
   //   Thread 1 is acquired the lock
   //   Thread 1 is sleeping
   //   Thread 2 is releasing lock
   //   Thread 1 is releasing lock

}