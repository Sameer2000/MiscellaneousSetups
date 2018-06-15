package com.app.src;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadOddEven
{

   public static void main( String[] args )
   {
      MyOERunnable runnable = new MyOERunnable();
      Thread t1 = new Thread( runnable, "odd" );
      Thread t2 = new Thread( runnable, "even" );
      t1.start();
      t2.start();
   }

}

class MyOERunnable implements Runnable
{

   Lock lock = new ReentrantLock();
   Condition condition = lock.newCondition();

   int number = 1;

   private boolean odd = true, even;

   @Override
   public void run()
   {
      print();
   }

   public void print()
   {
      try
      {
         while ( true )
         {
            if ( number == 10 )
            {
               break;
            }
            lock.lock();
            if ( Thread.currentThread().getName().equals( "odd" ) )
            {
               if ( !odd )
               {
                  condition.await();
               }
               else {
                  System.out.println( Thread.currentThread().getName() + ": "  +  number++ );
                  odd = false;
                  even = !odd;
                  condition.signalAll();
               }
            }

            if ( Thread.currentThread().getName().equals( "even" ) )
            {
               if(!even) {
                  condition.await();
               }
               else {
                  System.out.println( Thread.currentThread().getName() + ": "  + number++ );
                  odd = even;
                  even = !odd;
                  condition.signalAll();
               }
            }
            lock.unlock();
         }
      }
      catch ( Exception e )
      {
         e.printStackTrace();
      }
   }

}